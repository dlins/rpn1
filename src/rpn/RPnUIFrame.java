/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import rpn.usecase.*;
import rpn.parser.*;
import rpnumerics.RpNumerics;
import wave.util.Boundary;
import wave.multid.graphs.ClippedShape;
import java.awt.print.PrinterJob;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.FileWriter;
import rpn.controller.ui.*;
import rpn.message.*;


public class RPnUIFrame extends JFrame implements PropertyChangeListener {
    //
    // Members
    //
    JPanel contentPane, curvePanel;
    
    RPnCurveConfigPanel configPanel;
    
    JMenuBar jMenuBar1 = new JMenuBar();
    JCheckBox resultsOption = new JCheckBox("Save With Results");
    JMenu jMenuFile = new JMenu();
    JMenuItem jMenuFileExit = new JMenuItem();
    JMenu jMenuHelp = new JMenu();
    JMenuItem jMenuHelpAbout = new JMenuItem();
    BorderLayout borderLayout1 = new BorderLayout();
    JMenuItem exportMenuItem = new JMenuItem();
    JMenu jMenu1 = new JMenu();
    JMenuItem layoutMenuItem = new JMenuItem();
    JMenuItem errorControlMenuItem = new JMenuItem();
    
    JMenu modelInteractionMenu = new JMenu();
    JToolBar toolBar = new JToolBar();
    
    JMenuItem createJPEGImageMenuItem = new JMenuItem();
    JMenuItem printMenuItem = new JMenuItem();
    JMenuItem clearPhaseSpaceMenuItem = new JMenuItem();
    JMenuItem changeXZeroMenuItem = new JMenuItem();
    private UIController uiController_ = null;
    private RPnPhaseSpaceFrame[] frames_ = null;
    private RPnMenuCommand commandMenu_ = null;
    
    
    //    private static Dimension defaultFrameSize_;
    
    JMenuItem networkMenuItem = new JMenuItem();
    
    
    //Construct the frame
    public RPnUIFrame(RPnMenuCommand command) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            // TODO may be UIController should control PHASESPACE as well
            commandMenu_ = command;
            RPnNetworkStatusController.instance().addPropertyChangeListener(this);
            uiController_ = UIController.instance();
            phaseSpaceFramesInit(RpNumerics.boundary());
            createModelInteractionMenu();
            createToolBar();
            UndoActionController.createInstance();
            jbInit();
            
            if (commandMenu_ instanceof RPnAppletPlotter) { // Selecting itens to disable in Applet
                
                networkMenuItem.setEnabled(false);
                createJPEGImageMenuItem.setEnabled(false);
                printMenuItem.setEnabled(false);
                exportMenuItem.setEnabled(false);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    protected void createModelInteractionMenu() {
        modelInteractionMenu.add(ChangeXZeroAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(ChangeSigmaAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(FindProfileAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(ChangeFluxParamsAgent.instance());
    }
    
    protected void createToolBar() {
        
        toolBar.setFloatable(false);
        
        toolBar.add(ForwardOrbitPlotAgent.instance().getContainer());
        toolBar.add(BackwardOrbitPlotAgent.instance().getContainer());
        toolBar.add(ForwardManifoldPlotAgent.instance().getContainer());
        toolBar.add(BackwardManifoldPlotAgent.instance().getContainer());
        toolBar.add(StationaryPointPlotAgent.instance().getContainer());
        toolBar.add(PoincareSectionPlotAgent.instance().getContainer());
    }
    
    
    //Component initialization
    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(ShockFlowControlFrame.class.getResource("[Your Icon]")));
        
        borderLayout1.setHgap(10);
        borderLayout1.setVgap(10);
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setResizable(false);
        this.setTitle("");
        jMenuFile.setText("File");
        jMenuFileExit.setText("Exit");
        
        curvePanel= new JPanel();
        
        configPanel = new RPnCurveConfigPanel();
        
        GridBagLayout curvePanelLayout = new GridBagLayout();
        
        GridBagConstraints configPanelConstraints = new GridBagConstraints();
        
        GridBagConstraints curvePlotConstraints = new GridBagConstraints();
        
        configPanelConstraints.gridx=0;
        configPanelConstraints.gridy=0;
        
        curvePlotConstraints.gridx=1;
        curvePlotConstraints.gridx=0;
        
        Insets insets = new Insets(10,80,20,80);
        
        curvePlotConstraints.insets=insets;
        
        curvePlotConstraints.ipady=20;

        curvePlotConstraints.fill=GridBagConstraints.BOTH;

        curvePlotConstraints.anchor=GridBagConstraints.CENTER;
        
        curvePanelLayout.setConstraints(configPanel,configPanelConstraints);
        
        curvePanelLayout.setConstraints(CurvePlotAgent.instance().getContainer(),curvePlotConstraints);
        
        curvePanel.setLayout(curvePanelLayout);
        
        curvePanel.add(configPanel);
        
        curvePanel.add(CurvePlotAgent.instance().getContainer());
        
        JSeparator separator = new JSeparator();
        
        separator.setOrientation(SwingConstants.VERTICAL);
        
        contentPane.add(separator,BorderLayout.CENTER);
        
        contentPane.add(curvePanel,BorderLayout.EAST);
        
        CurvePlotAgent.instance().setEnabled(true); //TODO Remove from here !
        
        resultsOption.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkB = (JCheckBox) e.getSource();
                rpn.parser.RPnDataModule.RESULTS = checkB.isSelected();
            }
        });
        
