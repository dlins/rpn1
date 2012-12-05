/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class CurveConfiguration extends Configuration{


    public CurveConfiguration(ConfigurationProfile configurationProfile){
        
        super(configurationProfile);

    }

    CurveConfiguration(ConfigurationProfile profile, HashMap<String, Configuration> innerConfigurations) {
        super(profile, innerConfigurations);
    }
 
    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

            buffer.append("<CURVECONFIGURATION name=\"" + getName() + "\">\n");
            Set<Entry<String,String>> paramsSet = getParams().entrySet();
            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<CURVEPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
                buffer.append("\n");
            }

            buffer.append("</CURVECONFIGURATION>\n");

        return buffer.toString();
    }
}
