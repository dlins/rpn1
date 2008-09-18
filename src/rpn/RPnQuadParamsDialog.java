/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import rpnumerics.RPNUMERICS;
//import rpnumerics.physics.QuadNDFluxParams;
import rpn.usecase.ChangeFluxParamsAgent;
import javax.swing.*;
import java.awt.*;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import java.awt.event.*;

public class RPnQuadParamsDialog extends JDialog {
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JButton dismissButton = new JButton();
    JButton fetchButton = new JButton();
    JButton applyButton = new JButton();
    RPnQuadParamsPanel paramsPanel = new RPnQuadParamsPanel();

    public RPnQuadParamsDialog() {
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
        fetchButton.setText("Fetch");
        fetchButton.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fetchButton_actionPerformed(e);
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
        jPanel2.add(fetchButton, null);
        jPanel2.add(dismissButton, null);
    }

    void applyButton_actionPerformed(ActionEvent e) {
//        RealVector oldValue = new RealVector(RPNUMERICS.fluxFunction().fluxParams().getParams());
        int indxAField = new Integer(paramsPanel.paramAIndexField.getText()).intValue();
        int indxBField0 = new Integer(paramsPanel.paramBIndexField0.getText()).intValue();
        int indxBField1 = new Integer(paramsPanel.paramBIndexField1.getText()).intValue();
        int indxCField0 = new Integer(paramsPanel.paramCIndexField0.getText()).intValue();
        int indxCField1 = new Integer(paramsPanel.paramCIndexField1.getText()).intValue();
        int indxCField2 = new Integer(paramsPanel.paramCIndexField2.getText()).intValue();
        double valueAField = new Double(paramsPanel.paramAValueField.getText()).doubleValue();
        double valueBField = new Double(paramsPanel.paramBValueField.getText()).doubleValue();
        double valueCField = new Double(paramsPanel.paramCValueField.getText()).doubleValue();
//        ((QuadNDFluxParams)RPNUMERICS.fluxFunction().fluxParams()).setAParam(indxAField, valueAField);
//        ((QuadNDFluxParams)RPNUMERICS.fluxFunction().fluxParams()).setBParam(indxBField0, indxBField1, valueBField);
//        ((QuadNDFluxParams)RPNUMERICS.fluxFunction().fluxParams()).setCParam(indxCField0, indxCField1, indxCField2, valueCField);
//        RealVector newValue = RPNUMERICS.fluxFunction().fluxParams().getParams();
        // reset flow constants
        
        //TODO Check if this method is not needed anymore 
//	((ConservationShockFlow)RPNUMERICS.flow()).updateXZeroTerms();


//        ChangeFluxParamsAgent.instance().applyChange(
//            new PropertyChangeEvent(this, ChangeFluxParamsAgent.DESC_TEXT, oldValue, newValue));
    }

    void fetchButton_actionPerformed(ActionEvent e) {
//        QuadNDFluxParams currentParams = (QuadNDFluxParams)RPNUMERICS.fluxFunction().fluxParams();
        int indxAField = new Integer(paramsPanel.paramAIndexField.getText()).intValue();
        int indxBField0 = new Integer(paramsPanel.paramBIndexField0.getText()).intValue();
        int indxBField1 = new Integer(paramsPanel.paramBIndexField1.getText()).intValue();
        int indxCField0 = new Integer(paramsPanel.paramCIndexField0.getText()).intValue();
        int indxCField1 = new Integer(paramsPanel.paramCIndexField1.getText()).intValue();
        int indxCField2 = new Integer(paramsPanel.paramCIndexField2.getText()).intValue();
//        String newAValue = new Double(currentParams.getA() [indxAField]).toString();
//        paramsPanel.paramAValueField.setText(newAValue);
//        String newBValue = new Double(currentParams.getB() [indxBField0] [indxBField1]).toString();
//        paramsPanel.paramBValueField.setText(newBValue);
//        String newCValue = new Double(currentParams.getC() [indxCField0] [indxCField1] [indxCField2]).toString();
//        paramsPanel.paramCValueField.setText(newCValue);
    }

    /*
    // postponed feature...
    void resetButton_actionPerformed(ActionEvent e) {

        QuadNDFluxParams defaultParams = (QuadNDFluxParams)RPNUMERICS.PHYSICS.fluxParams().defaultParams();

        int indxAField = new Integer(paramsPanel.paramAIndexField.getText()).intValue();
        int indxBField0 = new Integer(paramsPanel.paramBIndexField0.getText()).intValue();
		int indxBField1 = new Integer(paramsPanel.paramBIndexField1.getText()).intValue();
        int indxCField0 = new Integer(paramsPanel.paramCIndexField0.getText()).intValue();
		int indxCField1 = new Integer(paramsPanel.paramCIndexField1.getText()).intValue();
        int indxCField2 = new Integer(paramsPanel.paramCIndexField2.getText()).intValue();

        String newAValue = new Double(defaultParams.getA()[indxAField]).toString();
        paramsPanel.paramAValueField.setText(newAValue);

        String newBValue = new Double(defaultParams.getB()[indxBField0][indxBField1]).toString();
        paramsPanel.paramBValueField.setText(newBValue);

        String newCValue = new Double(defaultParams.getC()[indxCField0][indxCField1][indxCField2]).toString();
        paramsPanel.paramCValueField.setText(newCValue);
    }*/

    void dismissButton_actionPerformed(ActionEvent e) {
        dispose();
    }
}
