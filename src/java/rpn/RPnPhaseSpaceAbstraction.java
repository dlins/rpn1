/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import rpn.controller.phasespace.*;
import wave.multid.model.AbstractScene;
import wave.multid.Space;
import rpnumerics.*;
import rpn.component.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import rpn.controller.ui.UIController;
import wave.multid.model.MultiGeometry;
import wave.multid.model.MultiPolyLine;
import wave.util.RealVector;

public class RPnPhaseSpaceAbstraction extends AbstractScene {
    //
    // Constants
    //
    //
    // Members
    //

    private PhaseSpaceState state_;
    private RpGeometry selectedGeom_;
    private ArrayList<ArrayList> groupArrayList_;

    //
    // Constructors
    //
    public RPnPhaseSpaceAbstraction(String id, Space domain, PhaseSpaceState state) {
        super(id, domain);
        changeState(state);
        selectedGeom_ = null;
//        groupArrayList_ = new ArrayList<ArrayList>();
    }

    //
    // Accessors/Mutators
    //
    public void changeState(PhaseSpaceState state) {
        state_ = state;
    }

    /**
     *@todo fix this... shouldn't have access to state_
     *
     */
    public PhaseSpaceState state() {
        return state_;
    }

    //
    // Methods
    //
    public void plot(RpGeometry geom) {

        state_.plot(this, geom);

    }

    public void delete(RpGeometry geom) {
        state_.delete(this, geom);
    }

    public void removeLastGeometry() {
        if (super.geomList_.size() >= 2) {
            MultiGeometry toBeRemoved = (MultiGeometry) super.geomList_.get(geomList_.size() - 2);
            remove(toBeRemoved);
            update();
        }

    }

    @Override
    public void join(MultiGeometry geom) {

        if (geom == null) {
            return;
        }
//        if (geom instanceof HugoniotCurveGeom) {
//            ArrayList activeList = new ArrayList();
//            groupArrayList_.add(activeList);
//            super.geomList_ = activeList;
//
//        }
        super.join(geom);

        RPnCurvesListFrame.addGeometry((geomList_.size() - 1) + "", geom.getClass().getSimpleName());
    }

    @Override
    public void remove(MultiGeometry geom) {

        super.remove(geom);
        RPnCurvesListFrame.removeGeometry(geom.toString());
//        for (int i = 0; i < groupArrayList_.size(); i++) {
//
//            ArrayList list = groupArrayList_.get(i);
//
//            if (list.size() == 0) {
//                groupArrayList_.remove(i);
//            }
//        }
    }

    public void clearGeometrySelection() {
        for (int i = 0; i < geomList_.size(); i++) {
            highlightGeometry(i);
        }
    }

    public void highlightGeometry(int index) {
        for (int i = 0; i < geomList_.size(); i++) {

            if (i == index) {
                MultiGeometry geometry = (MultiGeometry) geomList_.get(i);
                if (geometry instanceof SegmentedCurveGeom) {

                    SegmentedCurveGeom segGeom = (SegmentedCurveGeom) geometry;
                    segGeom.highLight();


                } else {

                    if (geometry instanceof MultiPolyLine) {

                        MultiPolyLine poly = (MultiPolyLine) geometry;
                        poly.highLight();

                    }
                }
            }
        }

        UIController.instance().panelsUpdate();
    }

    public void hideGeometry(int index) {

        for (int i = 0; i < geomList_.size(); i++) {

            if (i == index) {
                MultiGeometry geometry = (MultiGeometry) geomList_.get(i);
                geometry.viewingAttr().setVisible(false);


            }
        }

        UIController.instance().panelsUpdate();

    }

    public void displayGeometry(int index) {
        for (int i = 0; i < geomList_.size(); i++) {

            if (i == index) {
                MultiGeometry geometry = (MultiGeometry) geomList_.get(i);
                geometry.viewingAttr().setVisible(true);
            }

        }

        UIController.instance().panelsUpdate();


    }

    public void lowlightGeometry(int index) {


        for (int i = 0; i < geomList_.size(); i++) {

            if (i == index) {
                MultiGeometry geometry = (MultiGeometry) geomList_.get(i);
                if (geometry instanceof SegmentedCurveGeom) {

                    SegmentedCurveGeom segGeom = (SegmentedCurveGeom) geometry;
                    segGeom.lowLight();


                } else {

                    if (geometry instanceof MultiPolyLine) {

                        MultiPolyLine poly = (MultiPolyLine) geometry;
                        poly.lowLight();

                    }
                }
            }
        }
        UIController.instance().panelsUpdate();
    }

    @Override
    public void update() {

        Iterator geomList = getGeomObjIterator();

        List removeList =
                new ArrayList();
        List joinList = new ArrayList();
        while (geomList.hasNext()) {
            RpGeometry geom = (RpGeometry) geomList.next();
            RpGeomFactory factory = geom.geomFactory();

            if (factory.isGeomOutOfDate()) {

                removeList.add(geom);
                joinList.add(factory.geom());
                factory.setGeomOutOfDate(false);
            }
        }
        for (int i = 0; i < removeList.size(); i++) {
            super.remove((RpGeometry) removeList.get(i));
        }
        for (int i = 0; i < joinList.size(); i++) {
            super.join((RpGeometry) joinList.get(i));
        }
    }

