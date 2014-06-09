/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
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
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.controller.ui.*;
import rpnumerics.RPnCurve;
import rpn.message.RPnNetworkStatus;

public abstract class RpModelPlotCommand extends RpModelActionCommand implements Observer {

    protected AbstractButton button_;
    public static int curveID_;

    public RpModelPlotCommand(String shortDesc, ImageIcon icon, AbstractButton button) {

        super(shortDesc, icon);
        button_ = button;
        button_.setAction(this);
        
        String [] descriptionArray = shortDesc.split(" ");
        
        
        StringBuilder buttonCaption = new StringBuilder();
        
        buttonCaption.append("<html>");
        
        for (String string : descriptionArray) {
            buttonCaption.append(string);
            buttonCaption.append("<br>");
            
        }
        buttonCaption.append("</html>");
        
        button_.setText(buttonCaption.toString());
        
        
        
//        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
//
//        AffineTransform fontTransform = new AffineTransform();
//
//        fontTransform.scale(1.5, 1.5);
//        Font newFont = button_.getFont().deriveFont(fontTransform);
//        button_.setFont(newFont);

        button_.setToolTipText(shortDesc);

        putValue(Action.SHORT_DESCRIPTION, shortDesc);
        setEnabled(false);


    }
    
    public RpModelPlotCommand(String shortDesc, ImageIcon icon) {

        super(shortDesc, icon);

        putValue(Action.SHORT_DESCRIPTION, shortDesc);
        setEnabled(false);


    }
    

    public void execute() {

        //RealVector[] userInputList = UIController.instance().userInputList();
        RealVector[] userInputList = new RealVector[1];

	// TODO : retornar o codigo acima... mvera.
	userInputList[0] = UIController.instance().globalInputTable().values();


        Iterator oldValue = UIController.instance().getActivePhaseSpace().getGeomObjIterator();

        RpGeometry geometry = createRpGeometry(userInputList);



        if (geometry == null) {
            return;
        }

        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) geometry.geomFactory();

        RPnCurve curve = (RPnCurve) factory.geomSource();
         

        curve.setId(curveID_);
        curveID_++;

        PropertyChangeEvent event_ = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, geometry);

        ArrayList<RealVector> inputArray = new ArrayList<RealVector>();
        inputArray.addAll(Arrays.asList(userInputList));

        setInput(inputArray);

        logCommand(new RpCommand(event_, inputArray));

        RPnDataModule.PHASESPACE.join(geometry);

        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
            RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
        }

    }

    public void execute(RpGeomFactory factory) {

        RPnCurve curve = (RPnCurve) factory.geomSource();

        curve.setId(curveID_);
        curveID_++;

        Iterator oldValue = RPnDataModule.PHASESPACE.getGeomObjIterator();

        PropertyChangeEvent event = new PropertyChangeEvent(this,
                UIController.instance().getActivePhaseSpace().getName(),
                oldValue,
                factory.geom());

        ArrayList<RealVector> emptyInput = new ArrayList<RealVector>();
        logCommand(new RpCommand(event, emptyInput));

        RPnDataModule.PHASESPACE.join(factory.geom());

        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
            RPnNetworkStatus.instance().sendCommand(rpn.controller.ui.UndoActionController.instance().getLastCommand().toXML());
        }
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

    @Override
    public void actionPerformed(ActionEvent event) {

	super.actionPerformed(event);

	if (UIController.instance().globalInputTable().isComplete()) {

		execute();
		UIController.instance().panelsUpdate();
	}

    }

    public abstract RpGeometry createRpGeometry(RealVector[] coords);

    // the container for this Action
    public AbstractButton getContainer() {
        return button_;
    }

    protected void unselectedButton(ChangeEvent changeEvent) {
    }

    @Override
    public void update(Observable o, Object arg) {



        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();
        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();


            MouseMotionListener[] mouseMotionArray = (MouseMotionListener[]) panel.getListeners(MouseMotionListener.class);
            MouseListener[] mouseListenerArray = (MouseListener[]) panel.getListeners(MouseListener.class);

            for (MouseListener mouseListener : mouseListenerArray) {

                if (mouseListener instanceof RPn2DMouseController) {
                    panel.removeMouseListener(mouseListener);

                }
            }

            for (MouseMotionListener mouseMotionListener : mouseMotionArray) {

                if (mouseMotionListener instanceof RPn2DMouseController) {
                    panel.removeMouseMotionListener(mouseMotionListener);
                }

            }

        }

    }
}
