/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import wave.util.RealVector;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import rpn.RPnConfig;
import rpn.RPnCorey;
import rpn.RPnCoreyBrooks;
import rpn.RPnCoreyToStone;
import rpn.RPnDefaultParamsFluxSubject;
import rpn.RPnFluxParamsDialog;
import rpn.RPnFluxParamsSubject;
import rpn.RPnPalmeira;
import rpn.RPnSchearerSchaeffer;
import rpnumerics.Configuration;
import rpnumerics.RPNUMERICS;
import rpn.RPnFluxParamsObserver;
import rpn.RPnInputComponent;
import rpn.RPnRadioButtonToStone;
import rpn.RPnStoneToStone;

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

        RPnFluxParamsSubject[] fluxParamsSubject = RPnConfig.getFluxParamsSubject();
        RPnFluxParamsObserver fluxParamsObserver = RPnConfig.getFluxParamsObserver();

        RPnFluxParamsDialog dialog = new RPnFluxParamsDialog(fluxParamsSubject, fluxParamsObserver);
        dialog.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("Pronto pra chamar execute()");
        execute();
    }

    static public ChangeFluxParamsAgent instance() {
        if (instance_ == null)
            instance_ = new ChangeFluxParamsAgent();
        return instance_;
    }
}
