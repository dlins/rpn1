/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnUIFrame;
import rpn.component.BifurcationCurveGeom;
import rpn.component.BifurcationCurveView;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.PhaseSpacePanel2DController;
import rpn.usecase.TrackPointAgent;
import rpnumerics.BifurcationCurve;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.model.AbstractGeomObj;
import wave.multid.view.GeomObjView;
import wave.util.RealSegment;
import wave.util.RealVector;

public class TRACKPOINT_CONFIG extends UI_ACTION_SELECTED {

    public TRACKPOINT_CONFIG() {

        super(TrackPointAgent.instance());

        System.out.println("Construtor de TRACK_CONFIG");
        PhaseSpacePanel2DController.track = true;

    }

    public void trackPoint(RPnPhaseSpacePanel panel, Coords2D dcCoords) {

//        Iterator geometryIterator = panel.scene().geometries();
//
//        int segmentIndex = 0;
//
//
//        double height = panel.scene().getViewingTransform().viewPlane().getWindow().getHeight();
//
//        double width = panel.scene().getViewingTransform().viewPlane().getWindow().getWidth();
//
//        double distance = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
//
//
//        SegmentedCurve segCurve = null;
//
//        while (geometryIterator.hasNext()) {
//
//            CoordsArray worldCoords = new CoordsArray(new Space("", 2));
//            panel.scene().getViewingTransform().dcInverseTransform(dcCoords, worldCoords);
//
//            GeomObjView geomView = (GeomObjView) geometryIterator.next();
//
//            if (geomView.getViewingAttr().isVisible()) {
//                AbstractGeomObj absGeom = geomView.getAbstractGeom();
//
//                RpGeometry rpGeometry = (RpGeometry) absGeom;
//
//                SegmentedCurve curve = (SegmentedCurve) rpGeometry.geomFactory().geomSource();
//
//                RealVector worldCoordsVector = new RealVector(worldCoords.getCoords());
//
//                double actualDistance = curve.getClosestDistance(worldCoordsVector);
//
//                if (actualDistance < distance) {
//
//                    segmentIndex = curve.findClosestSegment(worldCoordsVector, 0);
//
//                    segCurve = curve;
//
//                    distance = actualDistance;
//
//                }
//
//
//
//            }


        }

//        Coords2D coords2D = new Coords2D();
//
//        RealVector segmentPoint = ((RealSegment) segCurve.segments().get(segmentIndex)).p1();
//
//        CoordsArray worldCoords = new CoordsArray(segmentPoint);
//
//        panel.scene().getViewingTransform().viewPlaneTransform(worldCoords, coords2D);
//
//        Double X = new Double(coords2D.getElement(0));
//        Double Y = new Double(coords2D.getElement(1));
//
//        Point point2D = new Point(X.intValue(), Y.intValue());
//
//        panel.setTrackedPoint(point2D);
//
//        panel.repaint();
//


//        RealVector worldPoint = new RealVector(worldCoords.getCoords());
//
//        Iterator iterator = panel.scene().geometries();
//
//        while (iterator.hasNext()) {
//
//            GeomObjView objectView = (GeomObjView) iterator.next();
//
//            if (objectView instanceof BifurcationCurveView) {
//                BifurcationCurveView curveView = (BifurcationCurveView) objectView;
//                BifurcationCurveGeom curveGeom = (BifurcationCurveGeom) curveView.getAbstractGeom();
//                RpGeomFactory factory = curveGeom.geomFactory();
//
//                RPnCurve geometry = (RPnCurve) factory.geomSource();
//
//                BifurcationCurve bifurcationCurve = (BifurcationCurve) geometry;
//
//                ArrayList<RealSegment> tempVerticesArrayList = new ArrayList<RealSegment>();
//
//                for (int i = 0; i < bifurcationCurve.segments().size(); i++) {
//
//                    RealSegment bifurcationSegment = ((RealSegment) bifurcationCurve.segments().get(i));
//
//                    RealVector bifurcationVertex = bifurcationSegment.p1();
//
//                    double[] tempArray = new double[2];
//
//                    tempArray[0] = bifurcationVertex.getElement(indices[0]);
//                    tempArray[1] = bifurcationVertex.getElement(indices[1]);
//
//                    RealVector tempVertex1 = new RealVector(tempArray);
//
//                    double[] tempArray2 = new double[2];
//
//
//                    RealVector bifurcationVertex2 = bifurcationSegment.p2();
//
//                    tempArray2[0] = bifurcationVertex2.getElement(indices[0]);
//                    tempArray2[1] = bifurcationVertex2.getElement(indices[1]);
//
//                    RealVector tempVertex2 = new RealVector(tempArray2);
//
//                    RealSegment tempSegment = new RealSegment(tempVertex1, tempVertex2);
//
//                    tempVerticesArrayList.add(tempSegment);
//
//                }


//                BifurcationCurve tempCurve = new BifurcationCurve(bifurcationCurve.getFamilyIndex(), tempVerticesArrayList);
//                RealVector worldPoint2D = new RealVector(2);
//                worldPoint2D.setElement(0, worldPoint.getElement(indices[0]));
//                worldPoint2D.setElement(1, worldPoint.getElement(indices[1]));
//                RealVector point = bifurcationCurve.projectionCurve(tempCurve, worldPoint2D);

//                trackedPoint(point);

            
        
//    }

    private void trackedPoint(RealVector nDPoint) {




    }
}







