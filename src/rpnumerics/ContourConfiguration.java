/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics;

import java.util.HashMap;
import rpn.parser.ConfigurationProfile;

public class ContourConfiguration extends Configuration{

    public ContourConfiguration(HashMap<String, String> paramsAndValues) {
        super("Contour","method",paramsAndValues);
    }

    public ContourConfiguration (ConfigurationProfile profile){
        super(profile);
    }

}
