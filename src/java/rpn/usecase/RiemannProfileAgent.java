/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpnumerics.*;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.GeomObjView;
import wave.multid.view.Scene;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class RiemannProfileAgent extends RpModelPlotAgent {
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

    //
    // Constructors/Initializers
    //
    protected RiemannProfileAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("Em action performed");
        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return null;
    }

    @Override
    public void execute() {

        List<Area> areaList = AreaSelectionAgent.instance().getListArea();

        Area firstArea = null;

        Iterator<RPnPhaseSpacePanel> panelsIterator = UIController.instance().getInstalledPanelsIterator();
        WaveCurve waveCurveForward0 = null;
        WaveCurve waveCurveBackward1 = null;

        while (panelsIterator.hasNext()) {

            RPnPhaseSpacePanel rPnPhaseSpacePanel = panelsIterator.next();

            List<GeomObjView> intersectCurves = rPnPhaseSpacePanel.intersectAreas();




            System.out.println(intersectCurves);
//            
//for (Rectangle2D.Double rect: rPnPhaseSpacePanel.getCastedUI().getSelectionAreas()){
//    
//    System.out.println(rect);
//    

//}


//
//            for (GeomObjView geomObjView : intersectCurves) {
//
//                if (geomObjView instanceof WaveCurveView) {
//
//                    WaveCurveGeom waveCurveGeom = (WaveCurveGeom) geomObjView.getAbstractGeom();
//
//                    WaveCurve waveCurve = (WaveCurve) waveCurveGeom.geomFactory().geomSource();
//
//                    if (waveCurve.getFamily() == 0 && waveCurve.getDirection() == 10) {
//
//                        waveCurveForward0 = waveCurve;
//
//                    } else {
//                        waveCurveBackward1 = waveCurve;
//
//                    }
//
//                }
//            }












        }



//        
//        
//        RiemannProfileCalc rc = new RiemannProfileCalc(firstArea, waveCurveForward0, waveCurveBackward1);
//
//        RiemannProfileGeomFactory riemannProfileGeomFactory = new RiemannProfileGeomFactory(rc);
//
//        RPnDataModule.RIEMANNPHASESPACE.join(riemannProfileGeomFactory.geom());
//
//        RPnDataModule.RIEMANNPHASESPACE.update();
//
//        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getRiemannFrames()) {
//
//            frame.setVisible(true);
//
//        }

    }

    static public RiemannProfileAgent instance() {
        if (instance_ == null) {
            instance_ = new RiemannProfileAgent();
        }
        return instance_;
    }

    void setForwardWaveCurve(WaveCurve forwardWaveCurve) {
        waveCurveForward_ = forwardWaveCurve;
    }

    void setBackwardWaveCurve(WaveCurve backwardWaveCurve) {
        waveCurveBackward_ = backwardWaveCurve;
    }

    public Area createWCArea(Scene scene, Rectangle2D.Double viewArea) {

        ViewingTransform viewTransform = scene.getViewingTransform();

        Rectangle bounds = viewArea.getBounds();

        Point upLefPointCorner = bounds.getLocation();

        Coords2D upLeftCorner = new Coords2D(upLefPointCorner.x, upLefPointCorner.y);

        Coords2D downRightCorner = new Coords2D(upLefPointCorner.x + bounds.width, upLefPointCorner.y + bounds.height);

        CoordsArray upLeftCornerArray = new CoordsArray(new Space("", 2));
        CoordsArray downRightArray = new CoordsArray(new Space("", 2));

        viewTransform.dcInverseTransform(upLeftCorner, upLeftCornerArray);

        viewTransform.dcInverseTransform(downRightCorner, downRightArray);

        RealVector upLeftCornerVector = new RealVector(upLeftCornerArray.getCoords());

        RealVector downRightArrayVector = new RealVector(downRightArray.getCoords());

        double[] dummyResolution = {10, 10};

        RealVector dummyResolutionVector = new RealVector(dummyResolution);

        Area result = new Area(dummyResolutionVector, upLeftCornerVector, downRightArrayVector);

        return result;



    }
}
