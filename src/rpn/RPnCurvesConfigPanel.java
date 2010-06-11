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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.usecase.ChangeDirectionAgent;
import rpn.usecase.RpModelPlotAgent;
import rpnumerics.RPNUMERICS;

public class RPnCurvesConfigPanel extends JPanel {

    private GridBagLayout gridLayout = new GridBagLayout();
    private JPanel familyPanel_;
    private JPanel directionPanel_;
    private JLabel familyLabel_;
    private JSpinner familySpinner_;
    private JCheckBox forwardCheckBox_;
    private JCheckBox backwardCheckBox_;
    private static Integer currentOrbitDirection_ = new Integer(1);
    private static JToggleButton addLastGeometryButton_;
    private static ToggleButtonListener buttonListener_;

    public RPnCurvesConfigPanel() {
        buildPanel();
        ChangeDirectionAgent.instance().execute();
        buttonListener_ = new ToggleButtonListener();

    }

    public static void setMultipleButton(boolean state){
        addLastGeometryButton_.setSelected(state);
        buttonListener_.actionPerformed(new ActionEvent(addLastGeometryButton_,0,""));


    }

    private void buildPanel() {
        addLastGeometryButton_ = new JToggleButton("Multiple plot");
        addLastGeometryButton_.addActionListener(new ToggleButtonListener());

        familyLabel_ = new JLabel("Family", SwingConstants.CENTER);
        //TODO Qual eh o valor maximo que o JSpinner deve ter ?? Esse valor muda conforme o tipo de curva para a qual ele configura a familia ??
        familySpinner_ = new JSpinner(new SpinnerNumberModel(0, 0, RPNUMERICS.domainDim(), 1));
        familySpinner_.addChangeListener(new FamilyListener());

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

        directionPanel_.add(addLastGeometryButton_);

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

    private class FamilyListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            JSpinner familySpinner = (JSpinner) e.getSource();
            Integer value = (Integer)familySpinner.getValue();
            RPNUMERICS.setFamily(value);


        }
    }


    private class ToggleButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            JToggleButton button = (JToggleButton) e.getSource();
               if ( UIController.instance().getState() instanceof UI_ACTION_SELECTED){
                   UI_ACTION_SELECTED actionSelected = (UI_ACTION_SELECTED)(UIController.instance().getState());
                   if (actionSelected.getAction() instanceof RpModelPlotAgent){
                       RpModelPlotAgent plotAgent = (RpModelPlotAgent)actionSelected.getAction();
                       plotAgent.setMultipleGeometry(!button.isSelected());

                   }
                       
               }

        }

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


