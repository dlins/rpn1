/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.component.*;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import wave.multid.CoordsArray;
import wave.util.RealVector;

public class BifurcationPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Bifurcation Curve";
    // Members
    //
    static private BifurcationPlotAgent instance_ = null;



    //
    // Constructors/Initializers
    //
    protected BifurcationPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

getContainer().setEnabled(false);

        return null;

    }

    static public BifurcationPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new BifurcationPlotAgent();
        }
        return instance_;
    }
}
