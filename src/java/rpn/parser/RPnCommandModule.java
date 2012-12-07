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
import rpn.command.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.GEOM_SELECTION;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.controller.ui.RAREFACTION_CONFIG;
import rpn.component.RpGeometry;


import rpnumerics.RPNUMERICS;
import rpnumerics.RpSolution;
import rpnumerics.RpCurve;
import rpnumerics.SegmentedCurve;
import rpnumerics.Orbit;

import wave.util.RealVector;
import wave.util.RealMatrix2;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashMap;


import wave.multid.Space;

/** With this class the calculus made in a previous session can be reloaded. A previous state can be reloaded reading a XML file that is used by this class */
public class RPnCommandModule {


    static protected class RPnCommandParser implements ContentHandler {

        private String currentElement_;
        private String currentCommand_;

        private StringBuilder stringBuffer_ = new StringBuilder();


        public RPnCommandParser() {


            stringBuffer_ = new StringBuilder();

        }

        @Override
        public void endDocument() {


        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes att) throws
                SAXException {

            // TODO : eventually the COMMANDs should be in a differente file from the CONFIG
            // and the COMMAND tag would carry away the phasespace information with it !??
            currentElement_ = name;


            if (currentElement_.equalsIgnoreCase("COMMAND")) {


                currentCommand_ = att.getValue("name");

                if (att.getValue("phasespace").equals("Phase Space"))
                    UIController.instance().setActivePhaseSpace(RPnDataModule.PHASESPACE);



                if (currentCommand_.equalsIgnoreCase("hugoniotcurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(HugoniotPlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("integralcurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(IntegralCurvePlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("levelcurve"))

/*                    if (att.getValue("inputpoint") == null)
                        calc_ = RPNUMERICS.createLevelCurveCalc(new Integer(att.getValue("family")), new Double(att.getValue("level")), params);
                     else
                        calc_ = RPNUMERICS.createPointLevelCalc(new RealVector(att.getValue("inputpoint")), new Integer(att.getValue("family")), params);


                    factory_ = new LevelCurveGeomFactory((LevelCurveCalc) calc_);*/
                    LevelCurvePlotCommand.instance().execute();

                else if (currentCommand_.equalsIgnoreCase("compositecurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(CompositePlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("rarefactioncurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(RarefactionCurvePlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("rarefactionextensioncurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(RarefactionExtensionCurvePlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("shockcurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(ShockCurvePlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("doublecontactcurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(DoubleContactCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("inflectioncurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(InflectionPlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("hysteresiscurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(HysteresisPlotCommand.instance()));
                else if (currentCommand_.equalsIgnoreCase("boundaryextensioncurve"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(BoundaryExtensionCurveCommand.instance()));
                
                 else if (currentCommand_.equalsIgnoreCase("changeorbitdirection"))
                    UIController.instance().setState(new UI_ACTION_SELECTED(ChangeDirectionCommand.instance()));
                
                
                
            }

            else if (currentElement_.equals("REALVECTOR")) {

                stringBuffer_ = new StringBuilder();
            }
        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {

            try {

                String data = new String(buff, offset, len);
                if (data.length() != 0) {
                    if (currentElement_.equals("REALVECTOR"))
                        stringBuffer_.append(data);

                }

            } catch (NumberFormatException ex) {
                System.out.println("Wrong format ! " + ex.getMessage());
                ex.printStackTrace();

            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {


            if (name.equals("REALVECTOR")) {
                UIController.instance().userInputComplete(new RealVector(stringBuffer_.toString()));
                UIController.instance().setState(new GEOM_SELECTION());
            }
                

            if (name.equals("PAUSE"))
                try {
                    System.in.read();
                } catch (java.io.IOException ex) {}


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
            parser.setContentHandler(new RPnCommandParser());
            System.out.println("Command Module parsing started...");
            parser.parse(configFile);
            System.out.println("Command Module parsing finshed sucessfully !");
        } catch (Exception saxex) {

            saxex.printStackTrace();

        }
    }

    /** Initializes the XML parser to reload a previous session. */
    public static void init(XMLReader parser, InputStream configFileStream) {
        try {
            parser.setContentHandler(new RPnCommandParser());
            System.out.println("Command Module parsing started...");
            parser.parse(new InputSource((configFileStream)));
            System.out.println("Command Module parsing finished sucessfully !");
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

        System.out.println("Command module export started...");

        Iterator<RpCommand> iterator1 = UIController.instance().getCommandIterator();

        while (iterator1.hasNext()) {
            writer.write(((RpCommand)iterator1.next()).toXML());
        }

        System.out.println("Command module export finished sucessfully !");
    }

}
