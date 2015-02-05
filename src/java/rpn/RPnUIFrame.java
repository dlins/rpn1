/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.util.logging.*;
import java.io.IOException;
import rpn.command.*;
import rpn.parser.*;
import rpnumerics.RPNUMERICS;
import wave.util.Boundary;
import java.awt.print.PrinterJob;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.filechooser.FileNameExtensionFilter;
import rpn.command.ClassifierCommand;
import rpn.command.VelocityCommand;
import rpn.component.RpGeometry;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.controller.phasespace.RiemanProfileState;
import rpn.controller.ui.*;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.glasspane.RPnGlassPane;
import rpn.message.RPnNetworkStatus;
import wave.multid.Space;

public class RPnUIFrame extends JFrame implements PropertyChangeListener {
    
    public static String dir = "";
    public static String RPN_SESSION_FILENAME = "RPNSESSION.XML";
    public static String RPN_LOG_FILENAME = "out/RPN_LAST_SESSION.XML";

    //
    // Members
    //
    private JPanel contentPane;
    private JPanel configPanel_ = new JPanel();
    private JPanel toolBarPanel_ = new JPanel();
    private JPanel panelsChooserPanel_ = new JPanel();
    private ArrayList<RPnPhaseSpaceFrame> selectectedPanels = new ArrayList<RPnPhaseSpaceFrame>();
    private JRadioButton curvesRadioButton_ = new JRadioButton("Wave Curves");
    private JRadioButton riemannRadioButton_ = new JRadioButton("Riemann Problem");
    private JRadioButton phaseDiagramRadioButton_ = new JRadioButton("Phase Diagram");
    private JRadioButton extensionRadioButton_ = new JRadioButton("Extension Curves");
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu editMenu = new JMenu();
    private JMenu fileMenu = new JMenu();
    private JMenu modelInteractionMenu = new JMenu();
    private JMenu helpMenu = new JMenu();
    private JMenuItem shockMenuItem_ = new JMenuItem("Shock Configuration ...");
    private JMenuItem configurationMenuItem_ = new JMenuItem(new ConfigAction());
    private JMenuItem jMenuFileExit = new JMenuItem();
    private JMenuItem exportMenuItem_ = new JMenuItem("Export results to XML ...");
    private JMenuItem matlabExportMenuItem_ = new JMenuItem("Export results to Matlab ...");
    private JMenuItem jMenuHelpAbout = new JMenuItem();
    private JMenuItem jMenuHelpUpdate = new JMenuItem();
    private JMenuItem saveSessionMenuItem_ = new JMenuItem("Save Session As ...");
    private JButton inputCoordsButton = new JButton("Input Coords ...");
    private JMenuItem createSVGImageMenuItem = new JMenuItem();
    private JMenuItem printMenuItem = new JMenuItem();
    
    private static RPnPhaseSpaceFrame[] frames_, auxFrames_;
    
    private RPnMenuCommand commandMenu_ = null;
    private JMenuItem networkMenuItem = new JMenuItem();
    private JToolBar toolBar_ = new JToolBar();
    private JToolBar utilitiesToolBar_ = new JToolBar();
    private JToolBar auxiliarCurvesToolBar_ = new JToolBar();
    private static JLabel statusLabel_ = new JLabel();
    private JMenu viewMenu_ = new JMenu("View");
    private JCheckBoxMenuItem showMainCurvesPaneltem_ = new JCheckBoxMenuItem("Show Main Curves Window", true);
    private JCheckBoxMenuItem showLeftCurvesPaneltem_ = new JCheckBoxMenuItem("Show Left Curves Window", true);
    private JCheckBoxMenuItem showRightCurvesPaneltem_ = new JCheckBoxMenuItem("Show Right Curves Window", true);
    private JCheckBoxMenuItem showAuxPanel_ = new JCheckBoxMenuItem("Show Auxiliar Panels", true);
    private JMenuItem editMenuItem1 = new JMenuItem("Clears All Strings");
    private JMenuItem editMenuItem2 = new JMenuItem("Clears Last String");
    private JMenuItem editMenuItem3 = new JMenuItem("Clears Velocities");
    private JMenuItem editMenuItem4 = new JMenuItem("Clears Classifiers");
    private JMenuItem editMenuItem5 = new JMenuItem("Starts with Black Background");
    private JMenuItem editMenuItem6 = new JMenuItem("Starts with White Background");
    
    private JMenu cursorMenu = new JMenu("Show Last Input Cursor");
    private JMenuItem alwaysOnCursorMenuItem = new JMenuItem("Always On");
    private JMenuItem alertCursorMenuItem = new JMenuItem("Alert");
    private JMenuItem neverCursorMenuItem = new JMenuItem("Never");
    private JButton extensionResetButton_ = new JButton("Reset Extension Command");
    
    private static FileWriter logWriter_;
    
    private RPnPhaseSpaceFrame frameZoom = null;
    private ArrayList<RPnPhaseSpaceFrame> listFrameZoom = new ArrayList();
    private static RPnPhaseSpaceFrame[] riemannFrames_;
    private static List<RPnPhaseSpaceFrame> characteristicsFrames_ = new ArrayList<RPnPhaseSpaceFrame>();
    private JPanel utilitiesToolBarPanel = new JPanel();
    private SortedSet<RpModelPlotCommand> mainButtonsToolBarList_ = new TreeSet<RpModelPlotCommand>();
    private SortedSet<RpModelPlotCommand> auxButtonsToolBarList_ = new TreeSet<RpModelPlotCommand>();
    
