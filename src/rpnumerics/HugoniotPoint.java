/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.RealVector;

public class HugoniotPoint {//extends RealVector {
    //
    // Constants
    //
    //
    // Members
    //
    private RealVector eigenValRLeft_;
    private RealVector eigenValRRight_;
    private HugoniotPointType type_;



    //
    // Constructors
    //

    public HugoniotPoint(PhasePoint xZero, RealVector x, HugoniotPointType type){// double sigma) {

        type_=type;
        
        
        
    }



    //
    // Accessors/Mutators
    //
    public HugoniotPointType type() { return type_; }

//    public double sigma() { return myFlow_.getSigma(); }

    public RealVector eigenValRLeft() { return eigenValRLeft_; }

    public RealVector eigenValRRight() { return eigenValRRight_; }
}
