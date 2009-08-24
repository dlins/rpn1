/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.usecase.*;
import rpn.parser.*;
import rpnumerics.RPNUMERICS;
import wave.util.Boundary;
import java.awt.print.PrinterJob;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.FileWriter;
import rpn.controller.ui.*;
import rpn.message.*;
import rpnumerics.ShockProfile;

public class RPnUIFrame extends JFrame implements PropertyChangeListener {
    //
    // Members
    //
    private JPanel contentPane;
    private JPanel configPanel_ = new JPanel(new GridLayout(2,1));
    private JComboBox stateComboBox = new JComboBox();
    
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu editMenu = new JMenu();
    private JMenu fileMenu = new JMenu();
    private JMenu modelInteractionMenu = new JMenu();
    private JMenu helpMenu = new JMenu();
    private JCheckBox resultsOption = new JCheckBox("Save With Results");
    private JMenuItem shockMenuItem_ = new JMenuItem("Shock Configuration ...");
    private JMenuItem bifurcationMenuItem_ = new JMenuItem("Bifurcation Configuration ...");
    private JMenuItem rarefactionMenuItem_ = new JMenuItem("Rarefaction Config ...");
    private JMenuItem jMenuFileExit = new JMenuItem();
    private JMenuItem jMenuHelpAbout = new JMenuItem();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JMenuItem exportMenuItem = new JMenuItem();
    private JMenuItem layoutMenuItem = new JMenuItem();
    private JMenuItem errorControlMenuItem = new JMenuItem();
    private JMenuItem createJPEGImageMenuItem = new JMenuItem();
    private JMenuItem printMenuItem = new JMenuItem();
    private JMenuItem pluginMenuItem = new JMenuItem();
    private RPnPhaseSpaceFrame[] frames_ = null;
    private RPnMenuCommand commandMenu_ = null;
    private JMenuItem networkMenuItem = new JMenuItem();
    private JCheckBoxMenuItem showCursorMenuItem_ = new JCheckBoxMenuItem("Show Cursor Lines");
    private JToolBar toolBar_ = new JToolBar();
    private static JLabel statusLabel_ = new JLabel();
    
    private JMenu viewMenu_ = new JMenu("View");

    //Construct the frame
    public RPnUIFrame(RPnMenuCommand command) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            // TODO may be UIController should control PHASESPACE as well
            commandMenu_ = command;
            RPnNetworkStatusController.instance().addPropertyChangeListener(this);
            UIController.instance().setStateController(new StateInputController(this));
            propertyChange(new PropertyChangeEvent(command, "aplication state", null, null));
            jbInit();
            phaseSpaceFramesInit(RPNUMERICS.boundary());
            addPropertyChangeListener(this);
            UndoActionController.createInstance();

