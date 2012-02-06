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
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpn.component.OrbitGeom;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.usecase.ChangeDirectionAgent;
import rpn.usecase.RpModelPlotAgent;
import rpnumerics.RPNUMERICS;

public class RPnCurvesConfigPanel extends JPanel implements PropertyChangeListener {

    private GridBagLayout gridLayout = new GridBagLayout();
    private JPanel familyPanel_;
    private JPanel directionPanel_;
    private JLabel familyLabel_, leftFamilyLabel_, rightFamilyLabel_;
    private JSpinner familySpinner_, leftFamilySpinner_, rightFamilySpinner_;
    private JCheckBox forwardCheckBox_;
    private JCheckBox backwardCheckBox_;
    private static Integer currentOrbitDirection_=OrbitGeom.FORWARD_DIR;
    private static JToggleButton addLastGeometryButton_;
    private static ToggleButtonListener buttonListener_;

    public RPnCurvesConfigPanel() {

        buildPanel();
        ChangeDirectionAgent.instance().execute();
        buttonListener_ = new ToggleButtonListener();

    }

    public static void setMultipleButton(boolean state) {
        addLastGeometryButton_.setSelected(state);
        buttonListener_.actionPerformed(new ActionEvent(addLastGeometryButton_, 0, ""));


    }

    private void buildPanel() {
        addLastGeometryButton_ = new JToggleButton("Multiple plot");
        addLastGeometryButton_.addActionListener(new ToggleButtonListener());


        FamilyListener familyListener = new FamilyListener();

        familyLabel_ = new JLabel("Family", SwingConstants.CENTER);
        familyLabel_.setEnabled(false);


        //TODO Qual eh o valor maximo que o JSpinner deve ter ?? Esse valor muda conforme o tipo de curva para a qual ele configura a familia ??
        int family = new Integer(RPNUMERICS.getParamValue("orbit", "family"));
        familySpinner_ = new JSpinner(new SpinnerNumberModel(family, 0, RPNUMERICS.domainDim(), 1));
        familySpinner_.setEnabled(false);
        familySpinner_.addChangeListener(familyListener);

        GridLayout familyPanelGridLayout = new GridLayout(1, 2, 10, 10);

        familyPanel_ = new JPanel(familyPanelGridLayout);
        directionPanel_ = new JPanel(new GridLayout(1, 2));

        forwardCheckBox_ = new JCheckBox();
        forwardCheckBox_.setSelected(true);//Default 

        forwardCheckBox_.setEnabled(false);


        forwardCheckBox_.addActionListener(new OrbitDirectionListener());
        forwardCheckBox_.setText("Forward");

        backwardCheckBox_ = new JCheckBox("Backward");


        backwardCheckBox_.setEnabled(false);
        backwardCheckBox_.addActionListener(new OrbitDirectionListener());

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

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getNewValue().equals("bifurcationcurve")) {//Bifurcation Curves selected
            if (evt.getPropertyName().equals("family")) {

                familySpinner_.setEnabled(false);
                familyLabel_.setEnabled(false);
//                leftFamilyLabel_.setEnabled(true);
//
//                rightFamilyLabel_.setEnabled(true);
//                leftFamilySpinner_.setEnabled(true);
//                rightFamilySpinner_.setEnabled(true);


            }


            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(false);
                backwardCheckBox_.setEnabled(false);
            }
        }

        if (evt.getNewValue().equals("phasediagram")) {//Phase Diagram Curves selected
            if (evt.getPropertyName().equals("family")) {

                familySpinner_.setEnabled(false);
                familyLabel_.setEnabled(false);




            }

            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(false);
                backwardCheckBox_.setEnabled(false);

            }

        }


        if (evt.getNewValue().equals("wavecurve")) {//Wave Curves selected
            if (evt.getPropertyName().equals("family")) {

                familySpinner_.setEnabled(true);
                familyLabel_.setEnabled(true);

            }

            if (evt.getPropertyName().equals("direction")) {
                forwardCheckBox_.setEnabled(true);
                backwardCheckBox_.setEnabled(true);

            }

        }
    }

    private class FamilyListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {

            Integer value = (Integer) familySpinner_.getValue();
            RPNUMERICS.setFamily(value);


        }
    }

    private class ToggleButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JToggleButton button = (JToggleButton) e.getSource();
            if (UIController.instance().getState() instanceof UI_ACTION_SELECTED) {
                UI_ACTION_SELECTED actionSelected = (UI_ACTION_SELECTED) (UIController.instance().getState());
                if (actionSelected.getAction() instanceof RpModelPlotAgent) {
                    RpModelPlotAgent plotAgent = (RpModelPlotAgent) actionSelected.getAction();
                    plotAgent.setMultipleGeometry(!button.isSelected());

                }

            }

        }
    }

    private class OrbitDirectionListener implements ActionListener {

       

        public void actionPerformed(ActionEvent e) {

            JCheckBox checkBox = (JCheckBox) e.getSource();


            if (forwardCheckBox_.isSelected() == false && backwardCheckBox_.isSelected() == false) {
                checkBox.setSelected(true);
                return;
            }


            if (forwardCheckBox_.isSelected() == true && backwardCheckBox_.isSelected() == true) {
                currentOrbitDirection_ = OrbitGeom.BOTH_DIR;

            } else {
                if (forwardCheckBox_.isSelected()) {
                    currentOrbitDirection_ = OrbitGeom.FORWARD_DIR;
                } else {
                    currentOrbitDirection_ = OrbitGeom.BACKWARD_DIR;
                }

            }
            ChangeDirectionAgent.instance().execute();
        }
    }
}


