/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.command.HugoniotPlotCommand;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class ShockConfig extends UI_ACTION_SELECTED {

    //
    // Constructors
    //
    public ShockConfig() {
        super(HugoniotPlotCommand.instance());

    }

   

    //
    // Methods
    //
    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        System.out.println("user input complete de shock config");

        RPNUMERICS.getViscousProfileData().setXZero(new PhasePoint(userInput));

        super.userInputComplete(ui, userInput);

        //ChangeDirectionAgent.instance().setEnabled(true);

        ui.setState(new SigmaConfig());
    }
}
