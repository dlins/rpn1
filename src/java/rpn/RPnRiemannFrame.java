/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import wave.multid.view.Scene;

import javax.swing.*;
import rpnumerics.RPNUMERICS;

public class RPnRiemannFrame extends JFrame {

    private JPanel contentPane;
    private RPnPhaseSpacePanel phaseSpacePanel = null;
    GridLayout borderLayout1 = new GridLayout(2, 1);
    JPanel statusPanel = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout();
    RPnMenuCommand commandMenu_ = null;
    private Dimension frameSize_ = null;

    private RPnRiemannCursorMonitor cursorMonitor_;

    //    int sliderValue_;
    public RPnRiemannFrame(Scene scene, RPnMenuCommand command) {

        disableEvents(AWTEvent.WINDOW_EVENT_MASK);
        disableEvents(AWTEvent.COMPONENT_EVENT_MASK);

        commandMenu_ = command;
        try {
            cursorMonitor_ = new RPnRiemannCursorMonitor(RPNUMERICS.domainDim());
            phaseSpacePanel = new RPnRiemannPanel(scene, cursorMonitor_);

            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout2);


        contentPane.add(phaseSpacePanel, BorderLayout.CENTER);

        jPanel2.add(cursorMonitor_.getSpeed());
        jPanel2.setBackground(Color.black);

        List<JLabel> labels = cursorMonitor_.getProfileCoords();
        for (JLabel jLabel : labels) {
            jLabel.setBackground(Color.black);
            jPanel2.add(jLabel);
        }

        contentPane.add(jPanel2, BorderLayout.SOUTH);

        setSize(400, 400);

        setFocusable(true);

    }

    public RPnPhaseSpacePanel phaseSpacePanel() {
        return phaseSpacePanel;
    }
}
