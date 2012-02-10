/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.OrbitGeomView;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeFluxParamsAgent;
import rpn.usecase.ChangeOrbitLevel;
import wave.multid.view.GeomObjView;

public class OrbitController implements RpController {
    //
    // Members
    //

    RpGeomFactory factory_;

    public void install(RpGeomFactory geom) {
        factory_ = geom;
        ChangeOrbitLevel.instance().addPropertyChangeListener(this);
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);

    }

    public void uninstall(RpGeomFactory geom) {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeOrbitLevel.instance().removePropertyChangeListener(this);
        factory_ = null;

    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("level")) {//Visual update only


            RPnPhaseSpaceFrame[] frames = RPnUIFrame.getPhaseSpaceFrames();

            for (int i = 0; i < frames.length; i++) {

                Iterator it = frames[i].phaseSpacePanel().scene().geometries();

                while (it.hasNext()) {

                    GeomObjView geometryView = (GeomObjView)it.next();

                    if ( geometryView instanceof OrbitGeomView) {
                            geometryView.update();
                      }

                }
                RPnDataModule.PHASESPACE.update();
            }

        } else {
            System.out.println("Atualizando geometria");
            factory_.updateGeom();
        }



    }
}
