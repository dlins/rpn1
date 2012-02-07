/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;

import rpnumerics.Configuration;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class RPnSchearerSchaeffer extends RPnFluxParamsSubject {


    //*** Leandro teste
    public RPnSchearerSchaeffer(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "SchearerSchaeffer", paramsNames, paramsValues);
    }



    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {
        System.out.println("Entrei no setValues de Schearer/Schaeffer");
        String[] newState = new String[RPNUMERICS.getFluxParams().getParams().getSize()];

        newState[0] = stringArray[0];
        newState[1] = stringArray[1];
        newState[2] = "1";
        newState[3] = "0";
        newState[4] = stringArray[2];
        newState[5] = stringArray[1];
        newState[6] = "1";
        newState[7] = "0";
        newState[8] = "-" +stringArray[2];
        newState[9] = "0";


        setState(newState);

    }
}
