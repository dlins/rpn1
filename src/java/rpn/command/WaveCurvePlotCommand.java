package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpn.configuration.CurveConfiguration;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.InflectionCurve;
import rpnumerics.InflectionCurveCalc;
import rpnumerics.OrbitPoint;
import rpnumerics.PhysicalBoundary;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.WaveCurve;
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
        WaveCurveGeomFactory factory = null;

        RPNUMERICS.setParamValue("wavecurve", "origin", String.valueOf(11));

        if (UIController.instance().getSelectedGeometriesList().size() == 1) {

            RpGeomFactory geomFactory = UIController.instance().getSelectedGeometriesList().get(0).geomFactory();
            RPnCurve curve = (RPnCurve) geomFactory.geomSource();
            if (curve instanceof PhysicalBoundary) {

                PhysicalBoundary physicalBoundary = (PhysicalBoundary) curve;

                int edge = physicalBoundary.getEdge(oPoint);
                RPNUMERICS.setParamValue("wavecurve", "origin", String.valueOf(1));
                RPNUMERICS.setParamValue("wavecurve", "edge", String.valueOf(edge));

            } else if (curve instanceof InflectionCurve) {

                RpCalcBasedGeomFactory rpFactory = (RpCalcBasedGeomFactory) geomFactory;
                InflectionCurveCalc rpCalc = (InflectionCurveCalc) rpFactory.rpCalc();

                int family = rpCalc.getFamilyIndex();

                RPNUMERICS.setParamValue("wavecurve", "origin", String.valueOf(12));
                RPNUMERICS.setParamValue("wavecurve", "family", String.valueOf(family));

            } else if (curve instanceof WaveCurve) {
                RPNUMERICS.setParamValue("wavecurve", "origin", String.valueOf(13));
                RPNUMERICS.setParamValue("wavecurve", "curve", String.valueOf(curve.getId()));

            }

        }

        CurveConfiguration waveCurveConfiguration = (CurveConfiguration) RPNUMERICS.getConfiguration("wavecurve");
        factory = new WaveCurveGeomFactory(new WaveCurveCalc(oPoint, waveCurveConfiguration));

        RiemannProfileCommand.instance().getState().add(factory.geom());

        return factory.geom();

    }

    static public WaveCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new WaveCurvePlotCommand();
        }
        return instance_;
    }
}
