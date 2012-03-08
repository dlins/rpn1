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
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.batik.ext.swing.GridBagConstants;
import rpn.component.util.AreaSelectionAgent2;
import rpn.component.util.ClassifierAgent;
import rpn.component.util.ControlClick;
import rpn.component.util.VelocityAgent;
import rpn.controller.ui.*;
import rpn.message.*;
import rpnumerics.ShockProfile;
import wave.util.RectBoundary;

public class RPnUIFrame extends JFrame implements PropertyChangeListener {

    //
    // Members
    //
    private JPanel contentPane;
    private JPanel configPanel_ = new JPanel();
    private JPanel toolBarPanel_ = new JPanel();
    private JPanel panelsChooserPanel_ = new JPanel();
    private ArrayList<RPnPhaseSpaceFrame> selectectedPanels = new ArrayList<RPnPhaseSpaceFrame>();
    private JComboBox stateComboBox = new JComboBox();
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu editMenu = new JMenu();
    private JMenu fileMenu = new JMenu();
    private JMenu modelInteractionMenu = new JMenu();
    private JMenu helpMenu = new JMenu();
    private JCheckBox resultsOption = new JCheckBox("Save With Results");
    private JMenuItem shockMenuItem_ = new JMenuItem("Shock Configuration ...");
    private JMenuItem configurationMenuItem_ = new JMenuItem(new ConfigAction());
    private JMenuItem jMenuFileExit = new JMenuItem();
    private JMenuItem matlabMenuFileExport_ = new JMenuItem("Export to Matlab ...");
    private JMenuItem jMenuHelpAbout = new JMenuItem();
    private GridBagLayout uiFrameLayout_ = new GridBagLayout();
    private JMenuItem exportMenuItem = new JMenuItem();
    private JMenuItem inputCoordsMenuItem = new JMenuItem("Input Coords ...");
    private JMenuItem createSVGImageMenuItem = new JMenuItem();
    private JMenuItem printMenuItem = new JMenuItem();
    private static RPnPhaseSpaceFrame[] frames_, auxFrames_;
    private RPnMenuCommand commandMenu_ = null;
    private JMenuItem networkMenuItem = new JMenuItem();
    private JCheckBoxMenuItem showCursorMenuItem_ = new JCheckBoxMenuItem("Show Cursor Lines");
    private JToolBar toolBar_ = new JToolBar();
    private static JLabel statusLabel_ = new JLabel();
    private JMenu viewMenu_ = new JMenu("View");
    private JCheckBoxMenuItem showCurvesPaneltem_ = new JCheckBoxMenuItem("Show Curves Window", true);
    private JCheckBoxMenuItem showAuxPanel_ = new JCheckBoxMenuItem("Show Auxiliar Panels", true);
    private RPnCurvesConfigPanel curvesConfigPanel_ = new RPnCurvesConfigPanel();
    private JFrame curvesFrame_;
    //*** declarei isso  -- Leandro
    private JMenuItem editMenuItem1 = new JMenuItem("Clears All Strings");
    private JMenuItem editMenuItem2 = new JMenuItem("Clears Last String");
    private JMenuItem editMenuItem3 = new JMenuItem("Clears Velocities");
    private JMenuItem editMenuItem4 = new JMenuItem("Clears Classifiers");
    private JMenuItem editMenuItem5 = new JMenuItem("Starts with Black Background");
    private JMenuItem editMenuItem6 = new JMenuItem("Starts with White Background");
    public static String dir = "";
    //***

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
            phaseSpaceFramesInit(RPNUMERICS.boundary());//Building default panel

            addPropertyChangeListener(this);
            UndoActionController.createInstance();

