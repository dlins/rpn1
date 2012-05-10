/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class RarefactionOrbit extends WaveCurveOrbit implements RpSolution {
    //
    // Constructor
    //
    public RarefactionOrbit(OrbitPoint[] points, int familyIndex,int flag) {
        super(points,familyIndex,flag);

      
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

   
}
