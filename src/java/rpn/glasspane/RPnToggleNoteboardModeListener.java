/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.glasspane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import rpn.RPnUIFrame;
import rpn.message.*;


/**
 *
 * @author mvera
 */
public class RPnToggleNoteboardModeListener implements ActionListener {


    public RPnToggleNoteboardModeListener() {

    }

    public void actionPerformed(ActionEvent e) {


        JFrame[] frames = RPnUIFrame.getPhaseSpaceFrames();
        JFrame[] aux_frames = RPnUIFrame.getAuxFrames();
        JFrame[] riemann_frames = RPnUIFrame.getRiemannFrames();

        for (int i = 0; i < frames.length; i++) {
            frames[i].getGlassPane().setVisible(!frames[i].getGlassPane().isVisible());
        }

        for (int i = 0; i < aux_frames.length; i++) {
            aux_frames[i].getGlassPane().setVisible(!aux_frames[i].getGlassPane().isVisible());
        }

        if (riemann_frames != null)
        for (int i = 0; i < riemann_frames.length; i++) {
            riemann_frames[i].getGlassPane().setVisible(!riemann_frames[i].getGlassPane().isVisible());
        }

        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {

            StringBuilder buffer = new StringBuilder();

            buffer.append("<COMMAND name=\"TOGGLE_NOTEBOARD_MODE\" >");
            buffer.append("<COMMANDPARAM name=\"pane_index\" value=\"" + RPnNetworkStatus.instance().NOTEBOARD_PANE_INDEX + "\" />");
            buffer.append("<COMMANDPARAM name=\"pane_frame_char\" value=\"" + RPnNetworkStatus.instance().NOTEBOARD_PANE_FRAME_CHAR + "\" />");
            buffer.append("</COMMAND>");


            RPnNetworkStatus.instance().sendCommand(buffer.toString());
        }

         if (RPnHttpPoller.POLLING_MODE == RPnHttpPoller.TEXT_POLLER)
            RPnHttpPoller.POLLING_MODE = RPnHttpPoller.OBJ_POLLER;

        else RPnHttpPoller.POLLING_MODE = RPnHttpPoller.TEXT_POLLER ;
     }

}