/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.*;
import rpnumerics.RPNUMERICS;

public class RPnFluxParamsDialog extends RPnDialog{

    RPnFluxParamsPanel paramsPanel_;

    public RPnFluxParamsDialog() {
        super(true, true);
        setTitle(RPNUMERICS.physicsID());
        paramsPanel_ = new RPnFluxParamsPanel();
        this.getContentPane().add(paramsPanel_, BorderLayout.CENTER);

        pack();


    }

    @Override
    protected void apply() {
        
        paramsPanel_.applyParams();
        dispose();

    }
    @Override
    protected void begin(){
        getContentPane().remove(paramsPanel_);
        paramsPanel_ = new RPnFluxParamsPanel(RPNUMERICS.physicsID());
        getContentPane().add(paramsPanel_, BorderLayout.CENTER);
        getContentPane().validate();
        
        
    }
    
    
}
