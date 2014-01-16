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
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.*;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.ClosestPointPlotter;
import rpn.controller.ui.UIController;
import rpnumerics.*;
import wave.util.RealVector;

public class ClosestPointPlotterTesteCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Closest Point Test";
    //
    // Members
    //
    static private ClosestPointPlotterTesteCommand instance_ = null;
    private RpGeometry geometry_;

    //
    // Constructors/Initializers
    //
    protected ClosestPointPlotterTesteCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JToggleButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new AREASELECTION_CONFIG(this));

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();

            ClosestPointPlotter boxPlotter = new ClosestPointPlotter(geometry_);
            panel.addMouseListener(boxPlotter);
            panel.addMouseMotionListener(boxPlotter);
        }

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());
        return factory.geom();

    }

    @Override
    public void execute() {


        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());
        execute(factory);
    }

    static public ClosestPointPlotterTesteCommand instance() {
        if (instance_ == null) {
            instance_ = new ClosestPointPlotterTesteCommand();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {

        List<RpGeometry> selectedGeometries = (List<RpGeometry>) arg;

        if (selectedGeometries != null && !selectedGeometries.isEmpty()) {

            if (selectedGeometries.size() == 1) {
                geometry_ = selectedGeometries.get(0);

            
                



            } else {
                System.out.println("Select only one curve");
            }

        }

    }

    
}
