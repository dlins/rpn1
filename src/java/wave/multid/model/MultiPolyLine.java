/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.model;

import java.awt.Color;
import wave.multid.view.*;
import wave.multid.*;

public class MultiPolyLine extends MultiGeometryImpl {
    //
    // Members
    //

    private final int ALFA_DOWN = 20;
    private final int ALFA_UP = 255;

    //
    // Constructors
    //

    public MultiPolyLine(CoordsArray[] vertices, ViewingAttr viewAttr) {
        super(vertices[0].getSpace(), viewAttr);

        try {
            AbstractSegment[] segList = new AbstractSegment[vertices.length];
            CoordsArray[] moveToVx = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            CoordsArray[] lineToVx = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            moveToVx[0] = new CoordsArray(vertices[0]);
            moveToVx[1] = new CoordsArray(vertices[0].getSpace());
            segList[0] = new AbstractSegment(moveToVx, new AbstractSegmentAtt(AbstractSegment.SEG_MOVETO));
            int i = 1;
            for (; i < vertices.length; i++) {
                // LINETO needs 2 coords only
                lineToVx[0] = new CoordsArray(vertices[i - 1]);
                lineToVx[1] = new CoordsArray(vertices[i]);
                segList[i] = new AbstractSegment(lineToVx, new AbstractSegmentAtt(AbstractSegment.SEG_LINETO));
            }
            for (int j = 0; j < segList.length; j++) {
                append(segList[j], false);
            }
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        } catch (WrongNumberOfDefPointsEx wex) {
            wex.printStackTrace();
        }
    }

    public MultiPolyLine(AbstractSegment[] segments, ViewingAttr viewAttr) {
        super(segments[0].getSpace(), viewAttr);
        int i = 0;
        try {
            for (i = 0; i < segments.length; i++) {
                append(segments[i], false);
            }
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new PolyLine(this, transf, viewingAttr());
    }

    public void lowLight() {
        Color newColor = new Color(viewingAttr().getColor().getRed(), viewingAttr().getColor().getGreen(), viewingAttr().getColor().getBlue(), ALFA_DOWN);
        viewingAttr().setColor(newColor);

    }

    public void highLight() {

        Color newColor = new Color(viewingAttr().getColor().getRed(), viewingAttr().getColor().getGreen(), viewingAttr().getColor().getBlue(), ALFA_UP);
        viewingAttr().setColor(newColor);


    }

    @Override
    public void setVisible(boolean visible) {
        viewingAttr().setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return viewingAttr().isVisible();
    }

    @Override
    public void setSelected(boolean selected) {
       
        viewingAttr().setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return viewingAttr().isSelected();
    }
}
