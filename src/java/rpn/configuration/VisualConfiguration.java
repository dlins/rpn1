/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class VisualConfiguration extends Configuration{


    public VisualConfiguration(ConfigurationProfile configurationProfile){
        
        super(configurationProfile);

    }

    VisualConfiguration(ConfigurationProfile profile, HashMap<String, Configuration> innerConfigurations) {
        super(profile, innerConfigurations);
    }
 
    @Override
    public String toXML() {
        
        StringBuilder buffer = new StringBuilder();

            buffer.append("<VIEWCONFIGURATION modeldomain=\"" + getName() + "\">\n");
            
            Set<Entry<String, Configuration>> configurationEntry = getConfigurationMap().entrySet();
            for (Entry<String, Configuration> configEntry : configurationEntry) {

                buffer.append("<PROJDESC name=\"" + configEntry.getValue().getName() + "\">\n");
                HashMap<String, String> configParams = configEntry.getValue().getParams();
                Set<Entry<String, String>> configSet = configParams.entrySet();
                for (Entry<String, String> internalConfigEntry : configSet) {
                    buffer.append("<VIEWPARAM name=\"" + internalConfigEntry.getKey() + "\" " + "value= \"" + internalConfigEntry.getValue() + "\"/>");
                    buffer.append("\n");
                }
                buffer.append("</PROJDESC>\n");
            }
            buffer.append("</VIEWCONFIGURATION>\n");
                

        return buffer.toString();
    }
}
