/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.command;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import rpn.RPnPhaseSpacePanel;
import rpn.component.BifurcationCurveGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;

import rpn.controller.phasespace.extensions.ExtensionCalcState;
import rpn.controller.phasespace.extensions.ExtensionCalcWaitState;
import rpn.controller.phasespace.extensions.ExtensionCurveReadyState;
import rpnumerics.ExtensionCurveCalc;
import rpnumerics.RPnCurve;
import rpnumerics.RpException;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolygon;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.util.RealSegment;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class GenericExtensionCurveCommand extends BifurcationPlotCommand {

    static public final String DESC_TEXT = "Extension Curve";
    //
    // Members
    //
    private static GenericExtensionCurveCommand instance_ = null;
    private RpGeometry curveToProcess_ = null;
    private RPnPhaseSpacePanel panelToProcess_ = null;
    private List<MultiPolygon> imageSelection_;//TODO To use a list of areas ?

    private ExtensionCalcState state_;

    //
    // Constructors
    //
    protected GenericExtensionCurveCommand() {

        super(DESC_TEXT, null, new JMenuItem(DESC_TEXT));
        imageSelection_ = new ArrayList<MultiPolygon>();
        state_ = new ExtensionCalcWaitState();
    }

    public void execute() {
        try {
            ExtensionCurveReadyState state = (ExtensionCurveReadyState) state_;
            
            ExtensionCurveCalc calc = state.createCalc();
            
            
            BifurcationCurveGeomFactory bifurcationFactory = new BifurcationCurveGeomFactory(calc);
            
            super.execute(bifurcationFactory);
        } catch (RpException ex) {
            Logger.getLogger(GenericExtensionCurveCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ExtensionCalcState getState() {
        return state_;
    }

    public void setState(ExtensionCalcState state_) {
        this.state_ = state_;
    }

    public void unexecute() {
    }

    static public GenericExtensionCurveCommand instance() {
        if (instance_ == null) {
            instance_ = new GenericExtensionCurveCommand();
        }
        return instance_;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        execute();
    }

    private RpGeometry extendsWholeCurve(RpGeometry selectedGeometry) {

        RpGeomFactory factory = selectedGeometry.geomFactory();
        RPnCurve curve = (RPnCurve) factory.geomSource();
        List<RealSegment> segments = curve.segments();

        ExtensionCurveCalc calc = rpnumerics.RPNUMERICS.createExtensionCurveCalc(segments, null);
        BifurcationCurveGeomFactory bifurcationFactory = new BifurcationCurveGeomFactory(calc);

        return bifurcationFactory.geom();

    }

    private RpGeometry extendsPartOfCurve(RpGeometry selectedGeometry, RPnPhaseSpacePanel phaseSpacePanel) {

        List<Integer> indexToRemove = new ArrayList<Integer>();

        RpGeomFactory factory = selectedGeometry.geomFactory();
        RPnCurve curve = (RPnCurve) factory.geomSource();

        List<AreaSelected> selectedAreas = phaseSpacePanel.getSelectedAreas();
        List<RealSegment> segments;

        CoordsArray[] areaPointsList = null;
        if (selectedAreas.isEmpty()) {
            segments = curve.segments();

        } else {

            for (AreaSelected polygon : selectedAreas) {
                Iterator geomIterator = phaseSpacePanel.scene().geometries();
                while (geomIterator.hasNext()) {
                    GeomObjView geomObjView = (GeomObjView) geomIterator.next();
                    if (((RpGeometry) geomObjView.getAbstractGeom()) == selectedGeometry) {
                        List<Integer> segmentIndex = containsCurve(curve, (Polygon) polygon.getShape(), phaseSpacePanel);
                        if (!segmentIndex.isEmpty()) {
                            indexToRemove.addAll(segmentIndex);
                        }
                    }
                }
            }

            segments = segmentsIntoArea(selectedGeometry, indexToRemove);

        }

        if (!imageSelection_.isEmpty()) {

            areaPointsList = imageSelection_.get(0).extractVertices();
        }

        ExtensionCurveCalc calc = rpnumerics.RPNUMERICS.createExtensionCurveCalc(segments, areaPointsList);
        BifurcationCurveGeomFactory bifurcationFactory = new BifurcationCurveGeomFactory(calc);

        return bifurcationFactory.geom();

    }

    public void setGeometryAndPanel(RpGeometry phasSpaceGeometry, RPnPhaseSpacePanel panel) {
        curveToProcess_ = phasSpaceGeometry;
        panelToProcess_ = panel;

    }

    private List<RealSegment> segmentsIntoArea(RpGeometry selectedGeometry, List<Integer> indexToRemove) {
        List<RealSegment> listSeg = new ArrayList<RealSegment>();
        RpGeomFactory factory = selectedGeometry.geomFactory();
        RPnCurve curve = (RPnCurve) factory.geomSource();

        for (int i = 0; i < indexToRemove.size(); i++) {
            int ind = Integer.parseInt((indexToRemove.get(i)).toString());
            listSeg.add(curve.segments().get(ind));
        }

        return listSeg;
    }

    private List<Integer> containsCurve(RPnCurve curve, Polygon polygon, RPnPhaseSpacePanel panel) {
        List<Integer> indexList = new ArrayList<Integer>();
        ViewingTransform transf = panel.scene().getViewingTransform();

        ArrayList segments = (ArrayList) curve.segments();

        for (int i = 0; i < segments.size(); i++) {
            RealVector p1 = new RealVector(((RealSegment) segments.get(i)).p1());
            CoordsArray wcCoordsCurve1 = new CoordsArray(p1);
            Coords2D dcCoordsCurve1 = new Coords2D();
            transf.viewPlaneTransform(wcCoordsCurve1, dcCoordsCurve1);
            double xCurve1 = dcCoordsCurve1.getElement(0);
            double yCurve1 = dcCoordsCurve1.getElement(1);

            RealVector p2 = new RealVector(((RealSegment) segments.get(i)).p2());
            CoordsArray wcCoordsCurve2 = new CoordsArray(p2);
            Coords2D dcCoordsCurve2 = new Coords2D();
            transf.viewPlaneTransform(wcCoordsCurve2, dcCoordsCurve2);
            double xCurve2 = dcCoordsCurve2.getElement(0);
            double yCurve2 = dcCoordsCurve2.getElement(1);

            double xMed = (xCurve1 + xCurve2) * 0.5;
            double yMed = (yCurve1 + yCurve2) * 0.5;

            if (polygon.contains(xMed, yMed)) {
                indexList.add(i);
            }

        }

        return indexList;
    }

    void setSelectedArea(MultiPolygon areaSelected) {
        imageSelection_.clear();
        imageSelection_.add(areaSelected);

    }
}
