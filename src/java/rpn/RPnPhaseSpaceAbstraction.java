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
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import rpn.command.CurveSelectionCommand;
import rpn.component.util.GeometryGraphND;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import wave.multid.model.MultiGeometry;
import wave.util.RealVector;

public class RPnPhaseSpaceAbstraction extends AbstractScene implements Observer {
    //
    // Constants
    //
    //
    // Members
    //

    private PhaseSpaceState state_;
    private RpGeometry selectedGeom_;
    private List<RPnCurvesList> curvesListFrames_;
    private UserInputTable userInputTable_;
    // TODO remove this please ! MVERA
    static public String namePhaseSpace = "";
    static public List listResolution = new ArrayList();
    static public int closestCurve;             //indice da curva mais proxima
    private List stringsToRemove = new ArrayList();
    private Timer timer_;
    private CurveFlasher flasher_;
    private List<RpGeometry> selectedGeometries_;
    //
    // Constructors
    //

    public RPnPhaseSpaceAbstraction(String id, Space domain, PhaseSpaceState state) {
        super(id, domain);
        changeState(state);
        selectedGeom_ = null;
        userInputTable_ = new UserInputTable(domain.getDim());
        curvesListFrames_ = new ArrayList<RPnCurvesList>();
        selectedGeometries_ = new ArrayList<RpGeometry>();

//        flasher_ = new CurveFlasher();
    }

    //
    // Accessors/Mutators
    //
    public final void changeState(PhaseSpaceState state) {
        state_ = state;
    }

    public void attach(RPnCurvesList curvesFrame) {
        curvesListFrames_.add(curvesFrame);
    }

    public void detach(RPnCurvesList curvesListFrame) {
        curvesListFrames_.remove(curvesListFrame);
    }

    public void showCurvesFrame(boolean show) {
        Iterator<RPnCurvesList> iterator = curvesListFrames_.iterator();

        while (iterator.hasNext()) {
            RPnCurvesList rPnCurvesListFrame = iterator.next();

            rPnCurvesListFrame.setVisible(show);

        }
    }

    public Iterator curvesListIterator() {
        return curvesListFrames_.iterator();
    }

