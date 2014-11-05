/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.configuration;

/**
 *
 * @author edsonlan
 */
public class ParameterComposite extends Parameter {

    public ParameterComposite(String name_, String value_) {
        super(name_, value_);
    }

    @Override
    public Parameter getAssociatedParameter(int index) {
        return parameterList_.get(index);
    }

    @Override
    public void addAssociatedParameter(Parameter parameter) {
        parameterList_.add(parameter);
    }

}
