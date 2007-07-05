/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import rpnumerics.RPNUMERICS;
import rpn.usecase.ChangeFluxParamsAgent;
import wave.util.RealVector;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.awt.event.*;

public class RPnFluxParamsDialog extends JDialog {
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JButton dismissButton = new JButton();
    JButton applyButton = new JButton();
    RPnFluxParamsPanel paramsPanel = new RPnFluxParamsPanel();

    public RPnFluxParamsDialog() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        jPanel1.setLayout(borderLayout1);
        jPanel2.setLayout(flowLayout1);
        dismissButton.setText("Dismiss");
        dismissButton.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dismissButton_actionPerformed(e);
                }
            });
        applyButton.setText("Apply");
        applyButton.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    applyButton_actionPerformed(e);
                }
            });
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(paramsPanel, BorderLayout.CENTER);
        jPanel1.add(jPanel2, BorderLayout.SOUTH);
        jPanel2.add(applyButton, null);
        jPanel2.add(dismissButton, null);
    }

    void applyButton_actionPerformed(ActionEvent e) {
//        RealVector oldValue = RPNUMERICS.fluxFunction().fluxParams().getParams();
//        RealVector newValue = paramsPanel.getParams();
//        ChangeFluxParamsAgent.instance().applyChange(
//            new PropertyChangeEvent(this, ChangeFluxParamsAgent.DESC_TEXT, oldValue, newValue));
    }

    void dismissButton_actionPerformed(ActionEvent e) {
        dispose();
    }
}
