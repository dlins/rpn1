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
public class RPnMLB extends RPnFluxParamsSubject {

    //*** Leandro teste
    public RPnMLB(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "MLB", paramsNames, paramsValues);

    }

    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {


        RealVector newState = new RealVector(RPNUMERICS.getFluxParams().getParams().getSize());

        double phimax = Double.parseDouble(stringArray[0]);
        double rho1 = Double.parseDouble(stringArray[1]);
        double rho2 = Double.parseDouble(stringArray[2]);
        
        
        double d1 = Double.parseDouble(stringArray[3]);
        double d2 = Double.parseDouble(stringArray[4]);
        
        double n1 = Double.parseDouble(stringArray[5]);
        double n2 = Double.parseDouble(stringArray[6]);
        
        newState.setElement(0, phimax);
        newState.setElement(1, rho1);
        newState.setElement(2, rho2);
        newState.setElement(3, d1*d1);
        newState.setElement(4, d2*d2);
        newState.setElement(5, n1);
        newState.setElement(6, n2);
        

        setState(RPnFluxParamsSubject.realVectorToStringArray(newState));   //*** Leandro teste


    }



}