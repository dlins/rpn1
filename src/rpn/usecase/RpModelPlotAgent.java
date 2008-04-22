/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import wave.util.RealVector;
import rpn.parser.RPnDataModule;
import rpn.component.RpGeometry;
import javax.swing.Action;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import java.util.Iterator;
import rpn.controller.ui.*;


public abstract class RpModelPlotAgent extends RpModelActionAgent {
    static public final String PHASESPACE_LIST = "Phase Space list of elements";
    private JToggleButton button_;

    public RpModelPlotAgent(String shortDesc, ImageIcon icon) {
        super(shortDesc, icon);
        button_ = new JToggleButton(this);
        button_.setToolTipText(shortDesc);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        putValue(Action.SHORT_DESCRIPTION, shortDesc);
        setEnabled(false);
    }

    public void execute() {
        RealVector[] userInputList = UIController.instance().userInputList();


        // stores the scene
        Iterator oldValue = RPnDataModule.PHASESPACE.getGeomObjIterator();
        RPnDataModule.PHASESPACE.plot(createRpGeometry(userInputList));
        Iterator newValue = RPnDataModule.PHASESPACE.getGeomObjIterator();
        logAction(new PropertyChangeEvent(this, PHASESPACE_LIST, oldValue, newValue));
    }

    public void unexecute() {
        Iterator current = (Iterator)log().getNewValue();
        RpGeometry added = null;
        while (current.hasNext()) {
            added = (RpGeometry)current.next();
        }
        // remove the last...
        RPnDataModule.PHASESPACE.delete(added);
    }

    public abstract RpGeometry createRpGeometry(RealVector[] coords);

    // the container for this Action
    public JToggleButton getContainer() { return button_; }
}
