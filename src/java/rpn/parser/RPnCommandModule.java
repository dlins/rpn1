/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import java.awt.Color;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import rpn.RPnLeftPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnUIFrame;


import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;
import rpn.configuration.Configuration;
import rpn.controller.ui.FILE_ACTION_SELECTED;
import rpn.controller.ui.UndoActionController;
import rpn.controller.ui.UserInputHandler;
import rpn.glasspane.RPnGlassPane;
import rpn.message.RPnHttpPoller;
import rpn.message.RPnNetworkStatus;
import rpnumerics.RPnCurve;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolygon;
import wave.multid.view.ViewingAttr;

/**
 * With this class the calculus made in a previous session can be reloaded. A
 * previous state can be reloaded reading a XML file that is used by this class
 */
public class RPnCommandModule {

    public static String SESSION_ID_ = "";

    static public class RPnCommandParser implements ContentHandler {

        private String currentElement_;
        private String currentCommand_;
        private StringBuilder stringBuffer_ = new StringBuilder();
        private Configuration currentConfiguration_;
        private boolean isChangePhysicsParamsCommand_;
        private List<RealVector> realVectorList_;
        private Integer curveId_;
        private RealVector pointVals_;
        private int glassDrawMode_;
        private boolean creatingSelection_;
        private static RPnPhaseSpacePanel curvesPanel_;
        private RpGeometry selectedGeometry_;

