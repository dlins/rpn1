/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

//import rpnumerics.methods.BifurcationContourMethod;

public class BifurcationCurveCalc implements RpCalculation{
    //
    // Constants

    //
    // Members
    //

//    private BifurcationContourMethod bifurcationMethod_;

    //
    // Constructors
    //
//    public BifurcationCurveCalc(BifurcationContourMethod bifurcationMethod) {
//
//        bifurcationMethod_ = bifurcationMethod;
//
//    }

    BifurcationCurveCalc() {
        
//        throw new UnsupportedOperationException("Not yet implemented");
    }



    //
    // Accessors/Mutators
    //


    public RpSolution calc() {

      
        return null;// bifurcationMethod_.curve(new RealVector(2)); //TODO Using dummy value !!

    }

    public RpSolution recalc() {
        return calc();
    }

    public String getCalcMethodName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public WaveFlow getFlow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
