/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.ode;

import wave.util.SimplexPoincareSection;
import wave.util.Boundary;
import wave.util.VectorField;
import wave.util.RealVector;

public class Rk4BPProfile extends ODESolverProfile {
    //
    // Members
    //
    private double epsilon_; // accuracy
    private double dYmax_; // maximum step in the state space
    private SimplexPoincareSection poincareSection_; // ps
    private boolean checkPoincareSection_; // check ps
    private Boundary boundary_; // b
    private boolean checkBoundary_; // check b
    private RealVector yScales_; // scales for state
    private int timeDirection_;
    private int MaxStepN_; // maximum number of steps

    //
    // Constructor
    //
    // no poincare
    public Rk4BPProfile(VectorField f, double epsilon, double dYmax, Boundary boundary, RealVector yScales, int MaxStepN) {
        this(f, epsilon, dYmax, yScales, MaxStepN);
        epsilon_ = epsilon;
        dYmax_ = dYmax;
        yScales_ = yScales;
        MaxStepN_ = MaxStepN;
        checkBoundary_ = true;
        boundary_ = boundary;
    }

    // no poincare and no boundary
    public Rk4BPProfile(VectorField f, double epsilon, double dYmax, RealVector yScales, int MaxStepN) {
        super(f);
        epsilon_ = epsilon;
        dYmax_ = dYmax;
        yScales_ = yScales;
        MaxStepN_ = MaxStepN;
        checkPoincareSection_ = false;
        checkBoundary_ = false;
    }

    // poincare and boundary
    public Rk4BPProfile(VectorField f, double epsilon, double dYmax, SimplexPoincareSection poincareSection, Boundary boundary,
        RealVector yScales, int MaxStepN) {
            this(f, epsilon, dYmax, yScales, MaxStepN);
            checkPoincareSection_ = true;
            checkBoundary_ = true;
            poincareSection_ = poincareSection;
            boundary_ = boundary;
    }

    //
    // Accessors/Mutators
    //
    public void setPoincareSection(SimplexPoincareSection pSection) {
        poincareSection_ = pSection;
        checkPoincareSection_ = true;
    }

    public void setPoincareSectionFlag(boolean flag) { checkPoincareSection_ = flag; }

    public double getEpsilon() { return epsilon_; }

    public double getDYmax() { return dYmax_; }

    public SimplexPoincareSection getPoincareSection() { return poincareSection_; }

    public boolean hasPoincareSection() { return checkPoincareSection_; }

    public Boundary getBoundary() { return boundary_; }

    public boolean hasBoundary() { return checkBoundary_; }

    public RealVector getYScales() { return yScales_; }

    public int getMaximumStepNumber() { return MaxStepN_; }
}
