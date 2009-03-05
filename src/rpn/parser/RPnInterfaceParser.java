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
import java.util.Iterator;
import java.util.StringTokenizer;
import org.xml.sax.ContentHandler;

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
    private ArrayList<String> boundsVectorArray_;

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


        if (name.equals("PROJDESC")) {

            int[] viewPort = new int[2];

            viewPort[0] = new Integer(att.getValue("vpwidth"));
            viewPort[0] = new Integer(att.getValue("vpheight"));
            currentVisualizationProfile_.setViewPort(viewPort);
            StringTokenizer axisTokenizer = new StringTokenizer(att.getValue("projaxisid"));
            int[] projIndices = new int[axisTokenizer.countTokens()];
            int i = 0;
            while (axisTokenizer.hasMoreTokens()) {
                projIndices[i++] = new Integer(axisTokenizer.nextToken()).intValue();
            }
            currentVisualizationProfile_.setProjectionAxis(projIndices);
        }

        if (name.equals("ISO2EQUI_TRANSFORM")) {

            if (att.getValue("value").equals("no")) {
                currentVisualizationProfile_.setIsoEquiTransform(false);
            }
            if (att.getValue("value").equals("yes")) {
                currentVisualizationProfile_.setIsoEquiTransform(true);
            }
        }


        if (name.equals("METHOD")) {
            MethodProfile methodProfile = new MethodProfile();
            currentMethodProfile_ = methodProfile;
            currentMethodProfile_.setName(att.getValue("name"));

        }


        if (name.equals("REALVECTOR")) {
            tempVector_ = new RealVector(att.getValue("dimension"));
        }

        if (name.equals("BOUNDARY")) {
            boundsVectorArray_ = new ArrayList();
            currentPhysicsProfile_.setBoundaryDimension(new Integer(att.getValue("dimension")));
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

//                if (boundsVectorArray_.size() == 2) {
//                    profile_.setBoundary(new RectBoundary((RealVector) boundsVectorArray_.get(0), (RealVector) boundsVectorArray_.get(1)));
//                }
//
//                if (boundsVectorArray_.size() == 3) {
//                    profile_.setBoundary(new IsoTriang2DBoundary((RealVector) boundsVectorArray_.get(0), (RealVector) boundsVectorArray_.get(1), (RealVector) boundsVectorArray_.get(2)));
//                }
        }

        if (name.equals("VIEWINGPARAMS")) {
            getVisualizationProfiles().add(currentVisualizationProfile_);
        }


    }
    //
    // Members
    //
    //
    // Initializers
    //
//    public static void init(Parser parser, String file) {
//        try {
//            parser.setDocumentHandler(new InputHandler());
//            parser.parse(file);
//        } catch (Exception saxex) {
//            saxex.printStackTrace();
//        }
//    }

//    public static void init(Parser parser, InputStream configFileStream) {
//        try {
//
//            parser.setDocumentHandler(new InputHandler());
//            System.out.println("Interface Module");
//
//            System.out.println("Will parse !");
//            parser.parse(new InputSource(configFileStream));
//
//            System.out.println("parsed !");
//
//        } catch (Exception saxex) {
//
//            if (saxex instanceof SAXParseException) {
//
//                SAXParseException e = (SAXParseException) saxex;
//
//                System.out.println("Line error: " + e.getLineNumber());
//                System.out.println("Column error: " + e.getColumnNumber());
//            }
//
//            saxex.printStackTrace();
//        }
//    }
    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {

//        System.out.println("Fisicas encontradas: " + physics_.size());
//
//        if (physics_.size() >= 0) {
//
//            Iterator<PhysicsProfile> physicsIterator = physics_.iterator();
//
//            while (physicsIterator.hasNext()) {
//
//                PhysicsProfile profile = physicsIterator.next();
//
//                System.out.println("Fisica: " + profile.getName());
//
//                System.out.print("Boundary: ");
//                for (int i = 0; i < profile.getBoundary().length; i++) {
//
//                    System.out.print(profile.getBoundary()[i]);
//
//
//                }
//                System.out.println();


//                HashMap<String, String> fluxParams = profile.getFluxParamArrayList();
//
//                Set<Entry<String, String>> entrySet = fluxParams.entrySet();
//
//                Iterator<Entry<String, String>> setIterator = entrySet.iterator();
//
//                while (setIterator.hasNext()) {
//
//
//                    Entry<String, String> entry = setIterator.next();
//
//                    System.out.println("Parametro: " + entry.getKey() + " Valor: " + entry.getValue());
//
//                }

//            }



//        }







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
}
