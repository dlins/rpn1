/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import java.util.ArrayList;
import java.util.List;

public class  WaveCurve extends WaveCurveOrbit {

    private int[] curveTypes_;

    public WaveCurve(List<OrbitPoint[]> points, int[] curveTypes, int family, int increase) {
        super(concatOrbitPoints(points), family, increase);
        curveTypes_ = curveTypes;
    }


    public int[] getCurveTypes() {
        return curveTypes_;
    }


    private static OrbitPoint [] concatOrbitPoints(List<OrbitPoint[]> pointsList){

        ArrayList<OrbitPoint> tempArrayList = new ArrayList<OrbitPoint>();
        
        for (OrbitPoint [] orbitPoint : pointsList) {
            
            
            for (int i = 0; i < orbitPoint.length; i++) {
                tempArrayList.add(orbitPoint[i]);
                
            }
            
        }


        OrbitPoint[] result = new OrbitPoint[tempArrayList.size()];


        for (int i = 0; i < result.length; i++) {
            result[i] = tempArrayList.get(i);

        }


        return result;
    }



}
