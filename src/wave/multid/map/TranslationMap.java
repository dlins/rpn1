/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;

import wave.multid.Space;
import wave.util.RealVector;

public class TranslationMap extends IdentityMap {
    //
    // Members
    //
    private double[] tParams_;

    //
    // Constructors
    //
    public TranslationMap(Space domain, Space codomain, double[] tParams) {
        super(domain, codomain);
        RealVector tRow = new RealVector(tParams);
        // homogenize
        tRow.setSize(tRow.getSize() + 1);
        tRow.setElement(tRow.getSize() - 1, 1d);
        // changes the last row of the identity matrix
        getTransfMatrix().setRow(getTransfMatrix().getNumRow() - 1, tRow);
    }

    //
    // Accessors/Mutators
    //
    public boolean hasInverse() { return true; }
}
