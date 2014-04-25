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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpn.command.RpModelActionCommand;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class RPnCoordsInputDialog extends RPnDialog {
    
    private List<JTextField> inputArrayList_;
    private JPanel inputCoordsPanel_ = new JPanel();
    private JPanel phaseSpaceGroupPanel_;
    private ButtonGroup phaspaceButtonGroup_;
    private JRadioButton rightCheckBox_, leftCheckBox_, mainCheckBox_;
    
    public RPnCoordsInputDialog(boolean displayBeginButton, boolean displayCancelButton) {
        super(displayBeginButton, displayCancelButton);
        setSize(new Dimension(600, 200));
        setTitle("Input Coordinates");
        setResizable(true);
        phaseSpaceGroupPanel_ = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        phaspaceButtonGroup_ = new ButtonGroup();
        inputCoordsPanel_.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        
        rightCheckBox_ = new JRadioButton(RPnDataModule.RIGHTPHASESPACE.getName());
        leftCheckBox_ = new JRadioButton(RPnDataModule.LEFTPHASESPACE.getName());
        mainCheckBox_ = new JRadioButton(RPnDataModule.PHASESPACE.getName());
        
        
        phaspaceButtonGroup_.add(rightCheckBox_);
        phaspaceButtonGroup_.add(leftCheckBox_);
        phaspaceButtonGroup_.add(mainCheckBox_);
        
        phaseSpaceGroupPanel_.add(leftCheckBox_);
        phaseSpaceGroupPanel_.add(rightCheckBox_);
        phaseSpaceGroupPanel_.add(mainCheckBox_);
        
        
        mainCheckBox_.addActionListener(new PhaseSpaceListener());
        rightCheckBox_.addActionListener(new PhaseSpaceListener());
        leftCheckBox_.addActionListener(new PhaseSpaceListener());
        
        
        mainCheckBox_.setSelected(true);//Default
        mainCheckBox_.doClick();
        
        
        inputArrayList_ = new ArrayList<JTextField>();
        
        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
            NumberFormat numberFormatter = NumberFormat.getInstance();
            numberFormatter.setMaximumFractionDigits(8);
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
            setLocation(400, 300);
            
        }
        
        getContentPane().add(inputCoordsPanel_, BorderLayout.NORTH);
        getContentPane().add(phaseSpaceGroupPanel_, BorderLayout.CENTER);
        
    }
    
    @Override
    protected void apply() {
        
        try {
            RealVector userInput = new RealVector(RPNUMERICS.domainDim());
            for (int i = 0; i < inputArrayList_.size(); i++) {
                userInput.setElement(i, new Double(inputArrayList_.get(i).getText()));
                UIController.instance().globalInputTable().setElement(i, new Double(inputArrayList_.get(i).getText()));
                
            }
            
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
    
    private class PhaseSpaceListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            
            
            if (UIController.instance().getState() instanceof UI_ACTION_SELECTED) {
                
                if (leftCheckBox_.isSelected()) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.LEFTPHASESPACE);
                }
                
                if (rightCheckBox_.isSelected()) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.RIGHTPHASESPACE);
                }
                if (mainCheckBox_.isSelected()) {
                    UIController.instance().setActivePhaseSpace(RPnDataModule.PHASESPACE);
                }
                
            }
        }
    }
}
