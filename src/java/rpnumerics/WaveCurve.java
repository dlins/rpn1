/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;

public class WaveCurve extends WaveCurveOrbit {

    private int[] curveTypes_;

    private static int[] curvesIndex_;

    public WaveCurve(List<OrbitPoint[]> points, int[] curveTypes, int family, int increase) {
        super(concatOrbitPoints(points), family, increase);
        curveTypes_ = curveTypes;

        System.out.println("Tamanho da wave curve: " + getPoints().length);

    }

    public int[] getCurveTypes() {
        return curveTypes_;
    }

   

    private static OrbitPoint[] concatOrbitPoints(List<OrbitPoint[]> pointsList) {
        curvesIndex_ = new int[pointsList.size()];

        ArrayList<OrbitPoint> tempArrayList = new ArrayList<OrbitPoint>();

        int curvesEnd = 0;


        for (OrbitPoint[] orbitPoint : pointsList) {
            for (int i = 0; i < orbitPoint.length; i++) {
                tempArrayList.add(orbitPoint[i]);
                System.out.println(orbitPoint[i]);
            }

            curvesIndex_[curvesEnd++] = orbitPoint.length;
            System.out.println("Fim da curva: "+orbitPoint.length);


        }

        OrbitPoint[] result = new OrbitPoint[tempArrayList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tempArrayList.get(i);
        }

        return result;
    }

    public static int[] getCurvesIndex() {
        return curvesIndex_;
    }
}
