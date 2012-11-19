
package rpn.component.util;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D.Double;
import java.util.List;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author moreira
 */
public class LinePlotted extends GraphicsUtil {

    public LinePlotted(List<Object> wcObjects, ViewingTransform viewingTransform, ViewingAttr viewAttr) {
        super(wcObjects, viewingTransform, viewAttr);
    }

    @Override
    public Shape createShape() {

        Line2D line = (Line2D) wcObjects_.get(0);

        Coords2D dcPoint1 = new Coords2D();
        Coords2D dcPoint2 = new Coords2D();
        CoordsArray wcPoint1 = new CoordsArray(new double[]{line.getX1(), line.getY1()});
        CoordsArray wcPoint2 = new CoordsArray(new double[]{line.getX2(), line.getY2()});

        getViewingTransform().viewPlaneTransform(wcPoint1, dcPoint1);
        getViewingTransform().viewPlaneTransform(wcPoint2, dcPoint2);

        return new Line2D.Double(dcPoint1.getX(), dcPoint1.getY(), dcPoint2.getX(), dcPoint2.getY());

    }

    @Override
    public Double getWCObject() {
        return null;
    }

}
