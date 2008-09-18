/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.ConservationShockFlow;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.awt.Dimension;
import java.awt.Point;

public class ChangeFluxParamsAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Change Flux Parameters...";
    //
    // Members
    //
    static private ChangeFluxParamsAgent instance_ = null;
    private JDialog dialog_;

    //
    // Constructors
    //
    protected ChangeFluxParamsAgent() {
        super(DESC_TEXT);
        if (rpnumerics.RPNUMERICS.physicsID().startsWith("Quad"))
            dialog_ = new rpn.RPnQuadParamsDialog();
        else
            dialog_ = new rpn.RPnFluxParamsDialog();
    }

    public void unexecute() {
        RealVector newValue = (RealVector)log().getOldValue();
        RealVector oldValue = (RealVector)log().getNewValue();
//        rpnumerics.RPNUMERICS.fluxFunction().fluxParams().setParams(newValue);
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    public void execute() {
        Dimension dlgSize = dialog_.getPreferredSize();
        Dimension frmSize = new Dimension(1280, 1024);
        Point loc = new Point(0, 0);
        dialog_.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dialog_.setModal(false);
        dialog_.pack();
        dialog_.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        execute();
    }

    static public ChangeFluxParamsAgent instance() {
        if (instance_ == null)
            instance_ = new ChangeFluxParamsAgent();
        return instance_;
    }
}
