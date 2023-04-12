package renderEngine.guiRendering;

import renderEngine.Loader;
import renderEngine.TexturedVertex;
import util.IAABB;
import util.RenderableBase;

import java.util.ArrayList;

//This is the base container for all GUI elements. It defaults to dead centre of the screen.
//You can /only/ set its size + position in proportion to the screen in order to ensure everything is uniform.
public class Container extends RenderableBase implements IAABB {

    //Look, it started working and I don't know why.
    //I'm going to add a method to renderable that translates proportional sizes into screenPositions.
    //I dream that this works dynamically, flexibly, and never needs to be touched again.
    public float[] screenPos = new float[]{-1f,1f, 0f};

    //This isn't an entity, so we don't use collision boxes for it.
    //Set our own public floats and be done with it.
    //TODO - Consider moving a lot of this up into the RenderableBase class.
    public float collisionX = -1f;
    public float collisionY = 1f;
    public float collisionW = 0.5f;
    public float collisionH = 0.5f;

    private Loader loader;

    //NOTE: Please read this and understand it before ever /EVER/ touching this code again.
    //Positive in x to go from -1 to 1 in the OpenGL coordinate space.
    //Negative in y to go from 1 to -1 in the OpenGL coordinate space.
    //Okay? Okay.
    public float[] screenSize= new float[]{0.5f, -0.5f, 0f};
    public boolean moveable = true;

    public Container(Loader loader, LayoutManager manager) {
        super();

        //TODO - Pull this shit out, it's just a test for now.
        for(TexturedVertex vert : vertices) {
            vert.setRGBA(0,0,0,0.5f);
        }
        this.loader = loader;

        LayoutManager.containerWidgetMap.put(this, new ArrayList<>());
        loader.createVAOAndVBO(this);
        loader.guis.add(this);
    }

    //This method is for setting things via clicks, which are already in an OpenGL coordinate system.
    //The method for setting a position in code is elsewhere!
    public void setPosThroughClick(float posX, float posY) {
        screenPos[0] = posX;
        screenPos[1] = posY;

        collisionX = posX;
        collisionY = posY;
    }

    //This method sets the size of the box proportionally to the screen.
    public void setProportionalSize(float sizeX, float sizeY){
        float[] toSet = convertMyCoordSystemToOpenGLCoordSystem(sizeX, sizeY);

        screenSize[0] = 2 * toSet[0];
        screenSize[1] = 2 * toSet[1];

        collisionW = 2*toSet[0];
        collisionH = -2*toSet[1];
    }

    public void doClick(float posX, float posY) {

        if(moveable){
            setPosThroughClick(posX, posY);
        }

    }

    //This method is for setting positions manually through code. For instance, when creating a container.
    //The float positions are /percentages/ meaning 0.1f is 10% of the screen (0.2f in OpenGL)
    //This way when creating UIs they are always consistent regardless of screen size.
    public void setPos(float posX, float posY) {
        //Turn the percentage positions into OpenGL positions.
        float[] toSet = convertMyCoordSystemToOpenGLCoordSystem(posX, posY);
        setPosThroughClick(toSet[0], toSet[1]);
    }

    //TODO - Add exit button to close windows.
    public void cull() {
        //Stop colliding with the object.
        LayoutManager.containerWidgetMap.remove(this);
        //Remove the object from the list of GUIs.
        loader.vaos.remove(vaoID);
        loader.guis.remove(this);

    }

}
