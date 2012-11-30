/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.Map.Entry;
import java.util.Set;

public class BoundaryConfiguration extends Configuration{


    public BoundaryConfiguration(ConfigurationProfile configurationProfile){
        
        super(configurationProfile);

    }
 
    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();
        
        Set<Entry<String, String>> paramsSet = getParams().entrySet();

            buffer.append("<COMMAND name=\"").append(getName()).append("\" ");

            for (Entry<String, String> entry : paramsSet) {
                buffer.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
                buffer.append(" ");
            }

            buffer.append("/>");

        return buffer.toString();
    }
}
