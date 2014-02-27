/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;

import wave.multid.CoordsArray;
import wave.multid.HomogeneousCoords;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.Multid;
import wave.util.RealMatrix2;

public class Viewing3DMap extends IdentityMap {
    //
    // Members
    //

    //
    // Constructors
    //
    public Viewing3DMap() {
	super(Multid.SPACE,Multid.SPACE);
    }

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

        getTransfMatrix().mul(rotateTransform.getTransfMatrix(), getTransfMatrix());
    }

    public void rotateY(double radians) {

        double sin = Math.sin(radians);
        double cos = Math.cos(radians);

        IdentityMap rotateTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        rotateTransform.getTransfMatrix().setElement(0, 0, cos);
        rotateTransform.getTransfMatrix().setElement(0, 2, -sin);
        rotateTransform.getTransfMatrix().setElement(2, 0, sin);
        rotateTransform.getTransfMatrix().setElement(2, 2, cos);

        getTransfMatrix().mul(rotateTransform.getTransfMatrix(), getTransfMatrix());
    }

    public void rotateZ(double radians) {

        double sin = Math.sin(radians);
        double cos = Math.cos(radians);

        IdentityMap rotateTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        rotateTransform.getTransfMatrix().setElement(0, 0, cos);
        rotateTransform.getTransfMatrix().setElement(0, 1, sin);
        rotateTransform.getTransfMatrix().setElement(1, 0, -sin);
        rotateTransform.getTransfMatrix().setElement(1, 1, cos);

        getTransfMatrix().mul(rotateTransform.getTransfMatrix(), getTransfMatrix());
    }

    public void translate(double tX, double tY, double tZ) {

        IdentityMap translateTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        translateTransform.getTransfMatrix().setElement(2, 0, tX);
        translateTransform.getTransfMatrix().setElement(2, 1, tY);
        translateTransform.getTransfMatrix().setElement(3, 1, tZ);

        getTransfMatrix().mul(translateTransform.getTransfMatrix(), getTransfMatrix());
    }

    public void scale(double sX, double sY, double sZ) {

        IdentityMap scaleTransform = new IdentityMap(Multid.SPACE, Multid.SPACE);
        scaleTransform.getTransfMatrix().setElement(0, 0, sX);
        scaleTransform.getTransfMatrix().setElement(1, 1, sY);
        scaleTransform.getTransfMatrix().setElement(2, 2, sZ);

        getTransfMatrix().mul(scaleTransform.getTransfMatrix(), getTransfMatrix());
    }
}
