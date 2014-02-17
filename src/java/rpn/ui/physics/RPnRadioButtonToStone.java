/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.ui.physics;

import rpn.RPnFluxParamsSubject;
import rpn.ui.RPnInputComponent;
import rpnumerics.RPNUMERICS;

/**
 *
 * @author moreira
 */
public class RPnRadioButtonToStone extends RPnFluxParamsSubject {


    //*** Leandro teste
    public RPnRadioButtonToStone(String[] paramsValues, String[] paramsNames) {

        // trocar RPNUMERICS.getFluxParams().getParams().getSize()   para  paramsValues.size()  -  neste caso, sao os 3 botoes

        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "Radio", paramsNames, paramsValues);   // depois, trocar o nome de Radio para .... (Pablo???)


    }

    @Override
    public void setValues(String[] stringArray) {     // stringArray deverá vir como booleano

        System.out.println("Entrei no setValues de RadioButton");
        String[] newState = new String[RPNUMERICS.getFluxParams().getParams().getSize()];


        if (RPnInputComponent.rb == 0) {
            newState[0] = "0";
            newState[1] = "0";
            newState[2] = "0";
            newState[6] = "1";

        } else if (RPnInputComponent.rb == 1) {
            newState[0] = "";
            newState[1] = "";
            newState[2] = "";
            newState[6] = "0";

        } else {
            newState[0] = "";
            newState[1] = "";
            newState[2] = "";
            newState[6] = "";
        }


        newState[3] = "";
        newState[4] = "";
        newState[5] = "";

        for (int i = 7; i < newState.length; i++) {
            newState[i] = "";
        }

        
        setState(newState);

        
    }

}
