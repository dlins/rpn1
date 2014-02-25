/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.SegmentedCurveGeom;
import rpn.controller.ui.RPnAdjustedSelectionPlotter;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.ContourCurveCalc;
import wave.util.RealVector;

public class AdjustedSelectionPlotCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "Adjusted Selection";
    static private AdjustedSelectionPlotCommand instance_ = null;

    private AdjustedSelectionPlotCommand() {
        super(DESC_TEXT, null, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new AREASELECTION_CONFIG(this));
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

                        CurveRefineCommand.instance().setLeftResolution(new RealVector(x + " " + y));
                        CurveRefineCommand.instance().setRefineGeometryAndPanel(phasSpaceGeometry, panel);
                        CurveRefineCommand.instance().setEnabled(true);

                        RPnAdjustedSelectionPlotter adjustedPlotter = new RPnAdjustedSelectionPlotter(x, y);
                        panel.addMouseListener(adjustedPlotter);
                        panel.addMouseMotionListener(adjustedPlotter);
                    }


                }

            }
        }

    }

    public static AdjustedSelectionPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new AdjustedSelectionPlotCommand();
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
            CurveRefineCommand.instance().setEnabled(false);
        } else {

            if (geometryList.get(0) instanceof SegmentedCurveGeom  ||  geometryList.get(0) instanceof BifurcationCurveGeom) {    // -----
                setEnabled(true);
            } else {
                setEnabled(false);
                CurveRefineCommand.instance().setEnabled(false);
            }
        }

    }
}
