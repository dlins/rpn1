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
public class RangedParameter extends Parameter{
    
    
    private String [] range_;
    private String increment_;
    
    

    public RangedParameter(String name, String value, String [] range, String increment ) {
        super(name, value);
        
        range_=range;
        increment_=increment;
    }

    public String[] getRange() {
        return range_;
    }

    public String getIncrement() {
        return increment_;
    }
    
    

    @Override
    public void addAssociatedParameter(Parameter parameter) { //TODO

    }

    @Override
    public Parameter getAssociatedParameter(int index) {//TODO 
        return this;

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
