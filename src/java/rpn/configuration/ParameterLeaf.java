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
public class ParameterLeaf extends Parameter{
    
    private String name_;
    private String value_;
    


    
    public ParameterLeaf(String name_, String value_) {
        super(name_, value_);
        
        parameterList_.add(this);

    }

    public String getName() {
        return name_;
    }


    public String getValue() {
        return value_;
    }

    public void setValue(String value_) {
        this.value_ = value_;
    }

    @Override
    public Parameter getAssociatedParameter(int index) {
        return this;
    }

    @Override
    public void addAssociatedParameter(Parameter parameter) {
       
    }

   
    
    
    
    
}
