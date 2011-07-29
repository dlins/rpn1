/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;


import wave.util.RealVector;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import rpn.RPnConfig;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpnumerics.ContourConfiguration;
import rpnumerics.RPNUMERICS;

/** This class implements methods to configure the numeric layer. The values are taked from a XML file and this values are used to setup the physics and all others numerics parameters. */
public class RPnNumericsModule {
    //
    // Constants
    //

    static class InputHandler implements ContentHandler {
        //
        // Members
        //

        private RealVector tempVector_;
        private String currentElement_;
        private ArrayList<RealVector> boundaryParamsArray_;
        private static ConfigurationProfile currentConfigurationProfile_;
        private static ConfigurationProfile physicsProfile_;
        private static ConfigurationProfile currentPhysicsConfigurationProfile_;

        public void startElement(String uri, String localName, String qName, Attributes att) throws SAXException {
            currentElement_ = localName;

            if (localName.equals("FLUXPARAMS")) {
                physicsProfile_.addParam(new Integer(att.getValue(1)), att.getValue(0), att.getValue(2));

            }

            if (localName.equals("CURVE")) {
                currentConfigurationProfile_ = new ConfigurationProfile(att.getValue(0), ConfigurationProfile.CURVE_PROFILE);

            }

            if (localName.equals("CURVEPARAM")) {
                currentConfigurationProfile_.addParam(att.getValue(0), att.getValue(1));
            }

            if (localName.equals("PHYSICS")) {
                physicsProfile_ = new ConfigurationProfile(att.getValue(0), ConfigurationProfile.PHYSICS_PROFILE);
//                physicsID_ = att.getValue(0);
            }

            if (localName.equals("BOUNDARY")) {
                physicsProfile_.addConfigurationProfile(ConfigurationProfile.BOUNDARY_PROFILE, new ConfigurationProfile(att.getValue(0), ConfigurationProfile.BOUNDARY_PROFILE));


            }

            if (localName.equals("PHYSICSCONFIG")) {

                currentPhysicsConfigurationProfile_ = new ConfigurationProfile(att.getValue(0), ConfigurationProfile.PHYSICS_CONFIG_PROFILE);

            }
            if (localName.equals("PHYSICSPARAM")) {

                currentPhysicsConfigurationProfile_.addParam(att.getValue(0), att.getValue(1));

            }



            if (localName.equals("BOUNDARYPARAM")) {

//                System.out.println("Parametro: " + att.getValue(0) + " " + att.getValue(1));

                if (physicsProfile_.profileArraySize() == 1) {
                    physicsProfile_.getConfigurationProfile(ConfigurationProfile.BOUNDARY_PROFILE).addParam(att.getValue(0), att.getValue(1));
                }

            }


            if (localName.equals("METHOD")) {

                currentConfigurationProfile_ = new ConfigurationProfile(att.getValue("name"), ConfigurationProfile.METHOD_PROFILE);

            }

            if (localName.equals("METHODPARAM")) {
                checkNumberFormat(att.getValue("value"));
                currentConfigurationProfile_.addParam(att.getValue("name"), att.getValue("value"));
            }



            if (localName.equals("PHASEPOINT")) {
                tempVector_ = new RealVector((new Integer(att.getValue(0))).intValue());
            }


        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.equals("PHYSICS")) {
                RPnConfig.setActivePhysics(physicsProfile_.getName());
                RPnConfig.addProfile(physicsProfile_.getName(), physicsProfile_);
                rpnumerics.RPNUMERICS.init(physicsProfile_.getName());

//                System.out.println("Adicionando profile: "+physicsProfile_.getName());

            }


            if (localName.equals("PHYSICSCONFIG")) {

                physicsProfile_.addConfigurationProfile(currentPhysicsConfigurationProfile_.getName(), currentPhysicsConfigurationProfile_);
            }

            if (localName.equals("CURVE")) {

//                System.out.println("Adicionando profile: " + currentConfigurationProfile_.getName());
                RPnConfig.addProfile(currentConfigurationProfile_.getName(), currentConfigurationProfile_);

            }

            if (localName.equals("METHOD")) {

//                System.out.println("Adicionando profile: " + currentConfigurationProfile_.getName());
                if (currentConfigurationProfile_.getName().equalsIgnoreCase("Contour")) {

                    ContourConfiguration contourConfiguration = new ContourConfiguration(currentConfigurationProfile_);
                    RPNUMERICS.setConfiguration(contourConfiguration.getName(), contourConfiguration);



                }
                RPnConfig.addProfile(currentConfigurationProfile_.getName(), currentConfigurationProfile_);

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

        public void setDocumentLocator(Locator locator) {
        }

        public void startDocument() throws SAXException {

            System.out.println("Start Document  de Numerics");

        }

        public void endDocument() throws SAXException {
            System.out.println("End Document  de Numerics");


        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }

        public void endPrefixMapping(String prefix) throws SAXException {
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }

        public void processingInstruction(String target, String data) throws SAXException {
        }

        public void skippedEntity(String name) throws SAXException {
        }

        private void checkNumberFormat(String value) {
            try {
                Double test = new Double(value);
            } catch (NumberFormatException ex) {
                System.err.println("Invalid number format in default values file: " + value);
                System.exit(1);
            }
        }
    }

    //
    // Members
    //
    //
    // Initializers
    //
    public static void init(XMLReader parser, String file) {
        try {
            parser.setContentHandler(new InputHandler());
            parser.parse(file);
        } catch (Exception saxex) {
            saxex.printStackTrace();
        }

    }

    public static void init(XMLReader parser, InputStream configFileStream) {
        try {

            parser.setContentHandler(new InputHandler());
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
