/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnLeftPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.RPnCurve;
import wave.multid.view.GeomObjView;
import wave.util.RealSegment;

public class CurveRefine extends RpModelConfigChangeAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Refine";
    //
    // Members
    //
    private static CurveRefine instance_ = null;

    //
    // Constructors
    //
    protected CurveRefine() {
        super(DESC_TEXT);
    }

    public void execute() {

        


        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            List<Polygon> areasList = panel.getCastedUI().getSelectionAreas();
            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction)panel.scene().getAbstractGeom();

            for (Polygon polygon : areasList) {
                Iterator geomIterator = panel.scene().geometries();

                while (geomIterator.hasNext()) {
                    GeomObjView geomObjView = (GeomObjView) geomIterator.next();

                    RpGeometry rpGeometry = (RpGeometry) geomObjView.getAbstractGeom();

                    if (rpGeometry==phaseSpace.getSelectedGeom()) {
                        List<Integer> segmentIndex = geomObjView.contains(polygon);
                        RPnCurve curve = (RPnCurve) rpGeometry.geomFactory().geomSource();
                        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) rpGeometry.geomFactory();
                        List<RealSegment> segRem = new ArrayList<RealSegment>();
                        for (Integer i : segmentIndex) {
                            segRem.add(curve.segments().get(i));
                        }

                        curve.segments().removeAll(segRem);

                        factory.updateGeom();

                        RPnDataModule.PHASESPACE.update();
                        RPnDataModule.LEFTPHASESPACE.update();
                        RPnDataModule.RIGHTPHASESPACE.update();
                    }


                }
            }





        }
//        while (iterator.hasNext()) {
//
//            RPnPhaseSpacePanel panel = iterator.next();
//
//            int testeSize= panel.getCastedUI().getSelectionAreas().size();
//            System.out.println("Quantidade de areas: " +testeSize);
//            
//            List<Polygon> areasList = panel.getCastedUI().getSelectionAreas();
//
//            Iterator geomIterator = panel.scene().geometries();
//
//            while (geomIterator.hasNext()) {
//                GeomObjView geomObjView = (GeomObjView) geomIterator.next();
//                for (Polygon polygon : areasList) {
//                    RpGeometry rpGeometry = (RpGeometry) geomObjView.getAbstractGeom();
//                    System.out.println(polygon);
////                    if (rpGeometry == phaseSpace_.getSelectedGeom()) {
//                        List<Integer> segmentIndex = geomObjView.contains(polygon);
//
////                        RPnCurve curve = (RPnCurve) rpGeometry.geomFactory().geomSource();
////                        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) rpGeometry.geomFactory();
////                        List<RealSegment> segRem = new ArrayList<RealSegment>();
////
////                        for (Integer i : segmentIndex) {
////                            segRem.add(curve.segments().get(i));
////                        }
////
////
////                        curve.segments().removeAll(segRem);
////
////                        factory.updateGeom();
////
////                        RPnDataModule.PHASESPACE.update();
////                        RPnDataModule.LEFTPHASESPACE.update();
////                        RPnDataModule.RIGHTPHASESPACE.update();
//
//
////                    }
//
//
//                }
//            }
//
//        }
//
////
////            for (Polygon polygon : areasList) {
////                
////              
////                RPnCurve curve = null;
////                RpCalcBasedGeomFactory factory = null;
////
//
////
////                while (geomIterator.hasNext()) {
////                    GeomObjView geomObjView = (GeomObjView) geomIterator.next();
////
////                    List<Integer> segmentIndex = geomObjView.contains(polygon);
////
////                    System.out.println(geomObjView + " " + segmentIndex.size());
////
////                    RpGeometry rpGeometry = (RpGeometry) geomObjView.getAbstractGeom();
////
////                    curve = (RPnCurve) rpGeometry.geomFactory().geomSource();
////                    factory = (RpCalcBasedGeomFactory) rpGeometry.geomFactory();
////
////                    for (Integer i : segmentIndex) {
////                        segRem.add(curve.segments().get(i));
////                    }
////                }
////
////                curve.segments().removeAll(segRem);
////
////                factory.updateGeom();
////
////                RPnDataModule.PHASESPACE.update();
////
////            }
////
////




//        applyChange(new PropertyChangeEvent(this, "level", null, null));
    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        Double newValue = (Double) log().getOldValue();
        applyChange(new PropertyChangeEvent(this, "level", oldValue, newValue));
    }

    static public CurveRefine instance() {
        if (instance_ == null) {
            instance_ = new CurveRefine();
        }
        return instance_;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        execute();
    }
}
