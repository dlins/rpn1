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
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import rpn.RPnMenuCommand;
import rpn.RPnProjDescriptor;
import rpn.RPnRiemannFrame;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class WaveCurveSpeedPlotCommand extends RpModelPlotCommand implements Observer, RPnMenuCommand, WindowListener {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Wave Curve Speed Plot";
    //
    // Members
    //
    static private WaveCurveSpeedPlotCommand instance_ = null;
    private double maxY_;
    private double maxX_;
    private List<RpGeometry> selectedCurves;
    private RPnRiemannFrame speedGraphicsFrame_;
    private OrbitPoint referencePoint_;

    //
    // Constructors/Initializers
    //
    protected WaveCurveSpeedPlotCommand() {
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

    static public WaveCurveSpeedPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new WaveCurveSpeedPlotCommand();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {

        boolean enable = UIController.instance().getSelectedGeometriesList().size() == 1;

        setEnabled(enable);

    }

    @Override
    public void execute() {

        selectedCurves = UIController.instance().getSelectedGeometriesList();
        RpGeometry curveSelected = selectedCurves.get(0);

        WaveCurve waveCurve = (WaveCurve) curveSelected.geomFactory().geomSource();

        RpDiagramFactory factory = new RpDiagramFactory(waveCurve);
        DiagramGeom geom = (DiagramGeom) factory.geom();
        
        
        RPnDataModule.SPEEDGRAPHICSPHASESPACE.join(geom);
        
        
        
        updateSpeedGraphicsFrame(geom.getMin(), geom.getMax());

//        RpSolution createDiagramSource = waveCurve.createDiagramSource();
//
//        RPnDataModule.SPEEDGRAPHICSPHASESPACE.clear();
//        List<MultiPolyLine> polyLineList = null;
//        for (RpGeometry rpGeometry : selectedCurves) {
//            WaveCurveBranchGeom waveCurveGeom = (WaveCurveBranchGeom) rpGeometry;
//
//            List<WaveCurveBranchGeom> orbitGeom = waveCurveGeom.getOrbitGeom();
//
//            for (WaveCurveBranchGeom waveCurveBranchGeom : orbitGeom) {
//
//                WaveCurveOrbitGeom fundamentalGeom = (WaveCurveOrbitGeom) waveCurveBranchGeom;
//                polyLineList = createGraphics(fundamentalGeom);
//
//            }
//
//        }
//
//       
//
//        for (MultiPolyLine multiPolyLine : polyLineList) {
//
//            RPnDataModule.SPEEDGRAPHICSPHASESPACE.join(multiPolyLine);
//        }
//
// 
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

    private MultiPolyLine drawReferencePointHorizontal(RealVector graphicsCoords, int familyIndex) {

        CoordsArray[] coordsArray = new CoordsArray[2];

        double[] referencePoint = new double[2];

        referencePoint[0] = graphicsCoords.getElement(0);
        referencePoint[1] = graphicsCoords.getElement(familyIndex + 1);//Speed at reference point

        coordsArray[0] = new Coords2D(referencePoint);

        double[] endPoint = new double[2];

        endPoint[0] = 10.0;
        endPoint[1] = graphicsCoords.getElement(familyIndex + 1);//Last distance data

        coordsArray[1] = new Coords2D(endPoint);

        MultiPolyLine polyLine = new MultiPolyLine(coordsArray, new ViewingAttr(Color.green));

        return polyLine;

    }

    private List<MultiPolyLine> createGraphics(WaveCurveOrbitGeom waveCurveOrbitGeom) {
        ArrayList<RealVector> graphicsCoords = new ArrayList<RealVector>();
        List<WaveCurveBranchGeom> orbitGeom = waveCurveOrbitGeom.getOrbitGeom();
        int eigenValuesLength = 2;
        List<MultiPolyLine> polyLineList = new ArrayList<MultiPolyLine>();

        for (WaveCurveBranchGeom waveCurveBranchGeom : orbitGeom) {
            try {
                WaveCurveOrbitGeom fundamentalGeom = (WaveCurveOrbitGeom) waveCurveBranchGeom;
                WaveCurveBranch geomSource = (WaveCurveBranch) fundamentalGeom.geomFactory().geomSource();
                
                referencePoint_ = geomSource.getReferencePoint();
                
                maxY_ = referencePoint_.getSpeed();
                
                eigenValuesLength = geomSource.getReferencePoint().getEigenValues().length;
                
                graphicsCoords.add(makeReferencePointData(referencePoint_));
                
                List<OrbitPoint> branchPoints = geomSource.getBranchPoints();
                
                for (int i = 0; i < branchPoints.size() - 1; i++) {
                    
                    OrbitPoint secondPoint = geomSource.getBranchPoints().get(i + 1);
                    
                    double previousDistance = graphicsCoords.get(i).getElement(0);
                    
                    double distance = previousDistance + branchPoints.get(i).getCoords().distance(secondPoint);
                    double speed = secondPoint.getSpeed();
                    
                    if (speed > maxY_) {
                        maxY_ = speed;
                    }
                    
                    if (distance > maxX_) {
                        maxX_ = distance;
                    }
                    
                    StringBuilder stringCoords = new StringBuilder();
                    
                    stringCoords.append(distance).append(" ").append(speed);
                    
                    for (int j = 0; j < eigenValuesLength; j++) {
                        
                        stringCoords.append(" ").append(secondPoint.getEigenValues()[j]);
                        
                    }
                    
                    System.out.println("String: " + stringCoords.toString());
                    RealVector coords = new RealVector(stringCoords.toString());
                    
                    graphicsCoords.add(coords);
                    
                }
            } catch (RpException ex) {
                Logger.getLogger(WaveCurveSpeedPlotCommand.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        for (int i = 1; i < eigenValuesLength + 2; i++) {

            MultiPolyLine polyLine = createPolyLine(graphicsCoords, i);

            Color color;
            if (i == 1) {
                color = Color.WHITE;
            } else if (i == 2) {
                color = Color.BLUE;
            } else if (i == 3) {

                color = Color.RED;
            } else {
                color = Color.gray;

            }

            polyLine.viewingAttr().setColor(color);

            polyLineList.add(polyLine);

        }

        return polyLineList;
    }

    private MultiPolyLine createPolyLine(List<RealVector> graphicsCoords, int dataIndex) {

        CoordsArray[] coordsArray = new CoordsArray[graphicsCoords.size()];

        for (int j = 0; j < coordsArray.length; j++) {

            double graphicsPoint[] = new double[2];

            graphicsPoint[0] = graphicsCoords.get(j).getElement(0);
            graphicsPoint[1] = graphicsCoords.get(j).getElement(dataIndex);

            coordsArray[j] = new Coords2D(graphicsPoint);
        }

        MultiPolyLine polyLine = new MultiPolyLine(coordsArray, new ViewingAttr(Color.WHITE));

        return polyLine;
    }

    private RealVector makeReferencePointData(OrbitPoint referencePoint) {

        double[] eigenValues = referencePoint.getEigenValues();
        double referencePointSpeed = referencePoint.getSpeed();

        StringBuilder stringCoords = new StringBuilder();

        stringCoords.append(0).append(" ").append(referencePointSpeed);

        for (int j = 0; j < eigenValues.length; j++) {

            stringCoords.append(" ").append(eigenValues[j]);

        }

        RealVector referencePointData = new RealVector(stringCoords.toString());

        return referencePointData;

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
