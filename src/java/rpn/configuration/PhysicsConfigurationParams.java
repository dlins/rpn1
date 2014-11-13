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


        for (int i = 0; i < getParamsSize(); i++) {

            buffer.append("<PHYSICSPARAM name=\"").append(getParamName(i)).append("\" " + "position=\"").append(i).append("\"" + " value= \"").append(getParam(i)).append("\"/>");
            buffer.append("\n");

        }


        buffer.append("</PHYSICSCONFIG>\n");

        return buffer.toString();
    }
}
