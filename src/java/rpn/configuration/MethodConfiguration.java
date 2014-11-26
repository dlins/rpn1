/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class MethodConfiguration extends Configuration{

    
   

    public MethodConfiguration(ConfigurationProfile configurationProfile){
        
        super(configurationProfile);
      

    }

    MethodConfiguration(ConfigurationProfile profile, HashMap<String, Configuration> innerConfigurations) {
        super(profile, innerConfigurations);
    }

   
 
    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

            buffer.append("<METHOD name=\"" + getName() + "\">\n");
              Set<Entry<String,String>> paramsSet = getParams().entrySet();
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<METHODPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }
            buffer.append("</METHOD>\n");
        return buffer.toString();
    }
}
