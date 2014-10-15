/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import java.util.Observable;
import javax.swing.JComponent;
import rpn.configuration.Configuration;

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


        componentCreator = new SpinButtonCreator(configuration_, configurationParameter_);
        return componentCreator.createUIComponent();

//        if (configuration_.getType().equalsIgnoreCase(ConfigurationProfile.METHOD)) {
//
//            componentCreator = new ComboBoxCreator(configuration_, configurationParameter_);
//            return componentCreator.createUIComponent();
//        } else {
//            ;
//        }






    }

    public RPnInputComponent getInputComponent() {
        return inputComponent_;
    }
}
