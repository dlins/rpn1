/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.ui.physics;

import rpn.RPnFluxParamsSubject;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class RPnCorey extends RPnFluxParamsSubject {


    //*** Leandro teste
    public RPnCorey(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "Corey", paramsNames, paramsValues);

    }


    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {

        RealVector newState = new RealVector(RPNUMERICS.getFluxParams().getParams().getSize());

        double a = Double.parseDouble(stringArray[0]);
        double b = Double.parseDouble(stringArray[1]);

        if (a>0  &&  b>0  &&  (a+b)<1) {
            double c = 1 - a - b;
            double b1 = -a/c;
            double b2 = -2.*b/c;

            newState.setElement(0, 2. * (1. / a - 1) + b1);
            newState.setElement(1, b1);
            newState.setElement(2, -2.*a/b + b1);
            newState.setElement(3, 0.);
            newState.setElement(4, 0.);
            newState.setElement(5, -2.*b/a + b2);
            newState.setElement(6, b2);
            newState.setElement(7, 2.*(1./b - 1) + b2);
            newState.setElement(8, 0.);
            newState.setElement(9, 0.);

            setState(RPnFluxParamsSubject.realVectorToStringArray(newState));   //*** Leandro teste


        }


    }


}