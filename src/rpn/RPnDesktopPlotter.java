package rpn;

import rpn.message.*;
import rpn.parser.*;
import rpnumerics.RpException;
import javax.swing.JOptionPane;
import java.io.*;
import rpn.controller.ui.UIController;
import org.iso_relax.verifier.*;

import org.xml.sax.SAXParseException;

public class RPnDesktopPlotter implements RPnMenuCommand {

    private String DTDPATH = System.getProperty("rpngrouphome") + System.getProperty("file.separator") + "var" + System.getProperty("file.separator") + "config" + System.getProperty("file.separator");
    private RPnConfigReader configReader_;
    private InputStream configStream_;

    public RPnDesktopPlotter(String configFile) {
        try {
//        // create a VerifierFactory with the default SAX parser
            System.out.println(DTDPATH);
            VerifierFactory factory = new com.sun.msv.verifier.jarv.TheFactoryImpl();

            // compile a RELAX schema (or whatever schema you like)

            Schema schema = factory.compileSchema(new File(DTDPATH + "rpnconfig.dtd"));

            // obtain a verifier
            Verifier verifier = schema.newVerifier();

            // set an error handler
            // this error handler will throw an exception if there is an error
            verifier.setErrorHandler(com.sun.msv.verifier.util.ErrorHandlerImpl.theInstance);

            try {

                if (verifier.verify(new File(configFile))) {

                    // the document is valid

                    System.out.println("The document is valid");
                } else {
                // the execution will never reach here because
                // if the document is invalid, then an exception should be thrown.
                }

            } catch (SAXParseException e) {

                System.out.println("The document is not valid");
                System.out.println("Because:  " + e);
                System.out.println("Line: " + e.getLineNumber());
                System.out.println("Column: " + e.getColumnNumber());
            // if the document is invalid, then the execution will reach here
            // because we throw an exception for an error.
            }

        } catch (Throwable any) {
            any.printStackTrace();
        }

        configReader_ = RPnConfigReader.getReader(configFile, false, null);

        configStream_ = configReader_.read();
    }

    public void finalizeApplication() {
        try {

            if (UIController.instance().getNetStatusHandler().isServerOnline() &&
                    UIController.instance().getNetStatusHandler().isOnline()) {
                UIController.instance().getNetStatusHandler().online(false);
            }

            if (UIController.instance().getNetStatusHandler().isMaster()) {
                RPnActionMediator.instance().sendMasterOffline();
            }

            libraryCleanUp();

            System.exit(0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private native void libraryCleanUp();

    public void networkCommand() {

        UIController.instance().getNetStatusHandler().init();

        RPnNetworkDialog netDialog = new RPnNetworkDialog();
        netDialog.setVisible(true);

    }

    static public void showCalcExceptionDialog(RpException ex) {
        JOptionPane.showMessageDialog(null, ex.toString(),
                "Rp Calculation Exception",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {

        String configFile = System.getProperty("rpngrouphome") + System.getProperty("file.separator") + args[0];

        RPnDesktopPlotter plotter = new RPnDesktopPlotter(configFile);

        RPnNumericsModule.setCommand(plotter);

        plotter.configReader_.init(plotter.configStream_);

        RPnUIFrame rpnUIFrame_ = new RPnUIFrame(plotter);

        rpnUIFrame_.pack();

        rpnUIFrame_.setVisible(true);

    }
}
