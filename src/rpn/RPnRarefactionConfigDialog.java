/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import rpn.plugininterface.PluginConfigDialog;
import rpn.plugininterface.PluginTableModel;
import rpnumerics.RarefactionProfile;

public class RPnRarefactionConfigDialog extends RPnDialog {

    private JPanel jPanel1 = new JPanel();
    private JPanel flowNamePanel = new JPanel();
    private JLabel flowNameLabel = new JLabel("Flow Name: ");
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel familyPanel = new JPanel();
    private JLabel flowPluginName_ = new JLabel();

    public RPnRarefactionConfigDialog(boolean b, boolean b0) {
        super(b0, b0);
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setTitle("Rarefaction Curve Configuration");
        RPnUIFrame.setStatusMessage("Curve Configuration");
        addFlowName();

        flowNamePanel.add(flowNameLabel);
        flowNamePanel.add(flowPluginName_);

        jPanel1.setLayout(borderLayout1);

        this.getContentPane().add(familyPanel, BorderLayout.CENTER);
        this.getContentPane().add(flowNamePanel, BorderLayout.NORTH);
        pack();


    }

    private void addFlowName() {

        flowPluginName_.setText((String) PluginTableModel.instance().getValueAt(1, 2));
        flowPluginName_.addMouseListener(new MouseHandler());

    }

    protected void apply() {

//        System.out.println("Chamando apply");
//        RPNUMERICS.setCurrentProfile(RPNUMERICS.getRarefactionProfile());
//        RPnUIFrame.setStatusMessage("Enter coordinates");
        dispose();
    }

    private class MouseHandler implements MouseListener {

        public void mouseClicked(MouseEvent e) {

            PluginConfigDialog dialog = new PluginConfigDialog(RarefactionProfile.RAREFACTIONFLOW_NAME);
            dialog.setVisible(true);

        }
  
        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {

            JLabel label = (JLabel) e.getSource();
            label.setToolTipText("Click to configure");
            Cursor cur = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            label.setCursor(cur);

        }

        public void mouseExited(MouseEvent e) {
        }
    }
}
