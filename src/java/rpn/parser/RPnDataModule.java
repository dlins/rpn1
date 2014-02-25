/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import rpn.*;
import rpn.controller.phasespace.*;
import rpn.controller.ui.UIController;
import rpn.command.*;
import rpn.component.*;
import rpn.configuration.Configuration;
import rpnumerics.*;
import wave.multid.Space;
import wave.util.RealVector;
import wave.util.RealSegment;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import rpn.component.RpGeomFactory;
import rpn.message.RPnNetworkStatus;

/**
 * With this class the calculus made in a previous session can be reloaded. A
 * previous state can be reloaded reading a XML file that is used by this class
 */
public class RPnDataModule {

    static public String XML_TAG = "RPNDATA";
    static public String BATCHDATA_XML_TAG = "RPNBATCH_DATA";
    
    static public RPnPhaseSpaceAbstraction PHASESPACE = null;
    static public RPnPhaseSpaceAbstraction LEFTPHASESPACE = null;
    static public RPnPhaseSpaceAbstraction RIGHTPHASESPACE = null;
    static public RPnPhaseSpaceAbstraction RIEMANNPHASESPACE = null;
    static public RPnPhaseSpaceAbstraction[] CHARACTERISTICSPHASESPACEARRAY = null;
    private static HashMap<String, RPnPhaseSpaceAbstraction> phaseSpaceMap_ = new HashMap<String, RPnPhaseSpaceAbstraction>();

    public RPnDataModule() {

        phaseSpaceMap_.put(PHASESPACE.getName(), PHASESPACE);
        phaseSpaceMap_.put(LEFTPHASESPACE.getName(), LEFTPHASESPACE);
        phaseSpaceMap_.put(RIGHTPHASESPACE.getName(), RIGHTPHASESPACE);

    }

    // TODO move out to somewhere else RPNUMERICS
    public static int[] processResolution(String resolution) {

        String[] splitedResolution = resolution.split(" ");
        int[] result = new int[splitedResolution.length];

        try {

            for (int i = 0; i < splitedResolution.length; i++) {
                String string = splitedResolution[i];

                result[i] = new Integer(string);

            }

        } catch (NumberFormatException ex) {
            System.out.println("Error in resolution format !");

        }

        return result;
    }

    public static void updatePhaseSpaces() {

        PHASESPACE.update();
        LEFTPHASESPACE.update();
        RIGHTPHASESPACE.update();

    }

    public static Iterator<RPnPhaseSpaceAbstraction> phaseSpaceIterator() {

        return phaseSpaceMap_.values().iterator();
    }

    public static RPnPhaseSpaceAbstraction getPhaseSpace(String phaseSpaceName) {

        return phaseSpaceMap_.get(phaseSpaceName);

    }

    static protected class RPnDataParser implements ContentHandler {

        private String currentElement_;
        private Attributes currentElementAtt_;
        private ArrayList<RealSegment> curveSegments_ = new ArrayList<RealSegment>();
        private StringBuilder stringBuffer_ = new StringBuilder();
        private String dimension_;
        private String curve_name_;
        private StringBuffer realSegDataBuffer_;
        private Vector orbitPoints_ = new Vector();
        private OrbitPoint startPoint_;
        private Configuration currentConfiguration_;
        // vector for reading the segments data for segmented curves
//        private Vector curveSegmentsCoords_;
        private ArrayList<RealSegment> leftSegmentsCoords_;
        private ArrayList<RealSegment> rightSegmentsCoords_;
        private ArrayList<HugoniotSegment> hugoniotSegments_;
        private boolean isLeft_;
        private ArrayList<RealSegment> realSegments_;
        private boolean isBifuraction_, isTransitionPoints_;
        private boolean isInflectionPoints_;
        private ArrayList<RealVector> inflectionPoint_;
        private ArrayList<RealVector> transitionPoint_;
        private String subCurveType_;
        private WaveCurve waveCurve_;

