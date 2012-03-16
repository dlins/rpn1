/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpn;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import rpn.component.BifurcationCurveGeom;

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

    public void remove(RPnPhaseSpaceAbstraction phaseSpace, BifurcationCurveGeom bifurcationGeom) {
        ArrayList<RPnPhaseSpaceAbstraction> pointedPhaseSpaceAbstractions = phaseSpaceMap_.get(phaseSpace);

        for (RPnPhaseSpaceAbstraction pointedPhaseSpace : pointedPhaseSpaceAbstractions) {

          Iterator iteratorFrame = pointedPhaseSpace.curvesFrameIterator();

            while (iteratorFrame.hasNext()) {
                RPnCurvesListFrame frame = (RPnCurvesListFrame)iteratorFrame.next();
                frame.setBifurcationToRemove(bifurcationGeom);
                frame.actionPerformed(new ActionEvent(this, 0, "Remove"));

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
