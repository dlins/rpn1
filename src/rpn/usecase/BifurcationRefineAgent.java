/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.Coords2D;
import wave.multid.model.MultiGeometry;
import wave.multid.view.ViewingTransform;
import wave.util.*;

public class BifurcationRefineAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Refine Bifurcation Curve";
    // Members
    //
    static private BifurcationRefineAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BifurcationRefineAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT);
        getContainer().setEnabled(false);

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        BifurcationCurveCalc curveCalc = RPNUMERICS.createBifurcationCalc();
        BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(curveCalc);
        AreaSelectionAgent.instance().getContainer().setSelected(false);
        AreaSelectionAgent.instance().eraseArea();
        removeBifurcationError();
        getContainer().setEnabled(false);

        return factory.geom();
//        return null;
    }

    static public BifurcationRefineAgent instance() {
        if (instance_ == null) {
            instance_ = new BifurcationRefineAgent();
        }
        return instance_;
    }

    private void removeBifurcationError() {

        Point topLeft = toDCcoords(RPNUMERICS.AREA_TOP);
        Point downRight = toDCcoords(RPNUMERICS.AREA_DOWN);

        double width = downRight.x - topLeft.x;
        double heigh = downRight.y - topLeft.y;

        Rectangle2D.Double rectangle = new Rectangle2D.Double(topLeft.x, topLeft.y, width, heigh);

        Iterator geometriesIterator = RPnDataModule.PHASESPACE.getGeomObjIterator();

        while (geometriesIterator.hasNext()) {

            RpGeometry geometry = (RpGeometry) geometriesIterator.next();

            if (geometry instanceof BifurcationCurveGeom) {

                BifurcationCurveGeomFactory factory = (BifurcationCurveGeomFactory) geometry.geomFactory();
                BifurcationCurve curve = (BifurcationCurve) factory.geomSource();

                List segmentsList = curve.segments();
                boolean toRemove = false;

                for (int i = 0; i < segmentsList.size(); i++) {

                    RealSegment segment = (RealSegment) segmentsList.get(i);

                    RealVector point1 = segment.p1();
                    RealVector point2 = segment.p2();

                    Point dcPoint1 = toDCcoords(point1);
                    Point dcPoint2 = toDCcoords(point2);

                    Line2D.Double bifurcationSegment = new Line2D.Double(dcPoint1, dcPoint2);

                    if (rectangle.intersectsLine(bifurcationSegment)) {
                        segmentsList.remove(i);
                        toRemove = true;
                    }

                }


                if (toRemove) {
                    RPnDataModule.PHASESPACE.delete(geometry);
                    RPnDataModule.PHASESPACE.update();
                    BifurcationSegGeom[] newBifurcationSegs = new BifurcationSegGeom[segmentsList.size()];

                    for (int i = 0; i < segmentsList.size(); i++) {

                        newBifurcationSegs[i] = new BifurcationSegGeom((RealSegment) segmentsList.get(i));
                    }


                    BifurcationCurveGeom bifurcationCurveGeom = new BifurcationCurveGeom(newBifurcationSegs, factory);
                    RPnDataModule.PHASESPACE.join(bifurcationCurveGeom);
                    RPnDataModule.PHASESPACE.update();

                }


            }

        }






    }

    private Point toDCcoords(RealVector input) {

        Coords2D wcCoords = new Coords2D(input.getElement(0), input.getElement(1));

        Coords2D dcCoords = new Coords2D();
        ViewingTransform viewingTransf = UIController.instance().getFocusPanel().scene().getViewingTransform();
        viewingTransf.viewPlaneTransform(wcCoords, dcCoords);

        Point point = new Point((int) dcCoords.getX(), (int) dcCoords.getY());

        return point;


    }
}
