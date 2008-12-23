/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.map;

import wave.multid.Space;
import wave.util.RealMatrix2;

public class ProjectionMap extends HomogeneousMap {
    //
    // Members
    //
    // this will map the coords to be reduced
    private int[] compIndexes_;

    //
    // Constructors
    //
    public ProjectionMap(Space domain, Space codomain, int[] compIndexes) {
        super(domain, codomain);
        compIndexes_ = new int[compIndexes.length];
        System.arraycopy(compIndexes, 0, compIndexes_, 0, compIndexes.length);
        getTransfMatrix().setZero();
        for (int i = 0; i < codomain.getDim(); i++)
            getTransfMatrix().setElement(compIndexes_[i], i, 1);
        getTransfMatrix().setElement(domain.getDim(), codomain.getDim(), 1); // homogeneous !
    }

    //
    // Accessors/Mutators
    //
    @Override
    public RealMatrix2 getInvTransfMatrix() {
        //TODO we will use several matrix transforms
        return null;
    }

    public int[] getCompIndexes() { return compIndexes_; }

    @Override
    public boolean hasInverse() { return false; }
    //
    // Methods
    //
}
