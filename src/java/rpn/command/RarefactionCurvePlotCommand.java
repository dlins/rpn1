package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RarefactionCurveGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpn.controller.ui.UIController;
import rpnumerics.OrbitPoint;
import rpnumerics.PhysicalBoundary;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.WaveCurveCalc;

public class RarefactionCurvePlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Rarefaction Curve";
    static private RarefactionCurvePlotCommand instance_ = null;

    public RarefactionCurvePlotCommand() {

        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        
         RarefactionCurveGeomFactory factory=null;
         if (UIController.instance().getSelectedGeometriesList().size() == 1) {

            RpGeomFactory geomFactory = UIController.instance().getSelectedGeometriesList().get(0).geomFactory();
            RPnCurve curve = (RPnCurve) geomFactory.geomSource();
            if (curve instanceof PhysicalBoundary) {

                PhysicalBoundary physicalBoundary = (PhysicalBoundary) curve;
                
                int edge = physicalBoundary.getEdge(oPoint);
                
                factory = new RarefactionCurveGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint, edge));

            }
            else {
                  factory = new RarefactionCurveGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint));
            }

        }
         
         else {
                
         factory = new RarefactionCurveGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint));
             
         }

        return factory.geom();
     }

    static public RarefactionCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new RarefactionCurvePlotCommand();
        }
        return instance_;
    }
}
