/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import wave.multid.view.Scene;

import java.awt.*;
import javax.swing.*;

public class RPnRiemannFrame extends RPnPhaseSpaceFrame {

    JPanel contentPane;
    private RPnPhaseSpacePanel phaseSpacePanel = null;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel statusPanel = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();
    RPnCursorMonitor coordsField = new RPnCursorMonitor();
    BorderLayout borderLayout2 = new BorderLayout();
    RPnMenuCommand commandMenu_ = null;
    private Dimension frameSize_ = null;
    private RPnRiemannCursorMonitor cursorMonitor_;
    //    int sliderValue_;
    public RPnRiemannFrame(Scene scene, RPnMenuCommand command) {

        super(scene,command);
        
        disableEvents(AWTEvent.WINDOW_EVENT_MASK);
        disableEvents(AWTEvent.COMPONENT_EVENT_MASK);

        commandMenu_ = command;
        try {
            phaseSpacePanel = new RPnRiemannPanel(scene);
            cursorMonitor_= new RPnRiemannCursorMonitor();
            phaseSpacePanel.addMouseMotionListener(cursorMonitor_.getMouseMotionController());

            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
      
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setResizable(true);
        jPanel2.setBackground(Color.gray);
        jPanel3.setBackground(Color.gray);
        jPanel4.setBackground(Color.gray);
        contentPane.setBackground(Color.gray);

        statusPanel.setBackground(Color.gray);
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setLayout(borderLayout2);

        contentPane.add(phaseSpacePanel, BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        contentPane.add(jPanel2, BorderLayout.WEST);
        contentPane.add(jPanel3, BorderLayout.EAST);
        contentPane.add(jPanel4, BorderLayout.NORTH);

        statusPanel.add(jPanel5, BorderLayout.SOUTH);
        statusPanel.add(cursorMonitor_);
        setSize(400,400);

        setFocusable(true);


    }
    

   
    public RPnPhaseSpacePanel phaseSpacePanel() {
        return phaseSpacePanel;
    }
}
