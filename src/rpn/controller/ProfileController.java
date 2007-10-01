/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.usecase.*;

import rpnumerics.RPNUMERICS;

import java.beans.PropertyChangeEvent;


public class ProfileController extends RpCalcController {
    
    //
    // Members
    //
    
    //
    // Constructors
    //
    
    //
    // Accessors/Mutators
    //
    
    //
    // Methods
    //
    protected void register() {
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        ChangeXZeroAgent.instance().addPropertyChangeListener(this);
    }
    
    protected void unregister() {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeXZeroAgent.instance().removePropertyChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent change) {
        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0) {
            
            // fires ChangeSigma event...

            
            double oldSigma = RPNUMERICS.getSigma();
            
            super.propertyChange(change);
//
            double newSigma =  RPNUMERICS.getSigma();
            ChangeSigmaAgent.instance().applyChange(              new java.beans.PropertyChangeEvent(this,
                    ChangeSigmaAgent.DESC_TEXT,
                    new Double(oldSigma),
                    new Double(newSigma)));
            
            
            
        }
    }
}
