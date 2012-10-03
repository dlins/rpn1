/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import rpn.RPnDesktopPlotter;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnUIFrame;
import rpn.component.*;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.view.GeomObjView;
import wave.util.Boundary;
import wave.util.RealVector;

public class RiemannProfileAgent extends RpModelPlotAgent implements Observer {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Riemann Profile";
    //
    // Members
    //
    static private RiemannProfileAgent instance_ = null;
    private WaveCurve waveCurveForward_;
    private WaveCurve waveCurveBackward_;
    private List<RpGeometry> selectedCurves;

    //
    // Constructors/Initializers
    //
    protected RiemannProfileAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return null;
    }

    static public RiemannProfileAgent instance() {
        if (instance_ == null) {
            instance_ = new RiemannProfileAgent();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {

        boolean enable = testRiemmanProfile((List<RpGeometry>) arg);

        setEnabled(enable);
        AreaSelectionAgent.instance().setEnabled(enable);

        if (enable) {
            selectedCurves = (List<RpGeometry>) arg;
        }




    }

    @Override
    public void execute() {

        Iterator<RPnPhaseSpacePanel> panelsIterator = UIController.instance().getInstalledPanelsIterator();

        while (panelsIterator.hasNext()) {

            RPnPhaseSpacePanel phaseSpacePanel = panelsIterator.next();
            List<List<AreaSelected>> intersectionAreas = new ArrayList();

            Iterator<GeomObjView> geomObjViewIterator = phaseSpacePanel.scene().geometries();

            while (geomObjViewIterator.hasNext()) {
                GeomObjView geomObjView = geomObjViewIterator.next();

                if (selectedCurves.contains((RpGeometry) geomObjView.getAbstractGeom())) {
                    List<AreaSelected> polygonList = phaseSpacePanel.interceptedAreas(geomObjView);
                    intersectionAreas.add(polygonList);
                }

                if (intersectionAreas.size() == 2) {

                    List<AreaSelected> finalSelectedAreas = processIntersectionAreas(intersectionAreas);

                    for (AreaSelected sArea : finalSelectedAreas) {
                        Area selectedArea = new Area(sArea);
                        RiemannProfileCalc rc = new RiemannProfileCalc(selectedArea, waveCurveForward_, waveCurveBackward_);
                        RiemannProfileGeomFactory riemannProfileGeomFactory = new RiemannProfileGeomFactory(rc);

                        RiemannProfile riemannProfile = (RiemannProfile) riemannProfileGeomFactory.geomSource();

                        RealVector profileMin = createProfileMinLimit(riemannProfile);
                        RealVector profileMax = createProfileMaxLimit(riemannProfile);

                        RPnDesktopPlotter.getUIFrame().updateRiemannProfileFrames(profileMin, profileMax);

                        RPnDataModule.RIEMANNPHASESPACE.clear();
                        RPnDataModule.RIEMANNPHASESPACE.join(riemannProfileGeomFactory.geom());
                        RPnDataModule.RIEMANNPHASESPACE.update();

                        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getRiemannFrames()) {

                            frame.setVisible(true);

                        }

                    }
                }

            }

        }

    }

    private RealVector createProfileMaxLimit(RiemannProfile riemannProfile) {
        RealVector profileMax = new RealVector(RPNUMERICS.domainDim() + 1);
        double maxXProfile = riemannProfile.getPoints()[riemannProfile.getPoints().length - 1].getLambda();
        profileMax.setElement(0, maxXProfile);

        Boundary boundary = RPNUMERICS.boundary();

        profileMax.setElement(1, boundary.getMaximums().getElement(0));
        profileMax.setElement(2, boundary.getMaximums().getElement(1));

        return profileMax;

    }

    private RealVector createProfileMinLimit(RiemannProfile riemannProfile) {

        RealVector profileMin = new RealVector(RPNUMERICS.domainDim() + 1);

        double minXProfile = riemannProfile.getPoints()[0].getLambda();

        profileMin.setElement(0, minXProfile);

        Boundary boundary = RPNUMERICS.boundary();
        profileMin.setElement(1, boundary.getMinimums().getElement(0));
        profileMin.setElement(2, boundary.getMinimums().getElement(1));

        return profileMin;

    }

    private boolean testRiemmanProfile(List<RpGeometry> selectedCurves) {

        if (selectedCurves.size() != 2) {
            return false;
        }

        boolean waveCurveForward0 = false;
        boolean waveCurveBackward1 = false;

        for (RpGeometry geometry : selectedCurves) {
            if (geometry instanceof WaveCurveGeom) {
                WaveCurveGeom waveCurveGeom = (WaveCurveGeom) geometry;
                WaveCurve waveCurve = (WaveCurve) waveCurveGeom.geomFactory().geomSource();
                if (waveCurve.getFamily() == 0 && waveCurve.getDirection() == Orbit.WAVECURVE_FORWARD) {
                    instance_.waveCurveForward_ = waveCurve;
                    waveCurveForward0 = true;
                }
                if (waveCurve.getFamily() == 1 && waveCurve.getDirection() == Orbit.WAVECURVE_BACKWARD) {
                    instance_.waveCurveBackward_ = waveCurve;
                    waveCurveBackward1 = true;
                }
            }
        }


        return (waveCurveForward0 && waveCurveBackward1);

    }

    private List<AreaSelected> processIntersectionAreas(List<List<AreaSelected>> intersectionAreasList) {

        if (intersectionAreasList.get(0).size() > intersectionAreasList.get(1).size()) {
            intersectionAreasList.get(0).retainAll(intersectionAreasList.get(1));
            return intersectionAreasList.get(0);
        } else {
            intersectionAreasList.get(1).retainAll(intersectionAreasList.get(0));
            return intersectionAreasList.get(1);
        }

    }
}
