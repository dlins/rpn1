/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import rpn.*;
import rpn.controller.phasespace.*;
import rpn.controller.ui.UIController;
import rpn.command.*;
import rpn.configuration.Configuration;
import rpn.component.RpGeometry;
import rpn.component.InflectionCurveGeomFactory;
import rpnumerics.FundamentalCurve;
import rpnumerics.InflectionCurve;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import rpnumerics.WaveCurve;
import wave.multid.Space;
import wave.util.RealVector;
import wave.util.RealSegment;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpn.component.RpGeomFactory;

/** With this class the calculus made in a previous session can be reloaded. A previous state can be reloaded reading a XML file that is used by this class */
public class RPnDataModule {

    static public String XML_TAG="RPNDATA";

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
            ex.printStackTrace();
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

        private String          currentElement_;
        private Attributes      currentElementAtt_;
        private Vector          curveSegments_;
        private StringBuilder   stringBuffer_ = new StringBuilder();
        private String          dimension_;
        private String          curve_name_;
	private StringBuffer 	realSegDataBuffer_ = new StringBuffer();

        private Configuration currentConfiguration_;

        // vector for reading the segments data for segmented curves
        private Vector curveSegmentsCoords_;


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

            if (currentElement_.equals(XML_TAG)){

                RPnCommandModule.RPnCommandParser.selectPhaseSpace(att.getValue("phasespace"));

            } else

            if (currentElement_.equals(SegmentedCurve.XML_TAG)){

                  curveSegmentsCoords_ = new Vector();
                  dimension_ = att.getValue("dimension");
                  curve_name_ = att.getValue("curve_name");

            }

            if (currentElement_.equals("CURVECONFIGURATION")) {


                  currentConfiguration_ = rpnumerics.RPNUMERICS.getConfiguration(att.getValue("name"));

            }

        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {
            
            if (currentElement_.equals("REALSEG")) {

            	realSegDataBuffer_.append(buff, offset, len);
            	String data = realSegDataBuffer_.toString().trim();
            	if (data.length() != 0) {

                        RealVector newV = new RealVector(data);

			for (int i=0;i < newV.getSize();i++) {

				Double dVal = new Double(newV.getElement(i));
                       		curveSegmentsCoords_.add(dVal);

			}
                }

            }     

        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {

            currentElement_ = name;

            if (currentElement_.equals("REALSEG")){

		realSegDataBuffer_.delete(0,realSegDataBuffer_.length());
            }

            if (currentElement_.equals(SegmentedCurve.XML_TAG)){

                int dimension = Integer.parseInt(dimension_);
                int num_of_coords_per_segment = dimension*2;

                int num_of_segments = curveSegmentsCoords_.size()/num_of_coords_per_segment;
		System.out.println("NUM OF SEGS = " + num_of_segments);

                curveSegments_ = new Vector();


                RealVector c1 = new RealVector(dimension);
                RealVector c2 = new RealVector(dimension);

                for (int i=0;i < num_of_segments;i++) {                                        
                    for (int j=0;j < dimension;j++) {

                        c1.setElement(j,((Double)curveSegmentsCoords_.elementAt(i*num_of_coords_per_segment + j)).doubleValue());
                        c2.setElement(j,((Double)curveSegmentsCoords_.elementAt(i*num_of_coords_per_segment + dimension + j)).doubleValue());

                    }

                    curveSegments_.add(new RealSegment(c1,c2));

                }

		// this is going to be a Bifurcation generic curve
                InflectionCurve inflectionCurve = new InflectionCurve(curveSegments_);
                if (curve_name_.equals(inflectionCurve.getClass().getSimpleName())) {

			System.out.println("BUIDING THE INFLECTION CURVE...");
                    InflectionCurveGeomFactory factory =
                            new InflectionCurveGeomFactory(RPNUMERICS.createInflectionCurveCalc(Integer.parseInt(currentConfiguration_.getParam("family"))),
                            inflectionCurve);
                    InflectionPlotCommand.instance().joinGeomToPhaseSpaces(factory);

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
    /** Initializes the XML parser to reload a previous session. */
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

    /** Initializes the XML parser to reload a previous session. */
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
    /** Writes the data of actual session into a XML file. */
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
