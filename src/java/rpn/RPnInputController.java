/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import rpnumerics.Configuration;
import wave.util.RealVector;

/**
 *
 * @author edsonlan
 */
public class RPnInputController implements PropertyChangeListener {

    private RPnInputComponent inputComponent_;
    private Configuration configuration_;

    public RPnInputController(RPnInputComponent inputComponent, Configuration config) {
        inputComponent_ = inputComponent;
        configuration_ = config;

    }

    public void propertyChange(PropertyChangeEvent evt) {

//
//        System.out.println(evt.getPropertyName());
//        System.out.println(evt.getOldValue());
//        System.out.println(evt.getNewValue());
//

        String[] parameterNames = configuration_.getParamNames();
        RealVector newValue = (RealVector) evt.getNewValue();

        for (int i = 0; i < parameterNames.length; i++) {
            String paramName = configuration_.getParamName(i);
            configuration_.setParamValue(paramName, String.valueOf(newValue.getElement(i)));

        }


    }
}


