package entities;

import base.SymbolTable;
import components.graphicalComponents.RenderableComponent;
import components.interactionComponents.BoxComponent;

/**
 * A class for entities with a small box, and a default renderable component to them.
 */
public class RenderableEntity extends Entity{

    public RenderableEntity() {
        addComponent(new BoxComponent(0f, 0f, 0.5f, -0.5f));
        addComponent(new RenderableComponent());
        SymbolTable.loader.createVAOAndVBO(this);
        SymbolTable.loader.entities.add(this);
    }
}
