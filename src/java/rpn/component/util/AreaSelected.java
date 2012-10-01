/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component.util;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.List;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

/**
 *
 * @author edsonlan
 */
public class AreaSelected extends GraphicsUtil {

    public AreaSelected(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);
    }

    @Override
    public Shape createShape() {

        PathIterator iterator = ((Path2D.Double) getWCObject()).getPathIterator(null);
        Polygon selectedPolygon = new Polygon();

        while (!iterator.isDone()) {

            double[] segmentArray = new double[2];

            int segment = iterator.currentSegment(segmentArray);
            if (segment != PathIterator.SEG_CLOSE) {

                Coords2D dcSelectionPoint = new Coords2D(0, 0);
                CoordsArray wcSelectionPoint = new CoordsArray(segmentArray);
                getViewingTransform().viewPlaneTransform(wcSelectionPoint, dcSelectionPoint);
                selectedPolygon.addPoint((int) dcSelectionPoint.getX(), (int) dcSelectionPoint.getY());
            }

            iterator.next();
        }

        return selectedPolygon;

    }

    @Override
    public Path2D.Double getWCObject() {
        Path2D.Double wcPath = (Path2D.Double) wcObjects_.get(0);
        return wcPath;

    }
}
