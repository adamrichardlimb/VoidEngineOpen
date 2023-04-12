package components;

/**
 * The abstract class for our components. Has a simple boolean to see if the component has an update method attached
 * to it or not. By default this is false, since most components won't need an update method.
 */
public class Component implements ComponentInterface {

    /**
     * Two placeholder variables, update and lastUpdate. The former indicates if a component should update on game update.
     * The latter indicates when the last update occurred.
     * The first method is, naturally, what to update the component with.
     * The second method is what to do upon being removed from an entity.
     * @see entities.Entity
     */
    protected boolean update = false;
    protected double lastUpdate;
    protected void update() { }
    protected void onDetach() { }

}
