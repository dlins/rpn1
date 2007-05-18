/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util.graphs;

import wave.multid.map.Map;
import wave.multid.view.Viewing2DTransform;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector2;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.Polygon;
import java.util.ArrayList;

public class wcWindow {
    //
    // Members
    //
    private Point2D.Double origin_;
    private GeneralPath polygon_;

    //
    // Constructors/Initializers
    //


    /** Constructs a world window.
     *@param defPoints coordinates of the vertices of the window
     *@param origin Lower left corner of the window. This point will be the 0,0 in the view port .
     */
    public wcWindow(RealVector2[] defPoints, Point2D.Double origin) {
        origin_ = new Point2D.Double(origin.getX(), origin.getY());
        polygon_ = new GeneralPath();
        for (int i = 0; i < defPoints.length; i++) {
            if (i == 0)
                polygon_.moveTo(new Double(defPoints[i].getX()).floatValue(), new Double(defPoints[i].getY()).floatValue());
            else
                polygon_.lineTo(new Double(defPoints[i].getX()).floatValue(), new Double(defPoints[i].getY()).floatValue());
        }
        polygon_.closePath();
    }

    //
    // Accessors/Mutators
    //
    public Point2D.Double getOriginPosition() { return origin_; }

    public double getWidth() { return polygon_.getBounds2D().getWidth(); }

    public double getHeight() { return polygon_.getBounds2D().getHeight(); }

    //
    // Methods
    //
    public Polygon dcView(ViewingTransform viewTransform) {
        double[] dcMatrix = new double[6];
        Map coordSysTransform = viewTransform.coordSysTransform();

		/*
		 * AffineTransform assumes Column Matrix !!!
         */

        if (viewTransform instanceof Viewing2DTransform) {
            dcMatrix[0] = coordSysTransform.getTransfMatrix().getElement(0, 0);
            dcMatrix[1] = coordSysTransform.getTransfMatrix().getElement(0, 1);
            dcMatrix[2] = coordSysTransform.getTransfMatrix().getElement(1, 0);
            dcMatrix[3] = coordSysTransform.getTransfMatrix().getElement(1, 1);
            dcMatrix[4] = coordSysTransform.getTransfMatrix().getElement(2, 0);
            dcMatrix[5] = coordSysTransform.getTransfMatrix().getElement(2, 1);
        } else {
            // We will assume a Z plane parallel
            // 3D projection for now
            dcMatrix[0] = coordSysTransform.getTransfMatrix().getElement(0, 0);
            dcMatrix[1] = coordSysTransform.getTransfMatrix().getElement(0, 1);
            dcMatrix[2] = coordSysTransform.getTransfMatrix().getElement(1, 0);
            dcMatrix[3] = coordSysTransform.getTransfMatrix().getElement(1, 1);
            dcMatrix[4] = coordSysTransform.getTransfMatrix().getElement(3, 0);
            dcMatrix[5] = coordSysTransform.getTransfMatrix().getElement(3, 1);
        }
        AffineTransform dcTransform = new AffineTransform(dcMatrix);
        PathIterator dcIterator = polygon_.getPathIterator(dcTransform);
        ArrayList vertexList = new ArrayList();
        while (!(dcIterator.isDone())) {
            double[] vertexNode = new double[6];
            int segType = dcIterator.currentSegment(vertexNode);
            // we are assuming a closed general path
            if (segType != PathIterator.SEG_CLOSE)
                vertexList.add(vertexNode);
            dcIterator.next();
        }
        // the Polygon
        int[] defPointsX = new int[vertexList.size()];
        int[] defPointsY = new int[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            defPointsX[i] = new Double(((double[]) vertexList.get(i)) [0]).intValue();
            defPointsY[i] = new Double(((double[]) vertexList.get(i)) [1]).intValue();
        }
        return new Polygon(defPointsX, defPointsY, vertexList.size());
    }
}
