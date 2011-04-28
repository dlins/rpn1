/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import rpnumerics.RPNUMERICS;

public class RPnFluxParamsDialog extends RPnDialog{

    RPnFluxParamsPanel paramsPanel_;

    public RPnFluxParamsDialog() {
        super(false, true);
        setTitle(RPNUMERICS.physicsID());
        paramsPanel_ = new RPnFluxParamsPanel();

        paramsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply");

        paramsPanel_.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

        paramsPanel_.getActionMap().put("Apply", applyButton.getAction());
        paramsPanel_.getActionMap().put("Cancel", cancelButton.getAction());


        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();


    }

    @Override
    protected void apply() {
        
        paramsPanel_.applyParams();
        dispose();

    }

    protected void begin(){
        getContentPane().remove(paramsPanel_);
        paramsPanel_ = new RPnFluxParamsPanel(RPNUMERICS.physicsID());
        getContentPane().add(paramsPanel_, BorderLayout.CENTER);
        getContentPane().validate();
        
        
    }
    
    
}
