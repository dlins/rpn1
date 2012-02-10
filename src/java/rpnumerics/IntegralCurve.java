/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class IntegralCurve extends Orbit implements RpSolution {

    //
    // Constructor
    //
    private RealVector[] inflectionPoints_;
   

    public IntegralCurve(OrbitPoint[] points, int familyIndex, RealVector[] inflectionPoints) {
        super(points, familyIndex, 0);
        inflectionPoints_ = inflectionPoints;

    }

    public IntegralCurve(OrbitPoint[] points, int familyIndex) {
        super(points, familyIndex, 0);
    }

    // Methods
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n points = ");
        for (int i = 0; i < getPoints().length; i++) {
            buf.append("[" + i + "] = " + getPoints()[i] + "  ");
            buf.append("\n");
        }
        return buf.toString();
    }

    public RealVector[] getInflectionPoints() {
        return inflectionPoints_;
    }

  
}