        public RPnCommandParser() {


            stringBuffer_ = new StringBuilder();
            isChangePhysicsParamsCommand_ = false;
            realVectorList_ = new ArrayList<RealVector>();
            creatingSelection_ = false;

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

            if (currentElement_.equals("CURVESELECTION")) {

                realVectorList_.clear();
                creatingSelection_ = true;

            }


            if (currentElement_.equals("DOMAINSELECTION")) {

                realVectorList_.clear();
                creatingSelection_ = true;

            }



            if (currentElement_.equals("RPNSESSION")) {

                SESSION_ID_ = att.getValue("id");
            }

            if (isChangePhysicsParamsCommand_) {

                if (currentElement_.equals("PHYSICSCONFIG")) {
                    //System.out.println("Current configuration: " + currentConfiguration_.getName());

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


                if (att.getValue("name").equals("phasespace")) {

                    UIController.instance().setActivePhaseSpace(RPnDataModule.getPhaseSpace(att.getValue("value")));
                }

                if (att.getValue("name").equals("curveid")) {
                    curveId_ = new Integer(att.getValue("value"));
                }

                if (att.getValue("name").equals("activated_frame_title")) {

                    RPnNetworkStatus.instance().ACTIVATED_FRAME_TITLE = att.getValue("value");

                    if (currentCommand_.equalsIgnoreCase("TOGGLE_NOTEBOARD_MODE")) {

                        boolean padmodeOff = RPnUIFrame.toggleNoteboardMode(RPnNetworkStatus.ACTIVATED_FRAME_TITLE);

                        if (padmodeOff) {

                            RPnHttpPoller.POLLING_MODE = RPnHttpPoller.TEXT_POLLER;

                        } else {
                            RPnHttpPoller.POLLING_MODE = RPnHttpPoller.OBJ_POLLER;
                        }

                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Noteboard mode toggled successfuly...");

                    } else if (currentCommand_.equalsIgnoreCase("NOTEBOARD_CLEAR")) {

                        RPnUIFrame.noteboardClear();
                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Noteboard cleared successfuly...");

                    } else if (currentCommand_.equalsIgnoreCase("FOCUS_GAINED")) {


                        RPnUIFrame.toggleFocusGained();
                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Window Focus changed successfuly...");

                    }

                }

            }

            if (currentElement_.equalsIgnoreCase("COMMAND")) {
                if (att.getValue("curveid") != null) {
                    curveId_ = new Integer(att.getValue("curveid"));
                    RpModelPlotCommand.curveID_ = curveId_;

                }
                currentCommand_ = att.getValue("name");

                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Current command is :" + currentCommand_);

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


                } else if (currentCommand_.equalsIgnoreCase("extensioncurve")) {
                    UIController.instance().setState(new FILE_ACTION_SELECTED(GenericExtensionCurveCommand.instance()));
                    GenericExtensionCurveCommand.instance().execute();

                } else if (currentCommand_.equalsIgnoreCase("curveselection")) {
                    selectedGeometry_ = selectCurve(curveId_);
                    System.out.println("Selecionando curva :"+curveId_);

                    GenericExtensionCurveCommand.instance().setGeometryAndPanel(selectedGeometry_, curvesPanel_);


                }




            }

            if (currentElement_.equals("REALVECTOR")) {

                stringBuffer_ = new StringBuilder();
                stringBuffer_.append(att.getValue("coords"));
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {


            if (name.equals("CURVESELECTION")) {


                RealVector[] coords = new RealVector[realVectorList_.size()];

                for (int i = 0; i < coords.length; i++) {
                    coords[i] = realVectorList_.get(i);

                }

                RPnPhaseSpaceFrame frame = (RPnPhaseSpaceFrame) RPnUIFrame.getFrame(RPnNetworkStatus.ACTIVATED_FRAME_TITLE);

                RPnPhaseSpacePanel panel = frame.phaseSpacePanel();



                AreaSelected curveSelection = new AreaSelected(coords, panel.scene().getViewingTransform(), new ViewingAttr(Color.red));
                
                curvesPanel_=panel;

                panel.addGraphicUtil(curveSelection);
                panel.updateGraphicsUtil();
                panel.repaint();

                creatingSelection_ = false;

            }


            if (name.equals("DOMAINSELECTION")) {

                CoordsArray[] coords = new CoordsArray[realVectorList_.size()];

                for (int i = 0; i < realVectorList_.size(); i++) {

                    coords[i] = new CoordsArray(realVectorList_.get(i));

                }


                MultiPolygon polygon = new MultiPolygon(coords, new ViewingAttr(Color.green));
                RPnPhaseSpaceFrame frame = (RPnPhaseSpaceFrame) RPnUIFrame.getFrame(RPnNetworkStatus.ACTIVATED_FRAME_TITLE);

                RPnPhaseSpacePanel panel = frame.phaseSpacePanel();

                panel.setLastGenericSelection(polygon);

                panel.updateGraphicsUtil();
                panel.repaint();

                creatingSelection_ = false;

            }




            if (name.equals("REALVECTOR")) {

                if (creatingSelection_) {

                    realVectorList_.add(new RealVector(stringBuffer_.toString()));


                } else {
                    UIController.instance().userInputComplete(new RealVector(stringBuffer_.toString()));


                    if (UIController.instance().getState() instanceof FILE_ACTION_SELECTED) {

                        FILE_ACTION_SELECTED fileAction = (FILE_ACTION_SELECTED) UIController.instance().getState();

                        if (fileAction.getAction() instanceof RpModelPlotCommand) {
                            //System.out.println("ID Setting : " + curveId_);

                            RpGeometry geometry = UIController.instance().getActivePhaseSpace().getLastGeometry();
                            RPnCurve curve = (RPnCurve) geometry.geomFactory().geomSource();
                            curve.setId(curveId_);
                        }
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


                if (currentCommand_.equals("levelcurve")) {
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

        private RpGeometry selectCurve(int curveID) {


            System.out.println("Curve id :"+curveID);
            RPnPhaseSpaceAbstraction phaseSpace = UIController.instance().getActivePhaseSpace();



            Iterator geomObjIterator = phaseSpace.getGeomObjIterator();



            while (geomObjIterator.hasNext()) {
                RpGeometry geometry = (RpGeometry) geomObjIterator.next();

                RpGeomFactory factory = geometry.geomFactory();


                RPnCurve curve = (RPnCurve) factory.geomSource();

                if (curve.getId() == curveID) {
                    return geometry;
                }

            }
            
            System.out.println("Curva selecionada nao achada !");

            return null;


        }
    }

    //
    // Initializers
    //        
    /**
     * Initializes the XML parser to reload a previous session.
     */
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
    /**
     * Writes the data of actual session into a XML file.
     */
    static public void export(FileWriter writer) throws java.io.IOException {

        System.out.println("Command module export started...");

        Iterator<RpCommand> iterator1 = UndoActionController.instance().getCommandIterator();

        while (iterator1.hasNext()) {
            writer.write(((RpCommand) iterator1.next()).toXML());
        }

        System.out.println("Command module export finished sucessfully !");
    }
}
