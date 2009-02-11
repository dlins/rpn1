/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.*;
import java.awt.*;
import rpn.controller.ui.BIFURCATION_CONFIG;
import rpn.controller.ui.RAREFACTION_CONFIG;
import rpn.controller.ui.SHOCK_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpnumerics.RPNUMERICS;

public class RPnCurvesConfigDialog extends RPnDialog {

    FlowLayout flowLayout1 = new FlowLayout();
    JPanel curvePanel = new JPanel();
    private JComboBox curveComboBox_;

    public RPnCurvesConfigDialog() {
        

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCurveName() {
        curveComboBox_ = new JComboBox();


        curveComboBox_.addItem("Shock Diagram");
        curveComboBox_.addItem("Rarefaction");
        curveComboBox_.addItem("Bifurcation");

        curvePanel.setLayout(flowLayout1);

        curvePanel.add(new JLabel("Curve"));
        curvePanel.add(curveComboBox_);

    }

    private void jbInit() throws Exception {
        setTitle("Curves Choice");
        addCurveName();

        applyButton.setText("Ok");
        cancelButton.setText("Exit");
        buttonsPanel.remove(beginButton);
        cancelButton.setEnabled(true);


        this.getContentPane().add(curvePanel, BorderLayout.CENTER);
        setModal(false);

        pack();



    }

    @Override
    protected void cancel() {
        int option = JOptionPane.showConfirmDialog(this, "Close aplication", "Exit RPn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            RPNUMERICS.clean();
            System.exit(0);
        }

    }

    protected void apply() {

        dispose();

        UI_ACTION_SELECTED newState = null;

        if (curveComboBox_.getSelectedItem().equals("Shock Diagram")) {

            newState = new SHOCK_CONFIG();

        }

        if (curveComboBox_.getSelectedItem().equals("Rarefaction")) {
            newState = new RAREFACTION_CONFIG();

        }
        if (curveComboBox_.getSelectedItem().equals("Bifurcation")) {
            newState = new BIFURCATION_CONFIG();

        }

        UIController.instance().setState(newState);

    }
}
