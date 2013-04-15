/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.ui;

import java.util.Observable;
import javax.swing.JComponent;
import rpn.configuration.Configuration;
import rpn.configuration.ConfigurationProfile;

/**
 *
 * @author edsonlan
 */
public class UIComponentCreator extends Observable {

    Configuration configuration_;
    RPnInputComponent inputComponent_;
    String configurationParameter_;

    public UIComponentCreator(Configuration configuration_, RPnInputComponent inputComponent_) {
        this.configuration_ = configuration_;
        this.inputComponent_ = inputComponent_;
    }

    public UIComponentCreator(Configuration configuration_, String configurationParameter) {
        this.configuration_ = configuration_;

        configurationParameter_ = configurationParameter;

    }

    public JComponent createUIComponent() {

        UIComponentCreator componentCreator = null;

        if (configuration_.getType().equalsIgnoreCase(ConfigurationProfile.METHOD)) {

            componentCreator = new ComboBoxCreator(configuration_, configurationParameter_);
            return componentCreator.createUIComponent();
        } else {
            componentCreator = new SpinButtonCreator(configuration_, configurationParameter_);
            return componentCreator.createUIComponent();
        }






    }

    public RPnInputComponent getInputComponent() {
        return inputComponent_;
    }
}
