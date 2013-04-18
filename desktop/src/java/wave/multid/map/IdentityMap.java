/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;

import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.DimMismatchEx;

public class IdentityMap extends HomogeneousMap {
    //
    // Constructor
    //
    public IdentityMap(Space domain, Space codomain) {
        super(domain, codomain);
        for (int i = 0; i < domain.getDim(); i++)
            for (int j = 0; j < codomain.getDim(); j++)
                if (i == j)
                    getTransfMatrix().setElement(i, j, 1);
    }

    //
    // Accessors/Mutators
    //
    public boolean hasInverse() { return true; }
}
