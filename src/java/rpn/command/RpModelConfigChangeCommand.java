/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import rpn.controller.XZeroController;
import java.beans.*;
import java.util.List;
import java.util.ArrayList;
import rpn.controller.ui.*;
import rpn.parser.RPnDataModule;
import wave.util.RealVector;

public abstract class RpModelConfigChangeCommand extends RpModelActionCommand {
    //
    // Members
    //

    private List listenersList_;

    public RpModelConfigChangeCommand(String shortDesc) {
        // no ICONS
        super(shortDesc, null);
        setEnabled(false);
        listenersList_ = new ArrayList();
    }

    public void applyChange(PropertyChangeEvent change) {

        RealVector newParameters = (RealVector) change.getNewValue();

        UIController.instance().logCommand(new RpCommand(new UI_ACTION_SELECTED(this), newParameters));
        firePropertyChange(change);
        logAction(change);
        RPnDataModule.updatePhaseSpaces();
        UIController.instance().panelsUpdate();

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        /*
         * MAKES SURE XZERO IS NOTIFIED FIRST
         */

        if (listener instanceof XZeroController) {
            listenersList_.add(0, listener);
        } else {
            listenersList_.add(listener);
        }
        super.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listenersList_.remove(listener);
    }
    

    public void firePropertyChange(PropertyChangeEvent event) {

        for (int i = 0; i < listenersList_.size(); i++) {
            ((PropertyChangeListener) listenersList_.get(i)).propertyChange(
                    event);

        }
    }
    
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
