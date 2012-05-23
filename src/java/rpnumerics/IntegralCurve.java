/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealVector;

public class IntegralCurve extends WaveCurveOrbit implements RpSolution {

    //
    // Constructor
    //
    private List<RealVector>inflectionPoints_;
   

    public IntegralCurve(OrbitPoint[] points, int familyIndex, List<RealVector>inflectionPoints) {
        super(points, familyIndex, 0);
        inflectionPoints_ = inflectionPoints;
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

    public List<RealVector> getInflectionPoints() {
        return inflectionPoints_;
    }

  
}