            if (commandMenu_ instanceof RPnAppletPlotter) { // Selecting itens to disable in Applet

                networkMenuItem.setEnabled(false);
                createSVGImageMenuItem.setEnabled(false);
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

    //** constroi a tabela de curvas
    public void setCurvesFrame(JFrame curvesFrame) {
        this.curvesFrame_ = curvesFrame;
        UIController.instance().showCurvesPanel(showCurvesPaneltem_.isSelected());


    }

    public static RPnPhaseSpaceFrame[] getAuxFrames() {
        return auxFrames_;
    }

    //** para os botoes do menu de tipos de curvas
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("aplication state")) {

            toolBar_.setLayout(new GridLayout(8, 2));
            toolBar_.setOrientation(SwingConstants.VERTICAL);


            if (evt.getNewValue() instanceof SHOCK_CONFIG || evt.getNewValue() instanceof SIGMA_CONFIG) {

                shockConfigMenu();
                toolBar_.removeAll();

                toolBar_.add(OrbitPlotAgent.instance().getContainer());
                toolBar_.add(ForwardManifoldPlotAgent.instance().getContainer());
                toolBar_.add(BackwardManifoldPlotAgent.instance().getContainer());
                toolBar_.add(StationaryPointPlotAgent.instance().getContainer());
                toolBar_.add(PoincareSectionPlotAgent.instance().getContainer());
                toolBar_.add(HugoniotPlotAgent.instance().getContainer());
                toolBar_.revalidate();

            }

            if (evt.getNewValue() instanceof RAREFACTION_CONFIG) {

                rarefactionConfigMenu();
                toolBar_.removeAll();

                toolBar_.add(HugoniotPlotAgent.instance().getContainer());
                toolBar_.add(ShockCurvePlotAgent.instance().getContainer());
                toolBar_.add(RarefactionOrbitPlotAgent.instance().getContainer());
                toolBar_.add(IntegralCurvePlotAgent.instance().getContainer());
                toolBar_.add(PointLevelCurvePlotAgent.instance().getContainer());
                toolBar_.add(LevelCurvePlotAgent.instance().getContainer());
                toolBar_.add(CompositePlotAgent.instance().getContainer());


                if (RPNUMERICS.boundary() instanceof RectBoundary) {
                    toolBar_.add(AreaSelectionAgent.instance().getContainer());     //** Edson/Leandro
                } else {
                    toolBar_.add(AreaSelectionAgent.instance().getContainer());     //** Leandro  -- completar com 2. opcao de botao de selecao (triangular)
                    toolBar_.add(AreaSelectionAgent2.instance().getContainer());    //** Leandro
                }

                toolBar_.add(ClassifierAgent.instance().getContainer());      //** Leandro
                toolBar_.add(VelocityAgent.instance().getContainer());        //** Leandro
                //toolBar_.add(BifurcationRefineAgent.instance().getContainer());     //** Leandro

                toolBar_.add(RarefactionExtensionCurvePlotAgent.instance().getContainer());


                toolBar_.add(AreaSelectionAgent.instance().getContainer());     //** Edson/Leandro
                toolBar_.add(ClassifierAgent.instance().getContainer());        //** Leandro
                toolBar_.add(VelocityAgent.instance().getContainer());        //** Leandro

                toolBar_.revalidate();

            }

            if (evt.getNewValue() instanceof BIFURCATION_CONFIG) {

                bifurcationConfigMenu();

                toolBar_.removeAll();
                toolBar_.add(DoubleContactAgent.instance().getContainer());
                toolBar_.add(BoundaryExtensionCurveAgent.instance().getContainer());
                toolBar_.add(InflectionPlotAgent.instance().getContainer());
                toolBar_.add(HysteresisPlotAgent.instance().getContainer());
                toolBar_.revalidate();

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

    //** para fechar a aplicacao pelo menu de tipos de curvas
    public void jMenuFileExit_actionPerformed(ActionEvent e) {
        commandMenu_.finalizeApplication();
    }

    //** ainda nao implementado
    //Help | About action performed
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    }

    //Overridden so we can exit when window is closed
    @Override
    // para fechar a aplicacao a partir de qualquer janela
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }

    //** nao vi alteracao
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

    //** para criar os frames (paineis) - incluindo os auxiliares
    protected void phaseSpaceFramesInit(Boundary boundary) {
        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        int numOfPanels = RPnVisualizationModule.DESCRIPTORS.size();

        auxFrames_ = new RPnPhaseSpaceFrame[2 * numOfPanels];
        frames_ = new RPnPhaseSpaceFrame[numOfPanels];

        int auxNumOfPanels = RPnVisualizationModule.AUXDESCRIPTORS.size();
        for (int i = 0; i < auxNumOfPanels / 2; i++) {
            wave.multid.view.ViewingTransform auxViewingTransf =
                    ((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(
                    i)).createTransform(clipping);
            try {
                wave.multid.view.Scene leftScene = RPnDataModule.LEFTPHASESPACE.createScene(auxViewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));

                wave.multid.view.Scene rightScene = RPnDataModule.RIGHTPHASESPACE.createScene(auxViewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));

                auxFrames_[i] = new RPnPhaseSpaceFrame(leftScene, commandMenu_);
                auxFrames_[i + 1] = new RPnPhaseSpaceFrame(rightScene, commandMenu_);

                auxFrames_[i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(i)).label());
                auxFrames_[i + 1].setTitle(((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(i + 1)).label());

                UIController.instance().install(auxFrames_[i].phaseSpacePanel());
                UIController.instance().install(auxFrames_[i + 1].phaseSpacePanel());
                setFramesPosition(auxFrames_[i]);
                setFramesPosition(auxFrames_[i + 1]);
                auxFrames_[i].pack();
                auxFrames_[i + 1].pack();
                auxFrames_[i].setVisible(true);
                auxFrames_[i + 1].setVisible(true);

            } catch (wave.multid.DimMismatchEx dex) {
                dex.printStackTrace();
            }

        }

