/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class CompositeCurve extends Orbit implements RpSolution {
    private  int familyIndex_;
    //
    // Members
    //

    public CompositeCurve(OrbitPoint [] rarefaction,int increase,int familyIndex) {
        super(rarefaction,increase);

        familyIndex_=familyIndex;

    }


     public int getFamilyIndex() {
        return familyIndex_;
    }


}
