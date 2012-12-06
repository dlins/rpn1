/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class VisualConfiguration extends Configuration{


    public VisualConfiguration(ConfigurationProfile configurationProfile){
        
        super(configurationProfile);

    }

    public VisualConfiguration(ConfigurationProfile profile, HashMap<String, Configuration> innerConfigurations) {
        super(profile, innerConfigurations);
        
        System.out.println("Entrando no construtor:"+innerConfigurations.values().size());
        
        Collection<Configuration> testeSet = innerConfigurations.values();
        
        for (Configuration configuration : testeSet) {
            
            System.out.println("No construtor : "+configuration.toString());
            
        }
        
        
        
    }
 
    @Override
    public String toXML() {
        
//        Collection<Configuration> testeSet = getConfigurationMap().values();
//        
//        
//        for (Configuration configuration : testeSet) {
//            
//            System.out.println("No to XML: "+configuration.toString());
//            
//        }
        
        
        StringBuilder buffer = new StringBuilder();

            buffer.append("<VIEWCONFIGURATION modeldomain=\"").append(getName()).append("\">\n");
            
            Set<Entry<String, Configuration>> configurationEntry = getConfigurationMap().entrySet();
            
            
            
            System.out.println("config dentro do view:"+configurationEntry.size());
            for (Entry<String, Configuration> configEntry : configurationEntry) {

                buffer.append("<PROJDESC name=\"").append(configEntry.getValue().getName()).append("\">\n");
                HashMap<String, String> configParams = configEntry.getValue().getParams();
                Set<Entry<String, String>> configSet = configParams.entrySet();
                for (Entry<String, String> internalConfigEntry : configSet) {
                    buffer.append("<VIEWPARAM name=\"").append(internalConfigEntry.getKey()).append("\" " + "value= \"").append(internalConfigEntry.getValue()).append("\"/>");
                    buffer.append("\n");
                }
                buffer.append("</PROJDESC>\n");
            }
            buffer.append("</VIEWCONFIGURATION>\n");
                

        return buffer.toString();
    }
}