        // Init Main Frame
        for (int i = 0; i < numOfPanels; i++) {
            wave.multid.view.ViewingTransform viewingTransf =
                    ((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(
                    i)).createTransform(clipping);
            try {
                wave.multid.view.Scene scene = RPnDataModule.PHASESPACE.createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));


                frames_[i] = new RPnPhaseSpaceFrame(scene, commandMenu_);
                frames_[i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(i)).label());

//
//                /*
//                 * controllers installation
//                 *
//                 * Each controller will be installed in Panel
//                 * constructor and the central UIController
//                 * will be controlling all Panels.
//                 * All Panels listen to all Panels...
//                 */
//
//

                UIController.instance().install(frames_[i].phaseSpacePanel());

                setFramesPosition(frames_[i]);
                frames_[i].pack();
                frames_[i].setVisible(true);



            } catch (wave.multid.DimMismatchEx dex) {
                dex.printStackTrace();
            }

        }


        RPnCurvesListFrame curvesFrame = new RPnCurvesListFrame("Main");
        RPnCurvesListFrame leftFrame = new RPnCurvesListFrame("Left");
        RPnCurvesListFrame rightFrame = new RPnCurvesListFrame("Right");


        RPnDataModule.PHASESPACE.attach(curvesFrame);
        RPnDataModule.LEFTPHASESPACE.attach(leftFrame);
        RPnDataModule.RIGHTPHASESPACE.attach(rightFrame);


        curvesFrame.attach(RPnDataModule.PHASESPACE);
        leftFrame.attach(RPnDataModule.LEFTPHASESPACE);
        rightFrame.attach(RPnDataModule.RIGHTPHASESPACE);


        createPanelsChooser();

        curvesFrame.setVisible(true);
        leftFrame.setVisible(true);
        rightFrame.setVisible(true);
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

            if (selectectedPanels.size() == 0) {
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

    //** nao vi alteracao
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

    //** ???
    void matlabExport_actionPerformed(ActionEvent e) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("script.m"));
            chooser.setFileFilter(new FileNameExtensionFilter("Matlab file", "m"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

                int nFiles = chooser.getSelectedFile().getParentFile().listFiles().length;

                for (int k = 0; k < nFiles; k++) {
                    chooser.getSelectedFile().getParentFile().listFiles()[0].delete();
                }

                FileWriter writer = new FileWriter(chooser.getSelectedFile().
                        getAbsolutePath());
                dir = chooser.getSelectedFile().getParent();
                System.out.println("Diretorio selecionado : " + dir);

                RPnDataModule.matlabExport(writer);

                writer.close();
            }

        } catch (java.io.IOException ioex) {
            ioex.printStackTrace();
        } catch (java.lang.NullPointerException nullEx) {
        }
    }

    //**  ???
    void export_actionPerformed(ActionEvent e) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("output.xml"));
            chooser.setFileFilter(new FileNameExtensionFilter("XML File", "xml", "XML"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                FileWriter writer = new FileWriter(chooser.getSelectedFile().
                        getAbsolutePath());
                writer.write(RPnConfigReader.XML_HEADER);
                writer.write("<rpnconfiguration>\n");
                RPnNumericsModule.export(writer);
                RPnVisualizationModule.export(writer);
                RPnDataModule.export(writer);
                writer.write("</rpnconfiguration>");
                writer.close();
            }

        } catch (java.io.IOException ioex) {
            ioex.printStackTrace();
        } catch (java.lang.NullPointerException nullEx) {
            nullEx.printStackTrace();
        }
    }

    //** nao vi alteracao
    public static void clearStatusMessage() {
        statusLabel_.setForeground(Color.black);
        setStatusMessage("", 0);
    }

    //** nao vi alteracao
    public static void setStatusMessage(String message, int messageType) {
        switch (messageType) {
            case 1://Error message;
                statusLabel_.setForeground(Color.red);

                break;
        }

        statusLabel_.setText(message);

    }

    //** nao vi alteracao
    void networkMenuItem_actionPerformed(ActionEvent e) {

        RPnNetworkStatusController.instance().actionPerformed(new ActionEvent(this,
                0, null));

        commandMenu_.networkCommand();
    }

    //** para o menu de tipos de curvas
    private void jbInit() throws Exception {

        setUIFramePosition();

        stateComboBox.addItem("Phase Diagram");
        stateComboBox.addItem("Wave Curves");
        stateComboBox.addItem("Bifurcation Curves");
        stateComboBox.addActionListener(new StateHandler());
        UIController.instance().setState(new SHOCK_CONFIG());


        GridBagLayout configPanelLayout = new GridBagLayout();

        GridBagConstraints configPanelConstraints = new GridBagConstraints();

        configPanel_.setLayout(configPanelLayout);

        configPanelConstraints.gridy = 0;
        configPanelConstraints.gridx = 0;
        configPanelConstraints.fill = GridBagConstraints.BOTH;
        configPanel_.add(stateComboBox, configPanelConstraints);


        configPanelConstraints.gridy = 1;
        configPanelConstraints.gridx = 0;
        configPanelConstraints.fill = GridBagConstraints.BOTH;
        configPanel_.add(curvesConfigPanel_, configPanelConstraints);


        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(uiFrameLayout_);

        GridBagConstraints layoutConstrains = new GridBagConstraints();
        layoutConstrains.gridy = 0;

        layoutConstrains.gridy = 0;

        layoutConstrains.fill = GridBagConstants.BOTH;


        layoutConstrains.weightx = 0.8;
        layoutConstrains.weighty = 0.1;

        contentPane.add(configPanel_, layoutConstrains);

        layoutConstrains.gridy = 1;
        contentPane.add(toolBarPanel_, layoutConstrains);


        layoutConstrains.gridy = 2;
        getContentPane().add(statusLabel_, layoutConstrains);

        toolBar_.setOpaque(true);

        toolBarPanel_.add(toolBar_);


        setPreferredSize(new Dimension(350, 680));

        setResizable(true);

        setTitle(RPNUMERICS.physicsID());

        fileMenu.setText("File");

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


        showCurvesPaneltem_.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        UIController.instance().showCurvesPanel(showCurvesPaneltem_.isSelected());


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

        matlabMenuFileExport_.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        matlabExport_actionPerformed(e);
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



        //*** Leandro
        editMenuItem1.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ControlClick.clearAllStrings();
                    }
                });

        editMenuItem2.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ControlClick.clearLastString();
                    }
                });

        editMenuItem3.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        VelocityAgent.clearVelocities();
                    }
                });

        editMenuItem4.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ClassifierAgent.clearClassifiers();
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
        //******************************************************

        inputCoordsMenuItem.addActionListener(
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



        shockMenuItem_.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        RPnShockConfigDialog shockConfigDialog = new RPnShockConfigDialog(false, false);
                        shockConfigDialog.setVisible(true);

                    }
                });


        fileMenu.add(exportMenuItem);
        fileMenu.add(matlabMenuFileExport_);
        fileMenu.addSeparator();
        fileMenu.add(networkMenuItem);

        fileMenu.addSeparator();
        fileMenu.add(createSVGImageMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(jMenuFileExit);
        helpMenu.add(jMenuHelpAbout);
        jMenuBar1.add(fileMenu);
        jMenuBar1.add(editMenu);
        jMenuBar1.add(viewMenu_);
        viewMenu_.add(showCursorMenuItem_);
        viewMenu_.add(showCurvesPaneltem_);
        viewMenu_.add(showAuxPanel_);
        jMenuBar1.add(modelInteractionMenu);


        toolBar_.setFloatable(false);

        jMenuBar1.add(helpMenu);
        setJMenuBar(jMenuBar1);




        editMenu.add(ClearPhaseSpaceAgent.instance());
        editMenu.addSeparator();

        //*** Leandro
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
        //******

        editMenu.add(FillPhaseSpaceAgent.instance());




    }

    private void shockConfigMenu() {

        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeSigmaAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(FindProfileAgent.instance());
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(ChangeFluxParamsAgent.instance());
        modelInteractionMenu.add(inputCoordsMenuItem);
        modelInteractionMenu.add(shockMenuItem_);
//        modelInteractionMenu.add(errorControlMenuItem);



    }

    private void rarefactionConfigMenu() {



        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeFluxParamsAgent.instance());
        modelInteractionMenu.add(ChangeOrbitLevel.instance());

        modelInteractionMenu.add(inputCoordsMenuItem);
        modelInteractionMenu.addSeparator();
        modelInteractionMenu.add(configurationMenuItem_);
        modelInteractionMenu.add(BifurcationRefineAgent.instance());
        BifurcationRefineAgent.instance().setEnabled(true);

