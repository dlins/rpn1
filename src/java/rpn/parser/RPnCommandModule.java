/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import java.awt.Point;
import java.awt.geom.PathIterator;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import rpn.command.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;

import rpnumerics.RPNUMERICS;


import wave.util.RealVector;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;

import java.io.*;
import java.util.Iterator;
import javax.swing.JFrame;
import rpn.RPnUIFrame;


import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.configuration.Configuration;
import rpn.controller.ui.FILE_ACTION_SELECTED;
import rpn.controller.ui.UndoActionController;
import rpn.controller.ui.UserInputHandler;
import rpn.glasspane.RPnGlassPane;
import rpn.message.RPnHttpPoller;
import rpn.message.RPnNetworkStatus;
import rpnumerics.RPnCurve;

/** With this class the calculus made in a previous session can be reloaded. A previous state can be reloaded reading a XML file that is used by this class */
public class RPnCommandModule {

    public static String SESSION_ID_ = "";

    static public class RPnCommandParser implements ContentHandler {
       

        private String currentElement_;
        private String currentCommand_;
        private StringBuilder stringBuffer_ = new StringBuilder();
        private Configuration currentConfiguration_;
        private boolean isChangePhysicsParamsCommand_;
        private Integer curveId_;

        private RealVector pointVals_;
        private int glassDrawMode_;

        
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


            if (currentElement_.equals("RPNSESSION")) {

                    SESSION_ID_ = att.getValue("id");
            }

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

                currentConfiguration_.setParamValue(att.getValue(0), att.getValue(1));
            }


            if (currentElement_.equals("COMMANDPARAM")) {

                for (int i = 0; i < att.getLength(); i++) {

                    System.out.println(att.getValue(i));
                }

                if (att.getValue("name").equals("phasespace")) {

                    UIController.instance().setActivePhaseSpace(RPnDataModule.getPhaseSpace(att.getValue("value")));
                }

                if (att.getValue("name").equals("curveid")) {
                    curveId_ = new Integer(att.getValue("value"));
                }

                if (att.getValue("name").equals("note_frame_title")) {

                    RPnNetworkStatus.instance().NOTE_FRAME_TITLE = att.getValue("value");


                    JFrame[] frames = RPnUIFrame.getPhaseSpaceFrames();
                    JFrame[] aux_frames = RPnUIFrame.getAuxFrames();
                    JFrame[] riemann_frames = RPnUIFrame.getRiemannFrames();

                    JFrame[] allFrames = null;
                    if (riemann_frames != null) {
                        allFrames = new JFrame[frames.length + aux_frames.length + riemann_frames.length];
                    } else {
                        allFrames = new JFrame[frames.length + aux_frames.length];
                    }

                    // FILL UP the allFrames strucutre
                    int count = 0;
                    for (int i = 0; i < frames.length; i++) {
                        allFrames[count++] = frames[i];
                    }
                    for (int i = 0; i < aux_frames.length; i++) {
                        allFrames[count++] = aux_frames[i];
                    }
                    if (riemann_frames != null) {
                        for (int i = 0; i < riemann_frames.length; i++) {
                            allFrames[count++] = riemann_frames[i];
                        }
                    }
                   
                    if (currentCommand_.equalsIgnoreCase("TOGGLE_NOTEBOARD_MODE")) {
                        for (int i = 0; i < allFrames.length; i++) {
                            if (allFrames[i].getTitle().compareTo(RPnNetworkStatus.instance().NOTE_FRAME_TITLE) == 0) {

                                System.out.println("Toggling Frame : " + allFrames[i].getTitle());
                                allFrames[i].getGlassPane().setVisible(!allFrames[i].getGlassPane().isVisible());

                            } else {
                                allFrames[i].getGlassPane().setVisible(false);
                            }
                        }

                    } else if (currentCommand_.equalsIgnoreCase("TOGGLE_NOTEBOARD_CLEAR")) {
                        for (int i = 0; i < allFrames.length; i++) {
                            if (allFrames[i].getTitle().compareTo(RPnNetworkStatus.instance().NOTE_FRAME_TITLE) == 0)
                                ((RPnGlassPane) allFrames[i].getGlassPane()).clear();

                            

                        }
                    }
                }

            }

