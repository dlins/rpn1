/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.ui;

import javax.swing.JComponent;
import rpn.configuration.Parameter;

/**
 *
 * @author edsonlan
 */
public abstract class ParameterView {
    
private JComponent component_;
private Parameter parameter_;

    public ParameterView(JComponent component_, Parameter parameter_) {
        this.component_ = component_;
        this.parameter_ = parameter_;
    }


public abstract void associatedChanged (Parameter parameter);

    public JComponent getComponent() {
        return component_;
    }

    public Parameter getParameter() {
        return parameter_;
    }





    
}
