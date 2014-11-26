/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class PhysicsConfiguration extends Configuration {

    public PhysicsConfiguration(ConfigurationProfile configurationProfile, HashMap<String, Configuration> configurationHash) {

        super(configurationProfile, configurationHash);


    }
    
    public PhysicsConfiguration(String name) {

        super(name);
        setType(ConfigurationProfile.PHYSICS_CONFIG);

    }

    public PhysicsConfiguration(ConfigurationProfile profile) {
        super(profile);
    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

            buffer.append("<PHYSICS name=\"" + getName() + "\">\n");

            Set<Entry<String, Configuration>> configurationSet = getConfigurationMap().entrySet();

            for (Entry<String, Configuration> entry : configurationSet) {//Printing boundary first

                if (entry.getValue().getType().equals(ConfigurationProfile.BOUNDARY)) {
                    buffer.append(entry.getValue().toXML());
                }

            }

            for (Entry<String, Configuration> entry : configurationSet) {
                  if (!entry.getValue().getType().equals(ConfigurationProfile.BOUNDARY)) {
                    buffer.append(entry.getValue().toXML());
                }

            }
            buffer.append("</PHYSICS>\n");

        return buffer.toString();
    }
}
