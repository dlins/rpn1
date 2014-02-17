/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RpGeometry;
import rpn.component.SegmentedCurveGeom;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.ZoomingSquare;
import wave.util.RealVector;

public class ZoomingAreaCommand extends RpModelPlotCommand implements Observer {

    static public final String DESC_TEXT = "Zoom";
    static private ZoomingAreaCommand instance_ = null;

    private ZoomingAreaCommand() {
        super(DESC_TEXT, null, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

         UIController.instance().setState(new AREASELECTION_CONFIG(this));

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            ZoomingSquare boxPlotter = new ZoomingSquare();
            panel.addMouseListener(boxPlotter);
            panel.addMouseMotionListener(boxPlotter);
        }

        
        
        
        
        
        
        
        
        
        
        
       
    }

    public static ZoomingAreaCommand instance() {
        if (instance_ == null) {
            instance_ = new ZoomingAreaCommand();
        }
        return instance_;
    }

    @Override
    public void unexecute() {
    }

    @Override
    public void execute() {
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            List<RpGeometry> geometryList = ((List<RpGeometry>) arg);
            if (geometryList.isEmpty() || geometryList.size() != 1) {
                setEnabled(false);
                CurveRefineCommand.instance().setEnabled(false);
            } else {

                if (geometryList.get(0) instanceof SegmentedCurveGeom || geometryList.get(0) instanceof BifurcationCurveGeom) {    // -----
                    setEnabled(true);
                } else {
                    setEnabled(false);
                    CurveRefineCommand.instance().setEnabled(false);
                }
            }

        }
    }
}
