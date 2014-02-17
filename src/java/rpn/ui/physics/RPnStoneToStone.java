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
public class RPnStoneToStone extends RPnFluxParamsSubject {

    //*** Leandro teste
    public RPnStoneToStone(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "Stone", paramsNames, paramsValues);
    }

    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {
        System.out.println("Entrei no setValues de Stone");
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
        newState[9] = "0";
        newState[10] = stringArray[5];
        newState[11] = stringArray[6];
        
        newState[12] = String.valueOf(fluxParams.getElement(12));
        newState[13] = String.valueOf(fluxParams.getElement(13));
        newState[14] = String.valueOf(fluxParams.getElement(14));


        newState[15] = stringArray[7];
        newState[16] = stringArray[8];
        newState[17] = stringArray[9];
        newState[18] = "0";
        newState[19] = "1";

        newState[20] = String.valueOf(fluxParams.getElement(20));
        newState[21] = String.valueOf(fluxParams.getElement(21));
        newState[22] = String.valueOf(fluxParams.getElement(22));

        newState[23] = "1";
        newState[24] = "1";
        newState[25] = "1";
        newState[26] = "1";


        setState(newState);

    }
}