        public RPnDataParser() {


            stringBuffer_ = new StringBuilder();

            // initialize phase space state
            PHASESPACE = new RPnPhaseSpaceAbstraction("Phase Space",
                    RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),

            // initialize auxiliary phase space state
            RIEMANNPHASESPACE = new RPnPhaseSpaceAbstraction("Riemann Phase Space",
                    new Space("Riemann Space", RPNUMERICS.domainDim() + 1), new NumConfigImpl());
            LEFTPHASESPACE = new RPnLeftPhaseSpaceAbstraction("Left Phase Space",
                    RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),
            RIGHTPHASESPACE = new RPnRightPhaseSpaceAbstraction("Right Phase Space",
                    RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),
            RIEMANNPHASESPACE = new RPnPhaseSpaceAbstraction("Riemann Phase Space",
                    new Space("Riemann Space", RPNUMERICS.domainDim() + 1), new NumConfigImpl());

            CHARACTERISTICSPHASESPACEARRAY = new RPnPhaseSpaceAbstraction[RPNUMERICS.domainDim()];

            for (int i = 0; i < CHARACTERISTICSPHASESPACEARRAY.length; i++) {
                CHARACTERISTICSPHASESPACEARRAY[i] = new RPnPhaseSpaceAbstraction("Characteristics Phase Space",
                        new Space("Characteristics Space: " + i, 2), new NumConfigImpl());

            }

            phaseSpaceMap_.put(PHASESPACE.getName(), PHASESPACE);
            phaseSpaceMap_.put(LEFTPHASESPACE.getName(), LEFTPHASESPACE);
            phaseSpaceMap_.put(RIGHTPHASESPACE.getName(), RIGHTPHASESPACE);

        }

        @Override
        public void endDocument() {


            UIController.instance().setActivePhaseSpace(RPnDataModule.PHASESPACE);
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes att) throws
                SAXException {

            currentElement_ = name;
            currentElementAtt_ = att;

            if (currentElement_.equals(XML_TAG)) {

                RPnCommandModule.RPnCommandParser.selectPhaseSpace(att.getValue("phasespace"));

            } else if (currentElement_.equals(BATCHDATA_XML_TAG)) {

                String batch_id = att.getValue("batchid");
                String fullURL = new String("http://" + RPnNetworkStatus.SERVERNAME + "/data/rpnbatch_" + batch_id + ".rpn");

                URL rpnMediatorURL;

                try {

                    rpnMediatorURL = new URL(fullURL);

                    URLConnection rpnMediatorConn;

                    rpnMediatorConn = rpnMediatorURL.openConnection();
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(rpnMediatorConn.getInputStream()));

                    XMLReader parser = XMLReaderFactory.createXMLReader();

                    parser.setContentHandler(this);

                    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Batch data parsing started... ");
                    parser.parse(new InputSource(buffReader));
                    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Batch data parsing finished successfully... ");

                } catch (MalformedURLException ex) {
                    Logger.getLogger(RPnDataModule.class.getName()).log(Level.SEVERE, null, ex);


                } catch (IOException ex) {
                    Logger.getLogger(RPnDataModule.class.getName()).log(Level.SEVERE, null, ex);
                }
       
            } else if (currentElement_.equals(BifurcationCurve.XML_TAG)) {
                
                leftSegmentsCoords_ = new ArrayList<RealSegment>();
                rightSegmentsCoords_ = new ArrayList<RealSegment>();
                dimension_ = att.getValue("dimension");
                curve_name_ = att.getValue("curve_name");
                isLeft_ = true;
                isBifuraction_ = true;

            } else if (currentElement_.equals(RPnCurve.XML_TAG)) {
                hugoniotSegments_ = new ArrayList<HugoniotSegment>();
                realSegments_ = new ArrayList<RealSegment>();
                orbitPoints_ = new Vector();
                curve_name_ = att.getValue("curve_name");

                if (att.getValue("startpoint") != null) {
                    startPoint_ = new OrbitPoint(new RealVector(att.getValue("startpoint")));
                }


                isBifuraction_ = false;

            } else if (currentElement_.equals("CURVECONFIGURATION")) {

                currentConfiguration_ = rpnumerics.RPNUMERICS.getConfiguration(att.getValue("name"));

            } else if (currentElement_.equals("REALSEG")) {

                String data_1 = att.getValue("coords_1").trim();
                String data_2 = att.getValue("coords_2").trim();



                if ((data_1.length() != 0) && (data_2.length() != 0)) {

                    RealVector p1 = new RealVector(data_1);
                    RealVector p2 = new RealVector(data_2);
                    if (isBifuraction_) {
                        if (isLeft_) {

                            leftSegmentsCoords_.add(new RealSegment(p1, p2));
                        } else {

                            rightSegmentsCoords_.add(new RealSegment(p1, p2));
                        }

                    } else {
                        realSegments_.add(new RealSegment(p1, p2));
                    }


                }

            } else if (currentElement_.equals("HUGONIOTSEG")) {

                RealVector coords_1 = new RealVector(att.getValue("coords_1"));
                RealVector coords_2 = new RealVector(att.getValue("coords_2"));

                double leftSigma = Double.parseDouble(att.getValue("leftsigma"));
                double rightSigma = Double.parseDouble(att.getValue("rightsigma"));

                RealVector leftRealVector = new RealVector(att.getValue("leftlambda"));
                RealVector rightRealVector = new RealVector(att.getValue("rightlambda"));

                double[] leftLambda = leftRealVector.toDouble();
                double[] rightLambda = rightRealVector.toDouble();
                int type = Integer.parseInt(att.getValue("type"));
                String signature = att.getValue("signature");


                HugoniotSegment hugoniotSegment = new HugoniotSegment(coords_1,
                        leftSigma, coords_2, rightSigma, leftLambda[0], leftLambda[1],
                        rightLambda[0], rightLambda[1], type, signature);


                hugoniotSegments_.add(hugoniotSegment);



            } else if (currentElement_.equals("TRANSITIONPOINTS")) {
                isTransitionPoints_ = true;
                transitionPoint_ = new ArrayList<RealVector>();
            } else if (currentElement_.equals("INFLECTIONPOINTS")) {
                isInflectionPoints_ = true;
                inflectionPoint_ = new ArrayList<RealVector>();
            } else if (curve_name_ != null) {
                if (curve_name_.equals(rpnumerics.WaveCurve.class.getSimpleName())) {

                    int family = Integer.parseInt(currentConfiguration_.getParam("family"));
                    int direction = Integer.parseInt(currentConfiguration_.getParam("direction"));
                    waveCurve_ = new WaveCurve(family, direction);

                }

            }

