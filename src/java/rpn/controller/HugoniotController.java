/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.command.*;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.component.HugoniotCurveView;
import rpn.component.OrbitGeomView;
import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.HugoniotParams;
import wave.multid.view.GeomObjView;
import wave.util.RealVector;

public class HugoniotController extends RpCalcController {
    //
    // Members
    //

    private HugoniotCurveGeomFactory geomFactory_;
    //
    // Constructors
    //
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //

    @Override
    protected void register() {
        DragPlotCommand.instance().addPropertyChangeListener(this);
        ChangeFluxParamsCommand.instance().addPropertyChangeListener(this);
        BifurcationRefineCommand.instance().addPropertyChangeListener(this);      // ****
        ChangeXZeroCommand.instance().addPropertyChangeListener(this);
        ChangeDirectionCommand.instance().addPropertyChangeListener(this);


    }

    @Override
    protected void unregister() {
        DragPlotCommand.instance().removePropertyChangeListener(this);
        ChangeFluxParamsCommand.instance().removePropertyChangeListener(this);
        BifurcationRefineCommand.instance().removePropertyChangeListener(this);
        ChangeXZeroCommand.instance().removePropertyChangeListener(this);
        ChangeDirectionCommand.instance().removePropertyChangeListener(this);
    }

    @Override
    public void install(RpGeomFactory geom) {
        super.install(geom);
        geomFactory_ = (HugoniotCurveGeomFactory) geom;
    }

    @Override
    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);
        geomFactory_ = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent change) {
        
        System.out.println(change);

        if (change.getSource() instanceof DragPlotCommand) {
            ((HugoniotParams) ((HugoniotCurveCalcND) geomFactory_.rpCalc()).getParams()).setXZero((RealVector) change.getNewValue());
            geomFactory_.updateGeom();
            return;
        }



        if (change.getSource() instanceof ChangeDirectionCommand) {//Visual update only
            RPnPhaseSpaceFrame[] frames = RPnUIFrame.getPhaseSpaceFrames();

            RPnPhaseSpaceFrame[] auxFrames = RPnUIFrame.getAuxFrames();

            for (int i = 0; i < auxFrames.length; i++) {

                Iterator it = auxFrames[i].phaseSpacePanel().scene().geometries();



                while (it.hasNext()) {

                    GeomObjView geometryView = (GeomObjView) it.next();

                    if (geometryView.getViewingAttr().isSelected() && geometryView instanceof HugoniotCurveView) {
                        geometryView.update();
                    }

                }

            }

            for (int i = 0; i < frames.length; i++) {

                Iterator it = frames[i].phaseSpacePanel().scene().geometries();

                while (it.hasNext()) {

                    GeomObjView geometryView = (GeomObjView) it.next();

                    if (geometryView.getViewingAttr().isSelected() && geometryView instanceof HugoniotCurveView) {
                        geometryView.update();
                    }

                }

            }

            RPnDataModule.updatePhaseSpaces();
            return;
        }

        super.propertyChange(change);
    }
}
