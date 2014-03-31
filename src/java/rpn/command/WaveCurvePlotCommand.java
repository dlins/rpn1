package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpn.controller.ui.UIController;
import rpnumerics.InflectionCurve;
import rpnumerics.InflectionCurveCalc;
import rpnumerics.OrbitPoint;
import rpnumerics.PhysicalBoundary;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.RpCalculation;
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
                WaveCurveCalc waveCurveCalc = RPNUMERICS.createBoundaryWaveCurveCalc(oPoint,WaveCurveCalc.BOUNDARY, edge);
                factory = new WaveCurveGeomFactory(waveCurveCalc);

            }
            
            else if (curve instanceof InflectionCurve){
                
                RpCalcBasedGeomFactory rpFactory = (RpCalcBasedGeomFactory)geomFactory;
                InflectionCurveCalc rpCalc = (InflectionCurveCalc) rpFactory.rpCalc();
                
                int family = rpCalc.getFamilyIndex();
                
                factory = new WaveCurveGeomFactory(RPNUMERICS.createInflectionWaveCurveCalc(oPoint,family));
                

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
