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

public class HomogeneousMap implements Map {
    //
    // Members
    //
    private Space domain_;
    private Space codomain_;
    private RealMatrix2 mapMatrix_;
    private boolean hasInverse_;

    //
    // Constructors
    //
    public HomogeneousMap(Space domain, Space codomain) {
        domain_ = domain;
        codomain_ = codomain;
        // homogeneous coordinates is not necessarely an Identity
        mapMatrix_ = new RealMatrix2(domain_.getDim() + 1, codomain_.getDim() + 1);
        mapMatrix_.setZero();
        mapMatrix_.setElement(domain_.getDim(), codomain_.getDim(), 1);
        hasInverse_ = false; // each map has to specify
    }

    //
    // Accessors/Mutators
    //
    public Space getDomain() { return domain_; }

    public void setDomain(Space domain) { domain_ = domain; }

    public Space getCodomain() { return codomain_; }

    public void setCodomain(Space codomain) { codomain_ = codomain; }

    public RealMatrix2 getTransfMatrix() { return mapMatrix_; }

    public RealMatrix2 getInvTransfMatrix() {
        RealMatrix2 invMatrix = new RealMatrix2(mapMatrix_);
        invMatrix.invert(mapMatrix_);
        return invMatrix;
    }

    public void setTransfMatrix(RealMatrix2 mapMatrix) { mapMatrix_ = new RealMatrix2(mapMatrix); }

    public boolean hasInverse() { return hasInverse_; }

    public void setInversible(boolean hasInverse) { hasInverse_ = hasInverse; }

    //
    // Methods
    //
    public void concatenate(Map catMap) {
        getTransfMatrix().mul(catMap.getTransfMatrix());
    }

    /*
     * these linear transformations are post-multiplication (T=tXT)
     */

    protected void transf(CoordsArray in, CoordsArray out, RealMatrix2 transfMatrix) {
        HomogeneousCoords inCoords = new HomogeneousCoords(in);
        HomogeneousCoords outCoords = new HomogeneousCoords(out);
        // performs the transformation
        outCoords.mul(inCoords, transfMatrix);
        try {
            out.setCoords(outCoords.getRegularCoords());
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
    }

    public void image(CoordsArray in, CoordsArray out) {
        transf(in, out, getTransfMatrix());
    }

    public void inverse(CoordsArray in, CoordsArray out) throws NoInverseMapEx {
        if (!hasInverse())
            throw new NoInverseMapEx("Inverse not implemented");
        else
            transf(in, out, getInvTransfMatrix());
    }


}
