/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.*;
import rpn.parser.RPnDataModule;
import rpn.command.ChangeDirectionCommand;
import rpn.command.OrbitPlotCommand;
import rpnumerics.Configuration;
import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;

public class RPnCurvesConfigPanel extends JPanel implements PropertyChangeListener {


    private ButtonGroup directionButtonGroup_;
    private JPanel directionPanel_;
    private JRadioButton forwardCheckBox_;
    private JRadioButton backwardCheckBox_;
    private JRadioButton bothCheckBox_;
    private static Integer currentOrbitDirection_ = Orbit.FORWARD_DIR;
    private JTabbedPane curvesTabbedPanel_;

    public RPnCurvesConfigPanel() {

        ChangeDirectionCommand.instance().execute();

        curvesTabbedPanel_ = new JTabbedPane();
        directionButtonGroup_=new ButtonGroup();
        buildPanel();

    }

  
    private void buildPanel() {

        HashMap<String, Configuration> configMap = RPNUMERICS.getConfigurations();
        curvesTabbedPanel_.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        Set<Entry<String, Configuration>> configSet = configMap.entrySet();

        for (Entry<String, Configuration> entry : configSet) {

            String configurationType = entry.getValue().getType();

            if (!configurationType.equalsIgnoreCase("PHYSICS") && !configurationType.equalsIgnoreCase("VISUAL")) {
                RPnInputComponent inputComponent = new RPnInputComponent(entry.getValue());
                inputComponent.removeParameter("resolution");
                if (inputComponent.getContainer().getComponentCount() > 0) {
                    curvesTabbedPanel_.addTab(entry.getKey(), inputComponent.getContainer());
                }
            }

        }


        //directionPanel_ = new JPanel(new GridLayout(1, 2));
        directionPanel_ = new JPanel(new GridLayout(1, 3));

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

        GridBagLayout boxLayout = new GridBagLayout();

        setLayout(boxLayout);

        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.gridx=0;
        gridConstraints.gridy = 0;

        gridConstraints.fill = GridBagConstraints.BOTH;

        add(directionPanel_, gridConstraints);


        gridConstraints.gridy = 1;
        gridConstraints.fill = GridBagConstraints.BOTH;


        add(curvesTabbedPanel_, gridConstraints);


    }

    public static Integer getOrbitDirection() {
        return currentOrbitDirection_;
    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getNewValue().equals("bifurcationcurve")) {//Bifurcation Curves selected


            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(false);
                backwardCheckBox_.setEnabled(false);
                bothCheckBox_.setEnabled(false);
            }
        }

        if (evt.getNewValue().equals("phasediagram")) {//Phase Diagram Curves selected


            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(true);
                backwardCheckBox_.setEnabled(true);
                bothCheckBox_.setEnabled(true);

            }

        }


        if (evt.getNewValue().equals("wavecurve")) {//Wave Curves selected


            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(true);
                backwardCheckBox_.setEnabled(true);
                bothCheckBox_.setEnabled(true);

            }

        }
    }

 

   

    private class OrbitDirectionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

//               if (forwardCheckBox_.isSelected()) {
//                    currentOrbitDirection_ = Orbit.FORWARD_DIR;
//                } else {
//                    currentOrbitDirection_ = Orbit.BACKWARD_DIR;
//                }


            if (forwardCheckBox_.isSelected()) {
                    currentOrbitDirection_ = Orbit.FORWARD_DIR;
                } else if (backwardCheckBox_.isSelected()) {
                    currentOrbitDirection_ = Orbit.BACKWARD_DIR;
                } else if (bothCheckBox_.isSelected()) {
                    currentOrbitDirection_ = Orbit.BOTH_DIR;
                }


            ChangeDirectionCommand.instance().execute();


        }
    }
}


