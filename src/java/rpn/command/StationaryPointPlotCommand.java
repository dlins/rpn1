/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.JToggleButton;
import rpn.configuration.RPnConfig;
import rpn.component.*;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import wave.util.RealVector;

public class StationaryPointPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Stationary Point";

    //
    // Members
    //
    static private StationaryPointPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected StationaryPointPlotCommand() {
        super(DESC_TEXT, RPnConfig.STATPOINT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        
        
//        StationaryPointCalc sPointCalc = RPNUMERICS.createStationaryPointCalc(new PhasePoint(input[input.length-1]));
        
//        return new StationaryPointGeomFactory(sPointCalc).geom();
        return null;
//
//        return new StationaryPointGeomFactory(
//            new rpnumerics.StationaryPointCalc(new PhasePoint(input[input.length - 1]))).geom();
    }


    @Override
    public void actionPerformed(ActionEvent event) {        // NAO ESTA FUNCIONANDO PLENAMENTE !!!!

       JToggleButton button = (JToggleButton)getContainer();


       List<StationaryPointGeom> list = ((NumConfigReadyImpl) RPnDataModule.PHASESPACE.state()).getStationaryPointGeomList();
       Iterator<StationaryPointGeom> geomList = list.iterator();

       System.out.println("list.size() :::::::::::::::::: " +list.size());

       while (geomList.hasNext()) {
           geomList.next().viewingAttr().setVisible(button.isSelected());

       }

       UIController.instance().panelsUpdate();


    }


    static public StationaryPointPlotCommand instance() {
        if (instance_ == null)
            instance_ = new StationaryPointPlotCommand();
        return instance_;
    }
}
