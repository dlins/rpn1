
/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public interface WaveCurveBranch {

    List<WaveCurveBranch> getBranchsList();
    OrbitPoint getReferencePoint();
    void setReferencePoint(OrbitPoint referencePoint);

}
