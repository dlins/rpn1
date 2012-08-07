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
        System.out.println(RPNUMERICS.getShockProfile().getXZero());

        if(timeDirection_== Orbit.BOTH_DIR) {
            Orbit orbitFWD = (Orbit) nativeCalc(start_, RPNUMERICS.getShockProfile().getXZero(), RPNUMERICS.getShockProfile().getSigma(), Orbit.FORWARD_DIR ,poincareSection_);
            Orbit orbitBWD = (Orbit) nativeCalc(start_, RPNUMERICS.getShockProfile().getXZero(), RPNUMERICS.getShockProfile().getSigma(), Orbit.BACKWARD_DIR ,poincareSection_);

            return concat(orbitBWD, orbitFWD);
            
        }

        else
        return nativeCalc(start_, RPNUMERICS.getShockProfile().getXZero(), RPNUMERICS.getShockProfile().getSigma(), timeDirection_,poincareSection_);
    }


    private Orbit concat(Orbit backward, Orbit forward) {
        // opposite time directions assumed...
        OrbitPoint[] swap = new OrbitPoint[backward.getPoints().length
                + forward.getPoints().length-1];

//
//        for (int i = 0, j = backward.getPoints().length - 1; i < swap.length; i++) {
//            if (i >= backward.getPoints().length) {
//                swap[i] = new OrbitPoint((OrbitPoint) forward.getPoints()[i - backward.getPoints().length + 1]);
//            } else {
//                swap[i] = new OrbitPoint(backward.getPoints()[j--]);
//
//            }
//        }


         for (int i = 0, j = backward.getPoints().length-1 ; i < backward.getPoints().length; i++) {
                swap[i] = new OrbitPoint(backward.getPoints()[j--]);
        }


        for (int i =0 ; i < forward.getPoints().length-1; i++) {
             swap[i+backward.getPoints().length]= new OrbitPoint(forward.getPoints()[i]);

        }


        return new Orbit(swap, Orbit.BOTH_DIR);

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
