/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.util.RealVector;

public final class OrbitPoint extends PhasePoint {
    //
    // Constants
    //
    static public final double DEFAULT_TIME = 0d;
    //
    // Members
    //
    private double pTime_;

    //
    // Constructor
    //
    public OrbitPoint(OrbitPoint copy) {
        super(copy.getCoords());
        pTime_ = copy.getTime();
    }

    public OrbitPoint(RealVector pCoords, double pTime) {
        super(pCoords);
        pTime_ = pTime;
    }

    public OrbitPoint(RealVector pCoords) {
        this(pCoords, DEFAULT_TIME);
    }
    
    
    public OrbitPoint(double [] coords){
        super (new RealVector(coords));
    }

    public OrbitPoint(PhasePoint pPoint) {
        this(pPoint.getCoords());
    }

    //
    // Accessors/Mutators
    //
    public double getTime() { return pTime_; }

    public void setTime(double t) {
        pTime_ = t;
    }

    //
    // Methods
    //

//    public String toString() {
//        StringBuffer buf = new StringBuffer();
//        buf.append("\n pTime = " + pTime_);
//        return super.toString() + buf.toString();
//    }
}