            if (currentElement_.equalsIgnoreCase("COMMAND")) {
                if (att.getValue("curveid") != null) {
                    curveId_ = new Integer(att.getValue("curveid"));
                    RpModelPlotCommand.curveID_=curveId_;

                }
                currentCommand_ = att.getValue("name");

                System.out.println("Current command :" + currentCommand_);

                UIController.instance().setActivePhaseSpace(RPnDataModule.getPhaseSpace(att.getValue("phasespace")));


                if (currentCommand_.equalsIgnoreCase("Change Flux Parameters")) {
                    currentConfiguration_ = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
                } else if (currentCommand_.equalsIgnoreCase("hugoniotcurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(HugoniotPlotCommand.instance()));

                } else if (currentCommand_.equalsIgnoreCase("integralcurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(IntegralCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("wavecurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(WaveCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("levelcurve")) {
                      UIController.instance().setState(new FILE_ACTION_SELECTED(LevelCurvePlotCommand.instance()));

                } else if (currentCommand_.equalsIgnoreCase("pointlevelcurve")) {

                    UIController.instance().setState(new FILE_ACTION_SELECTED(PointLevelCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("compositecurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(CompositePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("rarefactioncurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(RarefactionCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("rarefactionextensioncurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(RarefactionExtensionCurvePlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("shockcurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(ShockCurvePlotCommand.instance()));

                } else if (currentCommand_.equalsIgnoreCase("poincaresection")) {

                    UIController.instance().setState(new FILE_ACTION_SELECTED(PoincareSectionPlotCommand.instance()));
                } else if (currentCommand_.equalsIgnoreCase("doublecontactcurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(DoubleContactCommand.instance()));
                    DoubleContactCommand.instance().execute();
                } else if (currentCommand_.equalsIgnoreCase("inflectioncurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(InflectionPlotCommand.instance()));
                    InflectionPlotCommand.instance().execute();
                } else if (currentCommand_.equalsIgnoreCase("hysteresiscurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(HysteresisPlotCommand.instance()));
                    HysteresisPlotCommand.instance().execute();
                } else if (currentCommand_.equalsIgnoreCase("boundaryextensioncurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(BoundaryExtensionCurveCommand.instance()));
                    BoundaryExtensionCurveCommand.instance().execute();

                } else if (currentCommand_.equalsIgnoreCase("secondarybifurcationcurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(SecondaryBifurcationCurveCommand.instance()));
                    SecondaryBifurcationCurveCommand.instance().execute();

                // MAY BE THIS SHOULD BE A CONFIG PARAM change ... mvera.
                } else if (currentCommand_.equalsIgnoreCase("TOGGLE_NOTEBOARD_MODE")) {

                    System.out.println("Will now parse the TOGGLE_NOTEBOARD_MODE");
                    
                    if (RPnHttpPoller.POLLING_MODE == RPnHttpPoller.TEXT_POLLER)
                        RPnHttpPoller.POLLING_MODE = RPnHttpPoller.OBJ_POLLER;
                    else RPnHttpPoller.POLLING_MODE = RPnHttpPoller.TEXT_POLLER;
                }              
            }

            if (currentElement_.equals("REALVECTOR")) {

                stringBuffer_ = new StringBuilder();
                stringBuffer_.append(att.getValue("coords"));
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {


            if (name.equals("REALVECTOR")) {

                UIController.instance().userInputComplete(new RealVector(stringBuffer_.toString()));
                                
                 if (UIController.instance().getState() instanceof FILE_ACTION_SELECTED){
                    
                    FILE_ACTION_SELECTED fileAction = (FILE_ACTION_SELECTED)UIController.instance().getState();
                
                    if (fileAction.getAction() instanceof RpModelPlotCommand){
                        System.out.println("ID Setting : " + curveId_);
                        
                        RpGeometry geometry = UIController.instance().getActivePhaseSpace().getLastGeometry();
                        RPnCurve curve = (RPnCurve) geometry.geomFactory().geomSource();
                        curve.setId(curveId_);                                               
                    }                                        
                }                
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


            if (name.equals("COMMAND")) {


                if (currentCommand_.equals("Curve Remove Command")) {
                    CurveRemoveCommand.instance().remove(curveId_);
                }

                
                if (currentCommand_.equals("levelcurve")){
                    LevelCurvePlotCommand.instance().execute();
                }
            }


        }

        public void characters(char[] c, int i, int j) {
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

        public static void selectPhaseSpace(String phaseSpaceName) {

            if (phaseSpaceName != null) {

                if (phaseSpaceName.equals("Phase Space")) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.PHASESPACE);
                } else if (phaseSpaceName.equals("LeftPhase Space")) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.LEFTPHASESPACE);
                } else if (phaseSpaceName.equals("RightPhase Space")) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.RIGHTPHASESPACE);
                }

            }
        }
    }

    //
    // Initializers
    //        
    /** Initializes the XML parser to reload a previous session. */
    public static void init(XMLReader parser, InputStream inputStream) {
        try {
            parser.setContentHandler(new RPnCommandParser());
            System.out.println("Command Module parsing started...");
            parser.parse(new InputSource((inputStream)));
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