    public RPnUIFrame(RPnMenuCommand command) {
        
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        
        try {

            // TODO may be UIController should control PHASESPACE as well
            commandMenu_ = command;
            UIController.instance().setStateController(new StateInputController(this));
            propertyChange(new PropertyChangeEvent(command, "aplication state", null, null));
            
            phaseSpaceFramesInit(RPNUMERICS.boundary());
            createUtilitiesToolBar();
            createCurvesTooBar();
            createAuxiliarCurvesToolBar();
            jbInit();
            curvesRadioButton_.doClick();
            
            associatesPhaseSpaces();
            associatePhaseSpacesAndCurvesList();
            
            addPropertyChangeListener(this);
            
            UndoActionController.createInstance();
            
            if (commandMenu_ instanceof RPnAppletPlotter) { // Selecting itens to disable in Applet

                networkMenuItem.setEnabled(false);
                createSVGImageMenuItem.setEnabled(false);
                printMenuItem.setEnabled(false);
                saveSessionMenuItem_.setEnabled(false);
            }
            
            exportMenuItem_.setEnabled(true);
            
            matlabExportMenuItem_.setEnabled(true);

//            openLogFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    // Methods
    //

    public static RPnPhaseSpaceFrame[] getAuxFrames() {
        return auxFrames_;
    }
    
    private void createAuxiliarCurvesToolBar() {
        auxiliarCurvesToolBar_.setLayout(new GridLayout(3, 2));
        auxiliarCurvesToolBar_.add(ContactRegionPlotCommand.instance().getContainer());
        for (RpModelPlotCommand button : auxButtonsToolBarList_) {
            auxiliarCurvesToolBar_.add(button.getContainer());
        }
        
        utilitiesToolBarPanel.add(auxiliarCurvesToolBar_);
        
    }
    
    private void createCurvesTooBar() {
        toolBar_.removeAll();
        
        toolBar_.setOrientation(SwingConstants.HORIZONTAL);
        toolBar_.setLayout(new GridLayout(4, 3));
        toolBar_.setFloatable(true);
        toolBarPanel_.add(toolBar_);
        
        for (RpModelPlotCommand button : mainButtonsToolBarList_) {
            
            toolBar_.add(button.getContainer());
        }
        
    }
    
    private void createViscousProfileToolBar() {
        
        toolBar_.setLayout(new GridLayout(2, 2));
        toolBar_.add(InvariantPlotCommand.instance().getContainer());
        toolBar_.add(TransitionalLinePlotCommand.instance().getContainer());
        toolBar_.add(ConnectionManifoldPlotCommand.instance().getContainer());
        toolBar_.add(PoincareSectionPlotCommand.instance().getContainer());
        
    }
    
    private void createUtilitiesToolBar() {
        
        utilitiesToolBar_.setLayout(new GridLayout(6, 2));
        utilitiesToolBarPanel.add(utilitiesToolBar_);
        
        utilitiesToolBar_.add(inputCoordsButton);
        utilitiesToolBar_.add(TrackPointCommand.instance().getContainer());
        
        utilitiesToolBar_.add(DomainSelectionCommand.instance().getContainer());
        utilitiesToolBar_.add(ImageSelectionCommand.instance().getContainer());
        
        utilitiesToolBar_.add(VelocityCommand.instance().getContainer());        //** Leandro
        utilitiesToolBar_.add(ClassifierCommand.instance().getContainer());      //** Leandro

        utilitiesToolBar_.add(AnnotationSelectionCommand.instance().getContainer());
        
        utilitiesToolBar_.add(ZoomingAreaCommand.instance().getContainer());
        utilitiesToolBar_.add(BifurcationCorrespondenceCommand.instance().getContainer());
        utilitiesToolBar_.add(WaveCurveSpeedPlotCommand.instance().getContainer());
    }
    
    private void createRiemmanProblemToolBar() {
        toolBar_.setLayout(new GridLayout(1, 2));
        toolBar_.add(RiemannResetCommand.instance().getContainer());
        toolBar_.add(RiemannProfileCommand.instance().getContainer());
        toolBar_.add(WaveCurvePlotCommand.instance().getContainer());
    }
    
    private void createExtensionCurveToolBar() {
        
        createCurvesTooBar();
        toolBar_.setLayout(new GridLayout(5, 3));
        toolBar_.add(GenericExtensionCurveCommand.instance().getContainer());
        toolBar_.add(extensionResetButton_);
        
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        if (evt.getPropertyName().equals("aplication state")) {
            
            if (evt.getNewValue() instanceof ShockConfig || evt.getNewValue() instanceof SigmaConfig) {
                
                shockConfigMenu();
                toolBar_.removeAll();
                createViscousProfileToolBar();
                toolBar_.revalidate();
            }
            
            if (evt.getNewValue() instanceof CurvesConfig) {
                
                rarefactionConfigMenu();
                toolBar_.removeAll();
                
                createCurvesTooBar();
                toolBar_.revalidate();
            }
            
            if (evt.getNewValue() instanceof RiemannProblemConfig) {
                
                bifurcationConfigMenu();
                toolBar_.removeAll();
                createRiemmanProblemToolBar();
                toolBar_.revalidate();
                
            }
            
            if (evt.getNewValue() instanceof ExtensionCurvesConfig) {
                
                bifurcationConfigMenu();
                toolBar_.removeAll();
                createExtensionCurveToolBar();
                toolBar_.remove(WaveCurveRRegionsPlotCommand.instance().getContainer());
                toolBar_.remove(BoundaryExtensionCurveCommand.instance().getContainer());
                toolBar_.revalidate();
                
            }
            
        }
        
        if (evt.getPropertyName().equals("Network MenuItem Clicked")) {
            networkMenuItem.setEnabled(false);
        }
        
        if (evt.getPropertyName().equals("Dialog Closed")) {
            networkMenuItem.setEnabled(true);
        }
        
        if (evt.getPropertyName().equals("Riemann Profile Added")) {
            exportMenuItem_.setEnabled(true);
        }
        
    }

    //File | Exit action performed
    public void jMenuFileExit_actionPerformed(ActionEvent e) {

        // TODO : a RPnNetworkModule out of parser (all others as well)
        if (rpn.message.RPnNetworkStatus.instance() != null) {
            rpn.message.RPnNetworkStatus.instance().disconnect();
        }
        
        closeLogFile();
        
        commandMenu_.finalizeApplication();
        
    }

    //Help | About action performed
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        
        RPnAboutDialog dialog = new RPnAboutDialog(this, "About RPn...", true);
        
        dialog.setVisible(true);
        
    }
    
    public void jMenuHelpUpdate_actionPerformed(ActionEvent e) throws IOException, InterruptedException {
        
        JOptionPane.showMessageDialog(this, "RPn is being upgraded...", "RPn Upgrade...", JOptionPane.INFORMATION_MESSAGE);
        
        Process pr = Runtime.getRuntime().exec("rpn -u");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        
        pr.waitFor();
        
        in.close();
        
        JOptionPane.showMessageDialog(this, "RPn must be restarted for complete upgrade...", "RPn Upgrade...", JOptionPane.INFORMATION_MESSAGE);
        
        System.exit(0);
        
    }

    //Overridden so we can exit when window is closed
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }
    
