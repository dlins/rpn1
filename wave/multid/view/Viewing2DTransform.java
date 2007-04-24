/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.view;

import wave.multid.map.*;
import wave.multid.CoordsArray;
import wave.multid.Coords2D;
import wave.multid.Multid;
import wave.util.graphs.ViewPlane;

public class Viewing2DTransform implements ViewingTransform {
    //
    // Constants
    //
    public static final int ABSISSA_INDEX = 0;
    public static final int ORDINATE_INDEX = 1;
    //
    // Members
    //
    private ProjectionMap projection_;
    private IdentityMap coordSysTransform_;
    private ViewPlane viewPlane_;
    private IdentityMap compositeTransform_;

    //
    // Constructors
    //
    public Viewing2DTransform(ProjectionMap projection, ViewPlane viewPlane) {
        projection_ = projection;
        viewPlane_ = viewPlane;
        makeCoordSysTransform();
        makeCompositeTransform();

    }

    //
    // Accessors/Mutators
    //
    public ProjectionMap projectionMap() { return projection_; }

    public Map coordSysTransform() { return coordSysTransform_; }

    public Map viewingMap() { return compositeTransform_; }

    public ViewPlane viewPlane() { return viewPlane_; }


    public void setViewPlane(ViewPlane vPlane){
      viewPlane_=vPlane;
      makeCoordSysTransform();
      makeCompositeTransform();

    }

    //
    // Methods
    //

    /*
     * these linear transformations are post-multiplication (T=tXT)
     */

    public void shear(double xshear, double yshear) {
        IdentityMap shearTransform = new IdentityMap(Multid.PLANE, Multid.PLANE);
        shearTransform.getTransfMatrix().setElement(0, 1, yshear);
        shearTransform.getTransfMatrix().setElement(1, 0, xshear);
        coordSysTransform().getTransfMatrix().mul(shearTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        // updates the composite transformation
        makeCompositeTransform();
    }

    public void rotate(double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        IdentityMap rotateTransform = new IdentityMap(Multid.PLANE, Multid.PLANE);
        rotateTransform.getTransfMatrix().setElement(0, 0, cos);
        rotateTransform.getTransfMatrix().setElement(0, 1, sin);
        rotateTransform.getTransfMatrix().setElement(1, 0, -sin);
        rotateTransform.getTransfMatrix().setElement(1, 1, cos);
        coordSysTransform().getTransfMatrix().mul(rotateTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        // updates the composite transformation
        makeCompositeTransform();
    }

    public void translate(double tX, double tY) {
        IdentityMap translateTransform = new IdentityMap(Multid.PLANE, Multid.PLANE);
        translateTransform.getTransfMatrix().setElement(2, 0, tX);
        translateTransform.getTransfMatrix().setElement(2, 1, tY);
        coordSysTransform().getTransfMatrix().mul(translateTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        // updates the composite transformation
        makeCompositeTransform();
    }

    public void scale(double sX, double sY) {
        IdentityMap scaleTransform = new IdentityMap(Multid.PLANE, Multid.PLANE);
        scaleTransform.getTransfMatrix().setElement(0, 0, sX);
        scaleTransform.getTransfMatrix().setElement(1, 1, sY);
        coordSysTransform().getTransfMatrix().mul(scaleTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        // updates the composite transformation
        makeCompositeTransform();
    }

    public void makeCoordSysTransform() {
 		/*
	     * COORDINATE SYSTEM TRANSFORMATION
         *
         * this is the translation + scaling + translation coordinate
    	 * System transformation
	     */

        coordSysTransform_ = new IdentityMap(Multid.PLANE, Multid.PLANE);
        double XScaleFactor = viewPlane_.getViewport().getWidth() / viewPlane_.getWindow().getWidth();
        // not to be upside down...
        double YScaleFactor = -viewPlane_.getViewport().getHeight() / viewPlane_.getWindow().getHeight();
        double XTranslateFactor = -viewPlane_.getWindow().getOriginPosition().x * XScaleFactor;
        // we are working with RASTER
        double YTranslateFactor = -viewPlane_.getWindow().getOriginPosition().y * YScaleFactor +
            viewPlane_.getViewport().getHeight();
        // the viewport translation
        XTranslateFactor += viewPlane_.getViewport().getOriginPosition().x;
        YTranslateFactor += viewPlane_.getViewport().getOriginPosition().y;
        coordSysTransform_.getTransfMatrix().setElement(0, 0, XScaleFactor);
        coordSysTransform_.getTransfMatrix().setElement(1, 1, YScaleFactor);
        coordSysTransform_.getTransfMatrix().setElement(2, 0, XTranslateFactor);
        coordSysTransform_.getTransfMatrix().setElement(2, 1, YTranslateFactor);
        coordSysTransform_.setInversible(true);
    }

    public void makeCompositeTransform() {
		/*
        * COMPOSITE TRANSFORMATION
        *
		* now we concatenate the projection map. It will carry the
        * coordinate system transformation parameters along with it
        */

        compositeTransform_ = new IdentityMap(projection_.getDomain(), Multid.PLANE);
        compositeTransform_.setTransfMatrix(projection_.getTransfMatrix());
        compositeTransform_.concatenate(coordSysTransform_);
    }

    public void viewPlaneTransform(CoordsArray worldCoords, Coords2D dcCoords) {
        compositeTransform_.image(worldCoords, dcCoords);
    }

    public void dcInverseTransform(Coords2D dcCoords, CoordsArray worldCoords) {
        // we will use the matrix multiplication
        // for the image only for now...
        try {
            CoordsArray swapCoords = new CoordsArray(Multid.PLANE);
            coordSysTransform_.inverse(dcCoords, swapCoords);
            // all unknown values are set to zero...
            worldCoords.setZero();
            worldCoords.setElement(projection_.getCompIndexes() [ABSISSA_INDEX], swapCoords.getCoords() [ABSISSA_INDEX]);
            worldCoords.setElement(projection_.getCompIndexes() [ORDINATE_INDEX], swapCoords.getCoords() [ORDINATE_INDEX]);
        } catch (NoInverseMapEx noInvEx) {
            noInvEx.printStackTrace();
        }
    }
}
