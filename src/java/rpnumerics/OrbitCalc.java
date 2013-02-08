  /*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import rpn.command.FillPhaseSpaceCommand;

import rpn.configuration.Configuration;

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
    protected Configuration configuration_;
    //
    // Constructors/Initializers
    //

    public OrbitCalc(OrbitPoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
        poincareSection_ = null;

    }

    public void setPoincareSection(RealVector[] poincare) {
        poincareSection_ = poincare;
    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        if (timeDirection_ == Orbit.BOTH_DIR  ||  FillPhaseSpaceCommand.instance().isBothDir()) {
            Orbit orbitFWD = (Orbit) nativeCalc(start_, RPNUMERICS.getViscousProfileData().getXZero(), RPNUMERICS.getViscousProfileData().getSigma(), Orbit.FORWARD_DIR, poincareSection_);
            Orbit orbitBWD = (Orbit) nativeCalc(start_, RPNUMERICS.getViscousProfileData().getXZero(), RPNUMERICS.getViscousProfileData().getSigma(), Orbit.BACKWARD_DIR, poincareSection_);

            Orbit concatOrbit = concat(orbitBWD, orbitFWD);

            return concatOrbit;

        } else {
            return nativeCalc(start_, RPNUMERICS.getViscousProfileData().getXZero(), RPNUMERICS.getViscousProfileData().getSigma(), timeDirection_, poincareSection_);
        }
    }

    private Orbit concat(Orbit backward, Orbit forward) {
        // opposite time directions assumed...
        OrbitPoint[] swap = new OrbitPoint[backward.getPoints().length
                + forward.getPoints().length - 1];


        for (int i = 0, j = backward.getPoints().length - 1; i < swap.length; i++) {
            if (i >= backward.getPoints().length) {
                swap[i] = (OrbitPoint) forward.getPoints()[i - backward.getPoints().length + 1];
            } else {
                swap[i] = backward.getPoints()[j--];

            }
        }

        return new Orbit(swap, Orbit.BOTH_DIR);

    }

    public OrbitPoint getStart() {
        return start_;
    }

    public int getDirection() {
        return timeDirection_;

    }

    public void setStart(RealVector startpoint) {
        start_ = new OrbitPoint(startpoint);
    }

    private native RpSolution nativeCalc(OrbitPoint initialPoint, PhasePoint referencePoint, double speed, int direction, RealVector[] poincareSection) throws RpException;
//       private native RpSolution nativeCalc (OrbitPoint initialPoint, PhasePoint referencePoint,double speed, int direction ) throws RpException;

    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Configuration getConfiguration() {
        return configuration_;

    }
}
