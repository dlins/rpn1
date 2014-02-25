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

public class Viewing2DMap extends IdentityMap {
    //
    // Members
    //

    //
    // Constructors
    //
    public Viewing2DMap() {
	super(Multid.PLANE,Multid.PLANE); 
    }


    public void shear(double xshear, double yshear) {

        IdentityMap shearTransform = new IdentityMap(Multid.PLANE,Multid.PLANE);
        shearTransform.getTransfMatrix().setElement(0, 1, yshear);
        shearTransform.getTransfMatrix().setElement(1, 0, xshear);

        getTransfMatrix().mul(shearTransform.getTransfMatrix(),getTransfMatrix());
    }

    public void rotate(double radians) {

        double sin = Math.sin(radians);
        double cos = Math.cos(radians);

        IdentityMap rotateTransform = new IdentityMap(Multid.PLANE,Multid.PLANE);
        rotateTransform.getTransfMatrix().setElement(0, 0, cos);
        rotateTransform.getTransfMatrix().setElement(0, 1, sin);
        rotateTransform.getTransfMatrix().setElement(1, 0, -sin);
        rotateTransform.getTransfMatrix().setElement(1, 1, cos);

        getTransfMatrix().mul(rotateTransform.getTransfMatrix(), getTransfMatrix());
    }

    public void translate(double tX, double tY) {

        IdentityMap translateTransform = new IdentityMap(Multid.PLANE,Multid.PLANE);
        translateTransform.getTransfMatrix().setElement(2, 0, tX);
        translateTransform.getTransfMatrix().setElement(2, 1, tY);

        getTransfMatrix().mul(translateTransform.getTransfMatrix(), getTransfMatrix());
    }

    public void scale(double sX, double sY) {

        IdentityMap scaleTransform = new IdentityMap(Multid.PLANE,Multid.PLANE);
        scaleTransform.getTransfMatrix().setElement(0, 0, sX);
        scaleTransform.getTransfMatrix().setElement(1, 1, sY);

        getTransfMatrix().mul(scaleTransform.getTransfMatrix(), getTransfMatrix());
    }
}
