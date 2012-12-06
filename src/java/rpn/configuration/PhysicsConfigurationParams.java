/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.Map.Entry;
import java.util.Set;

public class PhysicsConfigurationParams extends Configuration {

    public PhysicsConfigurationParams(String name) {

        super(name);

    }

    public PhysicsConfigurationParams(ConfigurationProfile profile) {
        super(profile);
    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        buffer.append("<PHYSICSCONFIG name=\"" + getName() + "\">\n");

        Set<Entry<String, String>> configurationSet = getParams().entrySet();

        for (Entry<String, String> paramEntry : configurationSet) {

            buffer.append("<PHYSICSPARAM name=\"" + paramEntry.getKey() + "\" " + "position=\"" + getParamOrder(paramEntry.getKey()) + "\"" + " value= \"" + paramEntry.getValue() + "\"/>");
            buffer.append("\n");

        }
        buffer.append("</PHYSICSCONFIG>\n");

        return buffer.toString();
    }
}
