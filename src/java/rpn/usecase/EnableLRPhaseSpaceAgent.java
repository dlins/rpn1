/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;

public class EnableLRPhaseSpaceAgent extends RpModelConfigChangeAgent implements Observer {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Enable/Disable L R Phase Space";
    //
    // Members
    //
    private static EnableLRPhaseSpaceAgent instance_ = null;
    private List<RpGeometry> selectedList_;

    //
    // Constructors
    //
    protected EnableLRPhaseSpaceAgent() {
        super(DESC_TEXT);
        setEnabled(true);
        selectedList_ = new ArrayList();

    }

    public void actionPerformed(ActionEvent event) {
        boolean LRPhaseSpaceEnable = UIController.instance().isAuxPanelsEnabled();

        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getAuxFrames()) {
            frame.setVisible(!LRPhaseSpaceEnable);
        }

        if (!LRPhaseSpaceEnable) {
            moveGeometriesToLRPhaseSpace();
        } else {
            moveGeometriesToPhaseSpace();
        }

        UIController.instance().setAuxPanels(!LRPhaseSpaceEnable);

        RPnDataModule.updatePhaseSpaces();


    }

    public void execute() {
    }

    public void unexecute() {
    }

    static public EnableLRPhaseSpaceAgent instance() {
        if (instance_ == null) {
            instance_ = new EnableLRPhaseSpaceAgent();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {
        instance_.selectedList_ = ((List<RpGeometry>) arg);

    }

    private void moveGeometriesToLRPhaseSpace() {



        for (RpGeometry rpGeometry : instance_.selectedList_) {

            if (!(rpGeometry instanceof BifurcationCurveGeom)) {


                RPnDataModule.LEFTPHASESPACE.plot(rpGeometry);
                RPnDataModule.RIGHTPHASESPACE.plot(rpGeometry);




            }

        }




    }

    private void moveGeometriesToPhaseSpace() {
        
//          Iterator<RpGeometry> geomIterator = RPnDataModule.PHASESPACE.getGeomObjIterator();
//
//                while (geomIterator.hasNext()) {
//                    if (!instance_.selectedList_.contains(geomIterator.next())){
//                        RPnDataModule.PHASESPACE.plot(rpGeometry);
//                    }
//
//                }
//        
//        
//        
//        
//
//        for (RpGeometry rpGeometry : instance_.selectedList_) {
//
//            if (!(rpGeometry instanceof BifurcationCurveGeom)) {
//
//                RPnDataModule.LEFTPHASESPACE.remove(rpGeometry);
//                RPnDataModule.RIGHTPHASESPACE.remove(rpGeometry);
//
//              
//            }
//        }
    }
}
