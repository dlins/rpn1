/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.view;

import wave.multid.map.*;
import wave.multid.CoordsArray;
import wave.multid.Coords2D;
import wave.multid.Coords3D;
import wave.multid.Multid;
import wave.util.graphs.*;

public class Viewing3DTransform implements ViewingTransform {
    //
    // Constants
    //
    public static final int ABSISSA_INDEX = 0;
    public static final int ORDINATE_INDEX = 1;
    public static final int DEPTH_INDEX = 2;
    //
    // Members
    //
    private ProjectionMap projection_;
    private IdentityMap coordSysTransform_;
    private ViewPlane viewPlane_;
    private double[] viewReferencePoint_;
    private IdentityMap compositeTransform_;
    private double zCoord_;

    //
    // Constructors
    //
    public Viewing3DTransform(ProjectionMap projection, ViewPlane viewPlane, double[] VRP, double vp_Z_Position) {
        projection_ = projection;
        viewPlane_ = viewPlane;
        viewReferencePoint_ = new double[VRP.length];
        System.arraycopy(VRP, 0, viewReferencePoint_, 0, VRP.length);

        /*
         * the zCoord and some other data will be
         * more useful when we start working with
         * the camera model. For now it will only
         * be used to set the third coord at the
         * inverse transformation
         */

        zCoord_ = vp_Z_Position;

        makeCoordSysTransform();
        makeCompositeTransform();


      }

    //
    // Accessors/Mutators
    //
    public ProjectionMap projectionMap() { return projection_; }

    public Map coordSysTransform() { return coordSysTransform_; }

    public ViewPlane viewPlane() { return viewPlane_; }

    public Map viewingMap() { return compositeTransform_; }

    public double[] viewReferencePoint(){return viewReferencePoint_;}

    public void setViewPlane(ViewPlane vPlane){
      viewPlane_=vPlane;
      makeCoordSysTransform();
      makeCompositeTransform();
    }

    public double zCoord(){return zCoord_;}

    //
    // Methods
    //

    /*
     * these linear transformations are post-multiplication (T=tXT)
     */

