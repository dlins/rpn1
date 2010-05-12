/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;


import wave.util.RealVector;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import org.xml.sax.ContentHandler;
import rpn.RPnConfig;
import rpnumerics.RPNUMERICS;

/** This class parses the default values for physics, visual configuration and methods. */
public class RPnInterfaceParser implements ContentHandler {
    //
    // Constants
    //

    private static ArrayList<ConfigurationProfile> configurationProfile_ = new ArrayList<ConfigurationProfile>(2);
    private static ArrayList<VisualizationProfile> visualizationProfiles_ = new ArrayList<VisualizationProfile>(2);
    private static ConfigurationProfile currentConfigurationProfile_;    //
    private static VisualizationProfile currentVisualizationProfile_;    // Members
    //
    private RealVector tempVector_;
    private String currentElement_;

    @Override
    public void startElement(String uri, String localName, String name, Attributes att) throws SAXException {

        currentElement_ = name;

        if (name.equals("PHYSICS")) {
            currentConfigurationProfile_ = new ConfigurationProfile(att.getValue(0), "physics");
        }

        if (name.equals("FLUXPARAMS")) {
            checkNumberFormat(att.getValue("value"));
            currentConfigurationProfile_.addParam(new Integer(att.getValue(1)), att.getValue(0), att.getValue(2));
        }

        if (name.equals("VIEWINGPARAMS")) {
            currentVisualizationProfile_ = new VisualizationProfile();
        }


        if (name.equals("VIEWPORT")) {

            String[] viewPort = new String[2];

            viewPort[0] = att.getValue("vpwidth");
            viewPort[1] = att.getValue("vpheight");
            currentVisualizationProfile_.setViewPort(viewPort);
        }


        if (name.equals("METHOD")) {

            currentConfigurationProfile_ = new ConfigurationProfile(att.getValue("name"), "method");

        }

        if (name.equals("METHODPARAM")) {
            checkNumberFormat(att.getValue("value"));
            currentConfigurationProfile_.addParam(att.getValue("name"), att.getValue("value"));
        }

        if (name.equals("NUMERICSPARAM")) {

            currentConfigurationProfile_.addParam(att.getValue(0), att.getValue(1));

        }


        if (name.equals("REALVECTOR")) {
            tempVector_ = new RealVector(att.getValue("dimension"));
        }

        if (name.equals("BOUNDARYPARAM")) {

            currentConfigurationProfile_.getConfigurationProfile(0).addParam(att.getValue(0), att.getValue(1));

        }
        if (name.equals("BOUNDARY")) {

            ConfigurationProfile boundaryProfile = new ConfigurationProfile(att.getValue(0), "boundary");
            currentConfigurationProfile_.addConfigurationProfile(0, boundaryProfile);
        }


    }

    public void characters(char[] buff, int offset, int len) throws
            SAXException {

        String data = new String(buff, offset, len);
        data =
                data.trim();

        if (data.length() != 0) {

            if (currentElement_.equals("REALVECTOR")) {
                tempVector_ = new RealVector(data);

            }

        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {

        if (name.equals("PHYSICS")) {
            RPnConfig.addConfiguration(currentConfigurationProfile_.getName(), currentConfigurationProfile_);
            configurationProfile_.add(currentConfigurationProfile_);
        }


        if (name.equals("VIEWINGPARAMS")) {
            getVisualizationProfiles().add(currentVisualizationProfile_);
        }

        if (name.equals("METHOD")) {

            RPnConfig.addConfiguration(currentConfigurationProfile_.getName(), currentConfigurationProfile_);

        }

    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {

        RPNUMERICS.resetParams();

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

    public static ArrayList<ConfigurationProfile> getPhysicsProfiles() {
        ArrayList<ConfigurationProfile> returned = new ArrayList<ConfigurationProfile>();
        for (ConfigurationProfile configurationProfile : configurationProfile_) {
            if (configurationProfile.getType().equalsIgnoreCase("physics")) {
                returned.add(configurationProfile);
            }
        }

        return returned;
    }

    public static ArrayList<VisualizationProfile> getVisualizationProfiles() {
        return visualizationProfiles_;
    }

    public static ArrayList<ConfigurationProfile> getMethodProfiles() {

        ArrayList<ConfigurationProfile> returned = new ArrayList<ConfigurationProfile>();
        for (ConfigurationProfile configurationProfile : configurationProfile_) {
            if (configurationProfile.getType().equalsIgnoreCase("method")) {
                returned.add(configurationProfile);
            }
        }

        return returned;

    }

    private void checkNumberFormat(String value) {
        try {
            Double teste = new Double(value);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid number format in default values file: " + value);
            System.exit(1);
        }
    }
}