        jMenuFileExit.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuFileExit_actionPerformed(e);
            }
        });
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuHelpAbout_actionPerformed(e);
            }
        });
        exportMenuItem.setText("Save As...");
        exportMenuItem.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                export_actionPerformed(e);
            }
        });
        jMenu1.setText("Edit");
        errorControlMenuItem.setText("Error Control...");
        layoutMenuItem.setText("Scene Layout...");
        layoutMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layoutMenuItem_actionPerformed(e);
            }
        });
        
        errorControlMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                errorControlMenuItem_actionPerformed(e);
            }
        });
        createJPEGImageMenuItem.setText("Create JPEG Image...");
        createJPEGImageMenuItem.addActionListener(
                new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJPEGImage_actionPerformed(e);
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
        jMenuFile.addSeparator();
        networkMenuItem.setText("Network ...");
        networkMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                networkMenuItem_actionPerformed(e);
            }
        });
        jMenuFile.add(exportMenuItem);
        jMenuFile.addSeparator();
        jMenuFile.add(networkMenuItem);
        jMenuFile.addSeparator();
        jMenuFile.add(createJPEGImageMenuItem);
        jMenuFile.addSeparator();
        jMenuFile.add(printMenuItem);
        jMenuFile.addSeparator();
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenu1);
        jMenuBar1.add(modelInteractionMenu);
        jMenuBar1.add(jMenuHelp);
        setJMenuBar(jMenuBar1);
        
        GridLayout gridLayout = new GridLayout(2,4,10,10);
        
        toolBar.setLayout(gridLayout);
        
        contentPane.add(toolBar, BorderLayout.WEST);
        jMenu1.add(UndoActionController.instance());
        jMenu1.addSeparator();
        jMenu1.add(layoutMenuItem);
        jMenu1.addSeparator();
        jMenu1.add(errorControlMenuItem);
        jMenu1.addSeparator();
        jMenu1.add(ClearPhaseSpaceAgent.instance());
        jMenu1.addSeparator();
        jMenu1.add(FillPhaseSpaceAgent.instance());
    }
    
    //
    // Accessors/Mutators
    public UIController uiController() {
        return uiController_;
    }
    
    public RPnPhaseSpaceFrame[] getPhaseSpaceFrames() {
        return frames_;
    }
    
    
    //
    // Methods
    //
    
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        if (evt.getPropertyName().equals("Network MenuItem Clicked")) {
            networkMenuItem.setEnabled(false);
        }
        
        if (evt.getPropertyName().equals("Dialog Closed")) {
            networkMenuItem.setEnabled(true);
        }
        
    }
    
    
    //File | Exit action performed
    public void jMenuFileExit_actionPerformed(ActionEvent e) {
        commandMenu_.finalizeApplication();
    }
    
    //Help | About action performed
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    }
    
    //Overridden so we can exit when window is closed
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
        RPnDataModule.PHASESPACE.update();
        uiController().panelsUpdate();
    }
    
    void errorControlMenuItem_actionPerformed(ActionEvent e) {
        RPnErrorControlDialog dialog = new RPnErrorControlDialog();
        Dimension dlgSize = dialog.getPreferredSize();
        Dimension frmSize = new Dimension(1280, 1024);
        Point loc = new Point(0, 0);
        dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);
    }
    
//    public void addFlowName(String flowName){
//
//        flowComboBox.addItem(flowName);
//
//    }
    
    
//    public void addMethodName(String methodName){
//
//        methodComboBox.addItem(methodName);
//
//    }
    
    
    protected void phaseSpaceFramesInit(Boundary boundary) {
        
        ClippedShape clipping = new ClippedShape(boundary);
        int numOfPanels = RPnVisualizationModule.DESCRIPTORS.size();
        frames_ = new RPnPhaseSpaceFrame[numOfPanels];
        for (int i = 0; i < numOfPanels; i++) {
            wave.multid.view.ViewingTransform viewingTransf =
                    ((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(
                    i)).createTransform(clipping);
            try {
                wave.multid.view.Scene scene = RPnDataModule.PHASESPACE.
                        createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
                frames_[i] = new RPnPhaseSpaceFrame(scene, commandMenu_);
                frames_[i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.
                        DESCRIPTORS.get(i)).label());
                
                /*
                 * controllers installation
                 *
                 * Each controller will be installed in Panel
                 * constructor and the central UIController
                 * will be controlling all Panels.
                 * All Panels listen to all Panels...
                 */
                
                uiController_.install(frames_[i].phaseSpacePanel());
                
                frames_[i].pack();
                frames_[i].setVisible(true);
            } catch (wave.multid.DimMismatchEx dex) {
                dex.printStackTrace();
            }
            
        }
    }
    
    // from here on just for 2D for now...
    void createJPEGImage_actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(this);
        
        try {
            frames_[0].phaseSpacePanel().createJPEGImageFile(chooser.
                    getSelectedFile().getAbsolutePath());
        } catch (java.lang.NullPointerException ex) {
            
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
    
    void export_actionPerformed(ActionEvent e) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setAccessory(resultsOption);
            int option = chooser.showSaveDialog(this);
            FileWriter writer = new FileWriter(chooser.getSelectedFile().
                    getAbsolutePath());
            writer.write(RPnConfigReader.XML_HEADER);
            writer.write("<rpnconfig>\n");
            RPnNumericsModule.export(writer);
            RPnVisualizationModule.export(writer);
            RPnDataModule.export(writer);
            writer.write("</rpnconfig>");
            writer.close();
        } catch (java.io.IOException ioex) {
            ioex.printStackTrace();
        } catch (java.lang.NullPointerException nullEx) {}
    }
    
    
    void networkMenuItem_actionPerformed(ActionEvent e) {
        
        RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this,
                0, null));
        
        commandMenu_.networkCommand();
        
    }
    
    
}
