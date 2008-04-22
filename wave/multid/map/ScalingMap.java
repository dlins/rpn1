/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;

import wave.multid.Space;
import wave.util.RealVector;

public class ScalingMap extends IdentityMap {
    //
    // Members
    //
    private double[] scaleParams_;

    //
    // Constructors
    //
    public ScalingMap(Space domain, Space codomain, double[] scaleParams) {
        super(domain, codomain);
        // homogeneous !
        for (int i = 0; i < getTransfMatrix().getNumRow() - 1; i++)
            getTransfMatrix().setElement(i, i, scaleParams[i]);
        scaleParams_ = new double[scaleParams.length];
        System.arraycopy(scaleParams, 0, scaleParams_, 0, scaleParams.length);
    }

    //
    // Accessors/Mutators
    //
    public boolean hasInverse() { return true; }
}
