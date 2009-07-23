/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;


import rpnumerics.RPNumericsProfile;
import wave.util.RealVector;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.xml.sax.ContentHandler;
import rpn.RPnConfig;
import rpnumerics.RPNUMERICS;

/** This class implements methods to configure the numeric layer. The values are taked from a XML file and this values are used to setup the physics and all others numerics parameters. */
public class RPnInterfaceParser implements ContentHandler {
    //
    // Constants
    //
    private static RPNumericsProfile profile_ = new RPNumericsProfile();
    private static ArrayList<PhysicsProfile> physics_ = new ArrayList<PhysicsProfile>(2);
    private static ArrayList<MethodProfile> methods_ = new ArrayList<MethodProfile>(2);
    private static ArrayList<VisualizationProfile> visualizationProfiles_ = new ArrayList<VisualizationProfile>(2);
    private static PhysicsProfile currentPhysicsProfile_;
    private static MethodProfile currentMethodProfile_;    //
    private static VisualizationProfile currentVisualizationProfile_;    // Members
    //
    private RealVector tempVector_;
    private String currentElement_;


    @Override
    public void startElement(String uri, String localName, String name, Attributes att) throws SAXException {

        currentElement_ = name;

        if (name.equals("PHYSICS")) {

            PhysicsProfile physicsProfile = new PhysicsProfile();
            currentPhysicsProfile_ = physicsProfile;
            currentPhysicsProfile_.setName(att.getValue("name"));

        }

        if (name.equals("FLUXPARAMS")) {
            currentPhysicsProfile_.addFluxParam(att.getValue("name"), att.getValue("value"), new Integer(att.getValue("position")));
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
            MethodProfile methodProfile = new MethodProfile(att.getValue("name"));
            currentMethodProfile_ = methodProfile;

        }
        
        if (name.equals("METHODPARAM")){
            currentMethodProfile_.addParam(att.getValue("name"), att.getValue("value"));
        }


        if (name.equals("REALVECTOR")) {
            tempVector_ = new RealVector(att.getValue("dimension"));
        }

        if (name.equals("BOUNDARY")) {

            currentPhysicsProfile_.setBoundaryDimension(new Integer(att.getValue("dimension")));
            
            if (att.getValue("boundary").equals("rect")){
                
                currentPhysicsProfile_.setIso2equiBoundary(false);
            }

            if (att.getValue("boundary").equals("triang")) {

                currentPhysicsProfile_.setIso2equiBoundary(true);
            }


        }



    }

    public void characters(char[] buff, int offset, int len) throws
            SAXException {

        String data = new String(buff, offset, len);
        data = data.trim();

        if (data.length() != 0) {

            if (currentElement_.equals("REALVECTOR")) {
                tempVector_ = new RealVector(data);

            }

        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {

        if (name.equals("PHYSICS")) {
            physics_.add(currentPhysicsProfile_);
        }


        if (name.equals("BOUNDARY")) {

            StringTokenizer axisTokenizer = new StringTokenizer(tempVector_.toString());
            String[] dataArray = new String[axisTokenizer.countTokens()];
            int i = 0;
            while (axisTokenizer.hasMoreTokens()) {
                dataArray[i++] = axisTokenizer.nextToken();
            }

            currentPhysicsProfile_.setBoundary(dataArray);

        }

        if (name.equals("VIEWINGPARAMS")) {
            getVisualizationProfiles().add(currentVisualizationProfile_);
        }

        if (name.equals("METHOD")) {
            
            RPnConfig.addMethod(currentMethodProfile_.getName(), currentMethodProfile_);
            
        }
        
        

    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {

        RPNUMERICS.resetMethodsParams();

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

    public static ArrayList<PhysicsProfile> getPhysicsProfiles() {
        return physics_;
    }

    public static ArrayList<VisualizationProfile> getVisualizationProfiles() {
        return visualizationProfiles_;
    }
    public static ArrayList<MethodProfile> getMethodProfiles(){
        return methods_;
    }
}
