/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.ode;

import rpnumerics.WavePoint;

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
//    private RealVector[] realCoords_;
    private WavePoint[] coords_;

    private double[] times_;
    private int flag_;

    //
    // Constructors

//    public ODESolution(RealVector[] coords, double[] times, int flag) {
//        realCoords_ = new RealVector[coords.length];
//        System.arraycopy(coords, 0, coords_, 0, coords.length);
//        times_ = (double[]) times.clone();
//        flag_ = flag;
//    }

    public ODESolution(WavePoint[] coords, double[] times, int flag) {
        coords_ = new WavePoint[coords.length];
        System.arraycopy(coords, 0, coords_, 0, coords.length);
        times_ = (double[]) times.clone();
        flag_ = flag;
    }


    //
    // Accessors
    //
    public double[] getTimes() {
        return times_;
    }

//    public RealVector[] getWavePoints() { return realCoords_; }

    public WavePoint[] getWavePoints() {
        return coords_;
    }

    public int getFlag() {
        return flag_;
    }
}