    public void selectVisibleDirection(int direction) {
        Iterator geomList = super.getGeomObjIterator();

        while (geomList.hasNext()) {
            RpGeometry geom = (RpGeometry) geomList.next();

            if (geom instanceof RarefactionGeom) {//Rarefaction Orbit

                RarefactionOrbitGeomFactory rarefactionFactory = (RarefactionOrbitGeomFactory) geom.geomFactory();
                RarefactionOrbit rarefactionOrbit = (RarefactionOrbit) rarefactionFactory.geomSource();

                if (rarefactionOrbit.getIntegrationFlag() == direction || direction == 0) {
                    geom.viewingAttr().setVisible(true);

                } else {
                    geom.viewingAttr().setVisible(false);
                }
            }

            if (geom instanceof OrbitGeom) {//Orbit
                OrbitGeomFactory orbitFactory = (OrbitGeomFactory) geom.geomFactory();
                Orbit orbit = (Orbit) orbitFactory.geomSource();
                if (orbit.getIntegrationFlag() == direction || direction == 0) {
                    geom.viewingAttr().setVisible(true);
                } else {
                    geom.viewingAttr().setVisible(false);
                }

            }
        }
    }
    // overwriting so we don't remove the last Hugoniot

    @Override
    public void clear() {

        ArrayList deleteList = new ArrayList();
//        for (int i = 0; i < groupArrayList_.size() - 1; i++) {
//            ArrayList list = groupArrayList_.get(i);
//            for (int j = 0; j < list.size(); j++) {
//                deleteList.add((RpGeometry) list.get(j));
//            }
//        }

        Iterator geomList = getGeomObjIterator();
        while (geomList.hasNext()) {
            RpGeometry geom = (RpGeometry) geomList.next();
//            if (!(geom instanceof HugoniotCurveGeom) && !(geom instanceof XZeroGeom)) {
            deleteList.add(geom);
//            }
        }
        for (int i = 0; i < deleteList.size(); i++) {
            delete((RpGeometry) deleteList.get(i));
        }

        if (RPNUMERICS.getCurrentProfile() instanceof ShockProfile) {
            changeState(new NumConfigReadyImpl(((NUMCONFIG_READY) state_).hugoniotGeom(), ((NUMCONFIG_READY) state_).xzeroGeom()));
        }

//        deleteList.clear();
//        deleteList.add(groupArrayList_.get(groupArrayList_.size() - 1));//using deleteList as temporary list
//        groupArrayList_.clear();
//        groupArrayList_.add((ArrayList) deleteList.get(0));
    }

    public RpGeometry getSelectedGeom() {
        return selectedGeom_;
    }

    public void setSelectedGeom(RpGeometry selected) {
        selectedGeom_ = selected;
    }

    //
    // Methods
    //
    public StationaryPointGeom find(RealVector coords) {
        // only searches for StatPointGeom...
        Iterator geomList = getGeomObjIterator();
        StationaryPointGeom closest = null;
        while (geomList.hasNext()) {
            RpGeometry geom = (RpGeometry) geomList.next();
            if (geom instanceof StationaryPointGeom) {
                StationaryPointGeom evaluate = (StationaryPointGeom) geom;
                if (closest == null) {
                    closest = evaluate;
                } else {
                    double closestDeltaSum = 0d;
                    double evaluateDeltaSum = 0d;
                    for (int i = 0; i < coords.getSize(); i++) {
                        double closestDelta = Math.pow(coords.getElement(i)
                                - ((StationaryPoint) closest.geomFactory().geomSource()).getPoint().getCoords().getElement(i), 2);
                        double evaluateDelta = Math.pow(coords.getElement(i)
                                - ((StationaryPoint) evaluate.geomFactory().geomSource()).getPoint().getCoords().getElement(i), 2);
                        closestDeltaSum += closestDelta;
                        evaluateDeltaSum += evaluateDelta;
                    }
                    if (evaluateDeltaSum < closestDeltaSum) {
                        closest = evaluate;
                    }
                }
            }

        }
        return closest;
    }

    public PhasePoint findSelectionMidPoint() {

        PhasePoint selectionPoint = ((StationaryPoint) selectedGeom_.geomFactory().geomSource()).getPoint();
        PhasePoint xzeroPoint = ((StationaryPoint) ((NUMCONFIG_READY) state_).xzeroGeom().geomFactory().geomSource()).getPoint();
        PhasePoint midPoint = new PhasePoint(xzeroPoint);
        midPoint.getCoords().add(selectionPoint.getCoords(), xzeroPoint.getCoords());
        midPoint.getCoords().scale(.5);
        return midPoint;
    }

    public void select(RealVector coords) {
        state_.select(this, coords);
    }

    public void unselectAll() {
        if (selectedGeom_ != null) {
            selectedGeom_.viewingAttr().setSelected(false);
        }
    }
}
