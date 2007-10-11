/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.*;
import wave.util.RealVector;
import wave.util.Boundary;
import wave.util.GridProfile;

public class HugoniotPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Hugoniot Curve";

    //     static public final String ICON_PATH = rpn.RPnConfigReader.IMAGEPATH + "hugoniot.jpg";


    //
    // Members
    //
    static private HugoniotPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //

    protected HugoniotPlotAgent() {
	super(DESC_TEXT,rpn.RPnConfigReader.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        
        Boundary bound = RPNUMERICS.boundary();
        GridProfile xProfile = new GridProfile(bound.getMinimums().getElement(0), bound.getMaximums().getElement(0), 100);
        GridProfile yProfile = new GridProfile(bound.getMinimums().getElement(1), bound.getMaximums().getElement(1), 100);

        // input is the current Shock Flow U minus
        RPNUMERICS.hugoniotCurveCalc().uMinusChangeNotify(new PhasePoint(input[0]));
        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(RPNUMERICS.hugoniotCurveCalc());
        return factory.geom();

    }
    static public HugoniotPlotAgent instance() {
        if (instance_ == null)
            instance_ = new HugoniotPlotAgent();
        return instance_;
    }
}
