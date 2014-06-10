/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import rpn.RPnDesktopPlotter;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.*;
import rpn.component.CharacteristicsCurveGeomFactory;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.Boundary;
import wave.util.RealVector;

public class RiemannAllProfileCommand extends RpModelPlotCommand implements Observer {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Riemann All Profile";
    //
    // Members
    //
    static private RiemannAllProfileCommand instance_ = null;
    private WaveCurve waveCurveForward_;
    private WaveCurve waveCurveBackward_;
    private List<RpGeometry> selectedCurves;

    //
    // Constructors/Initializers
    //
    protected RiemannAllProfileCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JToggleButton());
    }

  

    public RpGeometry createRpGeometry(RealVector[] input) {
        return null;
    }

    static public RiemannAllProfileCommand instance() {
        if (instance_ == null) {
            instance_ = new RiemannAllProfileCommand();
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

    @Override
    public void execute() {
        Iterator geomIterator = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
        
        int geomIndex=0;
        
        WaveCurve waveCurve1=null;
        WaveCurve waveCurve2 =null;
        while (geomIterator.hasNext()) {

            RpGeometry geometry = (RpGeometry) geomIterator.next();
            
            if(geomIndex==0){

              waveCurve2 = (WaveCurve) geometry.geomFactory().geomSource();
    
                
            }
            else {
                        
                waveCurve1 = (WaveCurve) geometry.geomFactory().geomSource();
                
            }

            geomIndex++;
        }
        
        
        
        
       
        RealVector[] userInputList = UIController.instance().userInputList();
         
                RiemannProfileCalc rc = new RiemannProfileCalc(waveCurve1,waveCurve2,userInputList[0]);
                RiemannProfileGeomFactory riemannProfileGeomFactory = new RiemannProfileGeomFactory(rc);

                RiemannProfile riemannProfile = (RiemannProfile) riemannProfileGeomFactory.geomSource();

                if (riemannProfile != null) {
                    if (riemannProfile.getPoints().length > 0) {
                        RealVector profileMin = createProfileMinLimit(riemannProfile);
                        RealVector profileMax = createProfileMaxLimit(riemannProfile);

                        RPnDesktopPlotter.getUIFrame().updateRiemannProfileFrames(profileMin, profileMax);

                        RPnDataModule.RIEMANNPHASESPACE.clear();

                        RPnDataModule.RIEMANNPHASESPACE.join(riemannProfileGeomFactory.geom());

                        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getRiemannFrames()) {
                            frame.setVisible(true);
                        }
                    }

                }

 
        firePropertyChange("Riemann Profile Added", "oldValue",
                "newValue");

    }

    private RealVector createProfileMaxLimit(RiemannProfile riemannProfile) {
        RealVector profileMax = new RealVector(RPNUMERICS.domainDim() + 1);
//        double maxXProfile = riemannProfile.getPoints()[riemannProfile.getPoints().length - 1].getLambda();
        
        String limits [] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");
        
        double maxXProfile = Double.valueOf(limits[1]);
        
        profileMax.setElement(0, maxXProfile);

        Boundary boundary = RPNUMERICS.boundary();

        profileMax.setElement(1, boundary.getMaximums().getElement(0));
        profileMax.setElement(2, boundary.getMaximums().getElement(1));

        return profileMax;

    }

    private RealVector createProfileMinLimit(RiemannProfile riemannProfile) {

        RealVector profileMin = new RealVector(RPNUMERICS.domainDim() + 1);
        
         
        String limits [] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");
        
        double minXProfile = Double.valueOf(limits[0]);
        

//        double minXProfile = riemannProfile.getPoints()[0].getLambda();

        profileMin.setElement(0, minXProfile);

        Boundary boundary = RPNUMERICS.boundary();
        profileMin.setElement(1, boundary.getMinimums().getElement(0));
        profileMin.setElement(2, boundary.getMinimums().getElement(1));

        return profileMin;

    }

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
            Logger.getLogger(RiemannAllProfileCommand.class.getName()).log(Level.SEVERE, null, ex);
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
}
