/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import wave.util.RealVector;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.awt.Dimension;
import java.awt.Point;
import rpn.RPnFluxParamsDialog;

public class ChangeFluxParamsAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Change Flux Parameters...";
    //
    // Members
    //
    static private ChangeFluxParamsAgent instance_ = null;


    //
    // Constructors
    //
    protected ChangeFluxParamsAgent() {
        super(DESC_TEXT);
    }

    public void unexecute() {
        RealVector newValue = (RealVector)log().getOldValue();
        RealVector oldValue = (RealVector)log().getNewValue();
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    public void execute() {
        
        RPnFluxParamsDialog dialog = new RPnFluxParamsDialog();
        dialog.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        execute();
    }

    static public ChangeFluxParamsAgent instance() {
        if (instance_ == null)
            instance_ = new ChangeFluxParamsAgent();
        return instance_;
    }
}
