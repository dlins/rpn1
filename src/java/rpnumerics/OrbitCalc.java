/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class OrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private OrbitPoint start_;
    private int timeDirection_;
    private RealVector[] poincareSection_;

    //
    // Constructors/Initializers
    //
    public OrbitCalc(OrbitPoint point,  int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
        poincareSection_=null;

    }


    public void setPoincareSection(RealVector [] poincare){
        poincareSection_=poincare;
    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {
//        return nativeCalc(start_, RPNUMERICS.getShockProfile().getXZero(), RPNUMERICS.getShockProfile().getSigma(), timeDirection_,poincareSection_);

        return nativeCalc(start_, RPNUMERICS.getShockProfile().getXZero(), RPNUMERICS.getShockProfile().getSigma(), timeDirection_,poincareSection_);
    }

   

    public OrbitPoint getStart() {
        return start_;
    }


    public int getDirection() {
        return timeDirection_;

    }

    public void setStart(RealVector startpoint){
        start_= new OrbitPoint(startpoint);
    }

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    private native RpSolution nativeCalc (OrbitPoint initialPoint, PhasePoint referencePoint,double speed, int direction ,RealVector[] poincareSection) throws RpException;
//       private native RpSolution nativeCalc (OrbitPoint initialPoint, PhasePoint referencePoint,double speed, int direction ) throws RpException;

}
