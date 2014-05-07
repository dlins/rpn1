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

        UIController.instance().getSelectedGeometriesList().isEmpty();

        boolean enable = !UIController.instance().getSelectedGeometriesList().isEmpty();
//
        setEnabled(enable);

//        }
    }

    @Override
    public void execute() {

        selectedCurves = UIController.instance().getSelectedGeometriesList();
        List<RealVector> graphics = null;
        for (RpGeometry rpGeometry : selectedCurves) {
            WaveCurveBranchGeom waveCurveGeom = (WaveCurveBranchGeom) rpGeometry;

            List<WaveCurveBranchGeom> orbitGeom = waveCurveGeom.getOrbitGeom();

            for (WaveCurveBranchGeom waveCurveBranchGeom : orbitGeom) {

                WaveCurveOrbitGeom fundamentalGeom = (WaveCurveOrbitGeom) waveCurveBranchGeom;
                graphics = createGraphics(fundamentalGeom);

            }

        }

        for (RealVector realVector : graphics) {
            System.out.println(realVector);

        }
        CoordsArray[] coordsArray = new CoordsArray[graphics.size()];

        for (int i = 0; i < coordsArray.length; i++) {

            coordsArray[i] = new Coords2D(graphics.get(i).toDouble());
        }

        MultiPolyLine polyLine = new MultiPolyLine(coordsArray, new ViewingAttr(Color.yellow));
        RPnDataModule.SPEEDGRAPHICSPHASESPACE.join(polyLine);

        RealVector min = new RealVector("0 0");
        RealVector max = new RealVector(maxX_+" " + maxY_);

        updateSpeedGraphicsFrame(min, max);

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

    private List<RealVector> createGraphics(WaveCurveOrbitGeom waveCurveOrbitGeom) {
        ArrayList<RealVector> graphicsCoords = new ArrayList<RealVector>();
        List<WaveCurveBranchGeom> orbitGeom = waveCurveOrbitGeom.getOrbitGeom();

        for (WaveCurveBranchGeom waveCurveBranchGeom : orbitGeom) {

            WaveCurveOrbitGeom fundamentalGeom = (WaveCurveOrbitGeom) waveCurveBranchGeom;
            WaveCurveBranch geomSource = (WaveCurveBranch) fundamentalGeom.geomFactory().geomSource();

            List<OrbitPoint> branchPoints = geomSource.getBranchPoints();

            double referencePointSpeed = geomSource.getSpeed(geomSource.getReferencePoint());

            maxY_ = referencePointSpeed;
            RealVector referencePoint = new RealVector(0 + " " + referencePointSpeed);

            graphicsCoords.add(referencePoint);

            for (int i = 0; i < branchPoints.size() - 1; i++) {

                OrbitPoint secondPoint = geomSource.getBranchPoints().get(i + 1);

                double previousDistance = graphicsCoords.get(i).getElement(0);

                double distance = previousDistance + branchPoints.get(i).getCoords().distance(secondPoint);
                double speed = secondPoint.getLambda();

                if (speed > maxY_) {
                    maxY_ = speed;
                }

                if (distance > maxX_) {
                    maxX_ = distance;
                }

                StringBuilder stringCoords = new StringBuilder();

                stringCoords.append(distance).append(" ").append(speed);
                RealVector coords = new RealVector(stringCoords.toString());
                graphicsCoords.add(coords);

            }

        }

        return graphicsCoords;
    }

    private boolean checkCurvesForSpeedPlot(List<RpGeometry> selectedCurves) {

        if (selectedCurves.size() != 2) {
            return false;
        }

        boolean waveCurveForward0 = false;
        boolean waveCurveBackward1 = false;

        for (RpGeometry geometry : selectedCurves) {
            if (geometry instanceof WaveCurveOrbitGeom) {

            }
        }

        return (waveCurveForward0 && waveCurveBackward1);

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
