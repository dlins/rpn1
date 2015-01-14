/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.configuration;

public class PhysicsConfigurationParams extends Configuration {

    public PhysicsConfigurationParams(String name) {

        super(name);
        setType(ConfigurationProfile.PHYSICS_CONFIG);

    }

    public PhysicsConfigurationParams(ConfigurationProfile profile) {
        super(profile);
    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        buffer.append("<PHYSICSCONFIG name=\"").append(getName()).append("\">\n");


        for (int i = 0; i < getParameterList().size(); i++) {

            Parameter parameter = getParameterList().get(i);
            buffer.append("<PHYSICSPARAM name=\"").append(parameter.getName()).append("\" " + "position=\"").append(i).append("\"" + " value= \"").append(parameter.getValue()).append("\"/>");
            buffer.append("\n");

        }


        buffer.append("</PHYSICSCONFIG>\n");

        return buffer.toString();
    }
}
