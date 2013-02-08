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
import rpn.controller.ui.UIController;





import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import java.io.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import rpn.component.RpGeometry;
import rpnumerics.FundamentalCurve;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;



import rpnumerics.RPnCurve;
import rpnumerics.RiemannProfile;
import rpnumerics.RpSolution;
import rpnumerics.SegmentedCurve;
import rpnumerics.WaveCurve;
import wave.multid.Space;

/** With this class the calculus made in a previous session can be reloaded. A previous state can be reloaded reading a XML file that is used by this class */
public class RPnDataModule {

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

        private String currentElement_;
        private StringBuilder stringBuffer_ = new StringBuilder();

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

        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
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
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Phase Space")) {
            phaseSpace = RPnDataModule.PHASESPACE;
        }
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space")) {
            phaseSpace = RPnDataModule.RIGHTPHASESPACE;
        }
        if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space")) {
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

    static public void exportRP(FileWriter writer) throws java.io.IOException {


        // TODO : this is working only for PHASESPACE ... must be implemented to all
        // others phasespaces (L , R etc...)
        Iterator<RpGeometry> iterator = PHASESPACE.getGeomObjIterator();

        // Inserting data

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
        Set<Entry<Integer, RpGeometry>> visibleGeometrySet = visibleGeometries.entrySet();

        for (Entry<Integer, RpGeometry> entry : visibleGeometrySet) {

            RpSolution element = (RpSolution) entry.getValue().geomFactory().geomSource();

            if (element instanceof RiemannProfile) {
                writer.write(element.toXML() + "\n");
            } else {
                writer.write(element.toXML() + "\n");
            }
        }
    }
}
