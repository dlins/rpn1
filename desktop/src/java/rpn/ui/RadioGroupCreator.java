/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import rpn.configuration.Configuration;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;

/**
 *
 * @author edsonlan
 */
public class RadioGroupCreator extends UIComponentCreator {

    private ButtonGroup buttonGroup_;
    private JRadioButton forwardCheckBox_;
    private JRadioButton backwardCheckBox_;
    private int currentOrbitDirection_;


    public RadioGroupCreator(Configuration configuration_, String configurationParameter) {
        super(configuration_, configurationParameter);

    }

    @Override
    public JComponent createUIComponent() {


        if (configurationParameter_.equals("direction")) {

            buttonGroup_ = new ButtonGroup();

            return fillButtonGroup();

        }
        TextFieldCreator textFieldCreator = new TextFieldCreator(configuration_, configurationParameter_);
        return textFieldCreator.createUIComponent();

    }

    private JPanel fillButtonGroup() {


        JPanel directionPanel = new JPanel();


        forwardCheckBox_ = new JRadioButton("Forward");
        forwardCheckBox_.setSelected(true);//Default 

        forwardCheckBox_.setEnabled(true);

        forwardCheckBox_.addActionListener(new OrbitDirectionListener());
        forwardCheckBox_.setText("Forward");

        backwardCheckBox_ = new JRadioButton("Backward");

        backwardCheckBox_.setEnabled(true);
        backwardCheckBox_.addActionListener(new OrbitDirectionListener());

        backwardCheckBox_.setText("Backward");

        buttonGroup_.add(forwardCheckBox_);
        buttonGroup_.add(backwardCheckBox_);

        directionPanel.add(forwardCheckBox_);
        directionPanel.add(backwardCheckBox_);


        return directionPanel;


    }

    private class OrbitDirectionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (forwardCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.FORWARD_DIR;
            } else {
                currentOrbitDirection_ = Orbit.BACKWARD_DIR;
            }

            RPNUMERICS.setParamValue("fundamentalcurve", "direction", String.valueOf(currentOrbitDirection_));
        }
    }
}
