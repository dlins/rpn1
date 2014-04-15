/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.graphs;

import wave.multid.map.*;
import wave.multid.view.Viewing2DTransform;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector2;
import java.awt.geom.Point2D;
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

    public wcWindow(wcWindow copy) {

	this(copy.getDefPoints(),copy.getOriginPosition());	

    }

    //
    // Accessors/Mutators
    //
    public Point2D.Double getOriginPosition() { return origin_; }

    public double getWidth() { return polygon_.getBounds2D().getWidth(); }

    public double getHeight() { return polygon_.getBounds2D().getHeight(); }

    protected RealVector2[] getDefPoints() {


        PathIterator wcIterator = polygon_.getPathIterator(new AffineTransform());
        ArrayList vertexList = new ArrayList();

        while (!(wcIterator.isDone())) {
            double[] vertexNode = new double[6];
            int segType = wcIterator.currentSegment(vertexNode);
            // we are assuming a closed general path
            if (segType != PathIterator.SEG_CLOSE) {

                vertexList.add(vertexNode);
	    }
	
            wcIterator.next();
        }

        RealVector2[] defPoints = new RealVector2[vertexList.size()];
	for (int i = 0; i < vertexList.size(); i++)
		defPoints[i] = new RealVector2(((double[])vertexList.get(i))[0],((double[])vertexList.get(i))[1]);

	return defPoints;
    }

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

    public void update(Map map) {

        double[] wcMatrix = new double[6];

	/*
	 * AffineTransform assumes Column Matrix !!!
         */

        if (map.getDomain().getDim() == 2) { 
            wcMatrix[0] = map.getTransfMatrix().getElement(0, 0);
            wcMatrix[1] = map.getTransfMatrix().getElement(0, 1);
            wcMatrix[2] = map.getTransfMatrix().getElement(1, 0);
            wcMatrix[3] = map.getTransfMatrix().getElement(1, 1);
            wcMatrix[4] = map.getTransfMatrix().getElement(2, 0);
            wcMatrix[5] = map.getTransfMatrix().getElement(2, 1);
        } else if (map.getDomain().getDim() == 3) { 
            // We will assume a Z plane parallel
            // 3D projection for now
            wcMatrix[0] = map.getTransfMatrix().getElement(0, 0);
            wcMatrix[1] = map.getTransfMatrix().getElement(0, 1);
            wcMatrix[2] = map.getTransfMatrix().getElement(1, 0);
            wcMatrix[3] = map.getTransfMatrix().getElement(1, 1);
            wcMatrix[4] = map.getTransfMatrix().getElement(3, 0);
            wcMatrix[5] = map.getTransfMatrix().getElement(3, 1);
        }
        AffineTransform wcTransform = new AffineTransform(wcMatrix);
        PathIterator wcIterator = polygon_.getPathIterator(wcTransform);
        ArrayList vertexList = new ArrayList();
        while (!(wcIterator.isDone())) {
            double[] vertexNode = new double[6];
            int segType = wcIterator.currentSegment(vertexNode);
            // we are assuming a closed general path
            if (segType != PathIterator.SEG_CLOSE)
                vertexList.add(vertexNode);
            wcIterator.next();
        }

        // rebuild the Polygon and Origin
	double[] old_origin = new double[2];
	double[] new_origin = new double[2];
	old_origin[0] = origin_.getX();
	old_origin[1] = origin_.getY();

	wcTransform.transform(old_origin,0,new_origin,0,1);
	
        origin_ = new Point2D.Double(new_origin[0], new_origin[1]);
        polygon_ = new GeneralPath();
        for (int i = 0; i < vertexList.size(); i++) {
            if (i == 0)
                polygon_.moveTo(((double[])vertexList.get(i))[0],((double[])vertexList.get(i))[1]);
            else
                polygon_.lineTo(((double[])vertexList.get(i))[0],((double[])vertexList.get(i))[1]);
        }
        polygon_.closePath();
        
    }
}
