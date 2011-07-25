/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;
/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class RPnCoordsInputDialog extends RPnDialog {

    List<JTextField> inputArrayList_;
    JPanel inputCoordsPanel_ = new JPanel();

    public RPnCoordsInputDialog(boolean displayBeginButton, boolean displayCancelButton) {
        super(displayBeginButton, displayCancelButton);
        setSize(new Dimension(300, 200));
        setTitle("Input Coordinates");

        GridBagLayout layout = new GridBagLayout();
        inputCoordsPanel_.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();


        inputArrayList_ = new ArrayList<JTextField>();

        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
            NumberFormat numberFormatter = NumberFormat.getInstance();
            numberFormatter.setMaximumFractionDigits(4);
            JTextField textInputField = new JFormattedTextField(numberFormatter);
            JLabel axisNumberLabel = new JLabel("" + i);
            axisNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
            inputArrayList_.add(textInputField);

            constraints.gridx = i;
            constraints.gridy = 0;

            constraints.ipadx = 50;
            constraints.ipady = 5;

            constraints.anchor = GridBagConstraints.EAST;

            constraints.insets = new Insets(5, 5, 5, 5);

            inputCoordsPanel_.add(textInputField, constraints);

            constraints.gridx = i;
            constraints.gridy = 1;

            inputCoordsPanel_.add(axisNumberLabel, constraints);

            beginButton.setText("Clear");


        }

        getContentPane().add(inputCoordsPanel_);



    }

    @Override
    protected void apply() {
        try {
            RealVector userInput = new RealVector(RPNUMERICS.domainDim());
            for (int i = 0; i < inputArrayList_.size(); i++) {
                userInput.setElement(i, new Double(inputArrayList_.get(i).getText()));
                UIController.instance().globalInputTable().setElement(i, new Double(inputArrayList_.get(i).getText()));
            }
            System.out.println("Valor entrado pelo usuario: " + userInput);
            UIController.instance().userInputComplete(userInput);
            UIController.instance().globalInputTable().reset();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(rootPane, "Wrong input", "ERROR", JOptionPane.ERROR_MESSAGE);

        }

    }

    @Override
    protected void begin() {
        for (JTextField jTextField : inputArrayList_) {

            jTextField.setText("");

        }


    }
}
