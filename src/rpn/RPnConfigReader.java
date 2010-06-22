/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.io.*;
import javax.swing.JApplet;
import java.awt.Font;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import rpn.parser.*;
import org.xml.sax.Parser;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.ParserFactory;
import org.xml.sax.helpers.XMLReaderFactory;
import rpn.plugininterface.PluginInfoController;
import rpn.plugininterface.PluginInfoParser;

/** This class contains methods to configure the applet and the desktop versions. */
public abstract class RPnConfigReader {

    static public Font MODELPLOT_BUTTON_FONT = new Font("Arial", 1, 8);
    static public String XML_HEADER = "<?xml version=\"1.0\"?>\n<!DOCTYPE rpnconfiguration SYSTEM \"rpnDTD.dtd\">\n";

    /** Constructs a configuration object to the applet or the desktop version */
    public static RPnConfigReader getReader(String file, boolean isApplet, JApplet applet) {

        RPnConfig.remoteImage();
        if (isApplet) {

            if (applet == null) {
                System.err.println("Error in applet creation");
                System.exit(1);
            }

            return new RPnAppletConfigReader(file, applet);

        } else {

            return new RPnDesktopConfigReader(file);
        }

    }

    public static void readDefaults(InputStream defaultsStream) {

        XMLReader xmlReader;
        try {
            xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new RPnInterfaceParser());
            xmlReader.parse(new InputSource(defaultsStream));
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }





    }

    /** Initializes the XML parser that reads the configuration file */
    public void init(InputStream configStream) {

        try {

            // initialize the XML parser
            Parser parser = ParserFactory.makeParser("com.ibm.xml.parsers.ValidatingSAXParser");
            // initialize numerics (no Poincare)

            configStream.mark(0);

            RPnNumericsModule.init(parser, configStream);
            // initialize visualization params

            configStream.reset();


            RPnVisualizationModule.init(parser, configStream);

            // initialize visualization paramsargs[0]);

            // initialize rpdata (if any)

            configStream.reset();

            RPnDataModule.init(parser, configStream);


//            RPnGeometryModule.init(parser, configStream);

            PluginInfoParser pluginParser = new PluginInfoParser();
            PluginInfoController.updatePluginInfo(pluginParser);



        } catch (Throwable any) {


            any.printStackTrace();
        }

    }

    /** Gives a stream with the configuration data */
    public abstract InputStream read();
}

