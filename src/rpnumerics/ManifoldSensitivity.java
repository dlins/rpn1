/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.util.RealMatrix2;
import wave.util.RealVector;

/*

x_Stationary_		stationary point coordinates

Xp_Stationary_	= - F0-1G0 (sensitivity of stationary point)

x_t0_			first point of the orbit

x_t1_			last point of the orbit

Xp_t1_		value of solution of equations (3.10), (3.11) at the last point of the orbit

X_t0_			value of the fundamental matrix Y(t) at the first point of the orbit

X_t1_			value of the fundamental matrix Y(t) at the last point of the orbit

*/

public class ManifoldSensitivity {
    //
    // Members
    //
    private RealVector x_Stationary_;
    private RealVector Xp_Stationary_;
    private RealVector x_t0_;
    private RealMatrix2 X_t0_;
    private RealVector x_t1_;
    private RealMatrix2 X_t1_;
    private RealVector Xp_t1_;

    //
    // Constructor
    //
    public ManifoldSensitivity(RealVector x_Stationary, RealVector Xp_Stationary, RealVector x_t0, RealMatrix2 X_t0,
        RealVector x_t1, RealMatrix2 X_t1, RealVector Xp_t1) {
            x_Stationary_ = new RealVector(x_Stationary);
            Xp_Stationary_ = new RealVector(Xp_Stationary);
            x_t0_ = new RealVector(x_t0);
            X_t0_ = new RealMatrix2(X_t0);
            x_t1_ = new RealVector(x_t1);
            X_t1_ = new RealMatrix2(X_t1);
            Xp_t1_ = new RealVector(Xp_t1);
    }


    //
    // Accessors/Mutators
    //
    public RealVector getx_Stationary() { return x_Stationary_; }

    public RealVector getXp_Stationary() { return Xp_Stationary_; }

    public RealVector getx_t0() { return x_t0_; }

    public RealMatrix2 getX_t0() { return X_t0_; }

    public RealVector getx_t1() { return x_t1_; }

    public RealMatrix2 getX_t1() { return X_t1_; }

    public RealVector getXp_t1() { return Xp_t1_; }
}
