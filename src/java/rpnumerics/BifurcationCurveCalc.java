/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public abstract class BifurcationCurveCalc implements RpCalculation {

    private BifurcationParams params_;

    //
    // Constructors
    //
    public BifurcationCurveCalc(BifurcationParams params) {
        params_ = params;

    }

    public RpSolution recalc() throws RpException {

        System.out.println("Chamando recalc da bifurcacao");
        return calc();
    }

      //
    // Accessors/Mutators
    //

    public BifurcationParams getParams() {
        return params_;
    }
}
