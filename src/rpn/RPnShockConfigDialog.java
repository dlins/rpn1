/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import rpnumerics.RPNUMERICS;

public class RPnShockConfigDialog extends RPnDialog {

    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel flowNamePanel_ = new JPanel();
    private FlowLayout flowLayout1 = new FlowLayout();
    private JPanel methodPanel_ = new JPanel();
    private JPanel methodTypePanel_ = new JPanel();
    private JComboBox methodComboBox_;
    private JComboBox flowNameComboBox_ = new JComboBox();
    private JCheckBox specificMethodCheckBox_ = new JCheckBox("Specific Method");

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

    private void addMethodName() {
        methodComboBox_ = new JComboBox();


        methodComboBox_.addItem("Continuation");
        methodComboBox_.addItem("Contour");

        methodPanel_.setLayout(flowLayout1);
        methodPanel_.add(new JLabel("Hugoniot Plot Method"));
        methodPanel_.add(methodComboBox_);

    }

    private void addFlowName() {

        flowNameComboBox_.addItem("Conservation Shock Flow");



    }

    private void jbInit() throws Exception {

        this.setTitle("Shock Curve Configuration");
        addMethodName();
        addFlowName();

        flowNamePanel_.add(new JLabel("Flow Name", SwingConstants.LEFT));
        flowNamePanel_.add(flowNameComboBox_);

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


    }
}
