package rpn.controller.phasespace.extensions;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import rpn.command.DomainSelectionCommand;
import rpn.command.GenericExtensionCurveCommand;
import rpn.command.ImageSelectionCommand;
import rpn.component.RpGeometry;
import rpn.component.util.GraphicsUtil;
import rpn.parser.RPnDataModule;
import rpnumerics.ContourParams;
import rpnumerics.ExtensionCurveCalc;
import rpnumerics.RPNUMERICS;
import static rpnumerics.RPNUMERICS.getParamValue;
import rpnumerics.RpException;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingTransform;
import wave.util.RealSegment;
import wave.util.RealVector;

public class ExtensionCurveReadyState extends ExtensionCalcState {

    public ExtensionCurveReadyState(RpGeometry geom) {

        super(geom);
        GenericExtensionCurveCommand.instance().setEnabled(true);
        DomainSelectionCommand.instance().setEnabled(true);
        ImageSelectionCommand.instance().setEnabled(true);

    }

    public ExtensionCurveReadyState(List<RealSegment> segmentsToExtend) {
        super(segmentsToExtend);
        GenericExtensionCurveCommand.instance().setEnabled(true);
        DomainSelectionCommand.instance().setEnabled(true);
        ImageSelectionCommand.instance().setEnabled(true);
    }

    public void select(GraphicsUtil area) {

        List<Integer> segmentIndex = containsCurve(getSegmentsToExtend(), area);
        
        System.out.println("segmentos dentro : "+ segmentIndex.size());
        System.err.println("Quantidade de segmentos para estender: "+ getSegmentsToExtend().size());

        if (!segmentIndex.isEmpty()) {//Tests if there are some segments inside the area . The user is selecting the domain.

            List<Integer> indexToRemove = new ArrayList<Integer>();
            indexToRemove.addAll(segmentIndex);
            GenericExtensionCurveCommand.instance().setState(new ExtensionCurvePartReadyState(segmentsIntoArea(getSegmentsToExtend(), indexToRemove), area));

        } else { // No segments inside selection. The user is selecting the image.
            GenericExtensionCurveCommand.instance().setState(new ExtensionImageSelectionReady(getSegmentsToExtend(), area));
        }

    }

    public ExtensionCurveCalc createCalc() throws RpException {
        int withImageSelection = 0;

        int[] resolution_ = RPNUMERICS.processResolution(getParamValue("bifurcationcurve", "resolution"));
        int family_ = new Integer(getParamValue("extensioncurve", "family"));
        int characteristic_ = new Integer(getParamValue("extensioncurve", "characteristic"));
        boolean singular_ = Boolean.valueOf(getParamValue("extensioncurve", "singular"));
        System.out.println("Valor de singular: " + singular_);

        ArrayList<RealVector> realVectorList = new ArrayList<RealVector>();

        ExtensionCurveCalc calc = new ExtensionCurveCalc(new ContourParams(resolution_), getSegmentsToExtend(), realVectorList, family_, characteristic_, singular_, withImageSelection);

        return calc;

    }

    protected List<RealSegment> segmentsIntoArea(List<RealSegment> selectedGeometry, List<Integer> indexToRemove) {
        List<RealSegment> listSeg = new ArrayList<RealSegment>();

        for (int i = 0; i < indexToRemove.size(); i++) {
            int ind = Integer.parseInt((indexToRemove.get(i)).toString());
            listSeg.add(selectedGeometry.get(ind));
        }

        return listSeg;
    }

    protected List<Integer> containsCurve(List<RealSegment> curve, GraphicsUtil areaSelected) {
        List<Integer> indexList = new ArrayList<Integer>();
        ViewingTransform transf = areaSelected.getViewingTransform();

        List<RealSegment> segments = curve;

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

            Polygon polygon = (Polygon) areaSelected.getShape();

            if (polygon.contains(xMed, yMed)) {
                indexList.add(i);
            }

        }

        return indexList;
    }

}
