/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import rpnumerics.Configuration;
import rpnumerics.RPNUMERICS;

public class RPnFluxParamsDialog extends RPnDialog {

    JPanel paramsPanel_;

    public RPnFluxParamsDialog() {
        super(false, true);
        setTitle(RPNUMERICS.physicsID());

        paramsPanel_ = new JPanel();

        HashMap<String, Configuration> configMap = RPNUMERICS.getConfigurations();

        Set<Entry<String, Configuration>> configSet = configMap.entrySet();

        for (Entry<String, Configuration> entry : configSet) {

            System.out.println(entry.getValue().getName());


            String configurationType = entry.getValue().getType();

            if (configurationType.equalsIgnoreCase("PHYSICS")) {
                RPnInputComponent inputComponent = new RPnInputComponent(entry.getValue());
                paramsPanel_.add(inputComponent.getContainer());
            }


        }

        paramsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");

        paramsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

        paramsPanel_.getActionMap().put("Apply", applyButton.getAction());
        paramsPanel_.getActionMap().put("Cancel", cancelButton.getAction());

        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();


    }

    @Override
    protected void apply() {
        RPNUMERICS.applyFluxParams();
        rpn.usecase.ChangeFluxParamsAgent.instance().applyChange(new PropertyChangeEvent(rpn.usecase.ChangeFluxParamsAgent.instance(), "", null, RPNUMERICS.getFluxParams()));
        dispose();

    }

    @Override
    protected void begin() {
    }
}
