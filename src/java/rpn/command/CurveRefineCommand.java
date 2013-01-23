/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpn.RPnDialog;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.Area;
import rpnumerics.ContourCurveCalc;
import rpnumerics.RpCalculation;
import wave.multid.view.GeomObjView;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import wave.util.Boundary;

public class CurveRefineCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Refine";
    //
    // Members
    //
    private static CurveRefineCommand instance_ = null;
    private RealVector resolution_;
    private RpGeometry curveToRefine_ = null;
    private RPnPhaseSpacePanel panelToRefine_ = null;
    private int[] arrayCellsPrincipal = {0, 0};
    private int[] arrayCellsCorrespondent = {0, 0};

    //
    // Constructors
    //
    protected CurveRefineCommand() {
        super(DESC_TEXT);
        resolution_ = new RealVector(2);
    }

    public void execute() {

        if (curveToRefine_ != null && panelToRefine_ != null) {
            processGeometry(curveToRefine_, panelToRefine_);
            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panelToRefine_.scene().getAbstractGeom();
            phaseSpace.update();
            panelToRefine_.clearAreaSelection();
        }


    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        Double newValue = (Double) log().getOldValue();
        applyChange(new PropertyChangeEvent(this, "level", oldValue, newValue));
    }

    static public CurveRefineCommand instance() {
        if (instance_ == null) {
            instance_ = new CurveRefineCommand();
        }
        return instance_;
    }

    public void setResolution(RealVector resolution) {
        resolution_ = resolution;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        ResolutionDialog resolutionDialog = new ResolutionDialog("Resolution");
        resolutionDialog.setVisible(true);
    }



    // --- Acrescentei este método em 21JAN2013
    private int[] cellsInsideArea(RpGeometry geometry, Area area) {
        int[] arrayCells = {0, 0};

        RpGeomFactory factory = geometry.geomFactory();
        RpCalcBasedGeomFactory geomFactory = (RpCalcBasedGeomFactory) factory;
        RpCalculation calc = geomFactory.rpCalc();
        ContourCurveCalc curveCalc = (ContourCurveCalc) calc;
        int[] calcRes = curveCalc.getParams().getResolution();

        Boundary boundary = RPNUMERICS.boundary();
        double bdryWidth = boundary.getMaximums().getElement(0) - boundary.getMinimums().getElement(0);
        double dx = bdryWidth / (1. * calcRes[0]);
        double areaWidth = area.getTopRight().getElement(0) - area.getDownLeft().getElement(0);
        arrayCells[0] = (int) Math.round(areaWidth / dx);

        double bdryHeight = boundary.getMaximums().getElement(1) - boundary.getMinimums().getElement(1);
        double dy = bdryHeight / (1. * calcRes[1]);
        double areaHeight = area.getTopRight().getElement(1) - area.getDownLeft().getElement(1);
        arrayCells[1] = (int) Math.round(areaHeight / dy);

        return arrayCells;
    }
    // ---


    // --- 21JAN : Nova versão de processGeometry(...) : decide quem é área direita/esquerda e seta no array de áreas sempre a ordem (direita, esquerda)
    // --- No JNIDoubleContact, fica mantido: leftArea index 0; rightArea index 1
    private void processGeometry(RpGeometry selectedGeometry, RPnPhaseSpacePanel phaseSpacePanel) {

        List<Integer> indexToRemove = new ArrayList<Integer>();
        List<Area> areasToRefine = new ArrayList<Area>();

        // ---
        Area principalArea = null;
        Area correspondentArea = null;
        Area areaLeft  = null;
        Area areaRight = null;
        // ---

        List<AreaSelected> graphicsArea = phaseSpacePanel.getSelectedAreas();

        for (AreaSelected polygon : graphicsArea) {
            Iterator geomIterator = phaseSpacePanel.scene().geometries();
            while (geomIterator.hasNext()) {
                GeomObjView geomObjView = (GeomObjView) geomIterator.next();
                if (((RpGeometry) geomObjView.getAbstractGeom()) == selectedGeometry) {
                    List<Integer> segmentIndex = geomObjView.contains((Polygon)polygon.getShape());
                    if (!segmentIndex.isEmpty()) {
                        indexToRemove.addAll(segmentIndex);
                        
                        principalArea = new Area(resolution_, polygon);
                        System.out.println();
                        System.out.println("Resolucao: "+resolution_);
                        System.out.println("Principal area: "+ principalArea);

                        // -----
                        arrayCellsPrincipal = cellsInsideArea(selectedGeometry, principalArea);
                        System.out.println("Contagem de células na área principal:");
                        System.out.println("Quantidade de células na horizontal : " + arrayCellsPrincipal[0]);
                        System.out.println("Quantidade de células na vertical   : " + arrayCellsPrincipal[1]);
                        // -----

                    }

                }

            }

        }
        
        // ------ Preenche uma lista de areas correspondentes, caso existam
        List<AreaSelected> correspondentAreas = new ArrayList<AreaSelected>();
        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
        while (phaseSpacePanelIterator.hasNext()) {
            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();
            if (panel!=phaseSpacePanel  &&  panel.getSelectedAreas().size()>0) {
                correspondentAreas.addAll(panel.getSelectedAreas());
                correspondentArea = new Area(resolution_, correspondentAreas.get(0));
                System.out.println("Correspondent area: "+correspondentArea);

                // ---
                if (panel.getName().equals("LeftPhase Space")) {
                    areaLeft  = correspondentArea;
                    areaRight = principalArea;
                }
                if (panel.getName().equals("RightPhase Space")) {
                    areaRight = correspondentArea;
                    areaLeft  = principalArea;
                }
                // ---

                // -----
                arrayCellsCorrespondent = cellsInsideArea(selectedGeometry, correspondentArea);
                System.out.println("Contagem de células na área correspondente:");
                System.out.println("Quantidade de células na horizontal : " + arrayCellsCorrespondent[0]);
                System.out.println("Quantidade de células na vertical   : " + arrayCellsCorrespondent[1]);
                // -----
            }
        }
        // -------------------

        // !!! ISTO É IMPORTANTE
        areasToRefine.add(areaRight);
        areasToRefine.add(areaLeft);
        
        
        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) selectedGeometry.geomFactory();

        factory.updateGeom(areasToRefine, indexToRemove);

    }



    void setRefineGeometryAndPanel(RpGeometry phasSpaceGeometry, RPnPhaseSpacePanel panel) {
        curveToRefine_ = phasSpaceGeometry;
        panelToRefine_ = panel;

    }

    private class ResolutionDialog extends RPnDialog {

        JTextField xresolution_;
        JTextField yresolution_;
        JButton okButton_;

        public ResolutionDialog(String title) throws HeadlessException {

            super(false, true);
            setSize(200, 200);
            setTitle(title);

            xresolution_ = new JTextField(String.valueOf((int) resolution_.getElement(0)));
            yresolution_ = new JTextField(String.valueOf((int) resolution_.getElement(1)));

            GridBagLayout gridBagLayout = new GridBagLayout();
            JPanel resolutionPanel = new JPanel(gridBagLayout);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;

            constraints.ipadx = 10;
            constraints.ipady = 5;

            resolutionPanel.add(xresolution_, constraints);

            constraints.anchor = GridBagConstraints.EAST;

            constraints.insets = new Insets(5, 5, 5, 5);

            constraints.gridx = 1;

            constraints.gridy = 0;

            resolutionPanel.add(yresolution_, constraints);


            getContentPane().add(resolutionPanel, BorderLayout.CENTER);


            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            int height = dim.height;
            int width = dim.width;
            this.setLocation((int) (width - (width * .55)), (int) (height - (height * .9)));
            this.setLocation((int) (width - (width * .55)), 100);

        }

        @Override
        protected void apply() {
            resolution_ = new RealVector(xresolution_.getText() + " " + yresolution_.getText());
            execute();
            dispose();

        }

        @Override
        protected void begin() {
        }
    }
}
