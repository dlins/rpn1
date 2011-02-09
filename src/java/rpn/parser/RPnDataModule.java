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
import java.util.StringTokenizer;
import java.io.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.HugoniotCurve;
import rpnumerics.PhasePoint;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import rpnumerics.StationaryPoint;
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

    static protected class InputHandler implements ContentHandler {
        // for PoincareData

        protected static ArrayList pPointList_, orbitPointsList_, vectorList_;
        protected static List segmentList_;
        private String currentElement_;
        protected static RealMatrix2 tempMatrix_;
        protected static RealVector tempVector_, point1_, point2_;
        protected static RealVector[] vectorArray_;
        protected static XZeroGeom xZeroGeom_;
        protected static CoordsArray[] tempCoords_;
        protected static OrbitGeom tempOrbit_;
        protected static PhasePoint tempPoint_, tempPhasePoint_;
        protected static int phaseSize_, ncol_, nrow_;
        private double tempPTime_;
        private HugoniotParser hugolistener_;
        private OrbitParser orbitListener_;
        private StationaryPointParser statPointListener_;
        private ManifoldParser manifoldListener_;
        private PoincareParser poincareListener_;
        private ConnectionOrbitCalcParser connectionCalcListener_;
        private OrbitCalcParser orbitCalcListener_;
        private ShockFlowParser shockFlowParser_;
        private ManifoldCalcParser manifoldCalcParser_;
        protected static boolean pointOneOK_, calcReady_, plotProfile_;

        public InputHandler() {
            orbitListener_ = new OrbitParser();
            hugolistener_ = new HugoniotParser(new RPnDataModule());

            statPointListener_ = new StationaryPointParser();
            manifoldListener_ = new ManifoldParser();
            poincareListener_ = new PoincareParser();
            shockFlowParser_ = new ShockFlowParser();
            connectionCalcListener_ = new ConnectionOrbitCalcParser();
            orbitCalcListener_ = new OrbitCalcParser();
            manifoldCalcParser_ = new ManifoldCalcParser();


            pPointList_ = new ArrayList();
            segmentList_ = new ArrayList();
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
            LEFTPHASESPACE = new RPnPhaseSpaceAbstraction("LeftPhase Space",
                    RPNUMERICS.domain(), new NumConfigImpl());//  RpNumerics.domain(),
            RIGHTPHASESPACE = new RPnPhaseSpaceAbstraction("RightPhase Space",
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


            if (name.equals("STATPOINT")) {
//                System.out.println("Abrindo statpoint");
//                tempPoint_ = new PhasePoint(tempVector_);
            }

            if (name.equals("MANIFOLDCALC")) {
                calcReady_ = new Boolean(att.getValue(1)).booleanValue();

                if (att.getValue(0).equals("pos")) {
                    OrbitParser.dir = 1;
                } else {
                    OrbitParser.dir = -1;
                }
                rpn.parser.ManifoldParser.manifoldNumber++;
                StationaryPointParser.plotStatPoint = true;

            }

            if ((name.equals("ORBITCALC"))) {
                orbitPointsList_.clear();


                calcReady_ = new Boolean(att.getValue(1)).booleanValue();

                if (att.getValue(0).equals("neg")) {
                    OrbitParser.dir = -1;
                } else {
                    OrbitParser.dir = 1;
                }
                OrbitParser.plotOrbit = true;
            }
            if (name.equals("STATPOINTCALC")) {
//                calcReady_ = new Boolean(att.getValue(1)).booleanValue();
//                tempPoint_=new PhasePoint(new RealVector(att.getValue(0)));
//                RPNUMERICS.getShockProfile().setFlowName(att.getValue(2));
//                
//                System.out.println("Abrindo StatPoint com calcready:" + calcReady_);
            }

            if (name.equals("CONNECTIONORBITCALC")) {

                calcReady_ = new Boolean(att.getValue(0)).booleanValue();

                StationaryPointParser.plotStatPoint = false;
                OrbitParser.plotOrbit = false;
                plotProfile_ = true;
            }
            if (name.equals("ORBITPOINT")) {
                tempPTime_ = (new Double(att.getValue(0))).doubleValue();
            }

            if (name.equals("ORBIT")) {

                OrbitParser.flag = (new Integer(att.getValue(0))).intValue();
            }

            if (name.equals("HUGONIOTCALC")) {

                StringTokenizer tokenizer = new StringTokenizer(att.getValue(0));
                double doubleList[] = new double[tokenizer.countTokens()];
                int i = 0;
                while (tokenizer.hasMoreTokens()) {
                    doubleList[i] = new Double(tokenizer.nextToken()).doubleValue();
                    i++;
                }

                RealVector vector = new RealVector(doubleList);
                XZERO = new PhasePoint(vector);
                RPNUMERICS.getShockProfile().setXZero(XZERO);
                RPNUMERICS.getShockProfile().setHugoniotMethodName(att.getValue(1));
                RPNUMERICS.getShockProfile().setFlowName(att.getValue(2));

            }

            if (name.equals("HUGONIOTCURVE")) {

                segmentList_.clear();
            }

            if (name.equals("PHASEPOINT")) {
                phaseSize_ = (new Integer(att.getValue(0))).intValue();
                tempVector_ = new RealVector(phaseSize_);

            }
            if (name.equals("REALSEGMENT")) {
                pointOneOK_ = false;
            }

            if (name.equals("EIGENVEC")) {
                vectorList_.clear();
            }

            if (name.equals("REALVECTOR")) {
                tempVector_ = new RealVector((new Integer(att.getValue(0))).intValue());

            }

            if (name.equals("REALMATRIX")) {

                nrow_ = new Integer(att.getValue(0)).intValue();
                ncol_ = new Integer(att.getValue(1)).intValue();

            }

            if (name.equals("MANIFOLD")) {
                OrbitParser.timeDirection = (new Integer(att.getValue(0))).intValue();
                StationaryPointParser.plotStatPoint = false;
                OrbitParser.plotOrbit = false;
            }

        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {

            try {
                String data = new String(buff, offset, len);

//        System.out.println("String pega no parser: "+ data);

                data = data.trim();

                if (data.length() != 0) {

                    if (currentElement_.equals("PHASEPOINT")) {
                        StringTokenizer tokenizer = new StringTokenizer(data);
                        if (tokenizer.countTokens() == phaseSize_) {
                            tempVector_ = new RealVector(data);
                            pPointList_.add(new PhasePoint(tempVector_));
                        }

                    }
                    if (currentElement_.equals("REALVECTOR")) {
                        tempVector_ = new RealVector(data);
                        vectorList_.add(tempVector_);

                    }

                    if (currentElement_.equals("REALMATRIX")) {

                        tempMatrix_ = new RealMatrix2(nrow_, ncol_, data);

                    }

                    if (currentElement_.equals("EIGENVALR")) {
                        StationaryPointParser.eigenvalr = StationaryPointParser.parserEingenVal(data);
                    }

                    if (currentElement_.equals("EIGENVALI")) {
                        StationaryPointParser.eigenvali = StationaryPointParser.parserEingenVal(data);
                    }
                    if (currentElement_.equals("DIMP")) {

                        OrbitParser.dimP = (new Integer(data)).intValue();
                    }

                    if (currentElement_.equals("DIMN")) {

                        OrbitParser.dimN = (new Integer(data)).intValue();
                    }

                    if (currentElement_.equals("INTEGRATIONFLAG")) {

                        OrbitParser.integrationFlag = (new Integer(data)).intValue();
                    }

                    if (currentElement_.equals("UMINUS")) {
                        StationaryPointParser.plotStatPoint = false;
                    }

                    if (currentElement_.equals("UPLUS")) {
                        StationaryPointParser.plotStatPoint = false;
                    }

                }
            } catch (NumberFormatException ex) {
                System.out.println("Erro de formato!");
            }

        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {

            if (name.equals("HUGONIOTCURVE")) {
                try {
                    hugoniotCurve_ = new HugoniotCurve(XZERO, segmentList_);

                    hugolistener_.actionPerformed(new ActionEvent(this, 0,
                            "endHugoniotCurve"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (name.equals("STATPOINT")) {
//                statPointListener_.actionPerformed(new ActionEvent(this, 0, "endStatPoint"));
            }

            if (name.equals("UMINUS")) {

                StationaryPointParser.uMinus = new StationaryPoint(
                        StationaryPointParser.statPoint);
            }

            if (name.equals("UPLUS")) {

                StationaryPointParser.uPlus = new StationaryPoint(
                        StationaryPointParser.statPoint);

            }

            if (name.equals("EIGENVEC")) {

                vectorArray_ = new RealVector[vectorList_.size()];

                for (int i = 0; i < vectorArray_.length; i++) {
                    vectorArray_[i] = (RealVector) vectorList_.get(i);

                }

            }
            if (name.equals("PHASEPOINT")) {

                if (pointOneOK_ == false) {
                    point1_ = new RealVector(tempVector_);
                    pointOneOK_ = true;
                } else {
                    point2_ = new RealVector(tempVector_);
                    pointOneOK_ = false;
                }

            }
            if (name.equals("REALSEGMENT")) {

                segmentList_.add(new RealSegment(point1_, point2_));
            }

            if (name.equals("ORBITPOINT")) {
                orbitPointsList_.add(new OrbitPoint(tempVector_, tempPTime_));
            }

            if (name.equals("MANIFOLD")) {
                manifoldListener_.actionPerformed(new ActionEvent(this, 0, "endManifold"));
            }

            if (name.equals("ORBIT")) {
                orbitListener_.actionPerformed(new ActionEvent(this, 0, "endOrbit"));
            }

            if (name.equals("SCHURFORMN")) {
                StationaryPointParser.schurFormN = new RealMatrix2(tempMatrix_);
            }

            if (name.equals("SCHURFORMP")) {
                StationaryPointParser.schurFormP = new RealMatrix2(tempMatrix_);
            }

            if (name.equals("SCHURVECP")) {
                StationaryPointParser.schurVecP = new RealMatrix2(tempMatrix_);
            }

            if (name.equals("SCHURVECN")) {
                StationaryPointParser.schurVecN = new RealMatrix2(tempMatrix_);
            }

            if (name.equals("SHOCKFLOWDATA")) {
                // plots XZero
                shockFlowParser_.actionPerformed(new ActionEvent(this, 0,
                        "endShockFlowData"));
            }

            if (name.equals("POINCAREDATA")) {
                poincareListener_.actionPerformed(new ActionEvent(this, 0,
                        "endPoincare"));
            }

            if (name.equals("ORBITCALC")) {

                orbitCalcListener_.actionPerformed(new ActionEvent(this, 0,
                        "endOrbitCalc"));
            }

            if (name.equals("MANIFOLDCALC")) {
                manifoldCalcParser_.actionPerformed(new ActionEvent(this, 0,
                        "endManifoldCalc"));
            }

            if (name.equals("CONNECTIONORBITCALC")) {

                connectionCalcListener_.actionPerformed(new ActionEvent(this, 0,
                        "endConnectionOrbitCalc"));

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
//        Iterator<Command> commandInterator = UIController.instance().getCommandIterator();
//        while (commandInterator.hasNext()) {
////           writer.write((commandInterator.next().toXML()));//TODO Fix.
//
//        }
//        System.out.println("Chamando export do data module");
//
//        Iterator<RpGeometry> iterator = PHASESPACE.getGeomObjIterator();
//
//        int i = 0;
//
//        while(iterator.hasNext()){
//
//
//
//
//            System.out.println(iterator.next().geomFactory().toMatlab());
//
//
//        }
    }

    static public void matlabExport(FileWriter writer) throws java.io.IOException {

        System.out.println("Chamando export matlab do data module");

        Iterator<RpGeometry> iterator = PHASESPACE.getGeomObjIterator();
        writer.write("close all; clear all;\n");
        writer.write(RpCalcBasedGeomFactory.createMatlabColorTable());


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

        for (Entry<Integer, RpGeometry> entry : geometrySet) {

            RPnCurve curve = (RPnCurve) entry.getValue().geomFactory().geomSource();

            if (curve instanceof SegmentedCurve) {

                writer.write("figure(2)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(0, 1, entry.getKey()));
                writer.write("figure(3)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(0, 2, entry.getKey()));
                writer.write("figure(4)\n");
                writer.write(SegmentedCurve.createSegmentedMatlabPlotLoop(1, 2, entry.getKey()));

            } else {
                Orbit orbit = (Orbit) curve;
                writer.write("figure(2)\n");
                writer.write(orbit.create2DPointMatlabPlot(0, 1, entry.getKey()));
                writer.write("figure(3)\n");
                writer.write(orbit.create2DPointMatlabPlot(0, 2, entry.getKey()));
                writer.write("figure(4)\n");
                writer.write(orbit.create2DPointMatlabPlot(1, 2, entry.getKey()));

            }

        }

    }
}

  
