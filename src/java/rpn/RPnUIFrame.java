
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
import javax.swing.filechooser.FileNameExtensionFilter;
import rpn.component.util.AreaSelectionAgent2;
import rpn.component.util.ClassifierAgent;
import rpn.component.util.ControlClick;
import rpn.component.util.VelocityAgent;
import rpn.controller.ui.*;
import rpn.message.*;
import rpnumerics.ShockProfile;
import wave.util.IsoTriang2DBoundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class RPnUIFrame extends JFrame implements PropertyChangeListener {

    //
    // Members
    //
    private JPanel contentPane;
    private JPanel configPanel_ = new JPanel(new GridLayout(2, 1));
    private JPanel toolBarPanel_ = new JPanel();
    private JComboBox stateComboBox = new JComboBox();
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu editMenu = new JMenu();
    private JMenu fileMenu = new JMenu();
    private JMenu modelInteractionMenu = new JMenu();
    private JMenu helpMenu = new JMenu();
    private JCheckBox resultsOption = new JCheckBox("Save With Results");
    private JMenuItem shockMenuItem_ = new JMenuItem("Shock Configuration ...");
//    private JMenuItem bifurcationMenuItem_ = new JMenuItem("Configuration ...");
    private JMenuItem configurationMenuItem_ = new JMenuItem(new ConfigAction());//"Configuration ...");
    private JMenuItem jMenuFileExit = new JMenuItem();
    private JMenuItem matlabMenuFileExport_ = new JMenuItem("Export to Matlab ...");
    private JMenuItem jMenuHelpAbout = new JMenuItem();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JMenuItem exportMenuItem = new JMenuItem();
    private JMenuItem layoutMenuItem = new JMenuItem();
    private JMenuItem inputCoordsMenuItem = new JMenuItem("Input Coords ...");
//    private JMenuItem errorControlMenuItem = new JMenuItem();
    private JMenuItem createSVGImageMenuItem = new JMenuItem();
    private JMenuItem printMenuItem = new JMenuItem();
//    private JMenuItem pluginMenuItem = new JMenuItem();
    private static RPnPhaseSpaceFrame[] frames_, auxFrames_, leftFrames_, rightFrames_;
    private RPnMenuCommand commandMenu_ = null;
    private JMenuItem networkMenuItem = new JMenuItem();
    private JCheckBoxMenuItem showCursorMenuItem_ = new JCheckBoxMenuItem("Show Cursor Lines");
    private JToolBar toolBar_ = new JToolBar();
    private static JLabel statusLabel_ = new JLabel();
    private JMenu viewMenu_ = new JMenu("View");
    private JCheckBoxMenuItem showCurvesPaneltem_ = new JCheckBoxMenuItem("Show Curves Window", true);
    private RPnCurvesConfigPanel curvesConfigPanel_ = new RPnCurvesConfigPanel();
    private JFrame curvesFrame_;


    //*** declarei isso  -- Leandro
    private JMenuItem editMenuItem1 = new JMenuItem("Clears All Strings");
    private JMenuItem editMenuItem2 = new JMenuItem("Clears Last String");
    private JMenuItem editMenuItem3 = new JMenuItem("Clears Velocities");
    private JMenuItem editMenuItem4 = new JMenuItem("Clears Classifiers");
    private JMenuItem editMenuItem5 = new JMenuItem("Starts with Black Background");
    private JMenuItem editMenuItem6 = new JMenuItem("Starts with White Background");
    public static String dir= "";
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


            getContentPane().add(statusLabel_, BorderLayout.SOUTH);


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


    //** parece que nao esta sendo usado
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
//                toolBar_.add(ScratchAgent.instance().getContainer());
//                ScratchAgent.instance().setEnabled(true);
                toolBar_.revalidate();

            }

            if (evt.getNewValue() instanceof RAREFACTION_CONFIG) {

                rarefactionConfigMenu();
                toolBar_.removeAll();

                toolBar_.add(HugoniotPlotAgent.instance().getContainer());
                toolBar_.add(ShockCurvePlotAgent.instance().getContainer());
                toolBar_.add(RarefactionOrbitPlotAgent.instance().getContainer());
                toolBar_.add(IntegralCurvePlotAgent.instance().getContainer());
                toolBar_.add(CompositePlotAgent.instance().getContainer());


                if (RPNUMERICS.boundary() instanceof RectBoundary) {
                    toolBar_.add(AreaSelectionAgent.instance().getContainer());     //** Edson/Leandro
                }
                else {
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

//                ScratchAgent.instance().setEnabled(true);
                toolBar_.revalidate();

            }

            if (evt.getNewValue() instanceof BIFURCATION_CONFIG) {

                bifurcationConfigMenu();

                toolBar_.removeAll();

//                toolBar_.add(CoincidencePlotAgent.instance().getContainer());
//                toolBar_.add(SubInflectionPlotAgent.instance().getContainer());
//                toolBar_.add(BuckleyLeverettiInflectionAgent.instance().getContainer());
                toolBar_.add(DoubleContactAgent.instance().getContainer());
                toolBar_.add(BoundaryExtensionCurveAgent.instance().getContainer());
//                toolBar_.add(SubInflectionExtensionCurveAgent.instance().getContainer());
//                toolBar_.add(CoincidenceExtensionCurvePlotAgent.instance().getContainer());
                toolBar_.add(InflectionPlotAgent.instance().getContainer());
                toolBar_.add(HysteresisPlotAgent.instance().getContainer());
//                ScratchAgent.instance().setEnabled(true);
//                toolBar_.validate();
                toolBar_.revalidate();

            }

//            pack();
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
        RPnDataModule.PHASESPACE.update();
        UIController.instance().panelsUpdate();
    }

