/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import wave.multid.model.MultiGeometry;

public class RPnPhaseSpaceManager {

    private HashMap<RPnPhaseSpaceAbstraction, ArrayList<RPnPhaseSpaceAbstraction>> phaseSpaceMap_;
    private static RPnPhaseSpaceManager instance_;

    private RPnPhaseSpaceManager() {
        phaseSpaceMap_ = new HashMap<RPnPhaseSpaceAbstraction, ArrayList<RPnPhaseSpaceAbstraction>>();
    }

    public void register(RPnPhaseSpaceAbstraction phaseSpace, ArrayList<RPnPhaseSpaceAbstraction> pointedPhaseSpaces) {

        phaseSpaceMap_.put(phaseSpace, pointedPhaseSpaces);

    }

    public void unregister(RPnPhaseSpaceAbstraction phaseSpace) {
        phaseSpaceMap_.remove(phaseSpace);
    }

    public void remove(RPnPhaseSpaceAbstraction phaseSpace, MultiGeometry bifurcationGeom) {
        System.out.println("metodo remove de RPnPhaseSpaceManager");
        System.out.println("phaseSpace.getName() : " +phaseSpace.getName());

        phaseSpace.remove(bifurcationGeom);
        if (phaseSpaceMap_.containsKey(phaseSpace)) {
            ArrayList<RPnPhaseSpaceAbstraction> pointedPhaseSpaceAbstractions = phaseSpaceMap_.get(phaseSpace);
            for (RPnPhaseSpaceAbstraction pointedPhaseSpace : pointedPhaseSpaceAbstractions) {

                Iterator iteratorList = pointedPhaseSpace.curvesListIterator();

                while (iteratorList.hasNext()) {
                    System.out.println("iteratorList.hasNext()");
                    RPnCurvesList list = (RPnCurvesList) iteratorList.next();
                    list.removeGeometrySide(bifurcationGeom);

                }
            }
        }


    }

    public static RPnPhaseSpaceManager instance() {

        if (instance_ == null) {
            instance_ = new RPnPhaseSpaceManager();
        }
        return instance_;


    }
}
