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


public abstract class RPnFluxParamsSubject extends RPnSubject{

    private String[] paramsVector_;   //*** Leandro teste
    private String[] paramNames_;
    

    public RPnFluxParamsSubject(String[] paramsVector, String name, String[] paramNames, String[] paramValues) {   //*** Leandro teste
        super(name, paramValues);
        paramsVector_ = paramsVector;
        paramNames_ = paramNames;

    }


    public void setState(String[] paramsVector) {   //*** Leandro teste

        for (int i = 0 ; i < paramsVector_.length; i++) {
            paramsVector_[i] = paramsVector[i];
        }

        notifyObserver(this);
    }



    @Override
    public String[] getState() {   //*** Leandro teste
        return paramsVector_;
    }

    

    @Override
    public String[] getParamsNames() {
        return paramNames_;
        
    }



    //*** para conversao de String[] em RealVector
    public static RealVector stringArrayToRealVector(String[] str) {

        RealVector realVector = new RealVector(str.length);
        int i = 0;

        for (String string : str) {
            realVector.setElement(i, Double.parseDouble(string));
            i++;
        }
        
        return realVector;
    }


    //*** para conversao de RealVector em String[]
    public static String[] realVectorToStringArray(RealVector rv) {

        String[] stringArray = new String[rv.getSize()];

        for (int i = 0; i < rv.getSize(); i++) {
            stringArray[i] = String.valueOf(rv.getElement(i));
        }

        return stringArray;
    }



}
