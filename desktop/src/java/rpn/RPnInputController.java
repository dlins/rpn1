/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import rpn.ui.RPnInputComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import rpn.configuration.Configuration;


public class RPnInputController implements PropertyChangeListener {

    private RPnInputComponent inputComponent_;
    private Configuration configuration_;

    public RPnInputController(RPnInputComponent inputComponent, Configuration config) {
        inputComponent_ = inputComponent;
        configuration_ = config;


    }

    public void propertyChange(PropertyChangeEvent evt) {
        String[] parameterNames = configuration_.getParamNames();
        String [] newValues = (String []) evt.getNewValue();
        
        for (int i = 0; i < parameterNames.length; i++) {
            String paramName = configuration_.getParamName(i);
            configuration_.setParamValue(paramName, newValues[i]);

        }

        }

    public Configuration getConfiguration(){return configuration_;}
    }









