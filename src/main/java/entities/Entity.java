package entities;

import components.Component;

import java.util.HashMap;
import java.util.UUID;

/**
 * The base entity class, allows for the attachment of components alongside some logic to check if we have said components.
 */

//TODO - Add 'remove components', and add a method to components called onDetach() to carry out some function on removal
public abstract class Entity {
    protected UUID id;
    public static HashMap<Class, HashMap<UUID, ? extends Component>> components = new HashMap<>();

    //NOTE - Is this wise? We may not want to hold onto all the entities in the world...
    //Commented out until it becomes necessary.
    //protected static List<Entity> entities = new ArrayList<>();

    /**
     * Entity constructor, merely assigns a UUID for said entity.
     */
    protected Entity() {
        this.id = UUID.randomUUID();
        //entities.add(this);
    }

    /**
     * Adds a component to the entity specified by its UUID.
     *
     * @param entity - The UUID of the entity we want to add a component to.
     * @param component - The component we wish to add.
     * @param <T> - TODO - Read about parameters.
     */
    public static <T extends Component> void addComponent(UUID entity, T component){
        synchronized (components) {
            HashMap<UUID, ? extends Component> toAdd = components.get(component.getClass());
            if(toAdd  == null) {
                toAdd = new HashMap<UUID, T>();
                components.put(component.getClass(), toAdd);
            }
            ((HashMap<UUID, T>) toAdd).put(entity, component);
        }
    }

    /**
     * Adds a component to the entity that called the method.
     *
     * @param component - The component to add to the entity.
     * @param <T>
     */
    public <T extends Component> void addComponent(T component){
        synchronized (components) {
            HashMap<UUID, ? extends Component> toAdd = components.get(component.getClass());
            if(toAdd  == null) {
                toAdd = new HashMap<UUID, T>();
                components.put(component.getClass(), toAdd);
            }
            ((HashMap<UUID, T>) toAdd).put(this.id, component);
        }
    }

    /**
     * A getter for the component of an entity specified by its UUID.
     *
     * @param entity - The UUID of the entity we want to grab a component from.
     * @param component - The component we want to grab.
     * @param <T>
     * @return - The requested component of the entity specified.
     */
    public static <T> T getComponent(UUID entity, Class <T> component) {
        HashMap<UUID, ? extends Component> toGet = components.get(component);
        T results = (T) toGet.get(entity);
        if(results == null) {
            throw new IllegalArgumentException("getComponent failure: " + entity.toString() + " does not have component " + component.toString());
        }

        return results;
    }

    /**
     * A getter for an entity to grab a component from itself.
     *
     * @param component - The component we want to grab.
     * @param <T>
     * @return - The component of the entity that executed the getter.
     */
    public <T> T getComponent(Class <T> component) {
        HashMap<UUID, ? extends Component> toGet = components.get(component);
        T results = (T) toGet.get(this.id);
        if(results == null) {
            throw new IllegalArgumentException("getComponent failure: " + this.toString() + " does not have component " + component.toString());
        }
        return results;
    }

    /**
     * Removes a component from a specified entity.
     * @param entity - The UUID of the entity we wish to remove components from.
     * @param component - The component we want to remove from that entity.
     * @param <T>
     */
    public static <T> void removeComponent(UUID entity, Class <T> component) {
        HashMap<UUID, ? extends Component> toRemove = components.get(component);
        T results = (T) toRemove.get(entity);
        if(results == null) {
            throw new IllegalArgumentException("removeComponent failue: " + entity.toString() + " does not have component " + component.toString());
        }
    }

    /**
     * Removes a component from a specified entity.
     *
     * @param component - The component we want to remove from the entity that called this method.
     * @param <T>
     */
    public <T> void removeComponent(Class <T> component) {
        HashMap<UUID, ? extends Component> toRemove = components.get(component);
        T results = (T) toRemove.get(this.id);
        if(results == null) {
            throw new IllegalArgumentException("removeComponent failue: " + this.toString() + " does not have component " + component.toString());
        }
    }

    /**
     * Checks if a specified entity has a requested component.
     *
     * @param entity - The entity we want to check the components of.
     * @param component - The component we wish to look for.
     * @param <T>
     * @return - True, if the entity has the component, false otherwise.
     */
    public static <T> boolean hasComponent(UUID entity, Class <T> component) {
        HashMap<UUID, ? extends Component> toGet = components.get(component);
        T results = (T) toGet.get(entity);
        if(results == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the entity that called the method has a requested component.
     *
     * @param component - The component we want to check for.
     * @param <T>
     * @return - True if the entity has the component, false otherwise.
     */
    public <T> boolean hasComponent(Class <T> component) {
        HashMap<UUID, ? extends Component> toGet = components.get(component);
        T results = (T) toGet.get(this.id);
        if(results == null) {
            return false;
        }
        return true;
    }
}