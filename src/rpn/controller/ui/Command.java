/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import java.util.ArrayList;
import wave.util.RealVector;


public class Command {

    private ArrayList<RealVector> inputArray_;
    private UI_ACTION_SELECTED actionSelected_;

    public Command(UI_ACTION_SELECTED actionSelected_,RealVector input) {
        this.actionSelected_ = actionSelected_;
        inputArray_=new ArrayList<RealVector>();
        inputArray_.add(input);
    }

    public Command( UI_ACTION_SELECTED actionSelected_,ArrayList<RealVector> inputArray_) {
        this.inputArray_ = inputArray_;
        this.actionSelected_ = actionSelected_;
    }


    public Command(UI_ACTION_SELECTED actionSelected_) {
        this.inputArray_ = new ArrayList<RealVector>();
        this.actionSelected_ = actionSelected_;
    }




    public UI_ACTION_SELECTED getActionSelected() {
        return actionSelected_;
    }

    public ArrayList<RealVector> getInputArray() {
        return inputArray_;
    }

    public String toXML(){

          StringBuffer buffer = new StringBuffer();

            buffer.append("<COMMAND name=\"" +actionSelected_.getAction().toString()+ "\">\n");
            for(RealVector input:inputArray_){
                buffer.append(input.toXML());
                buffer.append("\n");
            }

            buffer.append("</COMMAND>\n");

        return buffer.toString();

    }




}
