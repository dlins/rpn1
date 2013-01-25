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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpn.RPnDialog;
import rpn.RPnLeftPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnRightPhaseSpaceAbstraction;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpnumerics.Area;
import rpnumerics.ContourCurveCalc;
import wave.multid.view.GeomObjView;
import wave.util.RealVector;

public class CurveRefineCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Refine";
    //
    // Members
    //
    private static CurveRefineCommand instance_ = null;
    private RealVector leftResolution_;
    private RealVector rightResolution_;
    private RpGeometry curveToRefine_ = null;
    private RPnPhaseSpacePanel panelToRefine_ = null;

    //
    // Constructors
    //
    protected CurveRefineCommand() {
        super(DESC_TEXT);
        leftResolution_ = new RealVector(2);
        rightResolution_ = new RealVector(2);

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

    public void setLeftResolution(RealVector resolution) {
        leftResolution_ = resolution;
    }

    public void setRightResolution(RealVector resolution) {
        rightResolution_ = resolution;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (curveToRefine_ instanceof BifurcationCurveGeom) {
            System.out.println("Eh curva de bifurcacao");

            CorrespondentResolutionDialog rightResolutionDialog = new CorrespondentResolutionDialog("Resolution");
            rightResolutionDialog.setVisible(true);

        } else {
            ResolutionDialog resolutionDialog = new ResolutionDialog("Resolution");
            resolutionDialog.setVisible(true);

        }

    }

    private void processGeometry(RpGeometry selectedGeometry, RPnPhaseSpacePanel phaseSpacePanel) {

        List<Integer> indexToRemove = new ArrayList<Integer>();
        List<Area> areasToRefine = new ArrayList<Area>();

        Area principalArea = null;
        Area correspondentArea = null;
        Area areaLeft = null;
        Area areaRight = null;


        List<AreaSelected> graphicsArea = phaseSpacePanel.getSelectedAreas();

        RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) phaseSpacePanel.scene().getAbstractGeom();

        System.out.println("Phase space para refinar: " + phaseSpace.getName());

        System.out.println("Right res: " + rightResolution_ + " Left res: " + leftResolution_);
        
        
        
        
            for (AreaSelected polygon : graphicsArea) {

            Iterator geomIterator = phaseSpacePanel.scene().geometries();
            while (geomIterator.hasNext()) {
                GeomObjView geomObjView = (GeomObjView) geomIterator.next();
                if (((RpGeometry) geomObjView.getAbstractGeom()) == selectedGeometry) {
                    List<Integer> segmentIndex = geomObjView.contains((Polygon) polygon.getShape());
                    if (!segmentIndex.isEmpty()) {
                        indexToRemove.addAll(segmentIndex);
                        System.out.println("Nome do painel principal :" + phaseSpacePanel.getName());
                        principalArea = new Area(polygon);
                        System.out.println("Principal area: " + principalArea);


                    }

                }

            }

        }
        
        
        

        // ------ Preenche uma lista de areas correspondentes, caso existam
        List<AreaSelected> correspondentAreas = new ArrayList<AreaSelected>();
        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
        while (phaseSpacePanelIterator.hasNext()) {
            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();

            RPnPhaseSpaceAbstraction auxiliarPhaseSpace = (RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom();


            if (auxiliarPhaseSpace != phaseSpace && panel.getSelectedAreas().size() > 0) {
                correspondentAreas.addAll(panel.getSelectedAreas());
                System.out.println("Nome do painel auxiliar :" + panel.getName());
                correspondentArea = new Area(correspondentAreas.get(0));
                System.out.println("Correspondent area: " + correspondentArea);



                if (phaseSpace instanceof RPnLeftPhaseSpaceAbstraction) {
                    areaLeft = principalArea;
                    areaRight = correspondentArea;


                }
                if (phaseSpace instanceof RPnRightPhaseSpaceAbstraction) {
                    areaLeft = correspondentArea;
                    areaRight = principalArea;
                }

            }






        }
        // -------------------


    






        if (areaRight == null && areaLeft == null) {
            areasToRefine.add(principalArea);
            principalArea.setResolution(leftResolution_);
        } else {


            areaLeft.setResolution(leftResolution_);
            areaRight.setResolution(rightResolution_);

            areasToRefine.add(areaLeft);
            areasToRefine.add(areaRight);

        }



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

        public ResolutionDialog(String title) throws HeadlessException {

            super(false, true);
            setSize(200, 200);
            setTitle(title);

            xresolution_ = new JTextField(String.valueOf((int) leftResolution_.getElement(0)));
            yresolution_ = new JTextField(String.valueOf((int) leftResolution_.getElement(1)));

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
            setLeftResolution(new RealVector(xresolution_.getText() + " " + yresolution_.getText()));
            execute();
            dispose();


        }

        @Override
        protected void begin() {
        }
    }

    private class CorrespondentResolutionDialog extends RPnDialog {

        JTextField leftXResolution_;
        JTextField leftYResolution_;
        JTextField rightXResolution_;
        JTextField rightYResolution_;

        public CorrespondentResolutionDialog(String title) throws HeadlessException {

            super(false, true);
            setSize(600, 200);
            setTitle(title);


            BifurcationCurveGeom bifurcationCurveGeom = (BifurcationCurveGeom) curveToRefine_;

            RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) bifurcationCurveGeom.geomFactory();

            ContourCurveCalc calc = (ContourCurveCalc) factory.rpCalc();


            int resolution[] = calc.getParams().getResolution();

            JLabel leftResolutionLabel = new JLabel();
            JLabel rightResolutionLabel = new JLabel();



            Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();


            while (phaseSpacePanelIterator.hasNext()) {
                RPnPhaseSpacePanel rPnPhaseSpacePanel = phaseSpacePanelIterator.next();
                RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) rPnPhaseSpacePanel.scene().getAbstractGeom();

                List<AreaSelected> graphicsArea = rPnPhaseSpacePanel.getSelectedAreas();


                if (phaseSpace instanceof RPnLeftPhaseSpaceAbstraction) {


                    Area leftArea = new Area(graphicsArea.get(0));

                    int leftCells[] = leftArea.cellsInsideArea(resolution);


                    leftResolutionLabel.setText("Cells inside area:" + leftCells[0] + " x " + leftCells[1]);

                }


                if (phaseSpace instanceof RPnRightPhaseSpaceAbstraction) {

                    Area rightArea = new Area(graphicsArea.get(0));


                    int rightCells[] = rightArea.cellsInsideArea(resolution);


                    rightResolutionLabel.setText("Cells inside area:" + rightCells[0] + " x " + rightCells[1]);



                }





            }

















            leftXResolution_ = new JTextField(String.valueOf((int) leftResolution_.getElement(0)));
            leftYResolution_ = new JTextField(String.valueOf((int) leftResolution_.getElement(1)));



            rightXResolution_ = new JTextField(String.valueOf((int) leftResolution_.getElement(0)));
            rightYResolution_ = new JTextField(String.valueOf((int) leftResolution_.getElement(1)));


            GridBagLayout gridBagLayout = new GridBagLayout();
            JPanel resolutionPanel = new JPanel(gridBagLayout);

            GridBagConstraints constraints = new GridBagConstraints();


            constraints.weightx = 1.0;
            constraints.fill = GridBagConstraints.BOTH;

            constraints.gridx = 0;
            resolutionPanel.add(new JLabel("Left Resolution"), constraints);

            constraints.gridy = 1;
            resolutionPanel.add(leftXResolution_, constraints);

            constraints.gridy = 2;
            resolutionPanel.add(leftYResolution_, constraints);
            constraints.gridy = 3;
            resolutionPanel.add(leftResolutionLabel, constraints);


            constraints.gridx = 1;


            constraints.gridy = 0;

            resolutionPanel.add(new JLabel("Right Resolution"), constraints);

            constraints.gridy = 1;

            resolutionPanel.add(rightXResolution_, constraints);

            constraints.gridy = 2;

            resolutionPanel.add(rightYResolution_, constraints);

            constraints.gridy = 3;
            resolutionPanel.add(rightResolutionLabel, constraints);


            getContentPane().add(resolutionPanel, BorderLayout.CENTER);



            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            int height = dim.height;
            int width = dim.width;

            this.setLocation((int) (width - (width * .45)), (int) (height - (height * .9)));
            this.setLocation((int) (width - (width * .45)), 100);


        }

        @Override
        protected void apply() {
            setLeftResolution(new RealVector(leftXResolution_.getText() + " " + leftYResolution_.getText()));
            setRightResolution(new RealVector(rightXResolution_.getText() + " " + rightYResolution_.getText()));
            execute();
            dispose();
        }

        @Override
        protected void begin() {
        }
    }
}
