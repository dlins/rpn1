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
import java.util.Iterator;
import javax.swing.AbstractButton;
import rpn.RPnCurvesListFrame;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.controller.ui.*;

public abstract class RpModelPlotAgent extends RpModelActionAgent {

    static public final String PHASESPACE_LIST = "Phase Space list of elements";
    static public final String AUXPHASESPACE_LIST = "Auxiliary Phase Space list of elements";
    private boolean addOnlyLastGeometry_;
    private static boolean keepLastGeometry_;
    private AbstractButton button_;

    public RpModelPlotAgent(String shortDesc, ImageIcon icon, AbstractButton button) {
        super(shortDesc, icon);
        button_ = button;
        button_.setAction(this);
        button_.setToolTipText(shortDesc);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        putValue(Action.SHORT_DESCRIPTION, shortDesc);
        setEnabled(false);
        addOnlyLastGeometry_ = true;
        keepLastGeometry_ = true;

    }

    public void execute() {
        RealVector[] userInputList = UIController.instance().userInputList();
        RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;

        String listString = "";
        // selecting phase space

        // stores the scene
        Iterator oldValue = phaseSpace.getGeomObjIterator();

        RpGeometry geometry = createRpGeometry(userInputList);

      

//        if (geometry instanceof CoincidenceCurveGeom || geometry instanceof SubInflectionCurveGeom){
//            keepLastGeometry_=true;
//        }

        if (geometry == null) {
            return;
        }
        phaseSpace.plot(geometry);
        if (addOnlyLastGeometry_) {
            if (!keepLastGeometry_) {
                phaseSpace.removeLastGeometry();
                RPnCurvesListFrame.removeLastEntry();
                UIController.instance().removeLastCommand();

            }
        }
//        keepLastGeometry_ = false;
        keepLastGeometry_ = true;
        Iterator newValue = phaseSpace.getGeomObjIterator();
        logAction(new PropertyChangeEvent(this, listString, oldValue, newValue));

    }

    public void setMultipleGeometry(boolean multiple) {
        addOnlyLastGeometry_ = multiple;
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

    static void keepLastGeometry() {
        keepLastGeometry_ = true;

    }
}
