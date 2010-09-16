/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.ode;

import wave.util.VectorField;

public class ODESolverProfile {
    //
    // Members
    //
    private VectorField f_;

    //
    // Constructors
    //
    public ODESolverProfile(ODESolverProfile profile) {
        this(profile.getFunction());
    }

    public ODESolverProfile(VectorField f) {
        f_ = f;
    }

    //
    // Accessors/Mutators
    //
    public VectorField getFunction() { return f_; }

    public void setFunction(VectorField function) { f_ = function; }
}
