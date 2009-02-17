/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.component.*;
import wave.util.RealVector;

public class CompositeCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Composite Curve";

    //     static public final String ICON_PATH = rpn.RPnConfigReader.IMAGEPATH + "hugoniot.jpg";
    //
    // Members
    //
    static private CompositeCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected CompositeCurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfigReader.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        try {

            throw new Exception("not implemented yet");
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return null;

    }

    static public CompositeCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new CompositeCurvePlotAgent();
        }
        return instance_;
    }
}
