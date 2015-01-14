/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class WaveState {
    //
    // Members
    //
    private PhasePoint initial_;
    private PhasePoint final_;
    private double speed_;
    private int dim_;

    //
    // Constructor
    //
    public WaveState(WaveState copy) {
        initial_ = new PhasePoint(copy.initialState());
        final_ = new PhasePoint(copy.finalState());
        speed_ = copy.speed();
    }

    /**
     * 
     * @deprecated Only to test !!
     */
    public WaveState(PhasePoint initialPoint) {
        initial_ = new PhasePoint(initialPoint);
        final_ = new PhasePoint(initialPoint);
        speed_ = 0;
        dim_ = initialPoint.getSize();
    }

    public WaveState(PhasePoint initial, PhasePoint finit, double speed) {
        initial_ = new PhasePoint(initial);
        final_ = new PhasePoint(finit);
        speed_ = speed;
        dim_ = initial.getSize();
    }
    //
    // Accessors/Mutators
    //
    public double speed() {
        return speed_;
    }

    public PhasePoint initialState() {
        return initial_;
    }

    public PhasePoint finalState() {
        return final_;
    }

    public int stateSpaceDim() {
        return dim_;
    }

    //
    // Methods
    //
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n initial state = " + initialState());
        buf.append("\n final state = " + finalState());
        buf.append("\n speed = " + speed());
        return buf.toString();
    }
}
