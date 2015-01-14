/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

import java.util.Map.Entry;
import java.util.Set;
import rpn.controller.ui.UIController;

public class CommandConfiguration extends Configuration {

    public CommandConfiguration(String name) {

        super(name);

    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();
        if (!getConfigurationMap().isEmpty()) {
            String phaseSpaceNameString = "phasespace=\"" + UIController.instance().getActivePhaseSpace().getName() + "\"";
            buffer.append("<COMMAND name=\"").append(getName()).append("\" ").append(phaseSpaceNameString).append("/>\n");

            Set<Entry<String, Configuration>> configSet = getConfigurationMap().entrySet();
            for (Entry<String, Configuration> entry : configSet) {
                buffer.append(entry.getValue().toXML());
            }

        } else {


            Set<Entry<String, String>> paramsSet = getParams().entrySet();

            for (Entry<String, String> entry : paramsSet) {
                buffer.append("<COMMANDPARAM name=\"").append(entry.getKey()).append("\"").append(" value=\"").
                        append(entry.getValue()).append("\"");
                buffer.append("/>\n");
            }



        }



        return buffer.toString();
    }
}
