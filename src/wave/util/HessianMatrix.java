/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HessianMatrix.java
 */

package wave.util;

import wave.util.exceptions.HessianMatrixRangeViolation;
import javax.vecmath.GVector; 


public class HessianMatrix {
    private int n_comps_, size_;
    private GVector v_;
    
    
    /*! Default constructor
     */
    public HessianMatrix() {
        n_comps_ = 1;
        size_ = 1;
	v_ = new GVector(size_);
    }
        
    /*! Constructs a matrix with a arbitrary dimension
     *
     *  Dimension is the number of components whose function are stored into the HessianMatrix
     *
     *@param n_comps Dimension of the function stored into the HessianMatrix
     */
    public HessianMatrix(int n_comps) {
	n_comps_ = n_comps;
	size_ = n_comps * n_comps * n_comps;
        v_ = new GVector(size_);
    }


    /*! Copy constructor
     */
    public HessianMatrix(HessianMatrix hMatrix) {
	n_comps_ = hMatrix.n_comps();
	size_ = n_comps_ * n_comps_ * n_comps_;
	v_ = new GVector(hMatrix.getAllElements());
    }


    /*! Changes the second derivative values stored into the HessianMatrix
     *Second derivative components mutator
     *@param [in] hMatrix New second derivative components values
     */
    public void setHessian(HessianMatrix hMatrix) {
        int i, j, k;
        for (i=0;i < n_comps() ; i++){
            for (j=0; j < n_comps() ; j++){
                for (k=0 ;k < n_comps(); k++){
                    setElement(i, j, k, hMatrix.getElement(i, j, k));
                }
            }
        }
    }

    
    /*! Returns the HessianMatrix dimension
     */
    public int n_comps() { return n_comps_;  };
    

    /*! Changes the HessianMatrix dimension
     */
    public void resize(int n_comps) {
	size_ = n_comps * n_comps * n_comps;
        v_.setSize(size_);
        n_comps_ = n_comps;
    }


    /*! Checks for range violation
     */
    public void range_check(int comp) throws HessianMatrixRangeViolation {
        if (comp < 0 || comp >= n_comps()) throw new HessianMatrixRangeViolation();
    }


    /*! Sets all components to zero
     */
    public HessianMatrix zero() {
        v_.zero();
        return this;
    }


    /*! Gets all vector components
     */
    public GVector getAllElements() {
        return v_;
    }

    
    /*! Returns a second derivative component value
     *
     *@param i The second derivative component row
     *@param j The second derivative component  column
     *@param k The second derivative component  deep
     */
    public double getElement(int i, int j, int k) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(j); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(k); } catch (Exception e) { e.printStackTrace(); }

        return v_.getElement(i*n_comps_*n_comps_ + j*n_comps_ + k);
    }

    
    /*! Changes a second derivative component value
     *@param i The second derivative component row
     *@param j The second derivative component column
     *@param k The second derivative component deep
     *@param value The new second derivative component value
     */
    public void setElement(int i, int j, int k, double value) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(j); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(k); } catch (Exception e) { e.printStackTrace(); }
        v_.setElement((i*n_comps_*n_comps_ + j*n_comps_ + k), value);
    }
    
}

