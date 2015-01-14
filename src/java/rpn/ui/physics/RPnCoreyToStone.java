/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.ui.physics;

import rpn.RPnFluxParamsSubject;
import rpn.ui.RPnInputComponent;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class RPnCoreyToStone extends RPnFluxParamsSubject {

    //*** Leandro teste
    public RPnCoreyToStone(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "Corey", paramsNames, paramsValues);

    }

    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {      // no novo subject (radios) recebe o vetor de booleanos "como strings"
        String[] newState = new String[RPNUMERICS.getFluxParams().getParams().getSize()];
        
        
        RealVector fluxParams = RPNUMERICS.getFluxParams().getParams();

        newState[0] = "1.5";
        newState[1] = "1";
        newState[2] = "1";
        newState[6] = "0";

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

        } else if (RPnInputComponent.rb == 2) {
            newState[0] = "";
            newState[1] = "";
            newState[2] = "";
            newState[6] = "";
        }

        newState[3] = stringArray[0];
        newState[4] = stringArray[1];
        newState[5] = stringArray[2];
        newState[7] = stringArray[3];
        newState[8] = stringArray[4];
        newState[9] = stringArray[5];

        newState[10] = "0";
        newState[11] = "0";

        newState[12] = "0";
        newState[13] = "0";
        newState[14] = "0";

        newState[15] = stringArray[6];
        newState[16] = stringArray[7];
        newState[17] = stringArray[8];
        newState[18] = "0";
        newState[19] = "0";


//        newState[20] = String.valueOf(fluxParams.getElement(20));
//        newState[21] = String.valueOf(fluxParams.getElement(21));
//        newState[22] = String.valueOf(fluxParams.getElement(22));
//        newState[23] = "0";
//        newState[24] = "1";
//        newState[25] = "1";
//        newState[26] = "1";

        setState(newState);

    }
}
