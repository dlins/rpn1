/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JacobianMatrix.java
 */

package wave.util;

import wave.util.exceptions.JacobianMatrixRangeViolation;
import javax.vecmath.GVector; 


public class JacobianMatrix {
    private int n_comps_, size_;
    private GVector v_;
    private boolean c0_, c1_, c2_;
    
    
    /*! Default constructor
     */
    public JacobianMatrix() {
        n_comps_ = 1;
        size_ = 1;
	v_ = new GVector(size_);
    }
        
    /*! Constructs a matrix with a arbitrary dimension
     *
     *  Dimension is the number of components whose function are stored into the JacobianMatrix
     *
     *@param n_comps Dimension of the function stored into the JacobianMatrix
     */
    public JacobianMatrix(int n_comps) {
	n_comps_ = n_comps;
	size_ = n_comps * n_comps;
        v_ = new GVector(size_);
    }


    /*! Copy constructor
     */
    public JacobianMatrix(JacobianMatrix jMatrix) {
	n_comps_ = jMatrix.n_comps();
       	size_ = n_comps_ * n_comps_;
	v_ = new GVector(jMatrix.getAllElements());
    }

    
    /*! Changes the first derivative values stored into the JacobianMatrix
     *First derivative components mutator
     *@param [in] jMatrix New first derivative components values
     */
    public void setJacobian(JacobianMatrix jMatrix) {
        int i, j;
        for(i=0;i< n_comps();i++){
            for (j=0; j < n_comps();j++){
                setElement(i, j, jMatrix.getElement(i, j));
            }
        }
    }

    
    /*! Returns the JacobianMatrix dimension
     */
    public int n_comps() { return n_comps_;  };
    

    /*! Changes the JacobianMatrix dimension
     */
    public void resize(int n_comps) {
	size_ = n_comps * n_comps;
        v_.setSize(size_);
        n_comps_ = n_comps;
    }


    /*! Checks for range violation
     */
    public void range_check(int comp) throws JacobianMatrixRangeViolation {
        if (comp < 0 || comp >= n_comps()) throw new JacobianMatrixRangeViolation();
    }


    /*! Sets all components to zero
     */
    public JacobianMatrix zero() {
        v_.zero();
        return this;
    }


    /*! Gets all vector components
     */
    public GVector getAllElements() {
        return v_;
    }

    
    /*! Returns a first derivative component value
     *@param i The first derivative component row
     * *@param j The first derivative component  column
     */
    public double getElement(int i, int j) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(j); } catch (Exception e) { e.printStackTrace(); }
        return v_.getElement(i*n_comps_ + j);
    }

        
    /*! Changes a first derivative component value
     *@param i The first derivative component row
     *@param j The first derivative component column
     *@param value The new first derivative component value
     */
    
    public void setElement(int i, int j, double value) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(j); } catch (Exception e) { e.printStackTrace(); }
        v_.setElement((i*n_comps_ + j), value);
    }

}

