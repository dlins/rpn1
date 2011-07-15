/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import rpnumerics.Configuration;

/**
 *
 * @author edsonlan
 */
public class RPnInputController implements PropertyChangeListener {


    private RPnInputComponent inputComponent_;
    private Configuration configuration_;


    public RPnInputController(RPnInputComponent inputComponent, Configuration config) {
        inputComponent_ = inputComponent;
        configuration_=config;

    }



    public void propertyChange(PropertyChangeEvent evt) {

        configuration_.setParamValue(evt.getPropertyName(), (String)evt.getNewValue());

        System.out.println(evt.getPropertyName());
        System.out.println(evt.getOldValue());
        System.out.println(evt.getNewValue());


    }



   



}
