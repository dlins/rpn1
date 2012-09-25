/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.util.GeometryGraphND;
import rpn.controller.ui.BIFURCATIONREFINE_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import wave.util.*;

public class BifurcationRefineAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Refine Bifurcation Curve";
    // Members
    //
    static private BifurcationRefineAgent instance_ = null;
    
    //
    // Constructors/Initializers
    //
    protected BifurcationRefineAgent() {
        super(DESC_TEXT);
        
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("actionPerformed do BifurcationRefineAgent");
        UIController.instance().setState(new BIFURCATIONREFINE_CONFIG());    //*** chamada original

//        PropertyChangeEvent propertyEvent = new PropertyChangeEvent(this, "refine", "", AreaSelectionAgent.instance().getListArea().get(0));

//        applyChange(propertyEvent);

//        AreaSelectionAgent.instance().getListArea().clear();

        // Pode ser útil na hora de fazer inclusao dos novos segmentos (para nao serem eliminados)
        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();

        for (int i = 0; i < newValue.getSize(); i++) {
            GeometryGraphND.cornerRet.setElement(i, 0);
            GeometryGraphND.targetPoint.setElement(i, 0.);
        }
        //----------------------------------------------------------------------------------------

        int[] temp = (int[]) RPnPhaseSpaceAbstraction.listResolution.get(RPnPhaseSpaceAbstraction.closestCurve);
        RPnPhaseSpaceAbstraction.listResolution.remove(RPnPhaseSpaceAbstraction.closestCurve);
        RPnPhaseSpaceAbstraction.listResolution.add(temp);

    }


    static public BifurcationRefineAgent instance() {
        if (instance_ == null) {
            instance_ = new BifurcationRefineAgent();
        }
        return instance_;
    }



    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
