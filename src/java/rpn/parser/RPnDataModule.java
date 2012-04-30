/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.parser;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import rpn.*;
import rpn.controller.phasespace.*;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.multid.CoordsArray;
import wave.util.RealSegment;
import rpn.component.OrbitGeom;
import rpn.component.XZeroGeom;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpn.component.BoundaryExtensionCurveGeomFactory;
import rpn.component.CompositeGeomFactory;
import rpn.component.DoubleContactGeomFactory;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.component.HysteresisCurveGeomFactory;
import rpn.component.InflectionCurveGeomFactory;
import rpn.component.IntegralOrbitGeomFactory;
import rpn.component.LevelCurveGeomFactory;
import rpn.component.RarefactionExtensionGeomFactory;
import rpn.component.RarefactionOrbitGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.ShockCurveGeomFactory;
import rpnumerics.ContourParams;
import rpnumerics.BoundaryExtensionCurve;
import rpnumerics.BoundaryExtensionCurveCalc;
import rpnumerics.CompositeCalc;
import rpnumerics.DoubleContactCurve;
import rpnumerics.DoubleContactCurveCalc;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotCurveCalc;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.HugoniotParams;
import rpnumerics.HugoniotSegment;
import rpnumerics.HysteresisCurve;
import rpnumerics.HysteresisCurveCalc;
import rpnumerics.InflectionCurve;
import rpnumerics.InflectionCurveCalc;
import rpnumerics.IntegralCurveCalc;
import rpnumerics.LevelCurveCalc;
import rpnumerics.PhasePoint;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.PointLevelCalc;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.RarefactionExtensionCalc;
import rpnumerics.RarefactionOrbitCalc;
import rpnumerics.RpCalculation;
import rpnumerics.SegmentedCurve;
import rpnumerics.ShockCurveCalc;
import wave.multid.Space;

/** With this class the calculus made in a previous session can be reloaded. A previous state can be reloaded reading a XML file that is used by this class */
public class RPnDataModule {

    static public RPnPhaseSpaceAbstraction PHASESPACE = null;
    static public RPnPhaseSpaceAbstraction AUXPHASESPACE = null;
    static public RPnPhaseSpaceAbstraction LEFTPHASESPACE = null;
    static public RPnPhaseSpaceAbstraction RIGHTPHASESPACE = null;
    public static Orbit ORBIT = null;
    public static boolean RESULTS = false;
    private static HugoniotCurve hugoniotCurve_;
    protected static PhasePoint XZERO;

