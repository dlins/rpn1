/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.DimensionUIResource;
import rpnumerics.RPNUMERICS;

public class RPnRarefactionConfigDialog extends RPnDialog {

    JPanel jPanel1 = new JPanel();
    JPanel flowNamePanel = new JPanel();
    private JLabel flowNameLabel=new JLabel("Flow ");
    private JComboBox flowNameComboBox = new JComboBox();
    BorderLayout borderLayout1 = new BorderLayout();
    FlowLayout flowLayout1 = new FlowLayout();
    JPanel familyPanel = new JPanel();

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

    private void addFamilyIndex() {
        familyPanel.add(new JLabel("Family Index", SwingConstants.LEFT));
        familyIndexComboBox = new JComboBox();
        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
            familyIndexComboBox.addItem(new Integer(i));
        }
        familyPanel.setLayout(flowLayout1);
        familyPanel.add(familyIndexComboBox);
    }

    private void jbInit() throws Exception {
        setTitle("Rarefaction Curve Configuration");
        addFamilyIndex();


        flowNameComboBox.addItem("Blow Up");
        flowNameComboBox.addItem("Rarefaction Flow");
        flowNamePanel.add(flowNameLabel);
        flowNamePanel.add(flowNameComboBox);

        jPanel1.setLayout(borderLayout1);

        this.getContentPane().add(familyPanel, BorderLayout.CENTER);
        this.getContentPane().add(flowNamePanel, BorderLayout.NORTH);
        pack();


    }

    protected void apply() {
        
        RPNUMERICS.getRarefactionProfile().setFamily(((Integer) familyIndexComboBox.getSelectedItem()).intValue());
        RPNUMERICS.getRarefactionProfile().setFlowName((String) flowNameComboBox.getSelectedItem());
        RPNUMERICS.setCurrentProfile(RPNUMERICS.getRarefactionProfile());
        dispose();


    }

   
}
