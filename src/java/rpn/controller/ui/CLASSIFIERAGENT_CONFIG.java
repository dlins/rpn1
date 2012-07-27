/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.controller.ui;

//import rpn.component.util.ClassifierAgent;
import rpn.usecase.ClassifierAgent;

/**
 *
 * @author moreira
 */
public class CLASSIFIERAGENT_CONFIG extends UI_ACTION_SELECTED {

    public CLASSIFIERAGENT_CONFIG() {

        super(ClassifierAgent.instance());

    }

}
