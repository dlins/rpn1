/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

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
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpn.RPnDialog;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.SegmentedCurveGeom;
import rpnumerics.Area;
import wave.multid.view.GeomObjView;
import wave.util.RealVector;

public class CurveRefineAgent extends RpModelConfigChangeAgent  {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Refine";
    //
    // Members
    //
    private static CurveRefineAgent instance_ = null;
    private RealVector resolution_;
    private RpGeometry curveToRefine_ = null;
    private RPnPhaseSpacePanel panelToRefine_ = null;

    //
    // Constructors
    //
    protected CurveRefineAgent() {
        super(DESC_TEXT);
        resolution_ = new RealVector(2);
    }

    public void execute() {

        if (curveToRefine_ != null && panelToRefine_ != null) {
            processGeometry(curveToRefine_, panelToRefine_);
            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panelToRefine_.scene().getAbstractGeom();
            phaseSpace.update();
            panelToRefine_.getCastedUI().getSelectionAreas().clear();
        }


    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        Double newValue = (Double) log().getOldValue();
        applyChange(new PropertyChangeEvent(this, "level", oldValue, newValue));
    }

    static public CurveRefineAgent instance() {
        if (instance_ == null) {
            instance_ = new CurveRefineAgent();
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

    private void processGeometry(RpGeometry selectedGeometry, RPnPhaseSpacePanel phaseSpacePanel) {

        List<Integer> indexToRemove = new ArrayList<Integer>();
        List<Area> areasToRefine = new ArrayList<Area>();

        List<Polygon> selectedAreas = phaseSpacePanel.getCastedUI().getSelectionAreas();

        for (Polygon polygon : selectedAreas) {

            Iterator geomIterator = phaseSpacePanel.scene().geometries();
            while (geomIterator.hasNext()) {
                GeomObjView geomObjView = (GeomObjView) geomIterator.next();
                if (((RpGeometry) geomObjView.getAbstractGeom()) == selectedGeometry) {
                    List<Integer> segmentIndex = geomObjView.contains(polygon);
                    if (!segmentIndex.isEmpty()) {
                        indexToRemove.addAll(segmentIndex);
                        areasToRefine.add(new Area(resolution_, polygon, phaseSpacePanel.scene().getViewingTransform()));

                    }

                }

            }

        }

        System.out.println("areasToRefine.size() ::::::::::::: " +areasToRefine.size());
        System.out.println("indexToRemove.size() ::::::::::::: " +indexToRemove.size());

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
