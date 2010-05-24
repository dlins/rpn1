/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import org.iso_relax.verifier.VerifierConfigurationException;
import org.xml.sax.SAXException;



import rpn.message.*;
import rpnumerics.RpException;
import javax.swing.JOptionPane;
import java.io.*;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import rpn.controller.ui.UIController;
import rpn.parser.RPnInterfaceParser;
import rpn.parser.RPnNumericsModule;
import rpnumerics.RPNUMERICS;

public class RPnDesktopPlotter implements RPnMenuCommand {

    public static String DTDPATH = System.getProperty("rpnhome") + System.getProperty("file.separator") + "share" + System.getProperty("file.separator") + "rpn-dtd" + System.getProperty("file.separator");
    public static String INTERFACE_CONFIG_PATH = System.getProperty("rpnhome") + System.getProperty("file.separator") + "share" + System.getProperty("file.separator") + "rpn-examples" + System.getProperty("file.separator");
    private static RPnConfigReader configReader_;
    private static InputStream configStream_;
    private static RPnUIFrame rpnUIFrame;

    public RPnDesktopPlotter(String configFile) throws FileNotFoundException, VerifierConfigurationException, SAXException, IOException {

        // create a VerifierFactory with the default SAX parser
        VerifierFactory factory = new com.sun.msv.verifier.jarv.TheFactoryImpl();
        // compile a RELAX schema (or whatever schema you like)
        org.iso_relax.verifier.Schema schema = factory.compileSchema(new File(DTDPATH + "rpnDTD.dtd"));

        // obtain a verifier
        Verifier verifier = schema.newVerifier();
//        Verifier interfaceVerifier = intefaceSchema.newVerifier();
        // set an error handler
        // this error handler will throw an exception if there is an error
        verifier.setErrorHandler(com.sun.msv.verifier.util.ErrorHandlerImpl.theInstance);


        if (verifier.verify(new File(configFile))) {
            System.out.println("The input document is valid");
        }

    }

    public RPnDesktopPlotter() {
    }

    public void finalizeApplication() {
        try {

            if (UIController.instance().getNetStatusHandler().isServerOnline()
                    && UIController.instance().getNetStatusHandler().isOnline()) {
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

        RPnDesktopPlotter plotter = null;
        try {

            File interfaceConfigFile = new File(INTERFACE_CONFIG_PATH + "defaults.xml");
            InputStream defaultsConfigStream = new FileInputStream(interfaceConfigFile);


            if (args.length == 0) {
                throw new FileNotFoundException();
            }

            RPnConfigReader.readDefaults(defaultsConfigStream);//Reading defaults

            plotter = new RPnDesktopPlotter(args[0]);

            configReader_ = RPnConfigReader.getReader(args[0], false, null);
            configStream_ = configReader_.read();
            RPnDesktopPlotter.configReader_.init(configStream_); //Reading input file

            rpnUIFrame = new RPnUIFrame(plotter);

            rpnUIFrame.pack();

            rpnUIFrame.setVisible(true);

            RPnCurvesListFrame curvesFrame = new RPnCurvesListFrame();
            rpnUIFrame.setCurvesFrame(curvesFrame);


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            RPnConfigDialog configDialog = new RPnConfigDialog();
            configDialog.setVisible(true);


        } catch (VerifierConfigurationException ex) {
            System.out.println("Error in configuration file");
        } catch (SAXParseException e) {
            System.out.println("The document is not valid");
            System.out.println("Because:  " + e);
            System.out.println("Line: " + e.getLineNumber());
            System.out.println("Column: " + e.getColumnNumber());
            // if the document is invalid, then the execution will reach here
            // because we throw an exception for an error.


        } catch (SAXException ex) {

            ex.printStackTrace();



        } catch (IOException exception) {
        } finally {
            try {

                configStream_.close();
            } catch (NullPointerException ex) {
            } catch (IOException ex) {
                System.out.println("IO Error");
            }

        }

    }
}
