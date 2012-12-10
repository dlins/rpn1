/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import javax.swing.event.ChangeEvent;
import wave.util.RealVector;
import rpn.parser.RPnDataModule;
import rpn.component.RpGeometry;
import javax.swing.Action;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.util.Iterator;
import javax.swing.AbstractButton;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.controller.ui.*;
import rpnumerics.RpCalculation;
import rpnumerics.RpCurve;

public abstract class RpModelPlotCommand extends RpModelActionCommand {

    protected AbstractButton button_;
    private int idCounter_;

    public RpModelPlotCommand(String shortDesc, ImageIcon icon, AbstractButton button) {
        super(shortDesc, icon);
        idCounter_ = 0;
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

        Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();

        RpGeometry geometry = createRpGeometry(userInputList);
        idCounter_++;

        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) geometry.geomFactory();


        RpCurve curve = (RpCurve) factory.geomSource();


        if (geometry == null) {
            return;
        }

        curve.setId(idCounter_);

        UIController.instance().getActivePhaseSpace().plot(geometry);

        event_ = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, geometry);


        ArrayList<RealVector> inputArray = new ArrayList<RealVector>();
        inputArray.addAll(Arrays.asList(userInputList));

        setInput(inputArray);

        logCommand(this);
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

    protected void unselectedButton(ChangeEvent changeEvent) {
    }

    @Override
    public String toXML() {
        RpGeometry geometry = (RpGeometry) event_.getNewValue();
        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) geometry.geomFactory();

        RpCalculation calc = (RpCalculation) factory.rpCalc();

        StringBuilder buffer = new StringBuilder();

        String className = calc.getClass().getSimpleName().toLowerCase();

        String curveName = className.replace("calc", "");
        buffer.append("<COMMAND name=\"").append(curveName).append("\"");
        buffer.append("phasespace=\"").append(UIController.instance().getActivePhaseSpace().getName());

        buffer.append("\"/>\n");

        for (RealVector inputPoint : getInput()) {
            buffer.append(inputPoint.toXML());
        }



        String configurationXML = calc.getConfiguration().toXML();

        buffer.append(configurationXML);

        buffer.append("</COMMAND>\n");

        return buffer.toString();


    }
}
