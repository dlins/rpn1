/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.ode;

import wave.util.RealVector;

public class ODESolution {
    //
    // Constants
    //
    public static int STOP_OUT_OF_BOUNDARY = 1;
    public static int STOP_ON_POINCARE_SECTION = 2;
    public static int MADE_MAXIMUM_STEP_NUMBER = 0;
    //
    // Members
    //
    private RealVector[] coords_;
    private double[] times_;
    private int flag_;

    //
    // Constructors
    //
    public ODESolution(RealVector[] coords, double[] times, int flag) {
        coords_ = new RealVector[coords.length];
        System.arraycopy(coords, 0, coords_, 0, coords.length);
        times_ = (double[]) times.clone();
        flag_ = flag;
    }

    //
    // Accessors
    //
    public double[] getTimes() { return times_; }

    public RealVector[] getCoordinates() { return coords_; }

    public int getFlag() { return flag_; }
}
