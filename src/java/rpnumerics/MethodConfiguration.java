/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class MethodConfiguration extends Configuration {

    public MethodConfiguration(HashMap<String, String> paramsAndValues) {
        super("method","name",paramsAndValues);
    }

    @Override
    public String toXML() {

        StringBuffer buffer = new StringBuffer();
        Set<Entry<String, String>> paramsSet = getParams().entrySet();

        for (Entry entry : paramsSet) {

            buffer.append("<METHODPARAM name=\"" + entry.getKey() + "\" " + "value= \"" + entry.getValue() + "\"/>");
            buffer.append("\n");


        }

        buffer.append("<METHOD/>"+"\n");


        return buffer.toString();

    }
}