//    void errorControlMenuItem_actionPerformed(ActionEvent e) {
//        RPnErrorControlDialog dialog = new RPnErrorControlDialog();
//        Dimension dlgSize = dialog.getPreferredSize();
//        Dimension frmSize = new Dimension(1280, 1024);
//        Point loc = new Point(0, 0);
//        dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
//                (frmSize.height - dlgSize.height) / 2 + loc.y);
//        dialog.setModal(true);
//        dialog.pack();
//        dialog.setVisible(true);
//        rpnumerics.RPNUMERICS.errorControl().reset(dialog.getEps(),
//                rpnumerics.RPNUMERICS.boundary());
//    }


    //** para criar os frames (paineis) - incluindo os auxiliares
    protected void phaseSpaceFramesInit(Boundary boundary) {
        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        int numOfPanels = RPnVisualizationModule.DESCRIPTORS.size();

        //RPnVisualizationModule.DESCRIPTORS

        auxFrames_ = new RPnPhaseSpaceFrame[numOfPanels * 2];
        frames_ = new RPnPhaseSpaceFrame[numOfPanels];

//        leftFrames_ = new RPnPhaseSpaceFrame[numOfPanels];
//        rightFrames_ = new RPnPhaseSpaceFrame[numOfPanels];

        //Init Aux Frames

        Boundary auxBoundary = null;

        if (RPNUMERICS.boundary() instanceof RectBoundary) {
            RealVector originalMax = RPNUMERICS.boundary().getMaximums();
            RealVector originalMin = RPNUMERICS.boundary().getMinimums();

            RealVector newMax = new RealVector(2 * RPNUMERICS.boundary().getMaximums().getSize());
            RealVector newMin = new RealVector(2 * RPNUMERICS.boundary().getMinimums().getSize());

            newMin.setElement(0, originalMin.getElement(0));
            newMin.setElement(1, originalMin.getElement(1));
            newMin.setElement(2, originalMin.getElement(0));
            newMin.setElement(3, originalMin.getElement(1));

            newMax.setElement(0, originalMax.getElement(0));
            newMax.setElement(1, originalMax.getElement(1));
            newMax.setElement(2, originalMax.getElement(0));
            newMax.setElement(3, originalMax.getElement(1));

            auxBoundary = new RectBoundary(newMin, newMax);

        } else {

            RealVector A = new RealVector("0 0");
            RealVector B = new RealVector("0 1");
            RealVector C = new RealVector("1 0");

            auxBoundary = new IsoTriang2DBoundary(A, B, C);
        }

//*** CRIA OS PAINEIS AUXILIARES MAS NAO DESENHA NADA NELES
//        int auxNumOfPanels = RPnVisualizationModule.AUXDESCRIPTORS.size();
//        System.out.println("Quantidade de projecoes auxiliares: " + RPnVisualizationModule.AUXDESCRIPTORS.size());
//        wave.multid.graphs.ClippedShape auxClipping = new wave.multid.graphs.ClippedShape(auxBoundary);
//
//        for (int i = 0; i < auxNumOfPanels; i++) {
//            wave.multid.view.ViewingTransform auxViewingTransf =
//                    ((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(
//                    i)).createTransform(auxClipping);
//            try {
//                wave.multid.view.Scene auxScene = RPnDataModule.AUXPHASESPACE.createScene(auxViewingTransf,
//                        new wave.multid.view.ViewingAttr(Color.black));
//                System.out.println("Dimensao do auxiliar: " + RPnDataModule.AUXPHASESPACE.getSpace().getDim());   // Esta dando 6, o q significa?   (Leandro)
//                auxFrames_[i] = new RPnPhaseSpaceFrame(auxScene, commandMenu_);
//                auxFrames_[i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.AUXDESCRIPTORS.get(i)).label());
//
//                UIController.instance().install(auxFrames_[i].phaseSpacePanel());   // Se comentado, parece nao fazer diferenca
//
//                setFramesPosition(auxFrames_[i]);
//                auxFrames_[i].pack();
//                auxFrames_[i].setVisible(true);
//
//            } catch (wave.multid.DimMismatchEx dex) {
//                dex.printStackTrace();
//            }
//
//        }
//***


        // Init Main Frame
        for (int i = 0; i < numOfPanels; i++) {
            wave.multid.view.ViewingTransform viewingTransf =
                    ((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(
                    i)).createTransform(clipping);
            try {
                wave.multid.view.Scene scene = RPnDataModule.PHASESPACE.createScene(viewingTransf,
                        new wave.multid.view.ViewingAttr(Color.black));

////
//                wave.multid.view.Scene leftScene = RPnDataModule.LEFTPHASESPACE.createScene(viewingTransf,
//                        new wave.multid.view.ViewingAttr(Color.black));
//                wave.multid.view.Scene rightScene = RPnDataModule.RIGHTPHASESPACE.createScene(viewingTransf,
//                        new wave.multid.view.ViewingAttr(Color.black));

                frames_[i] = new RPnPhaseSpaceFrame(scene, commandMenu_);
                frames_[i].setTitle(((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(i)).label());

//

//                leftFrames_[i] = new RPnPhaseSpaceFrame(leftScene, commandMenu_);
//                leftFrames_[i].setTitle("Left " + ((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(i)).label());
////
//
//                rightFrames_[i] = new RPnPhaseSpaceFrame(rightScene, commandMenu_);
//                rightFrames_[i].setTitle("Right " + ((RPnProjDescriptor) RPnVisualizationModule.DESCRIPTORS.get(i)).label());
////
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
////
//                UIController.instance().install(leftFrames_[i].phaseSpacePanel());
//
//                setFramesPosition(leftFrames_[i]);
//                leftFrames_[i].pack();
//                leftFrames_[i].setVisible(true);
//
//
//                UIController.instance().install(rightFrames_[i].phaseSpacePanel());
//
//                setFramesPosition(rightFrames_[i]);
//                rightFrames_[i].pack();
//                rightFrames_[i].setVisible(true);


                UIController.instance().install(frames_[i].phaseSpacePanel());

                setFramesPosition(frames_[i]);
                frames_[i].pack();
                frames_[i].setVisible(true);



            } catch (wave.multid.DimMismatchEx dex) {
                dex.printStackTrace();
            }

        }

    }



//     from here on just for 2D for now...
    void createSVGImage_actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("SVG File", "svg", "SVG"));
        chooser.setSelectedFile(new File("image.svg"));
        int status = chooser.showSaveDialog(this);

        if (status == JFileChooser.CANCEL_OPTION || status == JFileChooser.ERROR_OPTION) {
            return;
        }
        try {
            UIController.instance().getFocusPanel().createSVG(chooser.getSelectedFile());
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
                
                for (int k=0; k<nFiles; k++) {
                    chooser.getSelectedFile().getParentFile().listFiles()[0].delete();
                }

                FileWriter writer = new FileWriter(chooser.getSelectedFile().
                        getAbsolutePath());
                dir = chooser.getSelectedFile().getParent();
                System.out.println("Diretorio selecionado : " +dir);

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


    //** parece que nao esta sendo usado
    private RPnProjDescriptor[] generateAuxProjDescriptors() {


        int dim = RPNUMERICS.domainDim();

        int index = 0;

        RPnProjDescriptor projDescriptors[] = new RPnProjDescriptor[RPNUMERICS.domainDim() * 2];

        int[] projIndex = new int[projDescriptors.length];

        for (int i = 0; i < projDescriptors.length; i++) {
            projIndex[i] = i;
        }


        while (index < dim) {

            int[] projI = new int[2];

            projI[0] = projIndex[index];
            projI[1] = projIndex[index + 1];

            projDescriptors[index] = new RPnProjDescriptor(RPnDataModule.AUXPHASESPACE.getSpace(), "Aux " + "Axis " + projI[0] + " Axis " + projI[1], 200, 200, projI, false);
            index++;


        }


        return projDescriptors;

    }


    //** para o menu de tipos de curvas
    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(ShockFlowControlFrame.class.getResource("[Your Icon]")));
        setUIFramePosition();

        stateComboBox.addItem("Phase Diagram");
        stateComboBox.addItem("Wave Curves");
        stateComboBox.addItem("Bifurcation Curves");
        stateComboBox.addActionListener(new StateHandler());
        UIController.instance().setState(new SHOCK_CONFIG());
//        configurationMenuItem_.setText(configurationMenuItem_.getText());

        configPanel_.add(stateComboBox);

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        toolBar_.setOpaque(true);
        toolBarPanel_.add(toolBar_);
//        this.setSize(new Dimension(200, 200));


        if (RPNUMERICS.boundary() instanceof RectBoundary) {
            this.setPreferredSize(new Dimension(400, 500));
        }
        else {
            this.setPreferredSize(new Dimension(400, 550));
        }
        
        this.setResizable(false);
        this.setTitle("");
        fileMenu.setText("File");
//        pluginMenuItem.setText("Plugins ...");
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
//        errorControlMenuItem.setText("Error Control...");
        layoutMenuItem.setText("Scene Layout...");
        layoutMenuItem.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        layoutMenuItem_actionPerformed(e);
                    }
                });


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
//        fileMenu.add(pluginMenuItem);
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
        jMenuBar1.add(modelInteractionMenu);


        toolBar_.setFloatable(false);

        jMenuBar1.add(helpMenu);
        setJMenuBar(jMenuBar1);
//        contentPane.add(toolBar_, BorderLayout.CENTER);

        contentPane.add(toolBarPanel_, BorderLayout.CENTER);

//        configPanel_.add(stateComboBox);
        configPanel_.add(curvesConfigPanel_);
        contentPane.add(configPanel_, BorderLayout.NORTH);



//        editMenu.add(UndoActionController.instance());
//        editMenu.addSeparator();
        editMenu.add(layoutMenuItem);
        editMenu.addSeparator();


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
                return "Configuration ...";
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
            System.out.println("Entrou em actionPerformed");
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
}
