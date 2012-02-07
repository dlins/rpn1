/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.usecase.AreaSelectionAgent;
import rpn.usecase.BifurcationRefineAgent;
import wave.util.RealVector;

public class RPnSelectedAreaDialog extends RPnDialog {

    private JPanel paramsPanel_ = new JPanel();
    JLabel xResolutionLabel;
    JLabel yResolutionLabel;
    JLabel resolutionLabel_;
    JTextField xResolutionTextField;
    JTextField yResolutionTextField;
    JTextField[] resolutionValues_;

    public RPnSelectedAreaDialog() {
        super(false, true);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        System.out.println("RPnSelectedAreaDialog : jbInit()");

        setTitle("Area Selection Resolution");
        
        UI_ACTION_SELECTED actionSelected = (UI_ACTION_SELECTED) UIController.instance().getState();
        
        resolutionValues_ = new JTextField[actionSelected.actionDimension()];

        paramsPanel_.setLayout(new GridLayout(resolutionValues_.length, 2));

        for (int i = 0; i < resolutionValues_.length; i++) {

            resolutionLabel_ = new JLabel("" + i);
            resolutionValues_[i] = new JTextField();

            paramsPanel_.add(resolutionLabel_);
            paramsPanel_.add(resolutionValues_[i]);


        }

        setMinimumSize(new Dimension(getTitle().length() * 10, 40));
        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();
    }

    @Override
    protected void cancel() {
        dispose();
    }

    protected void apply() {
        System.out.println("RPnSelectedAreaDialog : apply()");

        boolean okFlag = true;

        RealVector resolutionVector = new RealVector(resolutionValues_.length);

        for (int i = 0; i < resolutionValues_.length; i++) {
            JTextField jTextField = resolutionValues_[i];
            try {
                resolutionVector.setElement(i, Double.parseDouble(jTextField.getText()));
            } catch (NumberFormatException ex) {
                okFlag = false;
                JOptionPane.showMessageDialog(this, "Invalid resolution", "Error", JOptionPane.ERROR_MESSAGE);
                break;

            }
        }

        if (okFlag) {
            dispose();

        }



    }

    @Override
    protected void begin() {
    }
}