            if (currentElement_.equals("SUBCURVE")) {

                subCurveType_ = att.getValue("name");
            }
            if (currentElement_.equals("ORBITPOINT")) {

                String coords_s = att.getValue("coords").trim();
                String lambda_s = att.getValue("lambda").trim();

                RealVector coords = new RealVector(coords_s);
                double lambda = Double.parseDouble(lambda_s);

                OrbitPoint orbitPoint = new OrbitPoint(coords, lambda);

                if (isInflectionPoints_) {

                    inflectionPoint_.add(orbitPoint.getCoords());
                } else if (isTransitionPoints_) {
                    transitionPoint_.add(orbitPoint.getCoords());
                } else {
                    orbitPoints_.add(orbitPoint);
                }


            }


        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {

            currentElement_ = name;

            if (currentElement_.equals(BifurcationCurve.LEFT_TAG)) {
                isLeft_ = false;
            }
            if (currentElement_.equals("TRANSITIONPOINTS")) {
                isTransitionPoints_ = false;
            }

            if (currentElement_.equals("INFLECTIONPOINTS")) {
                isInflectionPoints_ = false;
            }

            if (currentElement_.equals("SUBCURVE")) {
                OrbitPoint[] orbitPointsArray = new OrbitPoint[orbitPoints_.size()];

                for (int i = 0; i < orbitPoints_.size(); i++) {
                    orbitPointsArray[i] = (OrbitPoint) orbitPoints_.elementAt(i);
                }



                if (subCurveType_.equals(rpnumerics.RarefactionCurve.class.getSimpleName())) {

                    RarefactionCurve curve = new RarefactionCurve(orbitPointsArray,
                            Integer.parseInt(currentConfiguration_.getParam("family")),
                            Integer.parseInt(currentConfiguration_.getParam("direction")));
                    waveCurve_.add(curve);


                } else if (subCurveType_.equals(rpnumerics.ShockCurve.class.getSimpleName())) {

                    ShockCurve curve = new ShockCurve(orbitPointsArray, Integer.parseInt(currentConfiguration_.getParam("family")),
                            Integer.parseInt(currentConfiguration_.getParam("direction")));
                    waveCurve_.add(curve);

                } else if (subCurveType_.equals(rpnumerics.CompositeCurve.class.getSimpleName())) {

                    CompositeCurve curve = new CompositeCurve(orbitPointsArray, Integer.parseInt(currentConfiguration_.getParam("direction")), Integer.parseInt(currentConfiguration_.getParam("family")));
                    waveCurve_.add(curve);
                }
                orbitPoints_.clear();
            }



            if (currentElement_.equals(BifurcationCurve.XML_TAG)) {


                if (curve_name_.equals(rpnumerics.InflectionCurve.class.getSimpleName())) {

                    InflectionCurve curve = new InflectionCurve(leftSegmentsCoords_);
                    InflectionCurveGeomFactory factory =
                            new InflectionCurveGeomFactory(RPNUMERICS.createInflectionCurveCalc(currentConfiguration_), curve);
                    InflectionPlotCommand.instance().execute(factory);

                } else if (curve_name_.equals(rpnumerics.DoubleContactCurve.class.getSimpleName())) {

                    DoubleContactCurve curve = new DoubleContactCurve(leftSegmentsCoords_, rightSegmentsCoords_);
                    DoubleContactGeomFactory factory =
                            new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc(currentConfiguration_), curve);
                    DoubleContactCommand.instance().execute(factory);

                }

                if (curve_name_.equals(rpnumerics.EllipticBoundaryExtension.class.getSimpleName())) {

                    EllipticBoundaryExtension curve = new EllipticBoundaryExtension(leftSegmentsCoords_);
                    EllipticBoundaryExtensionFactory factory =
                            new EllipticBoundaryExtensionFactory(RPNUMERICS.createEllipticBoundaryExtensionCalc(currentConfiguration_), curve);
                    EllipticBoundaryExtensionCommand.instance().execute(factory);

                }

                if (curve_name_.equals(rpnumerics.EnvelopeCurve.class.getSimpleName())) {

                    EnvelopeCurve curve = new EnvelopeCurve(leftSegmentsCoords_, rightSegmentsCoords_);
                    EnvelopeGeomFactory factory =
                            new EnvelopeGeomFactory(RPNUMERICS.createEnvelopeCurveCalc(currentConfiguration_), curve);
                    EnvelopeCurveCommand.instance().execute(factory);

                }



                if (curve_name_.equals(rpnumerics.CoincidenceCurve.class.getSimpleName())) {

                     int resolution[] = processResolution(currentConfiguration_.getParam("resolution"));
                    ContourParams params = new ContourParams(resolution);
                    
                    
                    CoincidenceCurve curve = new CoincidenceCurve(leftSegmentsCoords_);


                    CoincidenceCurveCalc calc = new CoincidenceCurveCalc(params);

                    CoincidenceCurveGeomFactory factory =
                            new CoincidenceCurveGeomFactory(calc, curve);
                    CoincidencePlotCommand.instance().execute(factory);

                }


                if (curve_name_.equals(rpnumerics.SecondaryBifurcationCurve.class.getSimpleName())) {

                    SecondaryBifurcationCurve curve = new SecondaryBifurcationCurve(leftSegmentsCoords_, rightSegmentsCoords_);
                    SecondaryBifurcationGeomFactory factory =
                            new SecondaryBifurcationGeomFactory(RPNUMERICS.createSecondaryBifurcationCurveCalc(currentConfiguration_), curve);
                    SecondaryBifurcationCurveCommand.instance().execute(factory);

                }

                if (curve_name_.equals(rpnumerics.BoundaryExtensionCurve.class.getSimpleName())) {

                    BoundaryExtensionCurve curve = new BoundaryExtensionCurve(leftSegmentsCoords_, rightSegmentsCoords_);
                    BoundaryExtensionCurveGeomFactory factory =
                            new BoundaryExtensionCurveGeomFactory(RPNUMERICS.createBoundaryExtensionCurveCalc(currentConfiguration_), curve);
                    BoundaryExtensionCurveCommand.instance().execute(factory);

                }

                if (curve_name_.equals(rpnumerics.HysteresisCurve.class.getSimpleName())) {

                    HysteresisCurve curve = new HysteresisCurve(leftSegmentsCoords_, rightSegmentsCoords_);
                    HysteresisCurveGeomFactory factory =
                            new HysteresisCurveGeomFactory(RPNUMERICS.createHysteresisCurveCalc(currentConfiguration_), curve);
                    HysteresisPlotCommand.instance().execute(factory);

                }

                if (curve_name_.equals(rpnumerics.EllipticBoundary.class.getSimpleName())) {

                    EllipticBoundary curve = new EllipticBoundary(leftSegmentsCoords_);
                    EllipticBoundaryFactory factory =
                            new EllipticBoundaryFactory(RPNUMERICS.createEllipticBoundaryCalc(currentConfiguration_), curve);
                    EllipticBoundaryCommand.instance().execute(factory);

                }




                if (curve_name_.equals(rpnumerics.BuckleyLeverettInflectionCurve.class.getSimpleName())) {

                    int resolution[] = processResolution(currentConfiguration_.getParam("resolution"));
                    ContourParams params = new ContourParams(resolution);
                    BuckleyLeverettInflectionCurve curve = new BuckleyLeverettInflectionCurve(leftSegmentsCoords_);
                    BuckleyLeverettinCurveGeomFactory factory = new BuckleyLeverettinCurveGeomFactory(new BuckleyLeverettinInflectionCurveCalc(params), curve);
                    BuckleyLeverettiInflectionCommand.instance().execute(factory);

                }




                if (curve_name_.equals(rpnumerics.SubInflectionCurve.class.getSimpleName())) {


                    SubInflectionCurve curve = new SubInflectionCurve(leftSegmentsCoords_);
                    int resolution[] = processResolution(currentConfiguration_.getParam("resolution"));
                    ContourParams params = new ContourParams(resolution);
                    SubInflectionCurveGeomFactory factory =
                            new SubInflectionCurveGeomFactory(new SubInflectionCurveCalc(params), curve);
                    SubInflectionPlotCommand.instance().execute(factory);

                }






            } else if (currentElement_.equals(RPnCurve.XML_TAG)) {

                OrbitPoint[] orbitPointsArray = new OrbitPoint[orbitPoints_.size()];

                for (int i = 0; i < orbitPoints_.size(); i++) {
                    orbitPointsArray[i] = (OrbitPoint) orbitPoints_.elementAt(i);
                }




                if (curve_name_.equals(rpnumerics.RarefactionCurve.class.getSimpleName())) {

                    RarefactionCurve curve = new RarefactionCurve(orbitPointsArray,
                            Integer.parseInt(currentConfiguration_.getParam("family")),
                            Integer.parseInt(currentConfiguration_.getParam("direction")));
                    RarefactionCurveGeomFactory factory =
                            new RarefactionCurveGeomFactory(RPNUMERICS.createRarefactionCalc(currentConfiguration_, startPoint_), curve);
                    RarefactionCurvePlotCommand.instance().execute(factory);

                }


                if (curve_name_.equals(rpnumerics.ShockCurve.class.getSimpleName())) {

                    ShockCurve curve = new ShockCurve(orbitPointsArray, Integer.parseInt(currentConfiguration_.getParam("family")),
                            Integer.parseInt(currentConfiguration_.getParam("direction")));

                    ShockCurveGeomFactory factory =
                            new ShockCurveGeomFactory(RPNUMERICS.createShockCurveCalc(startPoint_, currentConfiguration_), curve);
                    ShockCurvePlotCommand.instance().execute(factory);

                }



                if (curve_name_.equals(rpnumerics.CompositeCurve.class.getSimpleName())) {

                    CompositeCurve curve = new CompositeCurve(orbitPointsArray, Integer.parseInt(currentConfiguration_.getParam("direction")), Integer.parseInt(currentConfiguration_.getParam("family")));

                    CompositeGeomFactory factory =
                            new CompositeGeomFactory(RPNUMERICS.createCompositeCalc(startPoint_, currentConfiguration_), curve);
                    CompositePlotCommand.instance().execute(factory);

                }


                if (curve_name_.equals(rpnumerics.HugoniotCurve.class.getSimpleName())) {


                    HugoniotCurve curve = new HugoniotCurve(startPoint_, hugoniotSegments_, transitionPoint_);
                    int direction = Integer.parseInt(currentConfiguration_.getParam("direction"));

                    int[] resolution = processResolution(currentConfiguration_.getParam("resolution"));
                    HugoniotParams params = new HugoniotParams(startPoint_, direction, resolution);

                    HugoniotCurveCalcND calc = new HugoniotCurveCalcND(params);

                    HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(calc, curve);

                    HugoniotContinuationPlotCommand.instance().execute(factory);

                }

                if (curve_name_.equals(rpnumerics.LevelCurve.class.getSimpleName())) {

                    int family = Integer.parseInt(currentConfiguration_.getParam("family"));
                    double level = Double.parseDouble(currentConfiguration_.getParam("level"));
                    int[] resolution = processResolution(currentConfiguration_.getParam("resolution"));
                    ContourParams params = new ContourParams(resolution);
                    LevelCurve curve = new LevelCurve(family, realSegments_, level);
                    LevelCurveCalc calc = null;
                    if (startPoint_ != null) {

                        calc = new PointLevelCalc(startPoint_, family, params);
                        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(calc, curve);
                        PointLevelCurvePlotCommand.instance().execute(factory);


                    } else {
                        calc = new LevelCurveCalc(family, level, params);
                        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(calc, curve);
                        LevelCurvePlotCommand.instance().execute(factory);
                    }



                }

                if (curve_name_.equals(rpnumerics.IntegralCurve.class.getSimpleName())) {
                    int familyIndex = Integer.parseInt(currentConfiguration_.getParam("family"));
                    IntegralCurve curve = new IntegralCurve(orbitPointsArray, familyIndex, inflectionPoint_);
                    IntegralCurveCalc calc = new IntegralCurveCalc(startPoint_, familyIndex);
                    IntegralOrbitGeomFactory factory = new IntegralOrbitGeomFactory(calc, curve);
                    IntegralCurvePlotCommand.instance().execute(factory);

                }
                if (curve_name_.equals(rpnumerics.WaveCurve.class.getSimpleName())) {

                    WaveCurveCalc calc = new WaveCurveCalc(startPoint_, Integer.parseInt(currentConfiguration_.getParam("family")),
                            Integer.parseInt(currentConfiguration_.getParam("direction")));

                    WaveCurveGeomFactory factory = new WaveCurveGeomFactory(calc);

                    WaveCurvePlotCommand.instance().execute(factory);

                }



                if (curve_name_.equals(rpnumerics.RarefactionExtensionCurve.class.getSimpleName())) {

                    int[] resolution = processResolution(currentConfiguration_.getParam("resolution"));
                    ContourParams params = new ContourParams(resolution);


                    RarefactionExtensionCalc calc = new RarefactionExtensionCalc(params, startPoint_,
                            Integer.parseInt(currentConfiguration_.getParam("direction")),
                            Integer.parseInt(currentConfiguration_.getParam("curvefamily")),
                            Integer.parseInt(currentConfiguration_.getParam("extensionfamily")),
                            Integer.parseInt(currentConfiguration_.getParam("characteristic")));

                    RarefactionExtensionCurve curve = new RarefactionExtensionCurve(realSegments_, realSegments_);
                    RarefactionExtensionGeomFactory factory = new RarefactionExtensionGeomFactory(calc, curve);

                    RarefactionExtensionCurvePlotCommand.instance().execute(factory);

                }



            }
        }

