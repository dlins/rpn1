/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpnumerics.methods.*;

public class BifurcationCurveCalc implements RpCalculation {

    private BifurcationMethod bifurcationMethod_;
    private BifurcationParams params_;

    //
    // Constructors
    //
    public BifurcationCurveCalc() {
//        params_ = new BifurcationParams();
//        bifurcationMethod_ = new BifurcationContourMethod(params_);
//    

    }

    public BifurcationCurveCalc(BifurcationParams params) {
        params_ = params;
        bifurcationMethod_ = new BifurcationContourMethod(params_);

    }

    public BifurcationCurveCalc(BifurcationContourMethod bifurcationMethod) {
        bifurcationMethod_ = bifurcationMethod;
        params_ = bifurcationMethod.getParams();
    }

    //
    // Accessors/Mutators
    //
    public int getFamilyIndex() {
        return params_.getFamilyIndex();
    }

    public RpSolution calc() throws RpException{
        return bifurcationMethod_.curve();
    }

    public RpSolution recalc() throws  RpException{
        return calc();
    }


}
