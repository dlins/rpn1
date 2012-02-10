/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

//import wave.multid.Space;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JOptionPane;
import rpn.RPnConfig;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpn.RPnProjDescriptor;
import rpn.component.util.GeometryGraphND;
import wave.multid.Space;
import rpnumerics.RPNUMERICS;

/** This class configures the initial visualization properties. Reading a XML file that contains the necessary information, this class sets the axis, labels , domain, etc to represents correctly the physics. */
public class RPnVisualizationModule {

    static public List<RPnProjDescriptor> DESCRIPTORS = new ArrayList<RPnProjDescriptor>();
    static public List<RPnProjDescriptor> AUXDESCRIPTORS = new ArrayList<RPnProjDescriptor>();
    private static ConfigurationProfile currentConfigurationProfile_;
    private static ConfigurationProfile currentProjConfigurationProfile_;

    private static class InputHandler implements ContentHandler {

        public void startElement(String uri, String localName, String qName, Attributes att) throws SAXException {

            if (localName.equals("VIEWCONFIGURATION")) {

                currentConfigurationProfile_ = new ConfigurationProfile(att.getValue(0), ConfigurationProfile.VISUALIZATION);

            }


            if (localName.equals("PROJDESC")) {

                currentProjConfigurationProfile_ = new ConfigurationProfile(att.getValue(0), ConfigurationProfile.VISUALIZATION);

            }

            if (localName.equals("VIEWPARAM")) {

                currentProjConfigurationProfile_.addParam(att.getValue(0), att.getValue(1));

            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {


            if (localName.equals("PROJDESC")) {


                currentConfigurationProfile_.addConfigurationProfile(currentProjConfigurationProfile_.getName(), currentProjConfigurationProfile_);

            }


            if (localName.equals("VIEWCONFIGURATION")) {


                RPnConfig.addProfile(currentConfigurationProfile_.getName(), currentConfigurationProfile_);
                RPnConfig.setActiveVisualConfiguration(currentConfigurationProfile_.getName());
            }

        }

        public void setDocumentLocator(Locator locator) {
        }

        public void startDocument() throws SAXException {
        }

        public void endDocument() throws SAXException {
            processActiveVisualConfiguration();
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }

        public void endPrefixMapping(String prefix) throws SAXException {
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }

        public void processingInstruction(String target, String data) throws SAXException {
        }

        public void skippedEntity(String name) throws SAXException {
        }
    }
    //
// Initializers
//

    /** Initializes the XML parser to configure the visualization properties. */
    public static void init(XMLReader parser, String file) {
        try {

            DESCRIPTORS = new ArrayList<RPnProjDescriptor>();
            AUXDESCRIPTORS = new ArrayList<RPnProjDescriptor>();
            parser.setContentHandler(new InputHandler());
            parser.parse(file);


        } catch (Exception saxex) {
            saxex.printStackTrace();


        }
    }

    /** Initializes the XML parser to configure the visualization properties. */
    public static void init(XMLReader parser, InputStream configFileStream) {
        try {
            DESCRIPTORS = new ArrayList<RPnProjDescriptor>();
            AUXDESCRIPTORS = new ArrayList<RPnProjDescriptor>();
            parser.setContentHandler(new InputHandler());
            System.out.println("Visualization Module");

            System.out.println("Will parse !");
            parser.parse(new InputSource(configFileStream));

            System.out.println("parsed !");


        } catch (Exception saxex) {
            saxex.printStackTrace();


        }
    }

    private static void processActiveVisualConfiguration() {
        ConfigurationProfile visualizationProfile = RPnConfig.getActiveVisualProfile();

        Integer dimension = new Integer(visualizationProfile.getName());

        Set<Entry<String, ConfigurationProfile>> configurationSet = visualizationProfile.getProfiles().entrySet();

        Space space = new Space("Domain",dimension);

        for (Entry<String, ConfigurationProfile> profileEntry : configurationSet) {

            ConfigurationProfile profile = profileEntry.getValue();
            String label = profile.getName();

            String[] axisString = profile.getParam("axis").split(" ");
            String vpwidth = profile.getParam("vpwidth");
            String vpheight = profile.getParam("vpheight");
            String iso2equi = profile.getParam("iso2equi");


            int[] axisArray = new int[2];

            axisArray[0] = new Integer(axisString[0]);

            axisArray[1] = new Integer(axisString[1]);

            Integer w = new Integer(vpwidth);
            Integer h = new Integer(vpheight);

            Boolean iso = new Boolean(iso2equi);

            // *** Leandro
            try {
                if (RPNUMERICS.physicsID().equals("Stone")) {
                    String str = JOptionPane.showInputDialog(null, "Digite 0 para triangulo retangulo; 1 para triangulo equilatero", "Iso Map To Equi ?", JOptionPane.QUESTION_MESSAGE);
                    GeometryGraphND.mapToEqui = Integer.parseInt(str);
                    if (GeometryGraphND.mapToEqui == 0) {
                        iso = false;
                    }
                }

            } catch (Exception e) {
            }
            //***
            
            DESCRIPTORS.add(new RPnProjDescriptor(space, label, w, h, axisArray, iso));

        }
        Space auxSpace = new Space("AuxDomain", 2 * dimension);

        for (RPnProjDescriptor descriptor : DESCRIPTORS) {
            createAuxDescriptor(descriptor, auxSpace, descriptor.isIso2equi());

        }


    }

    public static void createAuxDescriptor(RPnProjDescriptor descriptor, Space space, boolean isIso2Equi) {

        int[] projIndices = descriptor.projMap().getCompIndexes();

        int w = descriptor.viewport().width;
        int h = descriptor.viewport().height;

        RPnProjDescriptor auxDescriptorLeft = new RPnProjDescriptor(space, "Left " + projIndices[0] + " " + projIndices[1], w, h, projIndices, isIso2Equi);
        AUXDESCRIPTORS.add(auxDescriptorLeft);

        System.out.println("auxDescriptorLeft: " + auxDescriptorLeft.label());
//
        int[] auxProj = new int[2];
        auxProj[0] = projIndices[0];// + space.getDim() ;
        auxProj[1] = projIndices[1];// + space.getDim() ;

        RPnProjDescriptor auxDescriptorRight = new RPnProjDescriptor(space, "Right " + auxProj[0] + " " + auxProj[1], w, h, auxProj, isIso2Equi);
        AUXDESCRIPTORS.add(auxDescriptorRight);
        System.out.println("auxDescriptorRight: "+auxDescriptorRight.label());

    }

    //
    // Methods
    //
    /** Writes the actual visualization configuration into a XML file. */
    static public void export(FileWriter writer) throws java.io.IOException {

        writer.write(RPnConfig.getVisualConfiguration().toXML());

    }
}