        public void setDocumentLocator(Locator locator) {
        }

        public void startDocument() throws SAXException {
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
    }

    //
    // Initializers
    //
    /**
     * Initializes the XML parser to reload a previous session.
     */
    public static void init(XMLReader parser, String configFile) {
        try {
            parser.setContentHandler(new RPnDataParser());
            System.out.println("Data Module parsing started...");
            parser.parse(configFile);
            System.out.println("Data Module parsing finshed sucessfully !");
        } catch (Exception saxex) {

            saxex.printStackTrace();

        }
    }

    /**
     * Initializes the XML parser to reload a previous session.
     */
    public static void init(XMLReader parser, InputStream configFileStream) {
        try {
            parser.setContentHandler(new RPnDataParser());
            System.out.println("Data Module parsing started...");
            parser.parse(new InputSource((configFileStream)));
            System.out.println("Data Module parsing finished sucessfully !");

        } catch (Exception saxex) {

            if (saxex instanceof org.xml.sax.SAXParseException) {
                System.out.println("Line: "
                        + ((org.xml.sax.SAXParseException) saxex).getLineNumber());
                System.out.println("Column: "
                        + ((org.xml.sax.SAXParseException) saxex).getColumnNumber());
            }

            saxex.printStackTrace();
        }
    }

    //
    // Methods
    //
    /**
     * Writes the data of actual session into a XML file.
     */
    static public void matlabExport(FileWriter writer) throws java.io.IOException {

        Iterator<RpGeometry> iterator = PHASESPACE.getGeomObjIterator();
        writer.write("close all; clear all;\n");
        //writer.write(RpCalcBasedGeomFactory.createMatlabColorTable());

        //Inserting data
        HashMap<Integer, RpGeometry> visibleGeometries = new HashMap<Integer, RpGeometry>();
        int geometryCounter = 0;

        while (iterator.hasNext()) {
            RpGeometry geometry = iterator.next();
            if (geometry.viewingAttr().isVisible()) {
                visibleGeometries.put(geometryCounter, geometry);
                geometryCounter++;
            }

        }

        // Inserting data
        Set<Entry<Integer, RpGeometry>> geometrySet = visibleGeometries.entrySet();

        for (Entry<Integer, RpGeometry> entry : geometrySet) {

            RPnCurve curve = (RPnCurve) entry.getValue().geomFactory().geomSource();

            if (curve instanceof SegmentedCurve) {

                SegmentedCurve sCurve = (SegmentedCurve) curve;
                writer.write(sCurve.toMatlabData(entry.getKey()));

            } else {
                Orbit orbit = (Orbit) curve;
                //writer.write(orbit.toMatlabData(entry.getKey()));
            }

        }

        ////// TESTAR COMENTADO NA STONE ---
        //Plotting 3D view
        writer.write("%% plotting 3D \n");
        writer.write("figure(1)\n");


        for (Entry<Integer, RpGeometry> entry : geometrySet) {

            RPnCurve curve = (RPnCurve) entry.getValue().geomFactory().geomSource();

            if (curve instanceof SegmentedCurve) {

                SegmentedCurve sCurve = (SegmentedCurve) curve;

                writer.write(sCurve.createSegment3DPlotMatlabPlot(entry.getKey()));       // TESTAR COMENTADO NA STONE

            } else {
                Orbit orbit = (Orbit) curve;
//                writer.write(orbit.createPoint3DMatlabPlot(entry.getKey()));
            }
        }
        ////// ---

        //Plotting 2D view
        writer.write("%% plotting 2D\n");

        //****  (Leandro)
        writer.write("disp('Digite 1 para imprimir as strings de classificacao; 0 caso contrario.')\n");
        writer.write("bool = input('Voce quer imprimir as strings? : ');\n");

        writer.write("disp('Digite 1 para imprimir as velocidades; 0 caso contrario.')\n");
        writer.write("bool2 = input('Voce quer imprimir as velocidades? : ');\n");
        //*****************

        for (Entry<Integer, RpGeometry> entry : geometrySet) {

            RPnCurve curve = (RPnCurve) entry.getValue().geomFactory().geomSource();

            if (curve instanceof SegmentedCurve) {                              //** Alterei os eixos (Leandro)

                writer.write("figure(2)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(1, 0, entry.getKey()));

                ////// TESTAR COMENTADO NA STONE
                writer.write("figure(3)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(2, 0, entry.getKey()));
                writer.write("figure(4)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(1, 2, entry.getKey()));
                //////

            } else {
//                Orbit orbit = (Orbit) curve;
////                writer.write("figure(2)\n");
////                writer.write(orbit.create2DPointMatlabPlot(0, 1, entry.getKey()));
////                writer.write("figure(4)\n");
////                writer.write("figure(3)\n");
////                writer.write(orbit.create2DPointMatlabPlot(0, 2, entry.getKey()));
////                writer.write(orbit.create2DPointMatlabPlot(1, 2, entry.getKey()));
//
//                writer.write("figure(2)\n");
//                writer.write(orbit.create2DPointMatlabPlot(1, 0, entry.getKey()));
//                writer.write("figure(3)\n");
//                writer.write(orbit.create2DPointMatlabPlot(2, 0, entry.getKey()));
//                writer.write("figure(4)\n");
//                writer.write(orbit.create2DPointMatlabPlot(1, 2, entry.getKey()));
            }

        }

    }

    // ---------- 15JAN Método para exportar dados 2D e gerar script Matlab (apenas para físicas 2D)
    // --- Isso foi feito para atender a uma necessidade emergencial do Cido
    static public void matlabExport2D(FileWriter writer) throws java.io.IOException {

        RPnPhaseSpaceAbstraction phaseSpace = null;
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.PHASESPACE.getName())) {
            phaseSpace = RPnDataModule.PHASESPACE;
        }
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName())) {
            phaseSpace = RPnDataModule.RIGHTPHASESPACE;
        }
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName())) {
            phaseSpace = RPnDataModule.LEFTPHASESPACE;
        }
        Iterator<RpGeometry> iterator = phaseSpace.getGeomObjIterator();

        writer.write("close all; clear all;\n");

        //Inserting data
        HashMap<Integer, RpGeometry> visibleGeometries = new HashMap<Integer, RpGeometry>();
        int geometryCounter = 0;

        while (iterator.hasNext()) {
            RpGeometry geometry = iterator.next();
            if (geometry.viewingAttr().isVisible()) {
                visibleGeometries.put(geometryCounter, geometry);
                geometryCounter++;
            }

        }

        // Inserting data
        Set<Entry<Integer, RpGeometry>> geometrySet = visibleGeometries.entrySet();

        for (Entry<Integer, RpGeometry> entry : geometrySet) {

            RPnCurve curve = (RPnCurve) entry.getValue().geomFactory().geomSource();
            //toMatlabReadFile();

            if (curve instanceof SegmentedCurve) {
                SegmentedCurve sCurve = (SegmentedCurve) curve;
                writer.write(sCurve.toMatlabData2D(entry.getKey(), phaseSpace));
            }
            if (curve instanceof FundamentalCurve) {
                FundamentalCurve fundamental = (FundamentalCurve) curve;
                writer.write(fundamental.toMatlabData2D(entry.getKey()));
            }
            if (curve instanceof WaveCurve) {
                WaveCurve wave = (WaveCurve) curve;
                writer.write(wave.toMatlabData2D(entry.getKey()));
            }

        }

        //Plotting 2D view
        writer.write("%% plotting 2D\n");

        for (Entry<Integer, RpGeometry> entry : geometrySet) {

            RPnCurve curve = (RPnCurve) entry.getValue().geomFactory().geomSource();

            writer.write("figure(1)\n");

            if (curve instanceof SegmentedCurve) {
                SegmentedCurve sCurve = (SegmentedCurve) curve;
                writer.write(sCurve.createSegmentedMatlabPlotLoop2D(0, 1, entry.getKey()));
            }
            if (curve instanceof FundamentalCurve) {
                FundamentalCurve fundamental = (FundamentalCurve) curve;
                writer.write(fundamental.create2DPointMatlabPlot(0, 1, entry.getKey()));
            }
            if (curve instanceof WaveCurve) {
                WaveCurve wave = (WaveCurve) curve;
                writer.write(wave.create2DPointMatlabPlot(0, 1, entry.getKey()));
            }

        }
    }
    // ----------

    static public void export(FileWriter writer) throws java.io.IOException {


        // TODO : this is working only for PHASESPACE ... must be implemented to all
        // others phasespaces (L , R etc...)
        Iterator<RpGeometry> iterator = PHASESPACE.getGeomObjIterator();

        // selecting visible data
        HashMap<Integer, RpGeometry> visibleGeometries = new HashMap<Integer, RpGeometry>();
        int geometryCounter = 0;

        while (iterator.hasNext()) {

            RpGeometry geometry = iterator.next();
            if (geometry.viewingAttr().isVisible()) {
                visibleGeometries.put(geometryCounter, geometry);
                geometryCounter++;
            }

        }

        writer.write("<" + XML_TAG + " phasespace=" + '\"' + PHASESPACE.getName() + '\"' + ">\n");

        // writing RpSolution data
        Set<Entry<Integer, RpGeometry>> visibleGeometrySet = visibleGeometries.entrySet();

        for (Entry<Integer, RpGeometry> entry : visibleGeometrySet) {

            RpGeomFactory factory = (RpGeomFactory) entry.getValue().geomFactory();
            writer.write(factory.toXML() + "\n");
        }

        writer.write("</" + XML_TAG + ">\n");

    }
}
