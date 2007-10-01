package rpn.parser;

import rpn.*;
import rpn.controller.phasespace.*;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.multid.CoordsArray;
import wave.util.RealSegment;
import rpn.component.OrbitGeom;
import rpn.component.XZeroGeom;
import rpnumerics.RPNUMERICS;
import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;
import java.awt.event.ActionEvent;
import rpnumerics.PhasePoint;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.StationaryPoint;

/** With this class the calculus made in a previous session can be reloaded. A previous state can be reloaded reading a XML file that is used by this class */

public class RPnDataModule {
    static public RPnPhaseSpaceAbstraction PHASESPACE = null;

    public static Orbit ORBIT = null;
    public static boolean RESULTS = false;

    static protected class InputHandler extends HandlerBase {
        // for PoincareData

        protected static ArrayList pPointList_, oPointsList_, vectorList_;

        protected static List segmentList_;

        private String currentElement_;

        protected static RealMatrix2 tempMatrix_;

        protected static RealVector tempVector_, point1_, point2_;

        protected static RealVector[] vectorArray_;

        protected static XZeroGeom xZeroGeom_;

        protected static CoordsArray[] tempCoords_;

        protected static OrbitGeom tempOrbit_;

        protected static PhasePoint tempPoint_,tempPhasePoint_, xZero_;

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
            orbitListener_=new OrbitParser();
            hugolistener_ = new HugoniotParser();

            statPointListener_ = new StationaryPointParser();
            manifoldListener_= new ManifoldParser();
            poincareListener_= new PoincareParser();
            shockFlowParser_=new ShockFlowParser();
            connectionCalcListener_ = new ConnectionOrbitCalcParser();
            orbitCalcListener_ = new OrbitCalcParser();
            manifoldCalcParser_ = new ManifoldCalcParser();


            pPointList_ = new ArrayList();
            segmentList_ = new ArrayList();
            vectorList_ = new ArrayList();
            oPointsList_ = new ArrayList();
            ManifoldParser.manifoldNumber = 0;

            plotProfile_ = false;
            calcReady_ = false;
            // initialize phase space state
            PHASESPACE = new RPnPhaseSpaceAbstraction("Phase Space",
                    RPNUMERICS.domain(),
                    new NumConfigImpl());


        }

        public void startElement(String name, AttributeList att) throws
                SAXException {

            currentElement_ = name;

            if (name.equals("SHOCKFLOWDATA")) {
                calcReady_ = new Boolean(att.getValue(1)).booleanValue();

                RPNUMERICS.changeSigma(new Double(att.getValue(0)).doubleValue());

            }

            if (name.equals("STATPOINT")) {

                System.out.println("Abrindo statpoint");

                tempPoint_ = new PhasePoint(tempVector_);

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
                oPointsList_.clear();

                calcReady_ = new Boolean(att.getValue(1)).booleanValue();

                if (att.getValue(0).equals("neg")) {
                    OrbitParser.dir = -1;
                } else {
                    OrbitParser.dir = 1;
                }
                OrbitParser.plotOrbit = true;
            }
            if (name.equals("STATPOINTCALC")) {

                calcReady_ = new Boolean(att.getValue(0)).booleanValue();
                System.out.println("Abrindo StatPoint com calcready:" + calcReady_);

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

            if (name.equals("HUGONIOTCURVE")) {

                StringTokenizer tokenizer = new StringTokenizer(att.getValue(0));
                double doubleList[] = new double[tokenizer.countTokens()];
                int i = 0;
                while (tokenizer.hasMoreTokens()) {
                    doubleList[i] = new Double(tokenizer.nextToken()).
                                    doubleValue();
                    i++;
                }

                RealVector vector = new RealVector(doubleList);
                tempPhasePoint_ = new PhasePoint(vector);

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
                tempVector_ = new RealVector((new Integer(att.getValue(0))).
                                             intValue());
            }

            if (name.equals("REALMATRIX")) {

                nrow_ = new Integer(att.getValue(0)).intValue();
                ncol_ = new Integer(att.getValue(1)).intValue();

            }

            if (name.equals("MANIFOLD")) {
                OrbitParser.timeDirection = (new Integer(att.getValue(0))).
                                            intValue();
                StationaryPointParser.plotStatPoint = false;
                OrbitParser.plotOrbit = false;
            }

            if (name.equals("FLUXPARAMSCHANGE")) {
                int indx = new Integer(att.getValue(0)).intValue();
                int value = new Double(att.getValue(1)).intValue();
//                RPNUMERICS.fluxFunction().fluxParams().setParam(indx, value);
            }
        }

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
                        StationaryPointParser.eigenvalr = StationaryPointParser.
                                parserEingenVal(data);
                    }

