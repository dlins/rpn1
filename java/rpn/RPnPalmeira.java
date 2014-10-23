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
public class RPnPalmeira extends RPnFluxParamsSubject {


    //*** Leandro teste
    public RPnPalmeira(String[] paramsValues, String[] paramsNames) {
        super(new String[10], "Palmeira", paramsNames, paramsValues);

    }



    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {
        System.out.println("Entrei no setValues de Palmeira");
        RealVector newState = new RealVector(RPNUMERICS.getFluxParams().getParams().getSize());

        newState.setElement(0, Double.parseDouble(stringArray[0]) + 1);
        newState.setElement(1, 0.);
        newState.setElement(2, 1.);
        newState.setElement(3, 0.);
        newState.setElement(4, -Double.parseDouble(stringArray[2])/2);
        newState.setElement(5, 0.);
        newState.setElement(6, 1.);
        newState.setElement(7, -Double.parseDouble(stringArray[1]));
        newState.setElement(8, Double.parseDouble(stringArray[2])/2);
        newState.setElement(9, 0.);

        setState(RPnFluxParamsSubject.realVectorToStringArray(newState));       //*** Leandro teste

    }



}