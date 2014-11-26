/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import wave.util.RealVector;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;

public class PointMarkCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Point Mark";
    static private PointMarkCommand instance_ = null;

    private PointMarkCommand() {
        super(DESC_TEXT, null, new JToggleButton());

        setEnabled(true);
    }

    public static PointMarkCommand instance() {
        if (instance_ == null) {
            instance_ = new PointMarkCommand();
        }
        return instance_;
    }

    @Override
    public void execute() {

        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();
        CoordsArray pointCoords = new CoordsArray(UIController.instance().userInputList()[0]);

        while (installedPanelsIterator.hasNext()) {

            List<Object> wcObjectList = new ArrayList<Object>();

            wcObjectList.add(pointCoords);

            RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();

            rpn.component.util.Point point = new rpn.component.util.Point(wcObjectList, rPnPhaseSpacePanel.scene().getViewingTransform(), new ViewingAttr(Color.white));
            
            RiemannProfileCommand.instance().getState().select(point);
            
            rPnPhaseSpacePanel.addGraphicUtil(point);

            rPnPhaseSpacePanel.repaint();
        }

    }

    @Override
    public void unexecute() {
      
    }
    
    
    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {

        return null;

    }

}
