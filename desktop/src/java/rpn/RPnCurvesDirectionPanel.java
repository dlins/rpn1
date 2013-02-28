/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.*;
import rpn.command.ChangeDirectionCommand;
import rpn.command.FillPhaseSpaceCommand;
import rpnumerics.Orbit;

public class RPnCurvesDirectionPanel extends Observable  {

    private ButtonGroup directionButtonGroup_;
    private JPanel directionPanel_;
    private JRadioButton forwardCheckBox_;
    private JRadioButton backwardCheckBox_;
    private JRadioButton bothCheckBox_;
    private static Integer currentOrbitDirection_ = Orbit.FORWARD_DIR;
  
    private JPanel mainPainel_;

    public RPnCurvesDirectionPanel() {


        addObserver(ChangeDirectionCommand.instance());
        mainPainel_ = new JPanel();

        directionButtonGroup_ = new ButtonGroup();
        buildPanel();

        

    }

    private void buildPanel() {

        directionPanel_ = new JPanel(new GridLayout(3, 1));

        forwardCheckBox_ = new JRadioButton("Forward");
        forwardCheckBox_.setSelected(true);//Default 

        forwardCheckBox_.setEnabled(true);

        forwardCheckBox_.addActionListener(new OrbitDirectionListener());
        forwardCheckBox_.setText("Forward");

        backwardCheckBox_ = new JRadioButton("Backward");

        backwardCheckBox_.setEnabled(true);
        backwardCheckBox_.addActionListener(new OrbitDirectionListener());

        backwardCheckBox_.setText("Backward");

        // ------------------------------------
        bothCheckBox_ = new JRadioButton("Both");
        bothCheckBox_.setEnabled(true);
        bothCheckBox_.addActionListener(new OrbitDirectionListener());
        bothCheckBox_.setText("Both");
        // ------------------------------------

        directionButtonGroup_.add(forwardCheckBox_);
        directionButtonGroup_.add(backwardCheckBox_);
        directionButtonGroup_.add(bothCheckBox_);

        directionPanel_.add(forwardCheckBox_);
        directionPanel_.add(backwardCheckBox_);
        directionPanel_.add(bothCheckBox_);

        
        mainPainel_.add(directionPanel_);
        
    }
  
    
    public JPanel getContainer(){return mainPainel_;}

    private class OrbitDirectionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (forwardCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.FORWARD_DIR;
            } else if (backwardCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.BACKWARD_DIR;
            } else if (bothCheckBox_.isSelected()) {
                currentOrbitDirection_ = Orbit.BOTH_DIR;
            }

            setChanged();
            notifyObservers(new Integer(currentOrbitDirection_));

        }
    }
}
