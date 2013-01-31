/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import rpn.command.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.GEOM_SELECTION;
import rpn.controller.ui.UI_ACTION_SELECTED;

import rpnumerics.RPNUMERICS;


import wave.util.RealVector;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;

import java.io.*;
import java.util.Iterator;


import rpn.configuration.Configuration;
import rpn.controller.ui.UndoActionController;

/** With this class the calculus made in a previous session can be reloaded. A previous state can be reloaded reading a XML file that is used by this class */
public class RPnCommandModule {

    static protected class RPnCommandParser implements ContentHandler {

        private String currentElement_;
        private String currentCommand_;
        private StringBuilder stringBuffer_ = new StringBuilder();
        private Configuration currentConfiguration_;
        private boolean isChangePhysicsParamsCommand_;

        public RPnCommandParser() {


            stringBuffer_ = new StringBuilder();
            isChangePhysicsParamsCommand_ = false;

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

            if (isChangePhysicsParamsCommand_) {
                if (currentElement_.equals("PHYSICSCONFIG")) {
                    System.out.println("Current configuration: " + currentConfiguration_.getName());

                    currentConfiguration_ = currentConfiguration_.getConfiguration(att.getValue("name"));

                }


                if (currentElement_.equals("PHYSICSPARAM")) {

                    currentConfiguration_.setParamValue(att.getValue("name"), att.getValue("value"));

                }

            }



            if (currentElement_.equals("CURVECONFIGURATION")) {
                
                

                currentConfiguration_ = rpnumerics.RPNUMERICS.getConfiguration(att.getValue("name"));

            }

            if (currentElement_.equals("CURVEPARAM")) {

                
//                System.out.println("Antes configuration : "+currentConfiguration_ +" "+currentConfiguration_.getParamsSize());
                
                currentConfiguration_.setParamValue(att.getValue(0), att.getValue(1));
                
                
//                System.out.println("Depois configuration : "+currentConfiguration_ +" "+currentConfiguration_.getParamsSize());
            }



            if (currentElement_.equalsIgnoreCase("COMMAND")) {


                currentCommand_ = att.getValue("name");

                System.out.println("Current command :" + currentCommand_);

                selectPhaseSpace(att.getValue("phasespace"));

                if (currentCommand_.equalsIgnoreCase("Change Flux Parameters")) {
                    currentConfiguration_ = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
                } else if (currentCommand_.equalsIgnoreCase("hugoniotcurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(HugoniotPlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("integralcurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(IntegralCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("wavecurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(WaveCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("levelcurve")) {
                    LevelCurvePlotCommand.instance().execute();
                } else if (currentCommand_.equalsIgnoreCase("pointlevelcurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(PointLevelCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("compositecurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(CompositePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("rarefactioncurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(RarefactionCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("rarefactionextensioncurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(RarefactionExtensionCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("shockcurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(ShockCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("doublecontactcurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(DoubleContactCommand.instance()));
                    DoubleContactCommand.instance().execute();
                } else if (currentCommand_.equalsIgnoreCase("inflectioncurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(InflectionPlotCommand.instance()));
                    InflectionPlotCommand.instance().execute();
                } else if (currentCommand_.equalsIgnoreCase("hysteresiscurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(HysteresisPlotCommand.instance()));
                    HysteresisPlotCommand.instance().execute();
                } else if (currentCommand_.equalsIgnoreCase("boundaryextensioncurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(BoundaryExtensionCurveCommand.instance()));
                    BoundaryExtensionCurveCommand.instance().execute();

                } else if (currentCommand_.equalsIgnoreCase("secondarybifurcationcurve")) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(SecondaryBifurcationCurveCommand.instance()));
                    SecondaryBifurcationCurveCommand.instance().execute();

                }
            }



            if (currentElement_.equals("REALVECTOR")) {

                stringBuffer_ = new StringBuilder();
            }
        }

        @Override
        public void characters(char[] buff, int offset, int len) throws
                SAXException {

            try {

                String data = new String(buff, offset, len);
                if (data.length() != 0) {
                    if (currentElement_.equals("REALVECTOR")) {
                        stringBuffer_.append(data);
                    }

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


            if (name.equals("PAUSE")) {
                try {
                    System.in.read();
                } catch (java.io.IOException ex) {
                }
            }

            if (name.equals("RPCONFIGURATION")) {
                isChangePhysicsParamsCommand_ = true;
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

        private void selectPhaseSpace(String phaseSpaceName) {

            if (phaseSpaceName != null) {

                if (phaseSpaceName.equals("Phase Space")) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.PHASESPACE);
                } else if (phaseSpaceName.equals("Left Phase Space")) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.LEFTPHASESPACE);
                } else if (phaseSpaceName.equals("Right Phase Space")) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.RIGHTPHASESPACE);
                }

            }
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

        Iterator<RpCommand> iterator1 = UndoActionController.instance().getCommandIterator();

        while (iterator1.hasNext()) {
            writer.write(((RpCommand) iterator1.next()).toXML());
        }

        System.out.println("Command module export finished sucessfully !");
    }
}
