/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.SAXParseException;


import rpnumerics.RPNumericsProfile;
import wave.util.RealVector;
import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import rpn.RPnConfig;
import rpnumerics.Configuration;
import rpnumerics.RPNUMERICS;

/** This class implements methods to configure the numeric layer. The values are taked from a XML file and this values are used to setup the physics and all others numerics parameters. */
public class RPnNumericsModule {
    //
    // Constants
    //

    /**@deprecated
     * Will be replaced by physics profile reference
     */
    private static RPNumericsProfile profile_ = new RPNumericsProfile();

    static class InputHandler extends HandlerBase {
        //
        // Members
        //

        private RealVector tempVector_;
        private String currentElement_;
        private ArrayList boundaryParamsArray_;
        private static ConfigurationProfile currentConfigurationProfile_;
        private static ConfigurationProfile physicsProfile_;

        @Override
        public void startElement(String name, AttributeList att) throws
                SAXException {

            currentElement_ = name;


            if (name.equals("FLUXPARAMS")) {
                physicsProfile_.addParam(new Integer(att.getValue(1)), att.getValue(0), att.getValue(2));

            }

            if (name.equals("CURVE")) {
                currentConfigurationProfile_ = new ConfigurationProfile(att.getValue(0), "curve");
            }

            if (name.equals("CURVEPARAM")) {
                currentConfigurationProfile_.addParam(att.getValue(0), att.getValue(1));
            }

            if (name.equals("PHYSICS")) {
                physicsProfile_ = new ConfigurationProfile(att.getValue(0), "physics");
                profile_.initPhysics(att.getValue(0));
            }

            if (name.equals("BOUNDARY")) {
                physicsProfile_.addConfigurationProfile(new ConfigurationProfile(att.getValue(0), "boundary"));
            }

            if (name.equals("BOUNDARYPARAM")) {

                if (physicsProfile_.profileArraySize() == 1) {
                    physicsProfile_.getConfigurationProfile(0).addParam(att.getValue(0), att.getValue(1));
                }

            }

            if (name.equals("PHASEPOINT")) {
                tempVector_ = new RealVector((new Integer(att.getValue(0))).intValue());
            }

        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {

            String data = new String(buff, offset, len);
            data = data.trim();
            if (data.length() != 0) {
                if (currentElement_.equals("REALVECTOR")) {
                    tempVector_ = new RealVector(data);
                    boundaryParamsArray_.add(tempVector_);

                }

            }

        }

        @Override
        public void endElement(String name) throws SAXException {
            if (name.equals("PHYSICS")) {
                RPNUMERICS.init(profile_);

                if (physicsProfile_.profileArraySize() >= 1) {

                    Configuration physConfiguration = RPNUMERICS.getConfiguration(profile_.getPhysicsID());
                    ConfigurationProfile physProfile = physicsProfile_.getConfigurationProfile(0);
                    Configuration boundaryConfigurantion = new Configuration(physProfile);
                    physConfiguration.addConfiguration(0, boundaryConfigurantion);

                }

                Configuration physConfiguration = RPNUMERICS.getConfiguration(profile_.getPhysicsID());
                physConfiguration.setParams(physicsProfile_);


            }

            if (name.equals("CURVE")) {

                RPnConfig.addConfiguration(currentConfigurationProfile_.getName(), currentConfigurationProfile_);
            }

        }

        @Override
        public void endDocument() throws SAXException {
        }
    }

    //
    // Members
    //
    //
    // Initializers
    //
    public static void init(Parser parser, String file) {
        try {
            parser.setDocumentHandler(new InputHandler());
            parser.parse(file);
        } catch (Exception saxex) {
            saxex.printStackTrace();
        }

    }

    public static void init(Parser parser, InputStream configFileStream) {
        try {

            parser.setDocumentHandler(new InputHandler());
            System.out.println("Numerics Module");

            System.out.println("Will parse !");
            parser.parse(new InputSource(configFileStream));

            System.out.println("parsed !");

        } catch (Exception saxex) {

            if (saxex instanceof SAXParseException) {

                SAXParseException e = (SAXParseException) saxex;

                System.out.println("Line error: " + e.getLineNumber());
                System.out.println("Column error: " + e.getColumnNumber());
            }

            saxex.printStackTrace();
        }

    }

    //
    // Methods
    //
    /** Writes the actual values of the numerics parameters into a XML file. */
    public static void export(FileWriter writer) throws java.io.IOException {

        writer.write(RPNUMERICS.toXML());

    }
}
