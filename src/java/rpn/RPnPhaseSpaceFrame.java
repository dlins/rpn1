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
import javax.swing.event.*;
import java.util.*;
import rpn.controller.ui.UIController;

public class RPnPhaseSpaceFrame extends JFrame {

    JPanel contentPane;
    RPnPhaseSpacePanel phaseSpacePanel = null;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel statusPanel = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();
    RPnCursorMonitor coordsField = new RPnCursorMonitor();
    BorderLayout borderLayout2 = new BorderLayout();
    RPnMenuCommand commandMenu_ = null;
    JSlider slider = new JSlider(-5, 5, 0);
    private Hashtable labels_ = new Hashtable();
    private Dimension frameSize_ = null;

    //    int sliderValue_;
    public RPnPhaseSpaceFrame(Scene scene, RPnMenuCommand command) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
        //	enableEvents(AWTEvent.FOCUS_EVENT_MASK);
        commandMenu_ = command;
        try {
            phaseSpacePanel = new RPnPhaseSpacePanel(scene);
        
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(Frame.class.getResource("[Your Icon]")));

        slider.setEnabled(true);

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setResizable(false);
        jPanel2.setBackground(Color.gray);
        jPanel3.setBackground(Color.gray);
        jPanel4.setBackground(Color.gray);
        contentPane.setBackground(Color.gray);
        setTitle(" RPn - "
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
        jPanel5.add(slider);

        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);

        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        createLabels(slider.getMinimum(), slider.getMaximum());

        slider.setLabelTable(labels_);
        slider.addChangeListener(new SliderState());
        setFocusable(true);
        addWindowFocusListener(new FocusController());
        addKeyListener(new KeyController());

    }

    public JSlider getSlider(){return slider;}

    private class FocusController implements WindowFocusListener {

        public void windowGainedFocus(WindowEvent e) {

            UIController.instance().setFocusPanel(phaseSpacePanel);

        }

        public void windowLostFocus(WindowEvent e) {
        }
    }

    private class KeyController extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent keyEvent) {


            if (keyEvent.getKeyChar() == 'l') {

                if (RPnPhaseSpacePanel.isCursorLine()) {
                    UIController.instance().showCursorLines(false);
                } else {
                    UIController.instance().showCursorLines(true);
                }

            }
        }
    }

    private class SliderState implements ChangeListener {

        private HashMap heightTable_;
        private HashMap widthTable_;

        public SliderState() {
            heightTable_ = new HashMap(20);
            widthTable_ = new HashMap(20);
        }

        private void createSizeScale(int min, int max) {

            int h = RPnPhaseSpaceFrame.this.frameSize_.height;
            int w = RPnPhaseSpaceFrame.this.frameSize_.width;
            int i;
            float delta = 0;
            for (i = -5; i <= 5; i++) {
                delta = (w * i * 5) / 100;
                heightTable_.put(new Integer(i), new Integer((int) (h + delta)));
                widthTable_.put(new Integer(i), new Integer((int) (w + delta)));
            }

        }

        public void stateChanged(ChangeEvent e) {

            int value = 0;
            Integer h = new Integer(value);
            Integer w = new Integer(value);
            if ((heightTable_.isEmpty())) {
                createSizeScale(-5, 5);
            }
            JSlider source = (JSlider) (e.getSource());
            if (!source.getValueIsAdjusting()) {
                value = source.getValue();
                h = (Integer) heightTable_.get(new Integer(value));
                w = (Integer) widthTable_.get(new Integer(value));

                for (int i = 0; i < RPnUIFrame.getPhaseSpaceFrames().length; i++) {

                    RPnPhaseSpaceFrame frame = RPnUIFrame.getPhaseSpaceFrames()[i];

                    frame.setSize(w.intValue(), h.intValue());
                    frame.validate();

                    ChangeListener changeListener = frame.getSlider().getChangeListeners()[0];

                    frame.getSlider().removeChangeListener(changeListener);

                    frame.getSlider().getModel().setValue(value);


                    frame.getSlider().addChangeListener(changeListener);


                }


//                RPnPhaseSpaceFrame.this.setSize(w.intValue(), h.intValue());
//                RPnPhaseSpaceFrame.this.validate();

            }
        }
    }
    //Overridden so we can exit when window is closed

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            commandMenu_.finalizeApplication();
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

    private void createLabels(int min, int max) {

        int i;
        for (i = min; i <= max; i++) {

            if (i == min) {
                labels_.put(new Integer(i), new JLabel("Min"));
            }
            if (i == max) {
                labels_.put(new Integer(i), new JLabel("Max"));
            }
        }

    }

    public RPnPhaseSpacePanel phaseSpacePanel() {
        return phaseSpacePanel;
    }
}
