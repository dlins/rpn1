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
import rpnumerics.RPNUMERICS;
import rpnumerics.RarefactionProfile;
import rpnumerics.ShockProfile;

public class RPnRarefactionConfigDialog extends RPnDialog {

    private JPanel jPanel1 = new JPanel();
    private JPanel flowNamePanel = new JPanel();
    private JLabel flowNameLabel = new JLabel("Flow Name: ");
    private JComboBox flowNameComboBox = new JComboBox();
    private BorderLayout borderLayout1 = new BorderLayout();
    private FlowLayout flowLayout1 = new FlowLayout();
    private JPanel familyPanel = new JPanel();
    private JLabel flowPluginName_ = new JLabel();
    private JComboBox familyIndexComboBox = new JComboBox();

    public RPnRarefactionConfigDialog() {
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RPnRarefactionConfigDialog(boolean enableBeginButton) {
        super(enableBeginButton);
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void addFamilyIndex() {
//        familyPanel.add(new JLabel("Family Index", SwingConstants.LEFT));
//        familyIndexComboBox = new JComboBox();
//        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
//            familyIndexComboBox.addItem(new Integer(i));
//        }
//        familyPanel.setLayout(flowLayout1);
//        familyPanel.add(familyIndexComboBox);
//    }

    private void jbInit() throws Exception {
        setTitle("Rarefaction Curve Configuration");
//        addFamilyIndex();
        addFlowName();


//        flowNameComboBox.addItem("Blow Up");
//        flowNameComboBox.addItem("Rarefaction Flow");
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

//        RPNUMERICS.getRarefactionProfile().setFamily(((Integer) familyIndexComboBox.getSelectedItem()).intValue());
//        RPNUMERICS.getRarefactionProfile().setFlowName((String) flowNameComboBox.getSelectedItem());
        RPNUMERICS.setCurrentProfile(RPNUMERICS.getRarefactionProfile());
        dispose();


    }
    
    
    
    private class MouseHandler implements MouseListener{

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