    public void rotateX(double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        IdentityMap rotateTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        rotateTransform.getTransfMatrix().setElement(1, 1, cos);
        rotateTransform.getTransfMatrix().setElement(1, 2, sin);
        rotateTransform.getTransfMatrix().setElement(2, 1, -sin);
        rotateTransform.getTransfMatrix().setElement(2, 2, cos);
        coordSysTransform().getTransfMatrix().mul(rotateTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        makeCompositeTransform();
    }

    public void rotateY(double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        IdentityMap rotateTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        rotateTransform.getTransfMatrix().setElement(0, 0, cos);
        rotateTransform.getTransfMatrix().setElement(0, 2, -sin);
        rotateTransform.getTransfMatrix().setElement(2, 0, sin);
        rotateTransform.getTransfMatrix().setElement(2, 2, cos);
        coordSysTransform().getTransfMatrix().mul(rotateTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        makeCompositeTransform();
    }

    public void rotateZ(double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        IdentityMap rotateTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        rotateTransform.getTransfMatrix().setElement(0, 0, cos);
        rotateTransform.getTransfMatrix().setElement(0, 1, sin);
        rotateTransform.getTransfMatrix().setElement(1, 0, -sin);
        rotateTransform.getTransfMatrix().setElement(1, 1, cos);
        coordSysTransform().getTransfMatrix().mul(rotateTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        makeCompositeTransform();
    }

    public void translate(double tX, double tY, double tZ) {
        IdentityMap translateTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        translateTransform.getTransfMatrix().setElement(2, 0, tX);
        translateTransform.getTransfMatrix().setElement(2, 1, tY);
        translateTransform.getTransfMatrix().setElement(3, 1, tZ);
        coordSysTransform().getTransfMatrix().mul(translateTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        makeCompositeTransform();
    }

    public void scale(double sX, double sY, double sZ) {
        IdentityMap scaleTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        scaleTransform.getTransfMatrix().setElement(0, 0, sX);
        scaleTransform.getTransfMatrix().setElement(1, 1, sY);
        scaleTransform.getTransfMatrix().setElement(2, 2, sZ);
        coordSysTransform().getTransfMatrix().mul(scaleTransform.getTransfMatrix(), coordSysTransform().getTransfMatrix());
        makeCompositeTransform();
    }

    public void makeCoordSysTransform() {
 	/*
	 * COORDINATE SYSTEM TRANSFORMATION
         *
         * this is the translation + scaling + translation coordinate
    	 * System transformation
         *
         * We are assuming the camera model settings :
         *
         * - ViewPlane normal parallel to Z (SXP,SYP = 0)
	 	 * the parallel projection Z factors don't take effect...
         * - ViewUp vector = (0,0,1)
         * - ViewDistance = 0
	 *
	 */

        coordSysTransform_ = new IdentityMap(Multid.SPACE, Multid.SPACE);
        double XScaleFactor = viewPlane_.getViewport().getWidth() / viewPlane_.getWindow().getWidth();
        // not to be upside down...
        double YScaleFactor = -viewPlane_.getViewport().getHeight() / viewPlane_.getWindow().getHeight();
        // no scaling on Z
        double ZScaleFactor = 1;
        double XTranslateFactor = -viewReferencePoint_[0] * XScaleFactor;
        double YTranslateFactor = -viewReferencePoint_[1] * YScaleFactor;
        double ZTranslateFactor = -viewReferencePoint_[2] * ZScaleFactor;
        XTranslateFactor += -viewPlane_.getWindow().getOriginPosition().x * XScaleFactor;
        // we are working with RASTER
        YTranslateFactor += -viewPlane_.getWindow().getOriginPosition().y * YScaleFactor +
            viewPlane_.getViewport().getHeight();
        // the viewport translation
        XTranslateFactor += viewPlane_.getViewport().getOriginPosition().x;
        YTranslateFactor += viewPlane_.getViewport().getOriginPosition().y;
        coordSysTransform_.getTransfMatrix().setElement(0, 0, XScaleFactor);
        coordSysTransform_.getTransfMatrix().setElement(1, 1, YScaleFactor);
        coordSysTransform_.getTransfMatrix().setElement(2, 2, ZScaleFactor);
        coordSysTransform_.getTransfMatrix().setElement(3, 0, XTranslateFactor);
        coordSysTransform_.getTransfMatrix().setElement(3, 1, YTranslateFactor);
        coordSysTransform_.getTransfMatrix().setElement(3, 2, ZTranslateFactor);
        coordSysTransform_.setInversible(true);
    }

    public void makeCompositeTransform() {
		/*
        * COMPOSITE TRANSFORMATION
        *
		* now we concatenate the projection map. It will carry the
        * coordinate system transformation parameters along with it
        */

        compositeTransform_ = new IdentityMap(projection_.getDomain(), Multid.SPACE);
        compositeTransform_.setTransfMatrix(projection_.getTransfMatrix());
        compositeTransform_.concatenate(coordSysTransform_);
    }

    public void viewPlaneTransform(CoordsArray worldCoords, Coords2D dcCoords) {
        CoordsArray swapCoords = new CoordsArray(Multid.SPACE);
        compositeTransform_.image(worldCoords, swapCoords);
        // throws depth coord away
        dcCoords.setElement(0, swapCoords.getElement(0));
        dcCoords.setElement(1, swapCoords.getElement(1));
    }

    public void dcInverseTransform(Coords2D dcCoords, CoordsArray worldCoords) {
        // we will use the matrix multiplication
        // for the image only for now...
        try {
            CoordsArray swapCoords = new CoordsArray(Multid.SPACE);
            // 0. is given just to avoid a dimMismatchException
            CoordsArray dcProjCoords = new CoordsArray(
                new double[] {
                    dcCoords.getElement(0), dcCoords.getElement(1), 0.
                });
            coordSysTransform_.inverse(dcProjCoords, swapCoords);
            // all unknown values are set to zero...
            worldCoords.setZero();
            worldCoords.setElement(projection_.getCompIndexes() [ABSISSA_INDEX], swapCoords.getCoords() [ABSISSA_INDEX]);
            worldCoords.setElement(projection_.getCompIndexes() [ORDINATE_INDEX], swapCoords.getCoords() [ORDINATE_INDEX]);
            worldCoords.setElement(projection_.getCompIndexes() [DEPTH_INDEX], zCoord_);
        } catch (NoInverseMapEx noInvEx) {
            noInvEx.printStackTrace();
        }
    }
}
