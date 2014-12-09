/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.ui;

import java.util.Observable;
import javax.swing.JComponent;
import rpn.configuration.Parameter;

/**
 *
 * @author edsonlan
 */
public abstract class ParameterView extends Observable {

    private Parameter parameter_;

    public ParameterView(Parameter parameter_) {

        this.parameter_ = parameter_;
    }

    public abstract void associatedChanged(Parameter associatedParameter, Object arg);

    public abstract JComponent getComponent();
    
    public abstract void changeView(Object obj);
    
    protected abstract void decorate(JComponent component);

    public Parameter getParameter() {
        return parameter_;
    }

}
