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
    private double lambda_;

    //
    // Constructor
    //
    public OrbitPoint(OrbitPoint copy) {
        super(copy.getCoords());
        lambda_ = copy.getLambda();
    }

    public OrbitPoint(RealVector pCoords, double pTime) {
        super(pCoords);
        lambda_ = pTime;
    }

    public OrbitPoint(RealVector pCoords) {
        this(pCoords, DEFAULT_TIME);
    }
    
    
    public OrbitPoint(double [] coords){
//        super(new RealVector(coords));

        super(setCoordsAndSpeed(coords));
//        pTime_=0;

        lambda_ = coords[coords.length - 1];
    }

    public OrbitPoint(PhasePoint pPoint) {
        this(pPoint.getCoords());
    }


    private static RealVector setCoordsAndSpeed(double [] coordsAndSpeed){

        RealVector coords = new RealVector(coordsAndSpeed.length - 1);

        for (int i = 0; i < coords.getSize(); i++) {
            coords.setElement(i, coordsAndSpeed[i]);

        }

        return coords;


    }


    //
    // Accessors/Mutators
    //
    public double getLambda() { return lambda_; }

    public void setLambda(double t) {
        lambda_ = t;
    }

    //
    // Methods
    //

    public String toString() {
//        StringBuffer buf = new StringBuffer();
        
        return super.toString() + " " + lambda_;
    }
}
