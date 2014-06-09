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
    private int correspondingPointIndex_;
    private int correspondingCurveIndex_;
    private double [] eigenValues_;

    //
    // Constructor
    //
    public OrbitPoint(OrbitPoint copy) {
        super(copy.getCoords());
        lambda_ = copy.getSpeed();
    }

    public OrbitPoint(RealVector pCoords, double pTime) {
        super(pCoords);
        lambda_ = pTime;
    }
    
    
    public OrbitPoint(RealVector pCoords, double [] eigenValues,double pTime) {
        super(pCoords);
        lambda_ = pTime;
        eigenValues_=eigenValues;
    }

    public OrbitPoint(RealVector pCoords) {
        this(pCoords, DEFAULT_TIME);
    }
    
    public OrbitPoint(double[] coords, double [] eigenValues,double lambda) {

        super(new RealVector(coords));
        lambda_ = lambda;
        eigenValues_=eigenValues;
    }
    
    public OrbitPoint(double[] coords, double lambda) {

        super(new RealVector(coords));
        lambda_ = lambda;
    }

    public OrbitPoint(double[] coords) {
        super(extractCoordsVector(coords));
        lambda_ = coords[coords.length - 1];
    }

    public double[] getEigenValues() {
        return eigenValues_;
    }

    
    
    
    public OrbitPoint(PhasePoint pPoint) {
        this(pPoint.getCoords());
    }

    public int getCorrespondingCurveIndex() {
        return correspondingCurveIndex_;
    }

    public void setCorrespondingCurveIndex(int correspondingCurveIndex_) {
        this.correspondingCurveIndex_ = correspondingCurveIndex_;
    }

    public int getCorrespondingPointIndex() {
        return correspondingPointIndex_;
    }

    public void setCorrespondingPointIndex(int correspondingPointIndex_) {
        this.correspondingPointIndex_ = correspondingPointIndex_;
    }





    private static RealVector extractCoordsVector(double[] coordsAndSpeed) {

        RealVector coords = new RealVector(coordsAndSpeed.length - 1);

        for (int i = 0; i < coords.getSize(); i++) {
            coords.setElement(i, coordsAndSpeed[i]);

        }

        return coords;


    }

    //
    // Accessors/Mutators
    //
    public double getSpeed() {
        return lambda_;
    }

    public void setLambda(double t) {
        lambda_ = t;
    }

    //
    // Methods
    //
    @Override
   public boolean equals(Object obj){

       if(!(obj instanceof OrbitPoint))
           return false;
       OrbitPoint orbitPoint = (OrbitPoint)obj;
       return (orbitPoint.getCoords().equals(getCoords())) && orbitPoint.lambda_==lambda_;
   }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.lambda_) ^ (Double.doubleToLongBits(this.lambda_) >>> 32));
        return hash;
    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();
        buffer.append("<ORBITPOINT lambda=\"").append(getSpeed()).append('\"' + " coords=\"").append(getCoords().toString()).append("\"/>\n");
        


        return buffer.toString();
    }
}