                    if (currentElement_.equals("EIGENVALI")) {
                        StationaryPointParser.eigenvali = StationaryPointParser.
                                parserEingenVal(data);
                    }
                    if (currentElement_.equals("DIMP")) {

                        OrbitParser.dimP = (new Integer(data)).intValue();
                    }

                    if (currentElement_.equals("DIMN")) {

                        OrbitParser.dimN = (new Integer(data)).intValue();
                    }

                    if (currentElement_.equals("INTEGRATIONFLAG")) {

                        OrbitParser.integrationFlag = (new Integer(data)).
                                intValue();
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

        public void endElement(String name) throws SAXException {

            if (name.equals("XZERO")) {
                xZero_ = new PhasePoint(tempVector_);
                pPointList_.clear();

            }

            if (name.equals("HUGONIOTCURVE")) {
                try {

                    hugolistener_.actionPerformed(new ActionEvent(this, 0,
                            "endHugoniot"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (name.equals("STATPOINT")) {
                statPointListener_.actionPerformed(new ActionEvent(this,0,"endStatPoint"));
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
                oPointsList_.add(new OrbitPoint(tempVector_, tempPTime_));
            }

            if (name.equals("MANIFOLD")) {
                manifoldListener_.actionPerformed(new ActionEvent(this,0,"endManifold"));
            }

            if (name.equals("ORBIT")) {
                orbitListener_.actionPerformed(new ActionEvent(this,0,"endOrbit"));
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

    }


    //
    // Initializers
    //

    /** Initializes the XML parser to reload a previous session. */

    public static void init(Parser parser, String configFile) {
        try {
            parser.setDocumentHandler(new InputHandler());
            parser.parse(configFile);
        } catch (Exception saxex) {

            saxex.printStackTrace();

        }
    }

    /** Initializes the XML parser to reload a previous session. */

    public static void init(Parser parser, InputStream configFileStream) {
        try {
            parser.setDocumentHandler(new InputHandler());
            System.out.println("Data Module");
            System.out.println("Will parse !");
            parser.parse(new InputSource((configFileStream)));
            System.out.println("parsed !");
        } catch (Exception saxex) {

            if (saxex instanceof org.xml.sax.SAXParseException) {
                System.out.println("Line: " +
                                   ((org.xml.sax.SAXParseException) saxex).
                                   getLineNumber());
                System.out.println("Column: " +
                                   ((org.xml.sax.SAXParseException) saxex).
                                   getColumnNumber());
            }

            saxex.printStackTrace();
        }
    }

    //
    // Methods
    //

    /** Writes the data of actual session into a XML file. */

    static public void export(FileWriter writer) throws java.io.IOException {

        if ((RPNUMERICS.getProfile().getFlowType().equals("shockflow"))) {
            writer.write("<RPDATA>\n");
            writer.write("<FLOWDATA>\n");

            writer.write("<SHOCKFLOWDATA sigma=\"" +RPNUMERICS.getSigma() + "\" calcready=\"" + rpn.parser.RPnDataModule.RESULTS + "\">\n");
            writer.write("<XZERO>\n");
            writer.write(RPNUMERICS.getXZero().toXML());
            writer.write("</XZERO>\n");
            writer.write("</SHOCKFLOWDATA>\n");

            writer.write("</FLOWDATA>\n");
            writer.write(PHASESPACE.toXML());
            writer.write("</RPDATA>\n");
        }

    }
}
