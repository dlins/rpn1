package rpn;


/** This interface contains the menu commands that will be used in the applet and desktop versions */

public interface RPnMenuCommand {

    /** Command to close the application. The applet or the desktop application is closed and resources are released. */

    public void finalizeApplication();

    /** Command to initialize JMS communication */

    public void networkCommand();


}

