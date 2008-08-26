/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JetMatrix.java
 */

package wave.util;

import wave.util.exceptions.JetMatrixRangeViolation;
import javax.vecmath.GVector; 


public class JetMatrix {
    private int n_comps_, size_;
    private GVector v_;
    private boolean c0_, c1_, c2_;
    
    
    /*! Default constructor
     */
    public JetMatrix() {
        n_comps_ = 1;
        size_ = 3;
	v_ = new GVector(size_);
	c0_ = false;
	c1_ = false;
	c2_ = false;
    }
        
    /*! Constructs a matrix with a arbitrary dimension
     *
     *  Dimension is the number of components whose function are stored into the JetMatrix
     *
     *@param n_comps Dimension of the function stored into the JetMatrix
     */
    public JetMatrix(int n_comps) {
	n_comps_ = n_comps;
	size_ = n_comps * (1 + n_comps * (1 + n_comps));
        v_ = new GVector(size_);
        c0_ = false;
        c1_ = false;
        c2_ = false;
    }


    /*! Copy constructor
     */
    public JetMatrix(JetMatrix jetMatrix) {
	n_comps_ = jetMatrix.n_comps();
       	size_ = n_comps_ * (1 + n_comps_ * (1 + n_comps_));
	v_ = new GVector(jetMatrix.getAllElements());
       	c0_ = false;
       	c1_ = false;
       	c2_ = false;
    }

    
    /*! Fills a RealVector with the function components values
     *@param [out] values The RealVector to be filled
     */
    public RealVector f() {
	try { if (!c0_) throw new JetMatrixRangeViolation(); }
	    catch (Exception e) { e.printStackTrace(); }
        
        int i;
	RealVector vector = new RealVector(n_comps());

        for (i=0; i < n_comps();i++) {
            vector.setElement(i, getElement(i));
        }

	return vector;
    }

    
    /*!Fills a JacobianMatrix with the first derivative components values stored into the JetMatrix
     * Function components values accessor
     *@param [out] jMatrix The JacobianMatrix to be filled
     */
    public JacobianMatrix jacobian() {
	try { if (!c1_) throw new JetMatrixRangeViolation(); }
	catch (Exception e) { e.printStackTrace(); }

        int i, j;
	JacobianMatrix jMatrix = new JacobianMatrix(n_comps());
    
        for (i=0;i < n_comps(); i++){
            for (j=0; j < n_comps();j++ ){
                jMatrix.setElement(i, j, getElement(i, j));
            }
        }

	return jMatrix;
    }

    
    /*!Fills a HessianMatrix with the second derivative components values stored into the JetMatrix
     * Second derivative components values accessor
     *
     *@param[out] hMatrix The HessianMatrix to be filled
     */
    public HessianMatrix hessian() {
	try { if (!c2_) throw new JetMatrixRangeViolation(); }
	    catch (Exception e) { e.printStackTrace(); }
     
        int i, j, k;
	HessianMatrix hMatrix = new HessianMatrix(n_comps());

        for (i=0;i < n_comps() ; i++){
            for (j=0; j < n_comps() ; j++){
                for (k=0 ;k < n_comps(); k++){
                    hMatrix.setElement(i, j, k, getElement(i, j, k));
                }
            }
        }

	return hMatrix;
    }

    
    /*! Changes the function components values stored into the JetMatrix
     * Function components values mutator
     *@param [in] fValues New function components values
     */
    public void setF(RealVector fValues) {
        int i;
        for (i=0;i< n_comps();i++){
            setElement(i, fValues.getElement(i));
        }
    }

    
    /*! Changes the first derivative values stored into the JetMatrix
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

    
    /*! Changes the second derivative values stored into the JetMatrix
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

    
    /*! Returns the JetMatrix dimension
     */
    public int n_comps() { return n_comps_;  };
    

    /*! Changes the JetMatrix dimension
     */
    public void resize(int n_comps) {
	size_ = n_comps * (1 + n_comps * (1 + n_comps));
        v_.setSize(size_);
        n_comps_ = n_comps;
    }


    /*! Checks for range violation
     */
    public void range_check(int comp) throws JetMatrixRangeViolation {
        if (comp < 0 || comp >= n_comps()) throw new JetMatrixRangeViolation();
    }


    /*! Sets all components to zero
     */
    public JetMatrix zero() {
        v_.zero();
        return this;
    }


    /*! Gets all vector components
     */
    public GVector getAllElements() {
        return v_;
    }

    
    /*! Returns a function component value
     *@param i The function component index
     */
    public double getElement(int i) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
	try { if (!c0_) throw new JetMatrixRangeViolation(); }
	    catch (Exception e) { e.printStackTrace(); }
        return v_.getElement(i);
    }
    
    
    /*! Returns a first derivative component value
     *@param i The first derivative component row
     * *@param j The first derivative component  column
     */
    public double getElement(int i, int j) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(j); } catch (Exception e) { e.printStackTrace(); }
	try { if (!c1_) throw new JetMatrixRangeViolation(); }
	    catch (Exception e) { e.printStackTrace(); }
        return v_.getElement((n_comps_) + (i*n_comps_ + j));
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
	try { if (!c2_) throw new JetMatrixRangeViolation(); }
	    catch (Exception e) { e.printStackTrace(); }
        return v_.getElement((n_comps_ * (1 + n_comps_)) + (i*n_comps_*n_comps_ + j*n_comps_ + k));
    }

    
    /*!Changes a function component value
     *@param i The function component index
     *@param value The new component value
     */
    public void setElement(int i, double value) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
        c0_ = true;
        v_.setElement(i, value);
    }


    /*! Changes a first derivative component value
     *@param i The first derivative component row
     *@param j The first derivative component column
     *@param value The new first derivative component value
     */
    
    public void setElement(int i, int j, double value) {
        try { range_check(i); } catch (Exception e) { e.printStackTrace(); }
        try { range_check(j); } catch (Exception e) { e.printStackTrace(); }
        c1_ = true;
        v_.setElement((n_comps_) + (i*n_comps_ + j), value);
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
        c2_ = true;
        v_.setElement((n_comps_ * (1 + n_comps_)) + (i*n_comps_*n_comps_ + j*n_comps_ + k), value);
    }
    
}

