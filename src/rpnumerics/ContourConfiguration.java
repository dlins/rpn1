/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics;

import java.util.HashMap;

public class ContourConfiguration {

    private HashMap<String, String> params_ = new HashMap<String, String>();

    public void addParam(String paramName, String value) {
        params_.put(paramName, value);
    }

    public HashMap<String, String> getParams() {
        return params_;
    }

    public void setParamValue (String paramName,String paramValue){

        params_.put(paramName, paramValue);
    }

    public String getParamValue(String paramName){
        
        return params_.get(paramName);
    }

}
