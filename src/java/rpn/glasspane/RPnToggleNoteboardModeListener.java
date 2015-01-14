/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.glasspane;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.controller.ui.UIController;
import rpn.message.*;


/**
 *
 * @author mvera
 */
public class RPnToggleNoteboardModeListener implements ActionListener {


    public RPnToggleNoteboardModeListener() {

    }

    public void actionPerformed(ActionEvent e) {


        // TODO create class RPnToggleButton(JFrame parentFrame)...
        RPnPhaseSpaceFrame parentFrame = ((RPnNoteModeToggleButton)e.getSource()).getParentContainer();
        String noteFrameTitle = parentFrame.getTitle();       

        JFrame[] frames = RPnUIFrame.getPhaseSpaceFrames();
        JFrame[] aux_frames = RPnUIFrame.getAuxFrames();
        JFrame[] riemann_frames = RPnUIFrame.getRiemannFrames();

        JFrame[] allFrames = null;
        if (riemann_frames != null)
            allFrames = new JFrame[frames.length + aux_frames.length + riemann_frames.length];
        else
            allFrames = new JFrame[frames.length + aux_frames.length];

        // FILL UP the allFrames strucutre
        int count = 0;
        for (int i = 0; i < frames.length; i++)
            allFrames[count++] = frames[i];
        for (int i = 0; i < aux_frames.length; i++)
            allFrames[count++] = aux_frames[i];
        if (riemann_frames != null)
        for (int i = 0; i < riemann_frames.length; i++)
            allFrames[count++] = riemann_frames[i];



        // the TOGGLE !
        for (int i = 0; i < allFrames.length; i++)
            
            if (allFrames[i].getTitle().compareTo(noteFrameTitle) == 0) {

                
                allFrames[i].getGlassPane().setVisible(!allFrames[i].getGlassPane().isVisible());
                
                if (allFrames[i] instanceof RPnPhaseSpaceFrame)
                    ((RPnPhaseSpaceFrame)allFrames[i]).getNoteboardToggleButton().setSelected(true);
                
            }
            else {
                
                allFrames[i].getGlassPane().setVisible(false);
                if (allFrames[i] instanceof RPnPhaseSpaceFrame)
                    ((RPnPhaseSpaceFrame)allFrames[i]).getNoteboardToggleButton().setSelected(false);
            }
                
        

        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {

            StringBuilder buffer = new StringBuilder();

            buffer.append("<COMMAND name=\"TOGGLE_NOTEBOARD_MODE\" ");
            buffer.append("phasespace=\"").append(UIController.instance().getActivePhaseSpace().getName()).append("\">");
            buffer.append("<COMMANDPARAM name=\"activated_frame_title\" value=\"" + noteFrameTitle.toString() + "\"/>");
            buffer.append("</COMMAND>");


            RPnNetworkStatus.instance().sendCommand(buffer.toString());
        }

         if (RPnHttpPoller.POLLING_MODE == RPnHttpPoller.TEXT_POLLER)
            RPnHttpPoller.POLLING_MODE = RPnHttpPoller.OBJ_POLLER;

        else RPnHttpPoller.POLLING_MODE = RPnHttpPoller.TEXT_POLLER ;
     }

}