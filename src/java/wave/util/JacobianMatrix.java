/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JacobianMatrix.java
 */

package wave.util;


public class JacobianMatrix extends RealMatrix2{
    private int n_comps_;
    
    /*! Default constructor
     */
    public JacobianMatrix() {
        super(2,2);
        n_comps_ = 2;
    }
        
    /*! Constructs a matrix with a arbitrary dimension
     *
     *  Dimension is the number of components whose function are stored into the JacobianMatrix
     *
     *@param n_comps Dimension of the function stored into the JacobianMatrix
     */
    public JacobianMatrix(int n_comps) {
        super(n_comps, n_comps);
	n_comps_ = n_comps;

    }


    /*! Copy constructor
     */
    public JacobianMatrix(JacobianMatrix jMatrix) {
        super(jMatrix);
	n_comps_ = jMatrix.n_comps();
    }

    
    /*! Returns the JacobianMatrix dimension
     */
    public int n_comps() { return n_comps_;  };


    /*! Sets all components to zero
     */
    public JacobianMatrix zero() {
        for (int i = 0; i < getNumRow(); i++) {
            for (int j = 0; j < getNumCol(); j++) {
                setElement(i, j, 0);
            }
        }
        return this;
    }

}

