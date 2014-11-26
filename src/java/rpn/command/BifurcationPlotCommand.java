/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;


import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import rpn.RPnDesktopPlotter;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.*;

public class BifurcationPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Bifurcation Curve";
    // Members
    //
    static private BifurcationPlotCommand instance_ = null;
    //
    // Constructors/Initializers
    //

    protected BifurcationPlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton());
    }

    protected BifurcationPlotCommand(String shortDesc, ImageIcon icon, AbstractButton button) {
        super(shortDesc,icon,button);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);


        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        System.out.println("Calling createGeometry from BifurcationPlotAgent");

        ContourCurveCalc curveCalc = RPNUMERICS.createBifurcationCalc();
        BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(curveCalc);
        BifurcationRefineCommand.instance().setEnabled(true);


        return factory.geom();
    }

    @Override
    public void execute(RpGeomFactory factory) {


        
        try{
            
            super.execute(factory);
        
        BifurcationCurveGeomFactory bifurcationFactory = (BifurcationCurveGeomFactory)factory;
        
        BifurcationCurveGeom bifurcationGeom = (BifurcationCurveGeom) bifurcationFactory.geom();
        List<BifurcationCurveBranchGeom> bifurcationListGeom = bifurcationGeom.getBifurcationListGeom();
        

        RPnDataModule.LEFTPHASESPACE.join(bifurcationListGeom.get(0));
        RPnDataModule.RIGHTPHASESPACE.join(bifurcationListGeom.get(1));
//        
        
        
         } catch (NullPointerException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(new RpException("Error in native layer"));

        }

        
        
 
    }

    
    static public BifurcationPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new BifurcationPlotCommand();
        }
        return instance_;
    }
}
