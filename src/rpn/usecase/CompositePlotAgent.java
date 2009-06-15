/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.component.*;
import wave.util.RealVector;

public class CompositePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Composite Curve";


    //
    // Members
    //
    static private CompositePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected CompositePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        try {

            throw new Exception("not implemented yet");
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return null;

    }

    static public CompositePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new CompositePlotAgent();
        }
        return instance_;
    }
}
