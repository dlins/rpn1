package rpn.command;

import java.util.List;
import java.util.Observable;
import javax.swing.JToggleButton;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpn.configuration.CurveConfiguration;
import rpn.controller.ui.UIController;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.TransitionalLine;
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

    @Override
    public void update(Observable o,
            Object arg) {

        List<RpGeometry> selectedGeometriesList = UIController.instance().getSelectedGeometriesList();

        if (selectedGeometriesList.size() == 1) {

            for (RpGeometry rpGeometry : selectedGeometriesList) {

                RPnCurve curve = (RPnCurve) rpGeometry.geomFactory().geomSource();

                if (curve instanceof TransitionalLine) {

                    TransitionalLine line = (TransitionalLine) curve;
                    RPNUMERICS.setParamValue("wavecurve", "transitionalline", line.getName());
                    
                    System.out.println("adicionando linha: "+ line.getName());

                }

            }

        }
        
        else {
            
            RPNUMERICS.setParamValue("wavecurve", "transitionalline", "None");

            
        }

    }
    
    
    
    
}
