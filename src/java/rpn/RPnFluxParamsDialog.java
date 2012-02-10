/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
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
        cancelButton.setText("Close");
        paramsPanel_ = new JPanel();


        Configuration physicsConfiguration = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());


        Configuration fluxFunctionConfig = physicsConfiguration.getConfiguration("fluxfunction");


        RPnInputComponent inputComponent = new RPnInputComponent(fluxFunctionConfig);

        paramsPanel_.add(inputComponent.getContainer());

        paramsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");

        paramsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

        paramsPanel_.getActionMap().put("Apply", beginButton.getAction());
        paramsPanel_.getActionMap().put("Cancel", cancelButton.getAction());

        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();


    }

    @Override
    protected void apply() {
        RPNUMERICS.applyFluxParams();
        rpn.usecase.ChangeFluxParamsAgent.instance().applyChange(new PropertyChangeEvent(rpn.usecase.ChangeFluxParamsAgent.instance(), "", null, RPNUMERICS.getFluxParams()));
//        System.out.println("Chamando apply do flux dialog");
    }

    @Override
    protected void begin() {

        dispose();
    }
}
