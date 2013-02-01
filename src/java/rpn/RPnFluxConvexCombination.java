/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;

import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class RPnFluxConvexCombination extends RPnFluxParamsSubject {

    //*** Leandro teste
    public RPnFluxConvexCombination(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getFluxParams().getParams().getSize()], "Combination", paramsNames, paramsValues);
        System.out.println("Tamanho do fluxo: " + RPNUMERICS.getFluxParams().getParams().getSize());
    }

    //*** Leandro teste
    @Override
    public void setValues(String[] stringArray) {
        System.out.println("Entrei no setValues de Combination");
        RealVector newState = new RealVector(RPNUMERICS.getFluxParams().getParams().getSize()); //A*

        RealVector A = new RealVector(newState.getSize());  //initial
        RealVector B = new RealVector(newState.getSize());

//
//        for (int j = 0; j < stringArray.length; j++) {
//            String string = stringArray[j];
//            System.out.println(string);
//
//        }


        Double alpha = new Double(stringArray[stringArray.length - 1]);

        for (int i = 0; i < (stringArray.length - 1) / 2; i++) {
            A.setElement(i, new Double(stringArray[i]));
        }

        for (int i = (stringArray.length - 1) / 2; i < stringArray.length - 1; i++) {
            B.setElement(i - (stringArray.length - 1) / 2, new Double(stringArray[i]));
        }

        
        System.out.println(alpha);
        A.scale(1 - alpha);
        B.scale(alpha);
        A.add(B);

        for (int i = 0; i < newState.getSize(); i++) {
            newState.setElement(i, A.getElement(i));
        }

        setState(RPnFluxParamsSubject.realVectorToStringArray(newState));       //*** Leandro teste

    }
}