    void layoutMenuItem_actionPerformed(ActionEvent e) {
        RPnLayoutDialog dialog = new RPnLayoutDialog();
        Dimension dlgSize = dialog.getPreferredSize();
        Dimension frmSize = new Dimension(1280, 1024);
        Point loc = new Point(0, 0);
        dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);
        // apply the layout changes
        RPnDataModule.updatePhaseSpaces();
        UIController.instance().panelsUpdate();
    }
    
    public void phaseSpaceFrameZoom(Boundary boundary) {
        
        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        
        Space zoomSpace = new Space("", RPNUMERICS.domainDim());
        int[] testeArrayIndex = {0, 1};
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(zoomSpace, "", 700, 700, testeArrayIndex, false);
        wave.multid.view.ViewingTransform viewingTransf = projDescriptor.createTransform(clipping);
        
        JButton closeButton = new JButton("Close");
        
        try {
            wave.multid.view.Scene scene = null;
            
            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Phase Space")) {
                scene = RPnDataModule.PHASESPACE.createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
            }
            
            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Right Phase Space")) {
                scene = RPnDataModule.RIGHTPHASESPACE.createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
            }
            
            if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Left Phase Space")) {
                scene = RPnDataModule.LEFTPHASESPACE.createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
            }
            
            frameZoom = new RPnPhaseSpaceFrame(scene, commandMenu_);
            frameZoom.setTitle("Zoom " + RPnPhaseSpaceAbstraction.namePhaseSpace);
            
            frameZoom.jPanel5.removeAll();
            frameZoom.jPanel5.add(closeButton);
            
            UIController.instance().install(frameZoom.phaseSpacePanel());
            
            setFramesPosition(frameZoom);
            frameZoom.pack();
            frameZoom.setVisible(true);
            
            listFrameZoom.add(frameZoom);
            
            closeButton.addActionListener(
                    new java.awt.event.ActionListener() {
                        
                        public void actionPerformed(ActionEvent e) {
                            frameZoom.dispose();
                            for (int i = 0; i < listFrameZoom.size(); i++) {
                                if (listFrameZoom.get(i).phaseSpacePanel().getName().equals(RPnPhaseSpaceAbstraction.namePhaseSpace)) {
                                    listFrameZoom.get(i).dispose();
                                }
                            }
                            
                        }
                    });
            
        } catch (wave.multid.DimMismatchEx dex) {
            dex.printStackTrace();
        }
        
    }
    
    public void enableAllCommands() {
        
        Component[] allCmds = toolBar_.getComponents();
        
        for (int i = 0; i < allCmds.length; i++) {
            allCmds[i].setEnabled(true);
        }
        
    }
    
    public void disableAllCommands() {
        
        Component[] allCmds = toolBar_.getComponents();
        
        for (int i = 0; i < allCmds.length; i++) {
            allCmds[i].setEnabled(false);
        }
        
    }

