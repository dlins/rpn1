package rpn;

import rpn.message.*;
import rpn.parser.*;
import rpnumerics.RpException;
import javax.swing.JOptionPane;
import java.io.*;
import rpn.controller.ui.UIController;
import org.iso_relax.verifier.*;
import rpnumerics.RPNUMERICS;

public class RPnDesktopPlotter implements RPnMenuCommand {

//    private String DTDPATH = "." + System.getProperty("file.separator");
//    private String DTDPATH = "/impa/home/f/edsonlan/Java/rpnMerge/trunk/share/rpn-dtd/";// + System.getProperty("file.separator");
    private RPnConfigReader configReader_;
    private InputStream configStream_;
    private static RPnUIFrame rpnUIFrame;

    public RPnDesktopPlotter(String configFile) {
//        try {
//            //        // create a VerifierFactory with the default SAX parser
//            VerifierFactory factory = new com.sun.msv.verifier.jarv.TheFactoryImpl();
//            // compile a RELAX schema (or whatever schema you like)
//            Schema schema = factory.compileSchema(new File(DTDPATH + "rpnconfig.dtd"));
//            // obtain a verifier
//            Verifier verifier = schema.newVerifier();
//            // set an error handler
//            // this error handler will throw an exception if there is an error
//            verifier.setErrorHandler(com.sun.msv.verifier.util.ErrorHandlerImpl.theInstance);
//            try {
//                if (verifier.verify(new File(configFile))) {
//                    // the document is valid
//                    System.out.println("The document is valid");
//                } else {
//                // the execution will never reach here because
//                // if the document is invalid, then an exception should be thrown.
//                }
//            } catch (SAXParseException e) {
//                System.out.println("The document is not valid");
//                System.out.println("Because:  " + e);
//                System.out.println("Line: " + e.getLineNumber());
//                System.out.println("Column: " + e.getColumnNumber());
//            // if the document is invalid, then the execution will reach here
//            // because we throw an exception for an error.
//            }
//        } catch (Throwable any) {
//            any.printStackTrace();
//        }

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
            RPNUMERICS.clean();
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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

    public static RPnUIFrame getUIFrame() {
        return rpnUIFrame;
    }

    public static void main(String[] args) {

        RPnDesktopPlotter plotter = new RPnDesktopPlotter(args[0]);

        plotter.configReader_.init(plotter.configStream_);

        rpnUIFrame = new RPnUIFrame(plotter);

        rpnUIFrame.pack();

        rpnUIFrame.setVisible(true);

    }
}
