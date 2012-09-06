/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnUIFrame;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.Scene;
import wave.multid.view.ShapedGeometry;
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

        Area firstArea = areaList.get(areaList.size() - 1);

        Iterator<RPnPhaseSpacePanel> panelsIterator = UIController.instance().getInstalledPanelsIterator();

        WaveCurve waveCurveForward0 = null;
        WaveCurve waveCurveBackward1 = null;

        while (panelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = panelsIterator.next();

            Scene scene = rPnPhaseSpacePanel.scene();

            Iterator sceneIterator = scene.geometries();

            while (sceneIterator.hasNext()) {

                GeomObjView geomView = (GeomObjView) sceneIterator.next();

                if (geomView instanceof WaveCurveView) {

                    ShapedGeometry shapedGeometry = (ShapedGeometry) geomView;


                    if (shapedGeometry.intersects(firstArea)) {

                        WaveCurveGeom waveCurveGeom = (WaveCurveGeom) geomView.getAbstractGeom();


                        WaveCurve waveCurve = (WaveCurve) waveCurveGeom.geomFactory().geomSource();

                        if (waveCurve.getFamily() == 0 && waveCurve.getDirection() == 10) {

                            waveCurveForward0 = waveCurve;

                        } else {
                            waveCurveBackward1 = waveCurve;

                        }

                    }

                }
            }
        }

        RiemannProfileCalc rc = new RiemannProfileCalc(firstArea, waveCurveForward0, waveCurveBackward1);

        RiemannProfileGeomFactory riemannProfileGeomFactory = new RiemannProfileGeomFactory(rc);

        RPnDataModule.RIEMANNPHASESPACE.join(riemannProfileGeomFactory.geom());

        RPnDataModule.RIEMANNPHASESPACE.update();

        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getRiemannFrames()) {

            frame.setVisible(true);

        }

    }

    static public RiemannProfileAgent instance() {
        if (instance_ == null) {
            instance_ = new RiemannProfileAgent();
        }
        return instance_;
    }
}
