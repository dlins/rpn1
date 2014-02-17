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
public class RPnBasson extends RPnFluxParamsSubject {

    //*** Leandro teste
    public RPnBasson(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "Basson", paramsNames, paramsValues);

    }

    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {


        RealVector newState = new RealVector(RPNUMERICS.getFluxParams().getParams().getSize());

        double phimax = Double.parseDouble(stringArray[0]);
        double rho1 = 0.0;
        double rho2 = 0.0;
        
        
        double Vinf1 = Double.parseDouble(stringArray[1]);
        double Vinf2 = Double.parseDouble(stringArray[2]);
        
        double n1 = Double.parseDouble(stringArray[3]);
        double n2 = Double.parseDouble(stringArray[4]);
        
        newState.setElement(0, phimax);
        newState.setElement(1, rho1);
        newState.setElement(2, rho2);
        newState.setElement(3, -Vinf1);
        newState.setElement(4, -Vinf2);
        newState.setElement(5, n1);
        newState.setElement(6, n2);
        

        setState(RPnFluxParamsSubject.realVectorToStringArray(newState));   //*** Leandro teste


    }



}