//    public void updateCharacteristicsFrames(int charPhaseSpaceIndex, RealVector profileMin, RealVector profileMax) {
//        
//        RectBoundary boundary = new RectBoundary(profileMin, profileMax);
//        Space characteristicsSpace = new Space("CharacteristicsSpace", 2);
//        
//        int[] characteristicsIndices = {0, 1};
//        
//        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
//        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(characteristicsSpace, "CharacteristicsSpace", 400, 400, characteristicsIndices, false);
//        wave.multid.view.ViewingTransform characteristicsTransform = projDescriptor.createTransform(clipping);
//        
//        try {
//            wave.multid.view.Scene characteristicsScene = RPnDataModule.CHARACTERISTICSPHASESPACEARRAY[charPhaseSpaceIndex].createScene(characteristicsTransform, new wave.multid.view.ViewingAttr(Color.black));
//            RPnPhaseSpaceFrame characteristicsFrame = new RPnRiemannFrame(characteristicsScene, commandMenu_);
//            
//            characteristicsFrame.setTitle("Characteristic " + charPhaseSpaceIndex);
//            characteristicsFrame.pack();
//            characteristicsFrames_.add(characteristicsFrame);
//            
//        } catch (DimMismatchEx ex) {
//            ex.printStackTrace();
//        }
//        
//    }
//    public void updateRiemannProfileFrames(RealVector profileMin, RealVector profileMax) {
//        
//        if (riemannFrames_ != null) {
//            for (RPnPhaseSpaceFrame rPnPhaseSpaceFrame : riemannFrames_) {
//                rPnPhaseSpaceFrame.dispose();
//            }
//        }
//        
//        System.out.println("Limites de riemann: " + profileMin + profileMax);
//        RectBoundary boundary = new RectBoundary(profileMin, profileMax);
//        Space riemanProfileSpace = new Space("RiemannProfileSpace", RPNUMERICS.domainDim() + 1);
//        riemannFrames_ = new RPnRiemannFrame[RPNUMERICS.domainDim()];
//        
//        for (int i = 0; i < riemannFrames_.length; i++) {
//            int[] riemannProfileIndices = {0, i + 1};
//            
//            wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
//            RPnProjDescriptor projDescriptor = new RPnProjDescriptor(riemanProfileSpace, "RiemannProfileSpace", 400, 400, riemannProfileIndices, false);
//            wave.multid.view.ViewingTransform riemanTesteTransform = projDescriptor.createTransform(clipping);
//            
//            try {
//                wave.multid.view.Scene riemannScene = RPnDataModule.RIEMANNPHASESPACE.createScene(riemanTesteTransform, new wave.multid.view.ViewingAttr(Color.black));
//                riemannFrames_[i] = new RPnRiemannFrame(riemannScene, commandMenu_);
//                
//            } catch (DimMismatchEx ex) {
//                ex.printStackTrace();
//            }
//            riemannFrames_[i].pack();
//        }
//        
//    }
    protected void phaseSpaceFramesInit(Boundary boundary) {
        
        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        int numOfPanels = RPnVisualizationModule.DESCRIPTORS.size();
        
        auxFrames_ = new RPnPhaseSpaceFrame[2 * numOfPanels];
        frames_ = new RPnPhaseSpaceFrame[numOfPanels];
        
        int auxNumOfPanels = RPnVisualizationModule.AUXDESCRIPTORS.size();
        for (int i = 0; i < auxNumOfPanels / 2; i++) {
            wave.multid.view.ViewingTransform auxViewingTransf
                    = ((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(
                            i)).createTransform(clipping);
            try {
                wave.multid.view.Scene leftScene = RPnDataModule.LEFTPHASESPACE.createScene(auxViewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
                
                wave.multid.view.Scene rightScene = RPnDataModule.RIGHTPHASESPACE.createScene(auxViewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
                
                auxFrames_[2 * i] = new RPnPhaseSpaceFrame(leftScene, commandMenu_);
                auxFrames_[2 * i + 1] = new RPnPhaseSpaceFrame(rightScene, commandMenu_);
                
                auxFrames_[2 * i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(i)).label());
                auxFrames_[2 * i + 1].setTitle(((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(i + 1)).label());
                
                UIController.instance().install(auxFrames_[2 * i].phaseSpacePanel());
                UIController.instance().install(auxFrames_[2 * i + 1].phaseSpacePanel());
                setFramesPosition(auxFrames_[2 * i]);
                setFramesPosition(auxFrames_[2 * i + 1]);
                auxFrames_[2 * i].pack();
                auxFrames_[2 * i + 1].pack();
                auxFrames_[2 * i].setVisible(true);
                auxFrames_[2 * i + 1].setVisible(true);
                
            } catch (wave.multid.DimMismatchEx dex) {
                dex.printStackTrace();
            }
            
        }
        
        for (int i = 0; i < numOfPanels; i++) {
            
            wave.multid.view.ViewingTransform viewingTransf
                    = ((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(
                            i)).createTransform(clipping);
            
            try {
                wave.multid.view.Scene scene = RPnDataModule.PHASESPACE.createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
                
                frames_[i] = new RPnPhaseSpaceFrame(scene, commandMenu_);
                frames_[i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(i)).label());

                /*
                 * Each controller will be installed in Panel
                 * constructor and the central UIController
                 * will be controlling all Panels.
                 * All Panels listen to all Panels...
                 */
                UIController.instance().install(frames_[i].phaseSpacePanel());
                
                setFramesPosition(frames_[i]);
                frames_[i].pack();
                frames_[i].setVisible(true);
                
            } catch (wave.multid.DimMismatchEx dex) {
                dex.printStackTrace();
            }
            
        }
        
        createPanelsChooser();
        
    }
    
    private void associatesPhaseSpaces() {

        //Phase Spaces associations
        ArrayList<RPnPhaseSpaceAbstraction> leftPhaseSpaceArray = new ArrayList<RPnPhaseSpaceAbstraction>();
        ArrayList<RPnPhaseSpaceAbstraction> rightPhaseSpaceArray = new ArrayList<RPnPhaseSpaceAbstraction>();
        
        ArrayList<RPnPhaseSpaceAbstraction> centerPhaseSpaceArray = new ArrayList<RPnPhaseSpaceAbstraction>();
        
        centerPhaseSpaceArray.add(RPnDataModule.RIGHTPHASESPACE);
        centerPhaseSpaceArray.add(RPnDataModule.LEFTPHASESPACE);
        
        leftPhaseSpaceArray.add(RPnDataModule.RIGHTPHASESPACE);
        rightPhaseSpaceArray.add(RPnDataModule.LEFTPHASESPACE);
        
        RPnPhaseSpaceManager.instance().register(RPnDataModule.LEFTPHASESPACE, leftPhaseSpaceArray);
        RPnPhaseSpaceManager.instance().register(RPnDataModule.RIGHTPHASESPACE, rightPhaseSpaceArray);
        
        RPnPhaseSpaceManager.instance().register(RPnDataModule.PHASESPACE, centerPhaseSpaceArray);
        
    }
    
    private void associatePhaseSpacesAndCurvesList() {

        //Phase Spaces and curves list associations
        RPnCurvesList curvesList = new RPnCurvesList("Main", RPnDataModule.PHASESPACE);
        RPnCurvesList leftCurvesList = new RPnCurvesList("Left", RPnDataModule.LEFTPHASESPACE);
        RPnCurvesList rightCurvesList = new RPnCurvesList("Right", RPnDataModule.RIGHTPHASESPACE);
        
        RPnDataModule.PHASESPACE.attach(curvesList);
        RPnDataModule.LEFTPHASESPACE.attach(leftCurvesList);
        RPnDataModule.RIGHTPHASESPACE.attach(rightCurvesList);
        
        curvesList.setVisible(true);
        leftCurvesList.setVisible(true);
        rightCurvesList.setVisible(true);
        
        curvesList.addObserver(RPnDataModule.PHASESPACE);
        leftCurvesList.addObserver(RPnDataModule.LEFTPHASESPACE);
        rightCurvesList.addObserver(RPnDataModule.RIGHTPHASESPACE);
        
        curvesList.addObserver(RarefactionCurvePlotCommand.instance());
        curvesList.addObserver(CurveSelectionCommand.instance());
        
        curvesList.addObserver(CurveRefineCommand.instance());
        leftCurvesList.addObserver(CurveRefineCommand.instance());
        rightCurvesList.addObserver(CurveRefineCommand.instance());
        
        curvesList.addObserver(RiemannProfileCommand.instance());
        leftCurvesList.addObserver(RiemannProfileCommand.instance());
        rightCurvesList.addObserver(RiemannProfileCommand.instance());
        
        curvesList.addObserver(WaveCurveSpeedPlotCommand.instance());
        leftCurvesList.addObserver(WaveCurveSpeedPlotCommand.instance());
        rightCurvesList.addObserver(WaveCurveSpeedPlotCommand.instance());
        
        curvesList.addObserver(GenericExtensionCurveCommand.instance());
        leftCurvesList.addObserver(GenericExtensionCurveCommand.instance());
        rightCurvesList.addObserver(GenericExtensionCurveCommand.instance());
        
        curvesList.addObserver(AdjustedSelectionPlotCommand.instance());
        leftCurvesList.addObserver(AdjustedSelectionPlotCommand.instance());
        rightCurvesList.addObserver(AdjustedSelectionPlotCommand.instance());
        
        curvesList.addObserver(EnableLRPhaseSpaceCommand.instance());
        leftCurvesList.addObserver(EnableLRPhaseSpaceCommand.instance());
        rightCurvesList.addObserver(EnableLRPhaseSpaceCommand.instance());
        
        curvesList.addObserver(WaveCurveRRegionsPlotCommand.instance());
        leftCurvesList.addObserver(WaveCurveRRegionsPlotCommand.instance());
        rightCurvesList.addObserver(WaveCurveRRegionsPlotCommand.instance());
        
        curvesList.addObserver(WaveCurvePlotCommand.instance());
        leftCurvesList.addObserver(WaveCurvePlotCommand.instance());
        rightCurvesList.addObserver(WaveCurvePlotCommand.instance());
        
        curvesList.update();
//        leftCurvesList.update();
//        rightCurvesList.update();

    }
    
    private void createPanelsChooser() {
        
        int rows = getPhaseSpaceFrames().length + getAuxFrames().length;
        
        panelsChooserPanel_.setLayout(new GridLayout(rows, 1));
        
        for (RPnPhaseSpaceFrame mainFrame : getPhaseSpaceFrames()) {
            
            JCheckBox checkBox = new JCheckBox(mainFrame.getTitle());
            checkBox.addItemListener(new PanelsSeletectedListener(mainFrame));
            panelsChooserPanel_.add(checkBox);
        }
        
        for (RPnPhaseSpaceFrame auxFrame : getAuxFrames()) {
            JCheckBox checkBox = new JCheckBox(auxFrame.getTitle());
            checkBox.addItemListener(new PanelsSeletectedListener(auxFrame));
            panelsChooserPanel_.add(checkBox);
            
        }
        
    }

//     from here on just for 2D for now...
    void createSVGImage_actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setAccessory(panelsChooserPanel_);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("SVG File", "svg", "SVG"));
        chooser.setSelectedFile(new File("image.svg"));
        
        int status = chooser.showSaveDialog(this);
        
        if (status == JFileChooser.CANCEL_OPTION || status == JFileChooser.ERROR_OPTION) {
            return;
        }
        try {
            String path = chooser.getSelectedFile().getAbsolutePath();
            
            if (selectectedPanels.isEmpty()) {
                JOptionPane.showMessageDialog(chooser, "Choose a panel", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                
                for (RPnPhaseSpaceFrame phaseSpaceFrame : selectectedPanels) {
                    
                    String fileName = File.separator + phaseSpaceFrame.getTitle();
                    
                    File file = new File(path + fileName);
                    phaseSpaceFrame.phaseSpacePanel().createSVG(file);
                    
                }
                
            }
            
        } catch (java.lang.NullPointerException ex) {
            ex.printStackTrace();
        }
    }
    
    void printMenuItem_actionPerformed(ActionEvent e) {
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        for (int i = 0; i < frames_.length; i++) {
            pj.setPrintable(frames_[i].phaseSpacePanel());
            if (pj.printDialog()) {
                try {
                    pj.print();
                } catch (java.awt.print.PrinterException pe) {
                    pe.printStackTrace();
                }
            }
        }
    }
    
    void matlabExport_actionPerformed(ActionEvent e) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("script.m"));
            chooser.setFileFilter(new FileNameExtensionFilter("Matlab file", "m"));
            
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                
                dir = chooser.getSelectedFile().getParent();
                
                GregorianCalendar calendar = new GregorianCalendar();
                
                String day = String.valueOf(calendar.get(GregorianCalendar.DATE));
                String month = String.valueOf(GregorianCalendar.MONTH);
                String year = String.valueOf(calendar.get(GregorianCalendar.YEAR));
                String date = day + "_" + month + "_" + year;
                System.out.println(date);
                
                String hour = String.valueOf(calendar.get(GregorianCalendar.HOUR_OF_DAY));
                String minute = String.valueOf(calendar.get(GregorianCalendar.MINUTE));
                String time = hour + ":" + minute;
                System.out.println(time);
                // ---

                String newPath = "/" + date + " " + time;
                File folder = new File(dir + newPath);
                folder.mkdir();
                
                dir = dir + newPath;
                
                System.out.println("Diretorio selecionado : " + dir);
                
                FileWriter writer = new FileWriter(dir + "/" + chooser.getSelectedFile().getName());
                
                if (RPNUMERICS.domainDim() == 3) {
                    RPnDataModule.matlabExport(writer);
                } else {
                    RPnDataModule.matlabExport2D(writer);
                }
                
                writer.close();
            }
            
        } catch (java.io.IOException ioex) {
            ioex.printStackTrace();
        } catch (java.lang.NullPointerException nullEx) {
        }
    }

    // Exports the Riemann Profile solution only...
    void exportData_actionPerformed(ActionEvent e) {
        
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("RP.OUT"));
        chooser.setFileFilter(new FileNameExtensionFilter("rpn session output file", "out"));
        
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            
            RPnFileWriter.desktopExport(chooser.getSelectedFile().getAbsolutePath());
            dir = chooser.getSelectedFile().getParent();
            
        }
    }

    // saves the whole user commands session...
    void saveSession_actionPerformed(ActionEvent e) {
        try {
            
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(RPN_SESSION_FILENAME));
            chooser.setFileFilter(new FileNameExtensionFilter("XML File", "xml", "XML"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                
                saveRPnSession(chooser.getSelectedFile().getAbsolutePath());
                
            }
            
        } catch (java.io.IOException ioex) {
            ioex.printStackTrace();
        } catch (java.lang.NullPointerException nullEx) {
            nullEx.printStackTrace();
        }
    }
    
    public static void clearStatusMessage() {
        statusLabel_.setForeground(Color.black);
        setStatusMessage("", 0);
    }
    
    public static void setStatusMessage(String message, int messageType) {
        switch (messageType) {
            case 1://Error message;
                statusLabel_.setForeground(Color.red);
                
                break;
        }
        
        statusLabel_.setText(message);
        
    }
    
    void networkMenuItem_actionPerformed(ActionEvent e) {
        
        commandMenu_.networkCommand();
    }
    
    public static List<RPnPhaseSpaceFrame> getCharacteristicsFrames() {
        return characteristicsFrames_;
    }
    
    private void jbInit() throws Exception {
        
        setUIFramePosition();
        
        buttonGroup.add(curvesRadioButton_);
        buttonGroup.add(extensionRadioButton_);
        buttonGroup.add(riemannRadioButton_);
//        buttonGroup.add(phaseDiagramRadioButton_);

        curvesRadioButton_.addActionListener(new StateHandler());
        riemannRadioButton_.addActionListener(new StateHandler());
        phaseDiagramRadioButton_.addActionListener(new StateHandler());
        extensionRadioButton_.addActionListener(new StateHandler());
        
        UIController.instance().setState(new CurvesConfig());
        
        configPanel_.add(curvesRadioButton_);
        configPanel_.add(extensionRadioButton_);
        configPanel_.add(riemannRadioButton_);
//        configPanel_.add(phaseDiagramRadioButton_);
        contentPane = (JPanel) this.getContentPane();
        
        BoxLayout uiFrameLayout_ = new BoxLayout(contentPane, BoxLayout.PAGE_AXIS);
        BoxLayout auxiliarPanelLayout = new BoxLayout(utilitiesToolBarPanel, BoxLayout.X_AXIS);
        contentPane.setLayout(uiFrameLayout_);
        utilitiesToolBarPanel.setLayout(auxiliarPanelLayout);
        contentPane.add(configPanel_);
        
        contentPane.add(toolBarPanel_);
        
        contentPane.add(utilitiesToolBarPanel);
        getContentPane().add(statusLabel_);
        setPreferredSize(new Dimension(750, 850));
        setResizable(true);
        setTitle(RPNUMERICS.physicsID());
        fileMenu.setText("File");
        jMenuFileExit.setText("Exit");
        
        UIController.instance().showCursorLines(true);
        
        KeyStroke keyStroke = KeyStroke.getKeyStroke('l');
        
        showMainCurvesPaneltem_.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        RPnDataModule.PHASESPACE.showCurvesFrame(showMainCurvesPaneltem_.isSelected());
                        
                    }
                });
        
        showLeftCurvesPaneltem_.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        RPnDataModule.LEFTPHASESPACE.showCurvesFrame(showLeftCurvesPaneltem_.isSelected());
                        
                    }
                });
        
        showRightCurvesPaneltem_.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        RPnDataModule.RIGHTPHASESPACE.showCurvesFrame(showRightCurvesPaneltem_.isSelected());
                        
                    }
                });
        
        showAuxPanel_.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        UIController.instance().setAuxPanels(showAuxPanel_.isSelected());
                        for (RPnPhaseSpaceFrame rPnPhaseSpaceFrame : auxFrames_) {
                            rPnPhaseSpaceFrame.setVisible(showAuxPanel_.isSelected());
                        }
                        
                    }
                });
        
        jMenuFileExit.addActionListener(
                new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        jMenuFileExit_actionPerformed(e);
                    }
                });
        
        exportMenuItem_.addActionListener(
                new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        exportData_actionPerformed(e);
                    }
                });
        
        helpMenu.setText("Help");
        jMenuHelpAbout.setText("About...");
        jMenuHelpAbout.addActionListener(
                new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        jMenuHelpAbout_actionPerformed(e);
                    }
                });
        
        jMenuHelpUpdate.setText("Upgrade...");
        jMenuHelpUpdate.addActionListener(
                new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        try {
                            
                            jMenuHelpUpdate_actionPerformed(e);
                            
                        } catch (IOException ex1) {
                            
                            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "Error in updating RPn...");
                            
                        } catch (InterruptedException ex2) {
                            
                            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "Error in updating RPn...");
                        }
                        
                    }
                });
        
        saveSessionMenuItem_.addActionListener(
                new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        saveSession_actionPerformed(e);
                    }
                });
        editMenu.setText("Edit");
        
        editMenuItem1.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) RPnDataModule.PHASESPACE;
                        
                        Iterator<RpGeometry> geometryIterator = phaseSpace.getGeomObjIterator();
                        while (geometryIterator.hasNext()) {
                            RpGeometry geometry1 = geometryIterator.next();
                            geometry1.clearAnnotations();
                            
                        }
                        
                        RPnPhaseSpaceAbstraction leftPhaseSpace = (RPnPhaseSpaceAbstraction) RPnDataModule.LEFTPHASESPACE;
                        
                        Iterator<RpGeometry> geometryIteratorLeft = leftPhaseSpace.getGeomObjIterator();
                        while (geometryIteratorLeft.hasNext()) {
                            RpGeometry geometry2 = geometryIteratorLeft.next();
                            geometry2.clearAnnotations();
                            
                        }
                        
                        RPnPhaseSpaceAbstraction rightPhaseSpace = (RPnPhaseSpaceAbstraction) RPnDataModule.RIGHTPHASESPACE;
                        
                        Iterator<RpGeometry> geometryIteratorRight = rightPhaseSpace.getGeomObjIterator();
                        while (geometryIteratorRight.hasNext()) {
                            RpGeometry geometry3 = geometryIteratorRight.next();
                            geometry3.clearAnnotations();
                            
                        }
                        
                        UIController.instance().panelsUpdate();
                    }
                });
        
        editMenuItem2.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
                        while (phaseSpacePanelIterator.hasNext()) {
                            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();
                            panel.clearLastString();
                            panel.repaint();
                        }
                        
                    }
                });
        
        editMenuItem3.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
                        while (phaseSpacePanelIterator.hasNext()) {
                            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();
                            panel.clearVelocities();
                            panel.repaint();
                        }
                        
                    }
                });
        
        editMenuItem4.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
                        while (phaseSpacePanelIterator.hasNext()) {
                            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();
                            panel.clearClassifiers();
                            panel.repaint();
                        }
                        
                    }
                });
        
        editMenuItem5.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        RPnPhaseSpacePanel.blackBackground();
                    }
                });
        
        editMenuItem6.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        RPnPhaseSpacePanel.whiteBackground();
                    }
                });
        
        inputCoordsButton.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        
                        RPnCoordsInputDialog inputDialog = new RPnCoordsInputDialog(true, true);
                        inputDialog.setVisible(true);
                        
                    }
                });
        
        createSVGImageMenuItem.setText("Create SVG Image...");
        createSVGImageMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        createSVGImage_actionPerformed(e);
                    }
                });
        printMenuItem.setText("Print...");
        printMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        printMenuItem_actionPerformed(e);
                    }
                });
        modelInteractionMenu.setText("RP");
        
        networkMenuItem.setText("Network ...");
        networkMenuItem.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                networkMenuItem_actionPerformed(e);
            }
        });
        
        matlabExportMenuItem_.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                matlabExport_actionPerformed(e);
                
            }
        });
        
        alwaysOnCursorMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        RPnPhaseSpacePanel.setShowLastInputCursorMode(RPnPhaseSpacePanel.ALWAYSONTOP_LASTINPUT_CURSOR_MODE);
                        
                    }
                });
        
        alertCursorMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        RPnPhaseSpacePanel.setShowLastInputCursorMode(RPnPhaseSpacePanel.ALERT_LASTINPUT_CURSOR_MODE);
                        
                    }
                });
        
        neverCursorMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        RPnPhaseSpacePanel.setShowLastInputCursorMode(RPnPhaseSpacePanel.NEVER_LASTINPUT_CURSOR_MODE);
                        
                    }
                });
        
        extensionResetButton_.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                GenericExtensionCurveCommand.instance().reset();
                
            }
        });
        
        fileMenu.add(saveSessionMenuItem_);
        fileMenu.add(exportMenuItem_);
        fileMenu.add(matlabExportMenuItem_);
        fileMenu.addSeparator();
        fileMenu.add(networkMenuItem);
        
        fileMenu.addSeparator();
        fileMenu.add(createSVGImageMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(jMenuFileExit);
        helpMenu.add(jMenuHelpAbout);
        helpMenu.add(jMenuHelpUpdate);
        jMenuBar1.add(fileMenu);
        jMenuBar1.add(editMenu);
        jMenuBar1.add(viewMenu_);
        
        cursorMenu.add(alwaysOnCursorMenuItem);
        cursorMenu.add(alertCursorMenuItem);
        cursorMenu.add(neverCursorMenuItem);
        
        viewMenu_.add(cursorMenu);
        viewMenu_.addSeparator();
        
        viewMenu_.add(showMainCurvesPaneltem_);
        viewMenu_.add(showLeftCurvesPaneltem_);
        viewMenu_.add(showRightCurvesPaneltem_);
//        viewMenu_.add(showAuxPanel_);
        viewMenu_.add(EnableLRPhaseSpaceCommand.instance());
        jMenuBar1.add(modelInteractionMenu);
        
        jMenuBar1.add(helpMenu);
        setJMenuBar(jMenuBar1);
        
        editMenu.add(ClearPhaseSpaceCommand.instance());
        editMenu.addSeparator();
        
        editMenu.add(editMenuItem1);
        editMenu.addSeparator();
        editMenu.add(editMenuItem2);
        editMenu.addSeparator();
        editMenu.add(editMenuItem3);
        editMenu.addSeparator();
        editMenu.add(editMenuItem4);
        editMenu.addSeparator();
        editMenu.add(editMenuItem5);
        editMenu.addSeparator();
        editMenu.add(editMenuItem6);
        editMenu.addSeparator();
        
        editMenu.add(FillPhaseSpaceCommand.instance());
        editMenu.addSeparator();
    }
    
    private void shockConfigMenu() {
        
        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeSigmaCommand.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(FindProfileCommand.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(ChangeFluxParamsCommand.instance());
        modelInteractionMenu.add(shockMenuItem_);
        modelInteractionMenu.add(configurationMenuItem_);       // ??????
        modelInteractionMenu.add(ChangeXZeroCommand.instance());
        
    }
    
    private void rarefactionConfigMenu() {
        
        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeFluxParamsCommand.instance());
        modelInteractionMenu.add(ChangeOrbitLevelCommand.instance());
        modelInteractionMenu.add(CurveRefineCommand.instance());
        modelInteractionMenu.add(GenericExtensionCurveCommand.instance());
        
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(configurationMenuItem_);
        modelInteractionMenu.add(BifurcationRefineCommand.instance());
        BifurcationRefineCommand.instance().setEnabled(true);
        
    }
    
    private void bifurcationConfigMenu() {
        
        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeFluxParamsCommand.instance());
        modelInteractionMenu.add(configurationMenuItem_);
        
    }
    
    private void setUIFramePosition() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        int height = dim.height;
        int width = dim.width;
        this.setLocation((int) (width - (width * .55)), (int) (height - (height * .9)));
        this.setLocation((int) (width - (width * .55)), 100);
    }
    
    private void setFramesPosition(Component component) {
        
        int newwidth = (int) 100;
        int newheight = (int) 100;
        component.setLocation(newwidth, newheight);
    }
    
    public static RPnPhaseSpaceFrame[] getRiemannFrames() {
        return riemannFrames_;
    }
    
    public static RPnPhaseSpaceFrame[] getPhaseSpaceFrames() {
        return frames_;
    }
    
    public static JFrame[] getAllFrames() {
        
        JFrame[] frames = RPnUIFrame.getPhaseSpaceFrames();
        JFrame[] aux_frames = RPnUIFrame.getAuxFrames();
        JFrame[] riemann_frames = RPnUIFrame.getRiemannFrames();
        
        JFrame[] allFrames = null;
        
        if (riemann_frames != null) {
            allFrames = new RPnPhaseSpaceFrame[frames.length + aux_frames.length + riemann_frames.length];
        } else {
            allFrames = new RPnPhaseSpaceFrame[frames.length + aux_frames.length];
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
        
        return allFrames;
    }
    
    public static JFrame getFrame(String frameTitle) throws IllegalArgumentException {
        
        JFrame[] allFrames = getAllFrames();
        
        for (int i = 0; i < allFrames.length; i++) {
            if (allFrames[i].getTitle().compareTo(frameTitle) == 0) {
                return allFrames[i];
            }
        }
        
        throw new IllegalArgumentException("No Frame with the specified title...");
        
    }
    
    public static void disableSliders() {
        for (int i = 0; i < RPnUIFrame.getPhaseSpaceFrames().length; i++) {
            
            RPnPhaseSpaceFrame frame = RPnUIFrame.getPhaseSpaceFrames()[i];
            frame.getSlider().setEnabled(false);
        }
    }
    
    public static void enableSliders() {
        for (int i = 0; i < RPnUIFrame.getPhaseSpaceFrames().length; i++) {
            
            RPnPhaseSpaceFrame frame = RPnUIFrame.getPhaseSpaceFrames()[i];
            frame.getSlider().setEnabled(true);
        }
    }
    
    public static boolean toggleNoteboardMode(String frameTitle) {
        
        boolean padmodeOff = false;
        
        JFrame[] allFrames = RPnUIFrame.getAllFrames();
        
        for (int i = 0; i < allFrames.length; i++) {
            if (allFrames[i].getTitle().compareTo(frameTitle) == 0) {
                
                padmodeOff = allFrames[i].getGlassPane().isVisible();
                allFrames[i].getGlassPane().setVisible(!allFrames[i].getGlassPane().isVisible());
                
            } else {
                
                allFrames[i].getGlassPane().setVisible(false);
            }
        }
        
        return padmodeOff;
    }
    
    public static void noteboardClear() {
        
        JFrame[] allFrames = RPnUIFrame.getAllFrames();
        for (int i = 0; i < allFrames.length; i++) {
            if (allFrames[i].getTitle().compareTo(RPnNetworkStatus.instance().ACTIVATED_FRAME_TITLE) == 0) {
                
                UIController.instance().setActivePhaseSpace(
                        ((RPnPhaseSpaceAbstraction) ((RPnPhaseSpaceFrame) allFrames[i]).phaseSpacePanel().scene().getAbstractGeom()));
                
                ((RPnGlassPane) allFrames[i].getGlassPane()).clear();
                allFrames[i].repaint();
            }
        }
    }
    
    public static void toggleFocusGained() {
        
        JFrame[] allFrames = RPnUIFrame.getAllFrames();
        
        for (int i = 0; i < allFrames.length; i++) {
            if (allFrames[i].getTitle().compareTo(RPnNetworkStatus.instance().ACTIVATED_FRAME_TITLE) == 0) {
                
                if (allFrames[i] instanceof RPnPhaseSpaceFrame) {
                    UIController.instance().setActivePhaseSpace(
                            ((RPnPhaseSpaceAbstraction) ((RPnPhaseSpaceFrame) allFrames[i]).phaseSpacePanel().scene().getAbstractGeom()));
                }
                
                allFrames[i].setAlwaysOnTop(true);

                // there was a refresh issue going ... NOT A FIX !
                allFrames[i].repaint();
                
            } else {
                allFrames[i].setAlwaysOnTop(false);
                
            }
        }
        
    }
    
    public void enableNoteboard() {
        
        for (int i = 0; i < RPnUIFrame.getPhaseSpaceFrames().length; i++) {
            
            RPnPhaseSpaceFrame frame = RPnUIFrame.getPhaseSpaceFrames()[i];
            frame.getNoteboardToggleButton().setEnabled(true);
        }
        
        for (int i = 0; i < RPnUIFrame.getAuxFrames().length; i++) {
            RPnPhaseSpaceFrame frame = RPnUIFrame.getAuxFrames()[i];
            frame.getNoteboardToggleButton().setEnabled(true);
        }
        
        if (RPnUIFrame.getRiemannFrames() != null) {
            for (int i = 0; i < RPnUIFrame.getRiemannFrames().length; i++) {
                RPnPhaseSpaceFrame frame = RPnUIFrame.getRiemannFrames()[i];
                frame.getNoteboardToggleButton().setEnabled(true);
            }
        }
    }
    
    public void disableNoteboard() {
        
        for (int i = 0; i < RPnUIFrame.getPhaseSpaceFrames().length; i++) {
            
            RPnPhaseSpaceFrame frame = RPnUIFrame.getPhaseSpaceFrames()[i];
            frame.getNoteboardToggleButton().setEnabled(false);
        }
        
        for (int i = 0; i < RPnUIFrame.getAuxFrames().length; i++) {
            RPnPhaseSpaceFrame frame = RPnUIFrame.getAuxFrames()[i];
            frame.getNoteboardToggleButton().setEnabled(false);
        }
        
        if (RPnUIFrame.getRiemannFrames() != null) {
            for (int i = 0; i < RPnUIFrame.getRiemannFrames().length; i++) {
                RPnPhaseSpaceFrame frame = RPnUIFrame.getRiemannFrames()[i];
                frame.getNoteboardToggleButton().setEnabled(false);
            }
        }
    }
    
    protected void saveRPnSession(String absolutePath) throws IOException {
        
        FileWriter writer = new FileWriter(absolutePath);
        writer.write(RPnConfigReader.XML_HEADER);
        writer.write("<RPNSESSION id=" + '\"' + RPnCommandModule.SESSION_ID_ + '\"' + ">\n");
        writer.write(" <PHASESPACE name=\"Phase Space\">\n");
        writer.write("  <RPNCONFIGURATION>\n");
        RPnNumericsModule.export(writer);
        RPnVisualizationModule.export(writer);
        writer.write("  </RPNCONFIGURATION>\n");
        writer.write(" </PHASESPACE>\n");
        RPnCommandModule.export(writer);
        writer.write("</RPNSESSION>");
        writer.close();
        
    }
    
    protected static void openLogFile() {
        
        try {
            
            logWriter_ = new FileWriter(RPN_LOG_FILENAME);
            logWriter_.write(RPnConfigReader.XML_HEADER);
            logWriter_.write("<RPNSESSION id=" + '\"' + RPnCommandModule.SESSION_ID_ + '\"' + ">\n");
            logWriter_.write(" <PHASESPACE name=\"Phase Space\">\n");
            logWriter_.write("  <RPNCONFIGURATION>\n");
            
            RPnNumericsModule.export(logWriter_);
            RPnVisualizationModule.export(logWriter_);
            
            logWriter_.write("  </RPNCONFIGURATION>\n");
            logWriter_.write(" </PHASESPACE>\n");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public static void commandLogAppend(String msg) {

//        try {
//            
//            logWriter_.write(msg + "\n");
//            
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
    
    public static void closeLogFile() {

//        try {
//                                    
//            logWriter_.write("</RPNSESSION>");
//            logWriter_.close();           
//            
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }                                
    }
    
    void setMainToolBarButtons(SortedSet<RpModelPlotCommand> mainToolBar) {
        mainButtonsToolBarList_.addAll(mainToolBar);
        createCurvesTooBar();
    }
    
    void setAuxToolBarButtons(SortedSet<RpModelPlotCommand> auxToolSelectedButtons) {
        auxButtonsToolBarList_.addAll(auxToolSelectedButtons);
        createAuxiliarCurvesToolBar();
    }
    
    private class ConfigAction implements Action {
        
        public Object getValue(String key) {
            if (key.equals(Action.NAME)) {
                return "Change resolution ...";
            }
            return null;
        }
        
        public void putValue(String key, Object value) {
        }
        
        public void setEnabled(boolean b) {
        }
        
        public boolean isEnabled() {
            return true;
            
        }
        
        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }
        
        public void removePropertyChangeListener(PropertyChangeListener listener) {
        }
        
        public void actionPerformed(ActionEvent e) {
            
            RPnResolutionDialog extensionCurve = new RPnResolutionDialog();
            extensionCurve.setVisible(true);
            
        }
    }
    
    private class StateHandler implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            
            UI_ACTION_SELECTED newState = null;
            
            if (phaseDiagramRadioButton_.isSelected()) {
                
                newState = new ShockConfig();
                UIController.instance().getActivePhaseSpace().changeState(new NumConfigImpl());
            }
            
            if (curvesRadioButton_.isSelected()) {
                newState = new CurvesConfig();
                UIController.instance().getActivePhaseSpace().changeState(new NumConfigImpl());
                
            }
            
            if (extensionRadioButton_.isSelected()) {
                newState = new ExtensionCurvesConfig();
                UIController.instance().getActivePhaseSpace().changeState(new NumConfigImpl());
            }
            
            if (riemannRadioButton_.isSelected()) {
                
              newState = new RiemannProblemConfig();
              UIController.instance().getActivePhaseSpace().changeState(new RiemanProfileState());
 
                
            }
            
            UIController.instance().setState(newState);
            
        }
    }
    
    private class PanelsSeletectedListener implements ItemListener {
        
        private RPnPhaseSpaceFrame panel_;
        
        public PanelsSeletectedListener(RPnPhaseSpaceFrame panel) {
            panel_ = panel;
        }
        
        public void itemStateChanged(ItemEvent e) {
            
            JCheckBox checkBox = (JCheckBox) e.getItem();
            
            if (checkBox.isSelected()) {
                selectectedPanels.add(panel_);
                
            } else {
                selectectedPanels.remove(panel_);
            }
            
        }
    }
    
}
