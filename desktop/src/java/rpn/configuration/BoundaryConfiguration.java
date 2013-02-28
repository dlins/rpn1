/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class BoundaryConfiguration extends Configuration{


    public BoundaryConfiguration(ConfigurationProfile configurationProfile){
        
        super(configurationProfile);

    }

    BoundaryConfiguration(ConfigurationProfile profile, HashMap<String, Configuration> innerConfigurations) {
        super(profile, innerConfigurations);
    }
 
    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();
        
        
        

            buffer.append("<BOUNDARY name=\"" + getName() + "\">\n");
            
            
            Set<Entry<String,String>> paramsSet = getParams().entrySet();
            
            for (Entry<String, String> entry : paramsSet) {

                buffer.append("<BOUNDARYPARAM name=\"" + entry.getKey() + "\" " + " value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }

            buffer.append("</BOUNDARY>\n");



              return buffer.toString();
    }
}
