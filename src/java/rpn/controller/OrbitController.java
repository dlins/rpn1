/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.OrbitGeomFactory;
import rpn.component.OrbitGeomView;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.StationaryPointGeom;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeFluxParamsAgent;
import rpn.usecase.ChangeOrbitLevel;
import rpn.usecase.ChangeSigmaAgent;
import rpn.usecase.ChangeXZeroAgent;
import rpn.usecase.DragPlotAgent;
import rpnumerics.OrbitCalc;
import wave.multid.view.GeomObjView;
import wave.util.RealVector;

public class OrbitController extends RpCalcController {
    //
    // Members
    //

    OrbitGeomFactory factory_;

    @Override
    public void install(RpGeomFactory geom) {
        factory_ = (OrbitGeomFactory) geom;
        super.install(geom);

    }

    @Override
    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);

    }

    @Override
    protected void register() {
        DragPlotAgent.instance().addPropertyChangeListener(this);
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        ChangeOrbitLevel.instance().addPropertyChangeListener(this);
        ChangeSigmaAgent.instance().addPropertyChangeListener(this);
        ChangeXZeroAgent.instance().addPropertyChangeListener(this);


    }

    @Override
    protected void unregister() {
        DragPlotAgent.instance().removePropertyChangeListener(this);
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeOrbitLevel.instance().removePropertyChangeListener(this);
        ChangeSigmaAgent.instance().removePropertyChangeListener(this);
        ChangeXZeroAgent.instance().removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        // ---------------------------------------------------------------------
        // --- Implementar este trecho apenas para RarefactionOrbit
        if (evt.getSource() instanceof ChangeOrbitLevel) {//Visual update only
            RPnPhaseSpaceFrame[] frames = RPnUIFrame.getPhaseSpaceFrames();

            RPnPhaseSpaceFrame[] auxFrames = RPnUIFrame.getAuxFrames();

            for (int i = 0; i < auxFrames.length; i++) {

                Iterator it = auxFrames[i].phaseSpacePanel().scene().geometries();

                while (it.hasNext()) {

                    GeomObjView geometryView = (GeomObjView) it.next();

                    if (geometryView instanceof OrbitGeomView) {
                        geometryView.update();
                    }

                }

            }

            for (int i = 0; i < frames.length; i++) {

                Iterator it = frames[i].phaseSpacePanel().scene().geometries();

                while (it.hasNext()) {

                    GeomObjView geometryView = (GeomObjView) it.next();

                    if (geometryView instanceof OrbitGeomView) {
                        geometryView.update();
                    }

                }

            }

            RPnDataModule.updatePhaseSpaces();
            return;
        }
        // ---------------------------------------------------------------------

        // ---------------------------------------------------------------------
//        if (evt.getSource() instanceof ChangeSigmaAgent) {          // *** Usado para remocao dos pontos de equilibrio
//            Iterator it =RPnDataModule.PHASESPACE.getGeomObjIterator();
//            List<StationaryPointGeom> list = new ArrayList<StationaryPointGeom>();
//
//            while (it.hasNext()) {
//                RpGeometry geometry  = (RpGeometry)it.next();
//
//                if (geometry instanceof StationaryPointGeom)
//                    list.add((StationaryPointGeom) geometry);
//
//            }
//
//            for (StationaryPointGeom stationaryPointGeom : list) {
//                RPnDataModule.PHASESPACE.remove(stationaryPointGeom);
//
//            }
//
//        }
        // ---------------------------------------------------------------------


        if (evt.getSource() instanceof DragPlotAgent) {
            ((OrbitCalc) factory_.rpCalc()).setStart((RealVector) evt.getNewValue());
            factory_.updateGeom();
            return;
        }

        super.propertyChange(evt);


    }
}
