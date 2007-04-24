/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

/*
 * this is a Multiarray Matrix and
 * the access to it's elements
 * will be done through :
 *
 * (dim2**2)i + (dim1**1)j + (dim0**0)k
 *
 * where dims are the dimensions for i,j,k the
 * indexes.
 */

public class RealMatrix3 {
    //
    // Constants
    //

    //
    // Members
    //
    private int dim0_, dim1_, dim2_, maxSize_;
    private double[] values_;

    //
    // Constructors
    //

    /*
     * this will be a dim2 x dim1 x dim0 Matrix
     *
     */

    public RealMatrix3(int dim2, int dim1, int dim0) {
        dim0_ = dim0;
        dim1_ = dim1;
        dim2_ = dim2;
        // starts with 0
        maxSize_ = getIndex(dim2_ - 1, dim1_ - 1, dim0_ - 1);
        values_ = new double[maxSize_ + 1];
    }

    //
    // Accessors/Mutators
    //
    public int getSize() { return maxSize_ + 1; }

    protected int getIndex(int i, int j, int k) {
        // dim0_ will be powered by 0
        // and dim1_ by 1
        return (new Double(Math.pow(dim2_, 2) * i).intValue()) + dim1_ * j + k;
    }

    public void setElement(int i, int j, int k, double value) {
        values_[getIndex(i, j, k)] = value;
    }

    public double getElement(int i, int j, int k) {
        return values_[getIndex(i, j, k)];
    }
}
