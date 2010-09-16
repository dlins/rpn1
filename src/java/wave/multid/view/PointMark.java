/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.view;

import wave.multid.model.*;
import wave.multid.Coords2D;
import wave.multid.map.Map;
import wave.multid.DimMismatchEx;
import wave.multid.CoordsArray;
import java.awt.Shape;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

public class PointMark extends ShapedGeometry {
    //
    // Constants
    //
    public static final double DEFAULT_WIDTH = 2d;
    public static final double DEFAULT_HEIGHT = 2d;

    //
    // Members
    //
    //
    // Constructors
    //
    public PointMark(MultiGeometryImpl abstractGeom, ViewingTransform transf, ViewingAttr viewAttr) throws DimMismatchEx {
        super(abstractGeom, transf, viewAttr);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    public void draw(Graphics2D g) {
        Color previous = g.getColor();
        g.setColor(getViewingAttr().getColor());
        g.fill(getShape());
        if (getViewingAttr().isSelected())
            getBoundaryBox().draw(g);
        g.setColor(previous);
    }

    public Shape createShape() throws DimMismatchEx {
        Rectangle2D mark = new Rectangle2D.Double();
        AbstractPathIterator modelIterator = ((MultiGeometry)getAbstractGeom()).getPathIterator();
        modelIterator.next();
        CoordsArray[] firstSegCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        AbstractSegmentAtt firstSegAtt = modelIterator.currentSegment(firstSegCoords);
        CoordsArray wc_coords = firstSegCoords[0];
        Coords2D dc_coords = new Coords2D();
        getViewingTransform().viewPlaneTransform(wc_coords, dc_coords);
        mark.setRect(dc_coords.getCoords() [0] - DEFAULT_WIDTH / 2, dc_coords.getCoords() [1] - DEFAULT_HEIGHT / 2,
            DEFAULT_WIDTH, DEFAULT_HEIGHT);
        return mark;
    }
}
