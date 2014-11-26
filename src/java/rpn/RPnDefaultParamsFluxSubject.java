/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn;

import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class RPnDefaultParamsFluxSubject extends RPnFluxParamsSubject {


    //*** Leandro teste
    public RPnDefaultParamsFluxSubject (RealVector paramsValues, String[] paramsNames) {
        super(new String[paramsValues.getSize()], "Default", paramsNames, RPnFluxParamsSubject.realVectorToStringArray(paramsValues));

        for (int i = 0; i < paramsValues.getSize(); i++) {
            getState()[i] = String.valueOf(paramsValues.getElement(i));
        }

    }



//    @Override
//    public void setValues(RealVector realVector) {
//
//        setState(RPnFluxParamsSubject.realVectorToStringArray(realVector));     //*** Leandro teste
//
//    }


    @Override
    public void setValues(String[] stringArray) {

        setState(stringArray);     //*** Leandro teste

    }


}
