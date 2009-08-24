/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import rpn.usecase.ChangeDirectionAgent;
import rpnumerics.RPNUMERICS;

public class RPnCurvesConfigPanel extends JPanel {

    private GridBagLayout gridLayout = new GridBagLayout();
    private JPanel familyPanel_;
    private JPanel directionPanel_;
    private JLabel familyLabel_;
    private static JSpinner familySpinner_;
    private JCheckBox forwardCheckBox_;
    private JCheckBox backwardCheckBox_;
    private static Integer currentOrbitDirection_ = new Integer(1);


    public RPnCurvesConfigPanel() {
        buildPanel();
        ChangeDirectionAgent.instance().execute();
    }

    private void buildPanel() {

        familyLabel_ = new JLabel("Family", SwingConstants.CENTER);
        
        familySpinner_ = new JSpinner(new SpinnerNumberModel(1,1,RPNUMERICS.domainDim(),1));

        familyPanel_ = new JPanel(new GridLayout(2, 1));
        directionPanel_ = new JPanel(new GridLayout(2, 1));

        forwardCheckBox_ = new JCheckBox();
        forwardCheckBox_.setSelected(true);//Default 

        forwardCheckBox_.addActionListener(new OrbitDirectionListener(1));
        forwardCheckBox_.setText("Forward");

        backwardCheckBox_ = new JCheckBox("Backward");

        backwardCheckBox_.addActionListener(new OrbitDirectionListener(-1));

        backwardCheckBox_.setText("Backward");

        directionPanel_.add(forwardCheckBox_);
        directionPanel_.add(backwardCheckBox_);

        familyPanel_.add(familyLabel_);
        familyPanel_.add(familySpinner_);

        this.setLayout(gridLayout);

        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.ipadx = 40;

        gridConstraints.gridx = 0;

        this.add(directionPanel_, gridConstraints);
        gridConstraints.gridx = 1;
        this.add(familyPanel_, gridConstraints);
    }

    public static Integer getOrbitDirection() {
        return currentOrbitDirection_;
    }
    
    public static Integer getFamilyIndex(){
        return (Integer) familySpinner_.getModel().getValue();
    }
    

    private class OrbitDirectionListener implements ActionListener {

        private Integer direction_;

        public OrbitDirectionListener(int direction) {

            direction_ = new Integer(direction);
        }

        public void actionPerformed(ActionEvent e) {

            JCheckBox checkBox = (JCheckBox) e.getSource();


            if (forwardCheckBox_.isSelected() == false && backwardCheckBox_.isSelected() == false) {
                checkBox.setSelected(true);
                return;
            }


            if (forwardCheckBox_.isSelected() == true && backwardCheckBox_.isSelected() == true) {
                currentOrbitDirection_ = 0;

            } else {
                if (checkBox.isSelected()) {
                    currentOrbitDirection_ = direction_;
                } else {
                    currentOrbitDirection_ = -direction_;
                }

            }
            ChangeDirectionAgent.instance().execute();
        }
    }
}


