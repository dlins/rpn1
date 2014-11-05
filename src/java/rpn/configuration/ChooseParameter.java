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
public class ChooseParameter extends Parameter{
    

    public ChooseParameter(String name, String value) {
        super(name, value);
        System.out.println("Nome: "+name + "valor: " + value);
        
    }
    

    @Override
    public void addAssociatedParameter(Parameter parameter) { //TODO

    }

    @Override
    public Parameter getAssociatedParameter(int index) {//TODO 
        return this;

    }
    
    
    
    
}
