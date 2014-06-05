/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import rpn.RPnDesktopPlotter;
import rpn.RPnMenuCommand;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnProjDescriptor;
import rpn.RPnRiemannFrame;
import rpn.RPnUIFrame;
import rpn.component.*;
import rpn.component.CharacteristicsCurveGeomFactory;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.util.Boundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class RiemannProfileCommand extends RpModelPlotCommand implements Observer, RPnMenuCommand, WindowListener {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Riemann Profile";
    //
    // Members
    //
    static private RiemannProfileCommand instance_ = null;
    private WaveCurve waveCurveForward_;
    private WaveCurve waveCurveBackward_;

    private RPnRiemannFrame speedGraphicsFrame_;

    //
    // Constructors/Initializers
    //
    protected RiemannProfileCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return null;
    }

    static public RiemannProfileCommand instance() {
        if (instance_ == null) {
            instance_ = new RiemannProfileCommand();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {

//        if (!UIController.instance().getSelectedGeometriesList().isEmpty()) {
        boolean enable = checkCurvesForRiemmanProfile(UIController.instance().getSelectedGeometriesList());

        setEnabled(enable);
        DomainSelectionCommand.instance().setEnabled(!UIController.instance().getSelectedGeometriesList().isEmpty());

//        }
    }

    private void updateSpeedGraphicsFrame(RealVector profileMin, RealVector profileMax) {

        RectBoundary boundary = new RectBoundary(profileMin, profileMax);
        Space riemanProfileSpace = new Space("SpeedGraphics", 2);

        int[] riemannProfileIndices = {0, 1};

        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(riemanProfileSpace, "SpeedGraphicsSpace", 400, 400, riemannProfileIndices, false);
        wave.multid.view.ViewingTransform riemanTesteTransform = projDescriptor.createTransform(clipping);

        try {
            wave.multid.view.Scene riemannScene = RPnDataModule.SPEEDGRAPHICSPHASESPACE.createScene(riemanTesteTransform, new wave.multid.view.ViewingAttr(Color.black));
            speedGraphicsFrame_ = new RPnRiemannFrame(riemannScene, this);
            speedGraphicsFrame_.addWindowListener(this);

        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }
        speedGraphicsFrame_.pack();
        speedGraphicsFrame_.setVisible(true);

    }

    @Override
    public void execute() {

//        selectedCurves = UIController.instance().getSelectedGeometriesList();
        Iterator<RPnPhaseSpacePanel> panelsIterator = UIController.instance().getInstalledPanelsIterator();
        while (panelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = panelsIterator.next();

            for (AreaSelected sArea : rPnPhaseSpacePanel.getSelectedAreas()) {
                Area selectedArea = new Area(sArea);

                int[] waveCurvesID = new int[2];
                waveCurvesID[0] = waveCurveForward_.getId();
                waveCurvesID[1] = waveCurveBackward_.getId();
                RiemannProfileCalc rc = new RiemannProfileCalc(selectedArea, waveCurvesID);
//                RiemannProfileGeomFactory riemannProfileGeomFactory = new RiemannProfileGeomFactory(rc);

//                RiemannProfile riemannProfile = (RiemannProfile) riemannProfileGeomFactory.geomSource();
//                if (riemannProfile != null) {
//                    if (riemannProfile.getPoints().length > 0) {
//                      RealVector profileMin = createProfileMinLimit(riemannProfile);
//                        RealVector profileMax = createProfileMaxLimit(riemannProfile);
//                        RPnDesktopPlotter.getUIFrame().updateRiemannProfileFrames(profileMin, profileMax);
                RPnDataModule.RIEMANNPHASESPACE.clear();

                System.out.println("Em Java");
//                        RiemannProfile profile = (RiemannProfile) riemannProfileGeomFactory.geomSource();
//                        OrbitPoint[] points = rc..getPoints();
//
//                        for (OrbitPoint orbitPoint : points) {
//
//                            System.out.println(orbitPoint);
//
//                        }
                
                
                
                String Xlimits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");
        
        
                String Ylimits[] = RPNUMERICS.getParamValue("riemannprofile", "Yrange").split(" ");

                
                

                RealVector min = new RealVector(Xlimits[0]+" "+Ylimits[0]);
                RealVector max = new RealVector(Xlimits[1]+" "+Ylimits[1]);

                RpDiagramFactory factory = new RpDiagramFactory(rc);
                DiagramGeom geom = (DiagramGeom) factory.geom();
                


                RPnDataModule.SPEEDGRAPHICSPHASESPACE.join(geom);

                updateSpeedGraphicsFrame(min, max);

//
//                        RPnDataModule.RIEMANNPHASESPACE.join(riemannProfileGeomFactory.geom());
////
//                        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getRiemannFrames()) {
//                            frame.setVisible(true);
//                        }
//                        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
//                            plotCharacteristics(i, riemannProfile);
//                        }
//
//                        for (RPnPhaseSpaceFrame charFrame : RPnUIFrame.getCharacteristicsFrames()) {
//                            charFrame.setVisible(true);
//
//                        }
//                    }
//                }
            }

        }

//        Iterator<RPnPhaseSpacePanel> panelsIterator = UIController.instance().getInstalledPanelsIterator();
//
//        while (panelsIterator.hasNext()) {
//
//            RPnPhaseSpacePanel phaseSpacePanel = panelsIterator.next();
//            List<List<AreaSelected>> intersectionAreas = new ArrayList();
//
//            Iterator<GeomObjView> geomObjViewIterator = phaseSpacePanel.scene().geometries();
//
//            while (geomObjViewIterator.hasNext()) {
//                GeomObjView geomObjView = geomObjViewIterator.next();
//
//                if (selectedCurves.contains((RpGeometry) geomObjView.getAbstractGeom())) {
//                    List<AreaSelected> polygonList = phaseSpacePanel.interceptedAreas(geomObjView);
//                    intersectionAreas.add(polygonList);
//                }
//
//                if (intersectionAreas.size() == 2) {
//
//                    List<AreaSelected> finalSelectedAreas = processIntersectionAreas(intersectionAreas);
//
//                   
//                    }
//                }
//
//            }
//
//        }
        firePropertyChange("Riemann Profile Added", "oldValue",
                "newValue");

    }
//
//    private RealVector createProfileMaxLimit() {
//        RealVector profileMax = new RealVector(2);
////        double maxXProfile = riemannProfile.getPoints()[riemannProfile.getPoints().length - 1].getLambda();
//
//        String limits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");
//
//        double maxXProfile = Double.valueOf(limits[1]);
//
//        profileMax.setElement(0, maxXProfile);
//
//        Boundary boundary = RPNUMERICS.boundary();
//
//        profileMax.setElement(1, boundary.getMaximums().getElement(1));
////        profileMax.setElement(2, boundary.getMaximums().getElement(1));
//
//        return profileMax;
//
//    }

//    private RealVector createProfileMinLimit() {
//
//        RealVector profileMin = new RealVector(2);
//
//        String Xlimits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");
//        
//        
//        String Ylimits[] = RPNUMERICS.getParamValue("riemannprofile", "Yrange").split(" ");
//        
//
//        double minXProfile = Double.valueOf(limits[0]);
//
////        double minXProfile = riemannProfile.getPoints()[0].getLambda();
//        profileMin.setElement(0, minXProfile);
//
//        Boundary boundary = RPNUMERICS.boundary();
//        profileMin.setElement(1, boundary.getMinimums().getElement(0));
////        profileMin.setElement(2, boundary.getMinimums().getElement(1));
//
//        return profileMin;
//
//    }

    private void plotCharacteristics(int charFamily, RiemannProfile riemannProfile) {

        CharacteristicsCurveCalc charCalc = new CharacteristicsCurveCalc(riemannProfile, 128);
        try {
            CharacteristicsCurve charCurve = (CharacteristicsCurve) charCalc.calc();
            CharacteristicsCurveGeomFactory factory = new CharacteristicsCurveGeomFactory(charCalc, charCurve);
            RealVector charXAxis = createCharacteristicAbscissa(charFamily, charCurve);
            RealVector charMinRealVector = new RealVector(charXAxis.getElement(0) + " " + 0);
            RealVector charMaxRealVector = new RealVector(charXAxis.getElement(1) + " " + 0.45);
//            

            RPnDesktopPlotter.getUIFrame().updateCharacteristicsFrames(charFamily, charMinRealVector, charMaxRealVector);

            for (int i = 0; i < RPnDataModule.CHARACTERISTICSPHASESPACEARRAY.length; i++) {
                RPnPhaseSpaceAbstraction charPhaseSpace = RPnDataModule.CHARACTERISTICSPHASESPACEARRAY[i];

                RpGeometry testeChar = factory.getFamilyGeometry(i);
                charPhaseSpace.clear();
                charPhaseSpace.join(testeChar);

            }

        } catch (RpException ex) {
            Logger.getLogger(RiemannProfileCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private RealVector createCharacteristicAbscissa(int charFamily, CharacteristicsCurve charCurve) {

        List<PhasePoint[]> charPoints = charCurve.getFamilyPoints(charFamily);

        double minX = 0;
        double maxX = 0;

        for (int i = 0; i < charPoints.size(); i++) {
            PhasePoint[] phasePoints = charPoints.get(i);

            for (int j = 0; j < phasePoints.length; j++) {
                PhasePoint phasePoint = phasePoints[j];
                if (phasePoint.getElement(0) < minX) {
                    minX = phasePoint.getElement(0);
                }
                if (phasePoint.getElement(0) > maxX) {
                    maxX = phasePoint.getElement(0);
                }
            }
        }

        return new RealVector(minX + " " + maxX);

    }

    private boolean checkCurvesForRiemmanProfile(List<RpGeometry> selectedCurves) {

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

    @Override
    public void finalizeApplication() {

        System.out.println("Chamando finalize");

    }

    @Override
    public void networkCommand() {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("chamando closing");
        RPnDataModule.SPEEDGRAPHICSPHASESPACE.clear();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("chamando closed");

        RPnDataModule.SPEEDGRAPHICSPHASESPACE.clear();
        speedGraphicsFrame_.dispose();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}