//        modelInteractionMenu.add(errorControlMenuItem);

    }

    private void bifurcationConfigMenu() {



        modelInteractionMenu.removeAll();
        modelInteractionMenu.add(ChangeFluxParamsAgent.instance());
        modelInteractionMenu.add(configurationMenuItem_);


    }

    //** define a posicao inicial do menu de tipos de curvas
    private void setUIFramePosition() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int height = dim.height;
        int width = dim.width;
        this.setLocation((int) (width - (width * .55)), (int) (height - (height * .9)));
        this.setLocation((int) (width - (width * .55)), 100);
    }

    //** define a posicao inicial dos frames
    private void setFramesPosition(Component component) {

        int newwidth = (int) 100;
        int newheight = (int) 100;
        component.setLocation(newwidth, newheight);
    }

    //** retorna os frames para representar o phaseSpace e desenhar as curvas
    public static RPnPhaseSpaceFrame[] getPhaseSpaceFrames() {
        return frames_;
    }

    //** nao vi alteracao
    public static void disableSliders() {
        for (int i = 0; i < RPnUIFrame.getPhaseSpaceFrames().length; i++) {

            RPnPhaseSpaceFrame frame = RPnUIFrame.getPhaseSpaceFrames()[i];
            frame.getSlider().setEnabled(false);
        }
    }

    //** nao vi alteracao
    public static void enableSliders() {
        for (int i = 0; i < RPnUIFrame.getPhaseSpaceFrames().length; i++) {

            RPnPhaseSpaceFrame frame = RPnUIFrame.getPhaseSpaceFrames()[i];
            frame.getSlider().setEnabled(true);
        }
    }

    //** para mostrar a tabela de curvas
    public void showCurvesPanel(boolean show) {
        curvesFrame_.setVisible(show);

    }

    //** nao vi alteracao
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

            RPnConfigurationDialog extensionCurve = new RPnConfigurationDialog();
            extensionCurve.setVisible(true);

        }
    }

    //** desativa os botoes do menu de tipos de curvas
    private class StateHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            UI_ACTION_SELECTED newState = null;

            if (stateComboBox.getSelectedItem().equals("Phase Diagram")) {

                newState = new SHOCK_CONFIG();
                RPNUMERICS.getShockProfile().setHugoniotMethodName(ShockProfile.HUGONIOT_METHOD_NAMES[1]);
                curvesConfigPanel_.propertyChange(new PropertyChangeEvent(this, "family", "", "phasediagram"));
                curvesConfigPanel_.propertyChange(new PropertyChangeEvent(this, "direction", "", "phasediagram"));
            }

            if (stateComboBox.getSelectedItem().equals("Wave Curves")) {
                newState = new RAREFACTION_CONFIG();
                RPNUMERICS.getShockProfile().setHugoniotMethodName(ShockProfile.HUGONIOT_METHOD_NAMES[0]);
                curvesConfigPanel_.propertyChange(new PropertyChangeEvent(this, "family", "", "wavecurve"));
                curvesConfigPanel_.propertyChange(new PropertyChangeEvent(this, "direction", "", "wavecurve"));

            }
            if (stateComboBox.getSelectedItem().equals("Bifurcation Curves")) {
                newState = new BIFURCATION_CONFIG();
                curvesConfigPanel_.propertyChange(new PropertyChangeEvent(this, "family", "", "bifurcationcurve"));
                curvesConfigPanel_.propertyChange(new PropertyChangeEvent(this, "direction", "", "bifurcationcurve"));
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