    /**
     * @todo fix this... shouldn't have access to state_
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

    public RpGeometry getLastGeometry() {
        if (geomList_.isEmpty()) {
            return null;
        }
        return (RpGeometry) super.geomList_.get(super.geomList_.size() - 1);
    }

    @Override
    public void join(MultiGeometry geom) {
        if (geom == null) {
            return;
        }
        super.join(geom);

        for (Iterator<RpGeometry> it = selectedGeometries_.iterator(); it.hasNext();) {
            Object obj = it.next();

            RpGeometry geometry = (RpGeometry) obj;
            geometry.setVisible(true);
        }

        notifyState();

    }

    public boolean contains(MultiGeometry multiGeometry) {

        RpGeometry rpGeometry = (RpGeometry) multiGeometry;

        Object source = rpGeometry.geomFactory().geomSource();

        for (Object object : geomList_) {
            RpGeometry geometryInList = (RpGeometry) object;

            if (geometryInList.geomFactory().geomSource() == source) {
                return true;
            }
        }

        return false;

    }

    @Override
    public void remove(MultiGeometry geom) {

        ((RpGeometry) geom).geomFactory().getUI().uninstall(((RpGeometry) geom).geomFactory());
        selectedGeometries_.remove((RpGeometry) geom);

        RPnCurve curve = (RPnCurve) ((RpGeometry) geom).geomFactory().geomSource();

        RPNUMERICS.removeCurve(curve.getId());

        super.remove(geom);
        for (Iterator<RpGeometry> it = selectedGeometries_.iterator(); it.hasNext();) {
            Object obj = it.next();

            RpGeometry geometry = (RpGeometry) obj;
            geometry.setVisible(true);
        }

        notifyState();

    }

    @Override
    public String toString() {
        return getName() + " " + getSpace().toString() + " " + state_.toString();
    }

    public UserInputTable getUserInputTable() {
        return userInputTable_;
    }

    public RpGeometry findClosestGeometry(RealVector targetPoint) {             //*** Fazer alteracoes !!!!!

        RpGeometry closestGeometry_ = null;      //a curva mais proxima

        listResolution.clear();

        double distminCurve = 1000000.;
        double distancia = 0.;
        int k = 0;
        Iterator geomListIterator = geomList_.iterator();

//        //--------------------------
//        // **** Usar direto o objeto, sem testar
//        if (namePhaseSpace.equals(RPnDataModule.PHASESPACE.getName())) {
//            geomList = RPnDataModule.PHASESPACE.getGeomObjIterator();
//        }
//        if (namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName())) {
//            geomList = RPnDataModule.RIGHTPHASESPACE.getGeomObjIterator();
//        }
//        if (namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName())) {
//            geomList = RPnDataModule.LEFTPHASESPACE.getGeomObjIterator();
//        }
        while (geomListIterator.hasNext()) {
            RpGeometry geom = (RpGeometry) geomListIterator.next();

            if (GeometryGraphND.onCurve == 1) {
//                if ((namePhaseSpace.equals(RPnDataModule.PHASESPACE.getName()) && geom != RPnDataModule.PHASESPACE.getLastGeometry())
//                        || (namePhaseSpace.equals(RPnDataModule.RIGHTPHASESPACE.getName()) && geom != RPnDataModule.RIGHTPHASESPACE.getLastGeometry())
//                        || (namePhaseSpace.equals(RPnDataModule.LEFTPHASESPACE.getName()) && geom != RPnDataModule.LEFTPHASESPACE.getLastGeometry())) {

//                    if (geom.viewingAttr().hasHighLight() && !(geom instanceof StationaryPointGeom)) {
                RpGeomFactory factory = geom.geomFactory();

                RPnCurve curve = (RPnCurve) factory.geomSource();
                curve.findClosestSegment(targetPoint);

                distancia = curve.distancia;

                if (distminCurve >= distancia) {
                    distminCurve = distancia;
                    closestCurve = k;
                    closestGeometry_ = geom;
                }

//                    }
//                }
            }

            if (GeometryGraphND.onCurve == 0) {
//                if (geom.viewingAttr().hasHighLight() && !(geom instanceof StationaryPointGeom) && !(geom instanceof PoincareSectionGeom)) {

                RpGeomFactory factory = geom.geomFactory();
                RPnCurve curve = (RPnCurve) factory.geomSource();

                // -----------------------------------
                if (curve instanceof SegmentedCurve && !(curve instanceof PhysicalBoundary)) {
                    RpCalcBasedGeomFactory geomFactory = (RpCalcBasedGeomFactory) factory;
                    RpCalculation calc = geomFactory.rpCalc();
                    ContourCurveCalc curveCalc = (ContourCurveCalc) calc;
                    listResolution.add(curveCalc.getParams().getResolution());
                } else {
                    listResolution.add(new int[]{0, 0});

                }
                // ---------------------------------------------------------------

                curve.findClosestSegment(targetPoint);

                distancia = curve.distancia;

                if (distminCurve >= distancia) {
                    distminCurve = distancia;
                    closestCurve = k;
                    closestGeometry_ = geom;
                }

            } // ----------------------------------- Evita erro quando no PhaseDiagram
            else {
                listResolution.add(new int[]{0, 0});
            }
                // -----------------------------------

//            }
            k++;

        }
        //--------------------------

        return closestGeometry_;
    }

    @Override
    public void update() {

        Iterator geomList = getGeomObjIterator();
        List removeList
                = new ArrayList();
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

            RpGeometry geometryToRemove = (RpGeometry) removeList.get(i);

            super.remove(geometryToRemove);
        }
        for (int i = 0; i < joinList.size(); i++) {
            super.join((RpGeometry) joinList.get(i));

        }
        notifyState();
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

    private void notifyState() {
        for (RPnCurvesList curvesListFrame : curvesListFrames_) {
            curvesListFrame.update();
        }

    }

    public void updateCurveSelection() {

        for (Object object : geomList_) {
            RpGeometry rpGeometry = (RpGeometry) object;
            if (!selectedGeometries_.contains(rpGeometry)) {
                if (rpGeometry.isSelected()) {
                    selectedGeometries_.add(rpGeometry);
                    Timer timer = new Timer();
                    CurveFlasher flasher = new CurveFlasher(rpGeometry);
                    timer.schedule(flasher, 0, 1000);

                }
            } else {
                if (!rpGeometry.isSelected()) {
                    selectedGeometries_.remove(rpGeometry);

                }

            }
            
            RpGeometry geometry = (RpGeometry)geomList_;
            
            RPnCurve curve = (RPnCurve) geometry.geomFactory().geomSource();
            
            CurveSelectionCommand.instance().setCurveToSelect(curve.getId());
            
            CurveSelectionCommand.instance().execute();

            
            

        }


    }

    @Override
    public void update(Observable o, Object arg) {

        updateCurveSelection();
        UIController.instance().panelsUpdate();
    }
}
