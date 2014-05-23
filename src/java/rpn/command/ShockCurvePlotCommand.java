/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpnumerics.OrbitPoint;
import rpnumerics.PhysicalBoundary;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.ShockCurveCalc;

public class ShockCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Shock Curve";
    //
    // Members
    //
    static private ShockCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ShockCurvePlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        ShockCurveGeomFactory factory = null;
        if (UIController.instance().getSelectedGeometriesList().size() == 1) {

            RpGeomFactory geomFactory = UIController.instance().getSelectedGeometriesList().get(0).geomFactory();
            RPnCurve curve = (RPnCurve) geomFactory.geomSource();
            if (curve instanceof PhysicalBoundary) {

                PhysicalBoundary physicalBoundary = (PhysicalBoundary) curve;

                int edge = physicalBoundary.edgeSelection(oPoint);

                factory = new ShockCurveGeomFactory(RPNUMERICS.createShockCurveCalc(oPoint, edge));

            }
            else {
                 factory = new ShockCurveGeomFactory(RPNUMERICS.createShockCurveCalc(oPoint));
            }

        } else {

            factory = new ShockCurveGeomFactory(RPNUMERICS.createShockCurveCalc(oPoint));

        }

        return factory.geom();
    }

    static public ShockCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new ShockCurvePlotCommand();
        }
        return instance_;
    }
}
