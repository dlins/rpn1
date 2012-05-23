/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class ShockCurve extends Orbit implements RpSolution {

    private int familyIndex_;

    public ShockCurve(OrbitPoint[] points, int family, int increase) {
        super(points, increase);
        familyIndex_ = family;
    }

    public int getFamilyIndex() {
        return familyIndex_;
    }
}
