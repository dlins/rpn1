/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import rpn.plugininterface.PluginConfigDialog;
import rpn.plugininterface.PluginTableModel;
import rpnumerics.RPNUMERICS;
import rpnumerics.ShockProfile;

public class RPnShockConfigDialog extends RPnDialog {

    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel flowNamePanel_ = new JPanel();
    private FlowLayout flowLayout1 = new FlowLayout();
    private JPanel methodPanel_ = new JPanel();
    private JPanel methodTypePanel_ = new JPanel();
    private JComboBox methodComboBox_;
    private JComboBox flowNameComboBox_ = new JComboBox();
    private JCheckBox specificMethodCheckBox_ = new JCheckBox("Specific Method");
    private JLabel flowPluginName_ = new JLabel();

    public RPnShockConfigDialog() {
        try {

            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RPnShockConfigDialog(boolean enableBackButton) {

        super(enableBackButton);
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RPnShockConfigDialog(boolean b, boolean b0) {
        super(b0, b0);
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMethodName() {
        methodComboBox_ = new JComboBox();
        methodComboBox_.addMouseListener(new MouseHandler());


        methodComboBox_.addItem("Continuation");// TODO A better combo fill method
        methodComboBox_.addItem("Contour");

        methodPanel_.setLayout(flowLayout1);
        methodPanel_.add(new JLabel("Hugoniot Plot Method"));
        methodPanel_.add(methodComboBox_);

    }

    private void addFlowName() {

        flowPluginName_.setText((String) PluginTableModel.instance().getValueAt(0, 2));

    }

    private void jbInit() throws Exception {

        this.setTitle("Shock Curve Configuration");
        RPnUIFrame.setStatusMessage("Curve Configuration");
        addMethodName();
        addFlowName();
        flowPluginName_.addMouseListener(new MouseHandler());

        flowNamePanel_.add(new JLabel("Flow Name: ", SwingConstants.LEFT));
        flowNamePanel_.add(flowPluginName_);

        methodTypePanel_.setLayout(borderLayout1);
        methodTypePanel_.add(specificMethodCheckBox_, BorderLayout.SOUTH);
        methodTypePanel_.add(flowNamePanel_, BorderLayout.NORTH);

        this.getContentPane().add(methodPanel_, BorderLayout.NORTH);
        this.getContentPane().add(methodTypePanel_, BorderLayout.CENTER);
        pack();

    }

    protected void apply() {

        RPNUMERICS.getShockProfile().setHugoniotMethodName((String) methodComboBox_.getSelectedItem());

        if (specificMethodCheckBox_.isSelected()) {
            RPNUMERICS.getShockProfile().setHugoniotSpecific(true);
        } else {
            RPNUMERICS.getShockProfile().setHugoniotSpecific(false);
        }

        RPNUMERICS.getShockProfile().setFlowName((String) flowNameComboBox_.getSelectedItem());

        RPNUMERICS.setCurrentProfile(RPNUMERICS.getShockProfile());
        
        RPnUIFrame.setStatusMessage("Enter coordinates");


    }
    
    private class MouseHandler implements MouseListener{

        public void mouseClicked(MouseEvent e) {
            
            if ((e.getSource()instanceof JLabel) && (e.getButton()==MouseEvent.BUTTON3) ){

                PluginConfigDialog dialog = new PluginConfigDialog(ShockProfile.SHOCKFLOW_NAME);
                dialog.setVisible(true);

            }
            
            if ((e.getSource() instanceof JComboBox) && (e.getButton()==MouseEvent.BUTTON3)){
                
                Object selectedMethod = methodComboBox_.getSelectedItem();
                
                if (selectedMethod.equals("Continuation")){
                RPnContinuationMethodConfigDialog dialog = new RPnContinuationMethodConfigDialog();
                dialog.setVisible(true);

                }
            }
                        
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

            JComponent component = (JComponent) e.getSource();
            component.setToolTipText("Right Click to configure");
            Cursor cur = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            component.setCursor(cur);
            
        }

        public void mouseExited(MouseEvent e) {

            
        }
        
    }
    
}
