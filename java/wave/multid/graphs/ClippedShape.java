/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.graphs;

import wave.util.RectBoundary;
import wave.util.RealVector2;
import wave.util.RealVector;
import wave.util.Boundary;
import wave.multid.map.ProjectionMap;
import java.awt.geom.Point2D;

// TODO make this an interface and
// at the application level instantiate
// the specific boundary implementation
public class ClippedShape {
    //
    // Members
    //
    private Boundary bound_;
    private boolean isRectangular_;

    //
    // Constructors/Initializers
    //
    public ClippedShape(Boundary bound) {
        bound_ = bound;
        if (bound_ instanceof RectBoundary)
            isRectangular_ = true;
        else
            isRectangular_ = false;
    }

    //
    // Accessors/Mutators
    //
    public boolean isRectangular() { return isRectangular_; }

    public RealVector getMaximums() { return bound_.getMaximums(); }

    public RealVector getMinimums() { return bound_.getMinimums(); }

    //
    // Methods
    //
    public wcWindow createWindow(ProjectionMap proj) {
        if (isRectangular())
            return createRectWindow(proj);
        return createTriangWindow(proj);
    }

    protected wcWindow createTriangWindow(ProjectionMap proj) {
        // in case of 3D projection we will assume
        // the Z plane parallel projection
        int absIndx = proj.getCompIndexes() [0];
        int ordIndx = proj.getCompIndexes() [1];
        RealVector2[] defPoints = new RealVector2[3];
        defPoints[0] = new RealVector2(bound_.getMinimums().getElement(absIndx), bound_.getMinimums().getElement(ordIndx));
        defPoints[1] = new RealVector2(bound_.getMinimums().getElement(absIndx), bound_.getMaximums().getElement(ordIndx));
        defPoints[2] = new RealVector2(bound_.getMaximums().getElement(absIndx), bound_.getMinimums().getElement(ordIndx));
        // by default we are getting the lower left corner
        return new wcWindow(defPoints,
            new Point2D.Double(bound_.getMinimums().getElement(absIndx), bound_.getMinimums().getElement(ordIndx)));
    }

    protected wcWindow createRectWindow(ProjectionMap proj) {
        // in case of 3D projection we will assume
        // the Z plane parallel projection
        int absIndx = proj.getCompIndexes() [0];
        int ordIndx = proj.getCompIndexes() [1];
        RealVector2[] defPoints = new RealVector2[4];
        defPoints[0] = new RealVector2(bound_.getMinimums().getElement(absIndx), bound_.getMinimums().getElement(ordIndx));
        defPoints[1] = new RealVector2(bound_.getMinimums().getElement(absIndx), bound_.getMaximums().getElement(ordIndx));
        defPoints[2] = new RealVector2(bound_.getMaximums().getElement(absIndx), bound_.getMaximums().getElement(ordIndx));
        defPoints[3] = new RealVector2(bound_.getMaximums().getElement(absIndx), bound_.getMinimums().getElement(ordIndx));
        // by default we are getting the lower left corner
        return new wcWindow(defPoints,
            new Point2D.Double(bound_.getMinimums().getElement(absIndx), bound_.getMinimums().getElement(ordIndx)));
    }
}
