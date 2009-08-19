/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;


import wave.util.RealVector;


import rpn.usecase.ChangeDirectionAgent;


public class BIFURCATION_CONFIG extends UI_ACTION_SELECTED {

    public BIFURCATION_CONFIG() {

        super(ChangeDirectionAgent.instance());

    }

    public void userInputComplete(rpn.controller.ui.UIController ui,
                                  RealVector userInput) {

        ui.setState(new GEOM_SELECTION());

    }
}