    public static HugoniotCurve getHugoniotCurve() {
        return hugoniotCurve_;
    }

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
            ex.printStackTrace();
        }
        return result;

    }


    public static void updatePhaseSpaces(){
        PHASESPACE.update();
        LEFTPHASESPACE.update();
        RIGHTPHASESPACE.update();
    }

    static protected class InputHandler implements ContentHandler {
        // for PoincareData

        protected static ArrayList pPointList_, orbitPointsList_, vectorList_;
        protected static List<HugoniotSegment> hugoniotSegmentsList_;
        protected static List<RealSegment> realSegmentsList_;
        private String currentElement_;
        protected static RealMatrix2 tempMatrix_;
        protected static RealVector tempVector_, point1_, point2_;
        protected static RealVector[] vectorArray_;
        protected static XZeroGeom xZeroGeom_;
        protected static CoordsArray[] tempCoords_;
        protected static OrbitGeom tempOrbit_;
        protected static PhasePoint tempPoint_, tempPhasePoint_;
        protected static int phaseSize_, direction_, family_, ncol_, nrow_;
        private double lambda_;
        private HugoniotParser hugolistener_;
        private OrbitParser orbitListener_;
        private StationaryPointParser statPointListener_;
        private ManifoldParser manifoldListener_;
        private PoincareParser poincareListener_;
        private ConnectionOrbitCalcParser connectionCalcListener_;
        private OrbitCalcParser orbitCalcListener_;
        private ShockFlowParser shockFlowParser_;
        private ManifoldCalcParser manifoldCalcParser_;
        protected static boolean pointOneOK_ = false, calcReady_, plotProfile_;
        private String currentCommand_;
        private RpCalculation calc_;
        private RpGeomFactory factory_;
        private StringBuilder stringBuffer_ = new StringBuilder();
        private String level_;

        public InputHandler() {
            orbitListener_ = new OrbitParser();
            hugolistener_ = new HugoniotParser(new RPnDataModule(), tempPoint_);

            statPointListener_ = new StationaryPointParser();
            manifoldListener_ = new ManifoldParser();
            poincareListener_ = new PoincareParser();
            shockFlowParser_ = new ShockFlowParser();
            connectionCalcListener_ = new ConnectionOrbitCalcParser();
            orbitCalcListener_ = new OrbitCalcParser();
            manifoldCalcParser_ = new ManifoldCalcParser();
            stringBuffer_ = new StringBuilder();

            pPointList_ = new ArrayList();
            hugoniotSegmentsList_ = new ArrayList();
            realSegmentsList_ = new ArrayList();
            vectorList_ = new ArrayList();
            orbitPointsList_ = new ArrayList();
            ManifoldParser.manifoldNumber = 0;

            plotProfile_ = false;
            calcReady_ = false;
            // initialize phase space state
            PHASESPACE = new RPnPhaseSpaceAbstraction("Phase Space",
                    RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),
            // initialize auxiliary phase space state
            AUXPHASESPACE = new RPnPhaseSpaceAbstraction("Auxiliary Phase Space",
                    new Space("Auxiliary Space", RPNUMERICS.domainDim() * 2), new NumConfigImpl());
            LEFTPHASESPACE = new RPnLeftPhaseSpaceAbstraction("LeftPhase Space",
                    RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),
            RIGHTPHASESPACE = new RPnRightPhaseSpaceAbstraction("RightPhase Space",
                    RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),


        }

        @Override
        public void endDocument() {
//            System.out.println("Fim do documento");//TODO Set the initial state here
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes att) throws
                SAXException {
            currentElement_ = name;

            if (name.equalsIgnoreCase("COMMAND")) {
                currentCommand_ = att.getValue("name");
                hugoniotSegmentsList_.clear();
                realSegmentsList_.clear();
                orbitPointsList_.clear();

                if (currentCommand_.equalsIgnoreCase("hugoniotcurve")) {
                    HugoniotParams params = new HugoniotParams(new PhasePoint(new RealVector(att.getValue("inputpoint"))), processResolution(att.getValue("resolution")));
                    calc_ = new HugoniotCurveCalcND(params);
                    
                }
                if (currentCommand_.equalsIgnoreCase("rarefactionorbit")) {
                    tempPoint_ = new PhasePoint(new RealVector(att.getValue("inputpoint")));
                    direction_ = chooseDirection(att.getValue("direction"));
                    int family = new Integer(att.getValue("family"));

                    calc_ = new RarefactionOrbitCalc(tempPoint_, family, direction_);

                }
                if (currentCommand_.equalsIgnoreCase("rarefactionextensioncurve")) {
                    ContourParams params = new ContourParams(processResolution(att.getValue("resolution")));
                    tempPoint_ = new PhasePoint(new RealVector(att.getValue("inputpoint")));
                    direction_ = chooseDirection(att.getValue("direction"));
                    calc_ = new RarefactionExtensionCalc(params, tempPoint_, direction_, new Integer(att.getValue("curvefamily")), new Integer(att.getValue("characteristic")));

                }
                if (currentCommand_.equalsIgnoreCase("integralcurve")) {
                    tempPoint_ = new PhasePoint(new RealVector(att.getValue("inputpoint")));
                    direction_ = chooseDirection(att.getValue("direction"));
                    int family = new Integer(att.getValue("family"));

                    calc_ = new IntegralCurveCalc(tempPoint_, family);


                }
                if (currentCommand_.equalsIgnoreCase("shockcurve")) {
                    tempPoint_ = new PhasePoint(new RealVector(att.getValue("inputpoint")));
                    direction_ = chooseDirection(att.getValue("direction"));
                    int family = new Integer(att.getValue("family"));

                    calc_ = new ShockCurveCalc(tempPoint_, family, direction_);

                }
                if (currentCommand_.equalsIgnoreCase("compositecurve")) {
                    tempPoint_ = new OrbitPoint(new RealVector(att.getValue("inputpoint")));
                    direction_ = chooseDirection(att.getValue("direction"));
                    int family = new Integer(att.getValue("family"));

                    calc_ = new CompositeCalc(new OrbitPoint(tempPoint_), family, direction_);
                }

                if (currentCommand_.equalsIgnoreCase("levelcurve")) {
                    ContourParams params = new ContourParams(processResolution(att.getValue("resolution")));
                    if (att.getValue("inputpoint") == null) {
                        calc_ = new LevelCurveCalc(new Integer(att.getValue("family")), new Double(att.getValue("level")), params);
                    } else {
                        calc_ = new PointLevelCalc(new RealVector(att.getValue("inputpoint")), new Integer(att.getValue("family")), params);
                    }
                }


                if (currentCommand_.equalsIgnoreCase("doublecontactcurve")) {


                    ContourParams params = new ContourParams(processResolution(att.getValue("resolution")));

                    int curveFamily = new Integer(att.getValue("curvefamily"));
                    int domainFamily = new Integer(att.getValue("domainfamily"));
                    calc_ = new DoubleContactCurveCalc(params, curveFamily, domainFamily);


                }



                if (currentCommand_.equalsIgnoreCase("inflectioncurve")) {

                    int curveFamily = new Integer(att.getValue("family"));

                    ContourParams params = new ContourParams(processResolution(att.getValue("resolution")));

                    calc_ = new InflectionCurveCalc(params, curveFamily);

                }

                if (currentCommand_.equalsIgnoreCase("hysteresiscurve")) {


                    ContourParams params = new ContourParams(processResolution(att.getValue("resolution")));

                    int family = new Integer(att.getValue("curvefamily"));


                    calc_ = new HysteresisCurveCalc(params, family);



                }



                if (currentCommand_.equalsIgnoreCase("boundaryextensioncurve")) {

                    ContourParams params = new ContourParams(processResolution(att.getValue("resolution")));
                    int domainFamily = new Integer(att.getValue("family"));
                    int edge = new Integer(att.getValue("edge"));
                    int edgeResolution = new Integer(att.getValue("edgeresolution"));
                    int characteristic = new Integer(att.getValue("characteristicwhere"));
                    calc_ = new BoundaryExtensionCurveCalc(params, edgeResolution, domainFamily, edge,characteristic);



                }


            }





            if (name.equals("ORBITPOINT")) {
                stringBuffer_ = new StringBuilder();
                lambda_ = (new Double(att.getValue("lambda"))).doubleValue();
            }


            if (name.equals("HUGONIOTSEGMENT")) {

                RealVector leftPoint = new RealVector(att.getValue(0));
                RealVector rightPoint = new RealVector(att.getValue(1));

                double leftSigma = new Double(att.getValue("leftsigma"));
                double rightSigma = new Double(att.getValue("rightsigma"));

                //TODO Read right and left lambda in array form

                int type = new Integer(att.getValue("type"));
                HugoniotSegment segment = new HugoniotSegment(leftPoint, leftSigma, rightPoint, rightSigma, type);

                hugoniotSegmentsList_.add(segment);

            }

            if (name.equals("PHASEPOINT")) {

                stringBuffer_ = new StringBuilder();

            }




            if (name.equals("REALVECTOR")) {
                tempVector_ = new RealVector((new Integer(att.getValue(0))).intValue());

            }



        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {

            try {
                String data = new String(buff, offset, len);
                if (data.length() != 0) {
                    if (currentElement_.equals("PHASEPOINT") || currentElement_.equals("ORBITPOINT")) {
                        stringBuffer_.append(data);
                    }

                }
            } catch (NumberFormatException ex) {
                System.out.println("Erro de formato! " + ex.getMessage());
                ex.printStackTrace();

            }

        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {

            if (name.equals("PHASEPOINT")) {

                try {

                    RealVector coordsVector = new RealVector(stringBuffer_.toString());
                    if (pointOneOK_ == false) {
                        point1_ = new RealVector(coordsVector);
                        pointOneOK_ = true;
                    } else {
                        point2_ = new RealVector(coordsVector);

                    }


                } catch (Exception ex) {
                    ex.getMessage();
                }

            }

            if (name.equals("REALSEGMENT")) {
                realSegmentsList_.add(new RealSegment(point1_, point2_));
                pointOneOK_ = false;
            }

            if (name.equalsIgnoreCase("COMMAND")) {

                if (currentCommand_.equalsIgnoreCase("hugoniotcurve")) {//Hugoniot
                    factory_ = new HugoniotCurveGeomFactory((HugoniotCurveCalc) calc_);
                }


                if (currentCommand_.equalsIgnoreCase("integralcurve")) {//Integral curve
                    factory_ = new IntegralOrbitGeomFactory((IntegralCurveCalc) calc_);
                }


                if (currentCommand_.equalsIgnoreCase("levelcurve")) {//Integral curve

                    factory_ = new LevelCurveGeomFactory((LevelCurveCalc) calc_);

                }

                if (currentCommand_.equalsIgnoreCase("compositecurve")) {//Composite
                    factory_ = new CompositeGeomFactory((CompositeCalc) calc_);
                }


                if (currentCommand_.equalsIgnoreCase("rarefactionorbit")) {//Rarefaction
                    factory_ = new RarefactionOrbitGeomFactory((RarefactionOrbitCalc) calc_);
                }


                if (currentCommand_.equalsIgnoreCase("rarefactionextensioncurve")) {//RarefactionExtension

                    factory_ = new RarefactionExtensionGeomFactory((RarefactionExtensionCalc) calc_);


                }
                if (currentCommand_.equalsIgnoreCase("shockcurve")) {//Shock
                    factory_ = new ShockCurveGeomFactory((ShockCurveCalc) calc_);
                }


                if (currentCommand_.equalsIgnoreCase("doublecontactcurve")) {//DoubleContact

                    DoubleContactCurve curve = new DoubleContactCurve(realSegmentsList_);

                    factory_ = new DoubleContactGeomFactory((DoubleContactCurveCalc) calc_, curve);


                }

                if (currentCommand_.equalsIgnoreCase("inflectioncurve")) {//Inflection

                    InflectionCurve curve = new InflectionCurve(realSegmentsList_);
                    factory_ = new InflectionCurveGeomFactory((InflectionCurveCalc) calc_, curve);

                }


                if (currentCommand_.equalsIgnoreCase("hysteresiscurve")) {//Hysteresis
                    HysteresisCurve curve = new HysteresisCurve(realSegmentsList_, realSegmentsList_);
                    factory_ = new HysteresisCurveGeomFactory((HysteresisCurveCalc) calc_, curve);

                }

                if (currentCommand_.equalsIgnoreCase("boundaryextensioncurve")) {//Boundary extension

                    BoundaryExtensionCurve curve = new BoundaryExtensionCurve(realSegmentsList_, realSegmentsList_);
                    factory_ = new BoundaryExtensionCurveGeomFactory((BoundaryExtensionCurveCalc) calc_, curve);

                }

                PHASESPACE.join(factory_.geom());

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

        private int chooseDirection(String stringDirection) {
            if (stringDirection == null) {
                return Orbit.BOTH_DIR;
            }
            if (stringDirection.equalsIgnoreCase("forward")) {
                return Orbit.FORWARD_DIR;
            }
            if (stringDirection.equalsIgnoreCase("backward")) {
                return Orbit.BACKWARD_DIR;
            }

            return Orbit.BOTH_DIR;

        }

        private int chooseFamily(String stringFamily) {
            try {
                return new Integer(stringFamily);
            } catch (Exception ex) {
                return 0;
            }
        }
    }

    //
    // Initializers
    //
    /** Initializes the XML parser to reload a previous session. */
    public static void init(XMLReader parser, String configFile) {
        try {
            parser.setContentHandler(new InputHandler());
            parser.parse(configFile);
        } catch (Exception saxex) {

            saxex.printStackTrace();

        }
    }

    /** Initializes the XML parser to reload a previous session. */
    public static void init(XMLReader parser, InputStream configFileStream) {
        try {
            parser.setContentHandler(new InputHandler());
            System.out.println("Data Module");
            System.out.println("Will parse !");
            parser.parse(new InputSource((configFileStream)));
            System.out.println("parsed !");
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
    /** Writes the data of actual session into a XML file. */
    static public void export(FileWriter writer) throws java.io.IOException {

        System.out.println("Chamando export do data module");

        Iterator<RpGeometry> iterator = PHASESPACE.getGeomObjIterator();

        while (iterator.hasNext()) {
            writer.write(iterator.next().geomFactory().toXML());
        }


    }

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
                writer.write(orbit.toMatlabData(entry.getKey()));
            }

        }

        //Plotting 3D view
        writer.write("%% plotting 3D \n");
        writer.write("figure(1)\n");


        for (Entry<Integer, RpGeometry> entry : geometrySet) {

            RPnCurve curve = (RPnCurve) entry.getValue().geomFactory().geomSource();

            if (curve instanceof SegmentedCurve) {

                SegmentedCurve sCurve = (SegmentedCurve) curve;

                writer.write(sCurve.createSegment3DPlotMatlabPlot(entry.getKey()));

            } else {
                Orbit orbit = (Orbit) curve;
                writer.write(orbit.createPoint3DMatlabPlot(entry.getKey()));
            }
        }

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

//                writer.write("figure(2)\n");
//                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(0, 1, entry.getKey()));
//                writer.write("figure(3)\n");
//                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(0, 2, entry.getKey()));
//                writer.write("figure(4)\n");
//                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(1, 2, entry.getKey()));

                writer.write("figure(2)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(1, 0, entry.getKey()));
                writer.write("figure(3)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(2, 0, entry.getKey()));
                writer.write("figure(4)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(1, 2, entry.getKey()));

            } else {
                Orbit orbit = (Orbit) curve;
//                writer.write("figure(2)\n");
//                writer.write(orbit.create2DPointMatlabPlot(0, 1, entry.getKey()));
//                writer.write("figure(4)\n");
//                writer.write("figure(3)\n");
//                writer.write(orbit.create2DPointMatlabPlot(0, 2, entry.getKey()));
//                writer.write(orbit.create2DPointMatlabPlot(1, 2, entry.getKey()));

                writer.write("figure(2)\n");
                writer.write(orbit.create2DPointMatlabPlot(1, 0, entry.getKey()));
                writer.write("figure(3)\n");
                writer.write(orbit.create2DPointMatlabPlot(2, 0, entry.getKey()));
                writer.write("figure(4)\n");
                writer.write(orbit.create2DPointMatlabPlot(1, 2, entry.getKey()));

            }

        }

    }
}

  
