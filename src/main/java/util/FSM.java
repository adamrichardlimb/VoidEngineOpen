package util;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * A simple Finite State Machine, allows for establishment of valid transition and listeners that can execute some
 * functionality upon being notified of a state transition.
 *
 *
 * @param <T> - The enumerated list of states the FSM can be in.
 */
public class FSM<T extends Enum<T>> {

    private T currentState;
    private Map<T, List<T>> possibleTransitions = new HashMap<>();
    private List<FSMListener<T>> listeners = new ArrayList<>();

    /**
     * Constructs a FSM from some initial state.
     * @param initialState - The state to begin our FSM in.
     */
    public FSM(T initialState) {
        currentState = initialState;
    }

    /**
     * Constructs a FSM with a listener attached.
     * @param initialState - The state to begin our FSM in.
     * @param listener - The listener to listen to our state.
     */
    public FSM(T initialState, FSMListener<T> listener) {

        currentState = initialState;
        addListener(listener);

    }

    /**
     * Sets the current state of our FSM, with no checks for the validity of transition.
     * @param toState - The state to set our FSM to.
     * @return - Returns itself, now in the state you set.
     */
    public FSM<T> setCurrentState(T toState) {

        this.currentState = toState;
        return this;

    }

    /**
     * Simple getter, returns the current state of the FSM.
     * @return - The current state of the FSM.
     */
    public T getCurrentState() {

        return currentState;

    }

    /**
     * Adds a listener to the FSM.
     * @param listener - The listener to attach to the FSM.
     * @return - Returns itself, with the listener now attached.
     */
    public FSM<T> addListener(FSMListener<T> listener) {

        listeners.add(listener);
        return this;
    }

    /**
     * Constructs a valid transition for the FSM.
     * Note that adding a valid transition from A to B does not mean it is now valid to transition from B to A.
     *
     * @param fromState - The starting state to transition from.
     * @param toState - The ending state to transition to.
     * @return - Returns itself, with the transition between fromState to toState now validated.
     */
    public FSM<T> addValidTransition(T fromState, T toState) {

        List<T> toStates;
        if(possibleTransitions.containsKey(fromState)) {

            toStates = possibleTransitions.get(fromState);

        }else {

            toStates = new ArrayList<>();
            possibleTransitions.put(fromState, toStates);

        }
        toStates.add(toState);
        System.out.println(this.toString() + toState + " from " + fromState + " added.");
        return this;
    }

    /**
     * Check if there are any valid transitions available to us.
     *
     * @return - Returns true if there is a valid transition left for our state, false if there are no valid transitions
     */
    public boolean isAnyFutureStatePossible() {
        return possibleTransitions.containsKey(currentState);
    }

    /**
     * Checks if we can transition from our current state to some requested state.
     *
     * @param toState - The state we'd like to transition to.
     * @return - Returns true if we can transition to a requested state, false otherwise.
     */
    public boolean isTransitionToStatePossible(T toState)
    {
        if(possibleTransitions.containsKey(currentState))
        {
            List<T> toStates = possibleTransitions.get(currentState);
            return toStates.contains(toState);
        }
        return false;
    }

    /**
     * Checks if a transition is permitted, if so, we set it to our current state. Otherwise we throw an error.
     * Upon successful transition, notify our listeners as to what we transition from and to.
     *
     * @param toState - The state we wish to transition to.
     * @return - Return the FSM, now in the requested state.
     */
    public FSM<T> transitToState(T toState) {
        if(!isTransitionToStatePossible(toState)){
            throw new IllegalArgumentException("Future state transition to" + toState + "is not possible!");}
        notifyListenersOfAttemptedStateChange(currentState, toState);
        T lastState = currentState;
        currentState = toState;
        notifyListenersOfSuccessfulStateChange(lastState, currentState);
        return this;
    }

    private void notifyListenersOfAttemptedStateChange(T currentState, T toState) {
        for(FSMListener<T> listener : listeners)
        {
            try
            {
                boolean success = listener.preconditions(currentState, toState);
                if(!success) {
                    listener.onPreconditionsFailure(currentState, toState);
                }
            }
            catch(Exception ex)
            {
                System.out.println("FSMListener: " + listener.toString() + " threw Exception " + ex +
                        " upon ATTEMPTED from " + currentState + " to " + toState + " in " + this.toString()
                        +  " swallowing the error and informing other listeners.");
            }
        }
    }

    /**
     * Notifies the listeners of a state change, allowing them to define some functionality upon that state change.
     *
     * @param lastState - The state we just moved from.
     * @param newCurrentState - The state we moved to.
     */
    private void notifyListenersOfSuccessfulStateChange(T lastState, T newCurrentState)
    {
        for(FSMListener<T> listener : listeners)
        {
            try
            {
                listener.stateChanged(lastState, newCurrentState);
            }
            catch(Exception ex)
            {
                System.out.println("FSMListener: " + listener.toString() + " threw Exception " + ex +
                        " upon transition from " + lastState + " to " + newCurrentState + " in " + this.toString()
                        +  " swallowing the error and informing other listeners.");
            }
        }
    }

    /**
     * Clones a FSM, without cloning its listeners and including all of the valid transitions previously defined.
     *
     * @return - Returns the /cloned/ FSM.
     */
    @Override
    public FSM<T> clone()
    {
        FSM<T> cloned = new FSM<T>(currentState);
        //Create a new FSM, in the current state. Add our listeners to that FSM.
        for(FSMListener<T> listener : listeners)
            cloned.addListener(listener);

        //Then copy over the HashMap of allowed transitions, and send back the cloned FSM.
        cloned.possibleTransitions = new HashMap<T, List<T>>(possibleTransitions);
        return cloned;
    }

    /**
     * An interface that allows you to define some functionality upon a state change occurring.
     *
     * @param <T> - The enumerated states for that respective FSM.
     */
    public interface FSMListener<T extends Enum<T>>
    {
        void stateChanged(T fromState, T toState) throws IOException, ParseException;


        //These are my additions to the FSM class, the creation of pre and post conditions.
        default boolean preconditions(T fromState, T toState) throws IOException {
            return true;
        }

        default void onPreconditionsFailure(T fromState, T toState) {

        }

        default boolean postconditions(T fromState, T toState) {
            return true;
        }

        default void onPostconditionsFailure(T fromState, T toState) {

        }
    }


}