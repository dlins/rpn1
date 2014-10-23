/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid;

import wave.multid.DimMismatchEx;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class CoordsArray {
    //
    // Members
    //
    private RealVector coords_; // normalized !
    private Space space_;

    //
    // Constructors
    //
    public CoordsArray(Space space) {
        space_ = space;
        coords_ = new RealVector(space_.getDim());
        coords_.zero();
    }

    public CoordsArray(CoordsArray coords) {
        this(coords.getSpace());
        coords_.set(coords.getCoords());
    }

    public CoordsArray(double[] coords) {
        space_ = new Space("NO_NAME", coords.length);
        coords_ = new RealVector(coords);
    }

    public CoordsArray(RealVector coords) {
        space_ = new Space("NO_NAME", coords.getSize());
        coords_ = new RealVector(coords);
    }

    //
    // Accessors/Mutators
    //
    public boolean equals(Object a) {
        if (a instanceof CoordsArray)
            return coords_.equals(new RealVector(((CoordsArray)a).getCoords()));
        return false;
    }

    final public double getElement(int index) {
        return coords_.getElement(index);
    }

    final public double[] getCoords() {
        double[] coords = new double[getDim()];
        for (int i = 0; i < getDim(); i++)
            coords[i] = coords_.getElement(i);
        return coords;
    }

    public int[] getIntCoords() {
        int[] iCoords = new int[getDim()];
        for (int i = 0; i < getDim(); i++)
            iCoords[i] = new Double(coords_.getElement(i)).intValue();
        return iCoords;
    }

    final public Space getSpace() { return space_; }

    final public int getDim() { return space_.getDim(); }

    public void setZero() { coords_.zero(); }

    // a double array for transparency
    final public void setCoords(double[] coords) throws wave.multid.DimMismatchEx {
        if (coords.length != getDim())
            throw new DimMismatchEx(this);
        coords_ = new RealVector(coords);
    }

    final public void setElement(int index, double value) { coords_.setElement(index, value); }

    final public void set(double[] values) { coords_.set(values); }

    final public void set(CoordsArray source) { set(source.getCoords()); }

    //
    // Methods
    //
    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(space_.toString());
        sbuf.append("\nComponents : \n");
        for (int i = 0; i < getDim(); i++)
            sbuf.append("comp[" + i + "]=" + coords_.getElement(i) + '\n');
        return sbuf.toString();
    }

    public boolean sameSpaceAs(Space space) {
        return getSpace().equals(space);
    }

    public void scale(double factor) {
        coords_.scale(factor);
    }

    public void mul(RealMatrix2 matrix, CoordsArray vec) {
        coords_.mul(matrix, new RealVector(vec.getCoords()));
    }

    public void mul(CoordsArray vec, RealMatrix2 matrix) {
        coords_.mul(new RealVector(vec.getCoords()), matrix);
    }

    public void negate() {
        coords_.negate();
    }

    public void add(CoordsArray a, CoordsArray b) {
        RealVector vA = new RealVector(a.getCoords());
        RealVector vB = new RealVector(b.getCoords());
        coords_.add(vA, vB);
    }

    public void add(CoordsArray a) {
        RealVector vA = new RealVector(a.getCoords());
        coords_.add(vA);
    }

    public void sub(CoordsArray a) {
        RealVector vA = new RealVector(a.getCoords());
        coords_.sub(vA);
    }

    public void sub(CoordsArray a, CoordsArray b) {
        RealVector vA = new RealVector(a.getCoords());
        RealVector vB = new RealVector(b.getCoords());
        coords_.sub(vA, vB);
    }

    public double norm() { return coords_.norm(); }

    public double dot(CoordsArray coords) {
        return coords_.dot(new RealVector(coords.getCoords()));
    }

    public boolean sameSpaceAs(CoordsArray coords) {
        return (getDim() == coords.getDim());
    }
}
