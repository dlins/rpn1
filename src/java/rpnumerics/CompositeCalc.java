/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.ode.ODESolver;

public class CompositeCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private PhasePoint start_;
    private int increase_;
    private int familyIndex_;

    //
    // Constructors/Initializers
    //
  

    public CompositeCalc(PhasePoint point, int familyIndex, int increase) {
        start_ = point;
        increase_= increase;
        familyIndex_ = familyIndex;

    }
    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    public RpSolution calc() throws RpException {

        RpSolution result = null;


        result = (CompositeCurve) nativeCalc(start_, increase_, familyIndex_);
        if (result == null) {
            throw new RpException("Error in native layer");
        }



        return result;
    }

    private native RpSolution nativeCalc(PhasePoint start, int increase,int familyIndex) throws RpException;
}
