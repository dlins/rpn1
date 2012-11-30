  /*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.Configuration;

public class CharacteristicsCurveCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private RiemannProfile riemannProfile_;
    private int samplingRate_;

    //
    // Constructors/Initializers
    //
    public CharacteristicsCurveCalc(RiemannProfile riemannProfile, int samplingRate) {

        riemannProfile_ = riemannProfile;
        samplingRate_ = samplingRate;

    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        RpSolution result = nativeCalc(riemannProfile_.getPoints(), samplingRate_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc(OrbitPoint[] riemannProfilePoints, int samplingRate) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Configuration getConfiguration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
