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
import wave.multid.model.AbstractPathIterator;
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
    //
    // Constructors
    //
    public RPnPhaseSpaceAbstraction(String id, Space domain, PhaseSpaceState state) {
        super(id, domain);
        changeState(state);
        selectedGeom_ = null;
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

    @Override
    public void update() {

        Iterator geomList = getGeomObjIterator();

        List removeList = new ArrayList();
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

    // overwriting so we don't remove XZero and Hugoniot
    @Override
    public void clear() {


        for (int i=0; i < groupArrayList_.size()-1;i++){
            
            ArrayList list = groupArrayList_.get(i);

            for (int j = 0; j < list.size(); j++) {
                
                delete ((RpGeometry)list.get(j));
            }
            groupArrayList_.remove(i);
            
        }
        
        ArrayList deleteList = new ArrayList();
        Iterator geomList = getGeomObjIterator();
        while (geomList.hasNext()) {
            RpGeometry geom = (RpGeometry) geomList.next();
            if (!(geom instanceof HugoniotCurveGeom) && !(geom instanceof XZeroGeom)) {
                deleteList.add(geom);
            }
        }
        for (int i = 0; i < deleteList.size(); i++) {
            delete((RpGeometry) deleteList.get(i));
        }

        if (RPNUMERICS.getCurrentProfile() instanceof ShockProfile) {
            changeState(new NumConfigReadyImpl(((NUMCONFIG_READY) state_).hugoniotGeom(), ((NUMCONFIG_READY) state_).xzeroGeom()));
        }
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
                        double closestDelta = Math.pow(coords.getElement(i) -
                                ((StationaryPoint) closest.geomFactory().geomSource()).getPoint().getCoords().getElement(i), 2);
                        double evaluateDelta = Math.pow(coords.getElement(i) -
                                ((StationaryPoint) evaluate.geomFactory().geomSource()).getPoint().getCoords().getElement(i), 2);
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
