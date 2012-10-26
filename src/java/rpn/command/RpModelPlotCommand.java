/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import javax.swing.event.ChangeEvent;
import wave.util.RealVector;
import rpn.parser.RPnDataModule;
import rpn.component.RpGeometry;
import javax.swing.Action;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import java.util.Iterator;
import javax.swing.AbstractButton;
import rpn.controller.ui.*;

public abstract class RpModelPlotCommand extends RpModelActionCommand {

    protected AbstractButton button_;

    public RpModelPlotCommand(String shortDesc, ImageIcon icon, AbstractButton button) {
        super(shortDesc, icon);
        button_ = button;
        button_.setAction(this);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);

        AffineTransform fontTransform = new AffineTransform();

        fontTransform.scale(1.2, 1.2);
        Font newFont = button_.getFont().deriveFont(fontTransform);
        button_.setFont(newFont);

        button_.setToolTipText(shortDesc);

        putValue(Action.SHORT_DESCRIPTION, shortDesc);
        setEnabled(false);


    }

    public void execute() {

        RealVector[] userInputList = UIController.instance().userInputList();

        String listString = "";

        Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();

        RpGeometry geometry = createRpGeometry(userInputList);


        if (geometry == null) {
            return;
        }
        UIController.instance().getActivePhaseSpace().plot(geometry);


        Iterator newValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
        logAction(new PropertyChangeEvent(this, listString, oldValue, newValue));

    }

    public void unexecute() {
        Iterator current = (Iterator) log().getNewValue();
        RpGeometry added = null;
        while (current.hasNext()) {
            added = (RpGeometry) current.next();
        }
        // remove the last...
        RPnDataModule.PHASESPACE.delete(added);
    }

    public abstract RpGeometry createRpGeometry(RealVector[] coords);

    // the container for this Action
    public AbstractButton getContainer() {
        return button_;
    }
    
    protected void unselectedButton(ChangeEvent changeEvent){
        
    }

    
}
