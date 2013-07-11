/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 */
package rpn;

import rpn.configuration.Configuration;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class RPnViscosityMatrix extends RPnFluxParamsSubject {


    public RPnViscosityMatrix(String[] paramsValues, String[] paramsNames) {
        super(new String[RPNUMERICS.getConfiguration(RPNUMERICS.physicsID()).getConfiguration("fluxfunction").getParamsSize()]
                , "Viscosity Matrix", paramsNames, paramsValues);

    }


    @Override
    public void setValues(String[] stringArray) {
        System.out.println("Entrei no setValues de ViscosityMatrix");
        Configuration physiConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
        RealVector newState = new RealVector(physiConfiguration.getConfiguration("fluxfunction").getParamsSize());
        int fluxSize = RPNUMERICS.getFluxParams().getParams().getSize();
        
                
        for (int i = 0; i < fluxSize; i++) {
            String fluxConfigurationParam = physiConfiguration.getConfiguration("fluxfunction").getParam(i);            
            newState.setElement(i, Double.parseDouble(fluxConfigurationParam));
        }
        
        
        //The viscosity elements are the last ones in flux function configuration parameters
        for (int i = 0; i < stringArray.length; i++) {
            String string = stringArray[i];
            newState.setElement(fluxSize+i, Double.parseDouble(string));
            
        }
        
        setState(RPnFluxParamsSubject.realVectorToStringArray(newState)); 

    }
}