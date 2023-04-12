package renderEngine.guiRendering;


import base.SymbolTable;
import components.graphicalComponents.RenderableComponent;
import entities.Entity;

//This is the base class for the widget, we'll use a Decorator to add features to the Widgets.
//i.e. You may want a text box without scrolling or one with scrolling. We'll define some scrollbar functionality
//and use the Decorator to attach it.
public class Widget extends Entity {

    //A widget must have a container.
    private Container parent;

    //Starting widgetPos is just going to be in the top left corner.
    private float[] widgetPos = {-1f,1f,0f};

    //Starting widgetSize is 0 here, but default constructor will make it 10% of the container it's in.
    private float[] widgetSize = {0f,0f,0f};

    /**
     * Default constructor for a Widget.
     *
     * @param parent
     */
    public Widget(Container parent) {

        this.parent = parent;

        for(int i = 1; i<parent.screenSize.length; i++) {
            widgetSize[i] = 0.1f * parent.screenSize[i];
        }

        RenderableComponent widgetBox = new RenderableComponent();
        addComponent(widgetBox);

        SymbolTable.loader.createVAOAndVBO(this);

    }

    public Widget(Container parent, float widgetPosX, float widgetPosY, float widgetSizeX, float widgetSizeY) {

        this.parent = parent;
        this.widgetPos[0] = widgetPosX;
        this.widgetPos[1] = widgetPosY;
        this.widgetSize[0]= widgetSizeX;
        this.widgetSize[1]= widgetSizeY;

        LayoutManager.addWidget(parent,this);

    }

}
