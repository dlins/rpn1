package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.WaveCurveGeomFactory;
import rpn.controller.ui.UIController;
import rpnumerics.RPnCurve;
import rpnumerics.RpException;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveRRegions;
import rpnumerics.WaveCurveRRegionsCalc;
import wave.util.RealVector;

public class WaveCurveRRegionsPlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Wave Curve R Regions";
    static private WaveCurveRRegionsPlotCommand instance_ = null;

    public WaveCurveRRegionsPlotCommand() {

        super(DESC_TEXT, null, new JButton());
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] input) {

        return null;

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        execute();
    }

    public void execute() {
        RpGeometry geometry = UIController.instance().getSelectedGeometriesList().get(0);

        RPnCurve curve = (RPnCurve) geometry.geomFactory().geomSource();

        WaveCurveRRegionsCalc calc = new WaveCurveRRegionsCalc(curve.getId());
        try {
            WaveCurveRRegions rRegions = (WaveCurveRRegions) calc.calc();
            Iterator<WaveCurve> curvesIterator = rRegions.getCurvesIterator();
            int index = 0;
            while (curvesIterator.hasNext()) {

                WaveCurve waveCurve = (WaveCurve) curvesIterator.next();
                WaveCurveGeomFactory factory = new WaveCurveGeomFactory(calc, waveCurve, index);

                UIController.instance().getActivePhaseSpace().join(factory.geom());

                waveCurve.setId(curveID_);
                curveID_++;

                index++;

            }

        } catch (RpException ex) {
            Logger.getLogger(WaveCurveRRegionsPlotCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static public WaveCurveRRegionsPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new WaveCurveRRegionsPlotCommand();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {

        List<RpGeometry> geometryList = UIController.instance().getSelectedGeometriesList();

        if (geometryList != null) {

            if ((geometryList.size() == 1) && (geometryList.get(0) instanceof WaveCurveGeom)) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }

        }

    }

}
