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

    //
    // Constructors/Initializers
    //
    public OrbitCalc(OrbitPoint point,  int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;


    }


    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {
        System.out.println("Entrou no calc() de OrbitCalc");

        return nativeCalc(start_, RPNUMERICS.getShockProfile().getXZero(), RPNUMERICS.getShockProfile().getSigma(), timeDirection_);

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


    private native RpSolution nativeCalc (OrbitPoint initialPoint, PhasePoint referencePoint,double speed, int direction ) throws RpException;

}
