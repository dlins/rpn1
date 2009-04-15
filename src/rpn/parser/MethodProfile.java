/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import java.util.HashMap;

public class MethodProfile {

    private  String name_;
    private  HashMap<String, String> paramsNames_ = new HashMap<String, String>();

    public MethodProfile(String name) {
        name_ = name;
    }
    
    public  void addParam(String paramName, String defaulValue) {
        paramsNames_.put(paramName,defaulValue);
    }

    public  HashMap<String, String> getParams() {
        return paramsNames_;
    }

    public  String getName() {
        return name_;
    }
    
}
