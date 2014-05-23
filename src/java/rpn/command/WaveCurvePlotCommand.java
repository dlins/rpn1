package rpn.command;

import javax.swing.JToggleButton;
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

public class WaveCurvePlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Wave Curve";
    static private WaveCurvePlotCommand instance_ = null;

    public WaveCurvePlotCommand() {

        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD, new JToggleButton());
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        WaveCurveGeomFactory factory ;

        if (UIController.instance().getSelectedGeometriesList().size() == 1) {

            RpGeomFactory geomFactory = UIController.instance().getSelectedGeometriesList().get(0).geomFactory();
            RPnCurve curve = (RPnCurve) geomFactory.geomSource();
            if (curve instanceof PhysicalBoundary) {

                PhysicalBoundary physicalBoundary = (PhysicalBoundary) curve;

                int edge = physicalBoundary.edgeSelection(oPoint);


                WaveCurveCalc waveCurveCalc = RPNUMERICS.createBoundaryWaveCurve(oPoint, edge);
                factory = new WaveCurveGeomFactory(waveCurveCalc);



            } else {
                factory = new WaveCurveGeomFactory(RPNUMERICS.createWaveCurveCalc(oPoint));
            }


        } else {
            factory = new WaveCurveGeomFactory(RPNUMERICS.createWaveCurveCalc(oPoint));
        }


        return factory.geom();


    }

    static public WaveCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new WaveCurvePlotCommand();
        }
        return instance_;
    }
}
