/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.controller.ui;

import rpn.usecase.VelocityAgent;

/**
 *
 * @author moreira
 */
public class VELOCITYAGENT_CONFIG extends UI_ACTION_SELECTED {

    public VELOCITYAGENT_CONFIG() {
        super(VelocityAgent.instance());
    }

}
