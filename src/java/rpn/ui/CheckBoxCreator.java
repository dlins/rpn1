/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import rpn.configuration.Configuration;
import rpnumerics.RPNUMERICS;

public class CheckBoxCreator extends UIComponentCreator {

    private JCheckBox singularCheckBox_;
    private int currentOrbitDirection_;

    public CheckBoxCreator(Configuration configuration_, String configurationParameter) {
        super(configuration_, configurationParameter);

    }

    @Override
    public JComponent createUIComponent() {


        if (configurationParameter_.equals("singular")) {

            return fillButtonGroup();

        }
        
        if (configurationParameter_.contains("characteristic")||configurationParameter_.contains("sort")){
            ComboBoxCreator combo  = new ComboBoxCreator(configuration_, configurationParameter_);
            return combo.createUIComponent();
        }

        TextFieldCreator textFieldCreator = new TextFieldCreator(configuration_, configurationParameter_);
        return textFieldCreator.createUIComponent();

    }

    private JPanel fillButtonGroup() {

        JPanel directionPanel = new JPanel();

        singularCheckBox_ = new JCheckBox("singular");

        singularCheckBox_.setSelected(true);
        singularCheckBox_.addActionListener(new SingularListener());


        directionPanel.add(singularCheckBox_);


        return directionPanel;


    }

    private class SingularListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            boolean singular = false;
            if (singularCheckBox_.isSelected()) {
                singular = true;
            }

            RPNUMERICS.setParamValue(configuration_.getName(), "singular", String.valueOf(singular));
        }
    }
}
