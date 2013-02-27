package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.IntegralOrbitGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.IntegralCurveCalc;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class IntegralCurvePlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Integral Curve";
    static private IntegralCurvePlotCommand instance_ = null;

    public IntegralCurvePlotCommand() {

        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        System.out.println("Entrei no createRpGeometry de Integral");

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);

        IntegralCurveCalc calc = RPNUMERICS.createIntegralCurveCalc(oPoint);
        IntegralOrbitGeomFactory factory = new IntegralOrbitGeomFactory(calc);



        return factory.geom();

     }

    static public IntegralCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new IntegralCurvePlotCommand();
        }
        return instance_;
    }
}
