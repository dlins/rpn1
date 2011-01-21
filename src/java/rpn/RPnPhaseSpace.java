/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.util.List;
import wave.multid.model.MultiGeometry;

public class RPnPhaseSpace {
    //
    // Constants
    //
    //
    // Members
    //

//    private RpGeometry selectedGeom_;
    private List<RPnPhaseSpaceAbstraction> phaseSpaceAbstractions_;
    //
    // Constructors
    //

    public RPnPhaseSpace(List<RPnPhaseSpaceAbstraction> phaseSpaceAbstractions) {

        phaseSpaceAbstractions_ = phaseSpaceAbstractions;

    }

    public void join(int index, MultiGeometry geom) {

        if (geom == null) {
            return;
        }
        phaseSpaceAbstractions_.get(index).join(geom);

    }

    public void remove(int index, MultiGeometry geom) {



        phaseSpaceAbstractions_.get(index).remove(geom);


    }

    public void update() {


        for (RPnPhaseSpaceAbstraction rPnPhaseSpaceAbstraction : phaseSpaceAbstractions_) {

            rPnPhaseSpaceAbstraction.update();

        }

    }

    public void clear() {
        for (RPnPhaseSpaceAbstraction rPnPhaseSpaceAbstraction : phaseSpaceAbstractions_) {
            rPnPhaseSpaceAbstraction.clear();
        }
    }

    public RPnPhaseSpaceAbstraction getPhaseSpaceAbstraction(int index){
        System.out.println("Valor de index: "+ index);
        return phaseSpaceAbstractions_.get(index);
    }



}
