/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;

import wave.multid.Multid;

public class Shear2DMap extends IdentityMap {
    //
    // Members
    //
    //
    // Constructors
    //
    public Shear2DMap(double xshear, double yshear) {
        super(Multid.PLANE, Multid.PLANE);
        // homogeneous !
        getTransfMatrix().setElement(0, 1, yshear);
        getTransfMatrix().setElement(1, 0, xshear);
    }

    //
    // Accessors/Mutators
    //
    public boolean hasInverse() { return true; }
}