            getContentPane().add(statusLabel_, BorderLayout.SOUTH);
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
    //
    // Methods
    //
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("aplication state")) {

            toolBar_.setLayout(new GridLayout(8, 2));
            toolBar_.setOrientation(SwingConstants.VERTICAL);

            if (UIController.instance().getState() instanceof SHOCK_CONFIG || (evt.getNewValue() instanceof SIGMA_CONFIG)|| evt.getNewValue() instanceof SHOCK_CONFIG) {

                shockConfigMenu();
                toolBar_.removeAll();
                toolBar_.add(OrbitPlotAgent.instance().getContainer());
                toolBar_.add(ForwardManifoldPlotAgent.instance().getContainer());
                toolBar_.add(BackwardManifoldPlotAgent.instance().getContainer());
                toolBar_.add(StationaryPointPlotAgent.instance().getContainer());
                toolBar_.add(PoincareSectionPlotAgent.instance().getContainer());
                toolBar_.add(HugoniotPlotAgent.instance().getContainer());
                toolBar_.add(ScratchAgent.instance().getContainer());
                ScratchAgent.instance().setEnabled(true);
                pack();

            }

            if (UIController.instance().getState() instanceof RAREFACTION_CONFIG || evt.getNewValue() instanceof RAREFACTION_CONFIG) {
                rarefactionConfigMenu();

                toolBar_.removeAll();
                toolBar_.add(HugoniotPlotAgent.instance().getContainer());

                toolBar_.add(BackwardShockCurvePlotAgent.instance().getContainer());
                toolBar_.add(ForwardShockCurvePlotAgent.instance().getContainer());
                toolBar_.add(RarefactionOrbitPlotAgent.instance().getContainer());

                toolBar_.add(CompositePlotAgent.instance().getContainer());
                toolBar_.add(ScratchAgent.instance().getContainer());
                ScratchAgent.instance().setEnabled(true);
                pack();
            }


            if (UIController.instance().getState() instanceof BIFURCATION_CONFIG || evt.getNewValue() instanceof BIFURCATION_CONFIG) {

                bifurcationConfigMenu();

                toolBar_.removeAll();
                toolBar_.add(BifurcationPlotAgent.instance().getContainer());
                toolBar_.add(ScratchAgent.instance().getContainer());
                ScratchAgent.instance().setEnabled(true);
                setStatusMessage("");
                pack();
            }
        }



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
        RPnDataModule.PHASESPACE.update();
        UIController.instance().panelsUpdate();
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
        rpnumerics.RPNUMERICS.errorControl().reset(dialog.getEps(),
                rpnumerics.RPNUMERICS.boundary());
    }

    protected void phaseSpaceFramesInit(Boundary boundary) {
        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        int numOfPanels = RPnVisualizationModule.DESCRIPTORS.size();
        frames_ = new RPnPhaseSpaceFrame[numOfPanels];
        for (int i = 0; i < numOfPanels; i++) {
            wave.multid.view.ViewingTransform viewingTransf =
                    ((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(
                    i)).createTransform(clipping);
            try {
                wave.multid.view.Scene scene = RPnDataModule.PHASESPACE.createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));
                frames_[i] = new RPnPhaseSpaceFrame(scene, commandMenu_);
                frames_[i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(i)).label());

                /*
                 * controllers installation
                 *
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
    }
    // from here on just for 2D for now...
    void createJPEGImage_actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        try {
            frames_[0].phaseSpacePanel().createJPEGImageFile(chooser.getSelectedFile().getAbsolutePath());
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
            chooser.showSaveDialog(this);
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
        } catch (java.lang.NullPointerException nullEx) {
        }
    }

    public static void setStatusMessage(String message) {
        statusLabel_.setText(message);
    }

    void networkMenuItem_actionPerformed(ActionEvent e) {

        RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this,
                0, null));

        commandMenu_.networkCommand();

    }

    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(ShockFlowControlFrame.class.getResource("[Your Icon]")));
        setUIFramePosition();

        stateComboBox.addItem("Phase Diagram");
        stateComboBox.addItem("Wave Curves");
        stateComboBox.addItem("Bifurcation Curves");
        stateComboBox.addActionListener(new StateHandler());
        
        configPanel_.add(stateComboBox);
        
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setResizable(false);
        this.setTitle("");
        fileMenu.setText("File");
        pluginMenuItem.setText("Plugins ...");
        jMenuFileExit.setText("Exit");

        showCursorMenuItem_.setSelected(true);

        UIController.instance().showCursorLines(showCursorMenuItem_.isSelected());

        KeyStroke keyStroke = KeyStroke.getKeyStroke('l');
        showCursorMenuItem_.setAccelerator(keyStroke);

       

        showCursorMenuItem_.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        UIController.instance().showCursorLines(showCursorMenuItem_.isSelected());


                    }
                });


        pluginMenuItem.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        RPnPluginDialog pluginDialog = new RPnPluginDialog();
                        pluginDialog.setVisible(true);
                    }
                });

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
        helpMenu.setText("Help");
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
        editMenu.setText("Edit");
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

        networkMenuItem.setText("Network ...");
        networkMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                networkMenuItem_actionPerformed(e);
            }
        });

        fileMenu.add(exportMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(networkMenuItem);
        fileMenu.add(pluginMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(createJPEGImageMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(jMenuFileExit);
        helpMenu.add(jMenuHelpAbout);
        jMenuBar1.add(fileMenu);
        jMenuBar1.add(editMenu);
        jMenuBar1.add(viewMenu_);
        viewMenu_.add(showCursorMenuItem_);
        jMenuBar1.add(modelInteractionMenu);

        toolBar_.setFloatable(false);

        jMenuBar1.add(helpMenu);
        setJMenuBar(jMenuBar1);
        contentPane.add(toolBar_, BorderLayout.CENTER);

        configPanel_.add(stateComboBox);
        configPanel_.add(new RPnCurvesConfigPanel());
        contentPane.add(configPanel_, BorderLayout.NORTH);

        editMenu.add(UndoActionController.instance());
        editMenu.addSeparator();
        editMenu.add(layoutMenuItem);
        editMenu.addSeparator();

        editMenu.add(ClearPhaseSpaceAgent.instance());
        editMenu.addSeparator();
        editMenu.add(FillPhaseSpaceAgent.instance());

    }

    private void shockConfigMenu() {
        shockMenuItem_.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        RPnShockConfigDialog shockConfigDialog = new RPnShockConfigDialog(false, false);
                        shockConfigDialog.setVisible(true);

                    }
                });

        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeSigmaAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(FindProfileAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(ChangeFluxParamsAgent.instance());

        modelInteractionMenu.add(shockMenuItem_);
        modelInteractionMenu.add(errorControlMenuItem);



    }

    private void rarefactionConfigMenu() {

        rarefactionMenuItem_.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        System.out.println("Chamando action");

                        RPnRarefactionConfigDialog rarefactionConfigDialog = new RPnRarefactionConfigDialog(false, false);

                        rarefactionConfigDialog.setVisible(true);
                    }
                });

        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeFluxParamsAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(rarefactionMenuItem_);
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(errorControlMenuItem);
        modelInteractionMenu.addSeparator();

    }

    private void bifurcationConfigMenu() {

        bifurcationMenuItem_.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        RPnBifurcationConfigDialog bifurcationConfigDialog = new RPnBifurcationConfigDialog(false, false);
                        bifurcationConfigDialog.setVisible(true);

                    }
                });

        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(bifurcationMenuItem_);
        modelInteractionMenu.addSeparator();


    }

    private void setUIFramePosition() {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode displayMode = gs[0].getDisplayMode();
        int height = displayMode.getHeight();
        int width = displayMode.getWidth();
        this.setLocation((int) (width - (width * .2)), (int) (height - (height * .8)));

    }

    private void setFramesPosition(Component component) {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode displayMode = gs[0].getDisplayMode();
        int height = displayMode.getHeight();
        int width = displayMode.getWidth();

        int newwidth = (int) (width - (width * .6));
        int newheight = (int) (height - (height * .8));
        component.setLocation(newwidth, newheight);

    }

    public RPnPhaseSpaceFrame[] getPhaseSpaceFrames() {
        return frames_;
    }
    
    
    private class StateHandler implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            
              UI_ACTION_SELECTED newState = null;

        if (stateComboBox.getSelectedItem().equals("Phase Diagram")) {

            newState = new SHOCK_CONFIG();
            RPNUMERICS.getShockProfile().setHugoniotMethodName(ShockProfile.HUGONIOT_METHOD_NAMES[1]);

        }

        if (stateComboBox.getSelectedItem().equals("Wave Curves")) {
            newState = new RAREFACTION_CONFIG();
            RPNUMERICS.getShockProfile().setHugoniotMethodName(ShockProfile.HUGONIOT_METHOD_NAMES[0]);

        }
        if (stateComboBox.getSelectedItem().equals("Bifurcation Curves")) {
            newState = new BIFURCATION_CONFIG();
            rpn.usecase.BifurcationPlotAgent.instance().setEnabled(true);

        }

        UIController.instance().setState(newState);
            
        }
        
    }
    
    
}
