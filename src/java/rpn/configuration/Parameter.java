/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author edsonlan
 */
public abstract class Parameter {
    
    private String name_;
    private String value_;
   
    private List<String> options_;
    
    List<Parameter> parameterList_;
    
    public Parameter(String name_, String value_) {
        this.name_ = name_;
        this.value_ = value_;
        options_=new ArrayList<String>();
        parameterList_= new ArrayList<Parameter>();
        
    }

    public String getName() {
        return name_;
    }


    public String getValue() {
        return value_;
    }

    public void setValue(String value) {
        this.value_ = value;
    }

    public List<String> getOptions() {
        return options_;
    }
    
    
    public void addOption(String opt){
        options_.add(opt);
    }
    
    
    
    public Iterator<Parameter> getParameterIterator(){
       
        return  parameterList_.iterator();
        
    }
    
     public int getAssociatedParameterSize(){
       
        return  parameterList_.size();
        
    }


    public abstract  void addAssociatedParameter(Parameter parameter);
    public abstract Parameter getAssociatedParameter(int index);
    
}
