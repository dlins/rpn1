/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.controller.PhaseSpacePanelController;
import wave.multid.view.Scene;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

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

    //    int sliderValue_;
    public RPnRiemannFrame(Scene scene, RPnMenuCommand command) {

        super(scene,command);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
        //	enableEvents(AWTEvent.FOCUS_EVENT_MASK);
        commandMenu_ = command;
        try {
            phaseSpacePanel = new RPnRiemannPanel(scene);

            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(Frame.class.getResource("[Your Icon]")));



        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setResizable(true);
        jPanel2.setBackground(Color.gray);
        jPanel3.setBackground(Color.gray);
        jPanel4.setBackground(Color.gray);
        contentPane.setBackground(Color.gray);
        setTitle(" Riemann Phase Space Frame - "
                + new Integer(((PhaseSpacePanelController) phaseSpacePanel.getCastedUI()).getAbsIndex()).intValue() + ','
                + new Integer(((PhaseSpacePanelController) phaseSpacePanel.getCastedUI()).getOrdIndex()).intValue());
        phaseSpacePanel.addMouseMotionListener(coordsField.getMouseMotionController());
        statusPanel.setBackground(Color.gray);
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setLayout(borderLayout2);
        coordsField.setText("50:50");
        contentPane.add(phaseSpacePanel, BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        contentPane.add(jPanel2, BorderLayout.WEST);
        contentPane.add(jPanel3, BorderLayout.EAST);
        contentPane.add(jPanel4, BorderLayout.NORTH);
        statusPanel.add(coordsField, BorderLayout.EAST);
        statusPanel.add(jPanel5, BorderLayout.SOUTH);

        setFocusable(true);


    }
    //Overridden so we can exit when window is closed

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            dispose();
        }
    }

    @Override
    protected void processComponentEvent(ComponentEvent e) {

        if (e.getID() == ComponentEvent.COMPONENT_SHOWN) {

            if (frameSize_ == null) {
                frameSize_ = getSize();
            }
        }

    }

   
    public RPnPhaseSpacePanel phaseSpacePanel() {
        return phaseSpacePanel;
    }
}
