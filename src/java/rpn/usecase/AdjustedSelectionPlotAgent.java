/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.SegmentedCurveGeom;
import rpn.controller.ui.RPnAdjustedSelectionPlotter;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.ContourCurveCalc;
import wave.util.RealVector;

public class AdjustedSelectionPlotAgent extends RpModelPlotAgent implements Observer {

    static public final String DESC_TEXT = "Adjusted Selection";
    static private AdjustedSelectionPlotAgent instance_ = null;

    private AdjustedSelectionPlotAgent() {
        super(DESC_TEXT, null, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {


        UIController.instance().setState(new AREASELECTION_CONFIG());
        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom();

            Iterator phaseSpaceIterator = phaseSpace.getGeomObjIterator();
            while (phaseSpaceIterator.hasNext()) {
                RpGeometry phasSpaceGeometry = (RpGeometry) phaseSpaceIterator.next();

                if (phasSpaceGeometry.viewingAttr().isSelected()) {

                    RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) phasSpaceGeometry.geomFactory();

                    if (factory.rpCalc() instanceof ContourCurveCalc) {
                        ContourCurveCalc calc = (ContourCurveCalc) factory.rpCalc();

                        int x = calc.getParams().getResolution()[0];
                        int y = calc.getParams().getResolution()[1];

                        CurveRefineAgent.instance().setResolution(new RealVector(x + " " + y));
                        CurveRefineAgent.instance().setRefineGeometryAndPanel(phasSpaceGeometry, panel);
                        CurveRefineAgent.instance().setEnabled(true);

                        RPnAdjustedSelectionPlotter adjustedPlotter = new RPnAdjustedSelectionPlotter(x, y);
                        panel.addMouseListener(adjustedPlotter);
                        panel.addMouseMotionListener(adjustedPlotter);
                    }


                }

            }
        }

    }

    public static AdjustedSelectionPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new AdjustedSelectionPlotAgent();
        }
        return instance_;
    }

    @Override
    public void unexecute() {
    }

    @Override
    public void execute() {
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        List<RpGeometry> geometryList = ((List<RpGeometry>) arg);
        if (geometryList.isEmpty() || geometryList.size() != 1) {
            setEnabled(false);
            CurveRefineAgent.instance().setEnabled(false);
        } else {

            if (geometryList.get(0) instanceof SegmentedCurveGeom) {
                setEnabled(true);
            } else {
                setEnabled(false);
                CurveRefineAgent.instance().setEnabled(false);
            }
        }

    }
}
