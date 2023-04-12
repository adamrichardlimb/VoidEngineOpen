package renderEngine.guiRendering;

import java.util.ArrayList;
import java.util.HashMap;

//TODO - Define a layout manager. It should have:
//A list of GUI containers, and the ability to search them for containers clicked.
//The ability to handle collisions of those containers and authorise/forbid their movement.
//The ability to handle the contents of those GUI containers, with 'docking' functionality.
//This should probably just work based off of widths/heights and padding.
public class LayoutManager {

    public static HashMap<Container, ArrayList<Widget>> containerWidgetMap;

    public LayoutManager() {
        containerWidgetMap = new HashMap<Container, ArrayList<Widget>>();
    }

    //TODO - This works the same way as batch rendering... Consider consolidating them.
    public static void addWidget(Container parent, Widget child) {
        ArrayList<Widget> widgetList = containerWidgetMap.get(parent);
        widgetList.add(child);
    }

    public static void removeWidget(Container parent, Widget child) {
        ArrayList<Widget> widgetArrayList = containerWidgetMap.get(parent);
        widgetArrayList.remove(child);
    }

}