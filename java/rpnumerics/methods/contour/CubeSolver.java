/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods.contour;

import java.lang.reflect.Array;

import org.netlib.lapack.DGESV;
import org.netlib.util.intW;

public class CubeSolver { 
    //
    // Members
    //

    /** The dimension of the space */
    private int n_;

    /** The dimension of the faces */
    private int m_;

    /** The number of edges found. */
    private int nedges_;

    /** The number of solutions found */
    private int nsoln_;

    /** The initial size of the array of solutions (the default value is m_+1) */
    private int dims_;

    /** The initial size of the array of edges (the default value is 10) */
    private int dime_;

    /** A 2-dimensional array containing the solutions found */
    double[] [] sol_;

    /**
     * <p> A 2-dimensional array containing pointers to the solution in  sol_. </p>
     * <p> i.e.,  solptr_[j][i] is the column of sol_ containing the solution for the i-th face in the j-th simplex. </p>
     * <p> (if  solptr_ is 0, the corresponding face contains no solution.) </p>
     */
    private int[] [] solptr_;

    /** Pointer array to sol_ */
    private int[] sptr_;

    /**
     * A 2-dimensional array each column of which contains pointers
     * to the array  sol_  at which the vertices of an edge of the solution polyhedron are stored.
     */
    private int[] [] edges_;

    /**
     * <p> A 2-dimensional array each line of which contains the beginning and ending indices for the array edges_. </p>
     * <p> i.e., the edges_ for simplex number  ns are in lines smpedg_[ns][1] to  smpedg_[ns][2] of  edges_. </p>
     * <p> There are no edges_ if smpedg_[ns][1] > smpedg_[ns][2]. </p>
     */
    private int[] [] smpedg_;

    /** Object containing the data structures needed by CubeSolver */
    protected CubeFace cFace_;
    //
    // Constructors
    //

    /**
     *@param cfunc Interface that implements the funtion to be approximated
     *@param cface Object containing the data structures needed by CubeSolver
     *@param z Zeros (must have size equal to face dimension)
     *@param a Linear coefficients (array with size = space dimension X face dimension)
     */
    public CubeSolver(CubeFace cface) {
        if (cface == null)
            throw(new IllegalArgumentException("ERROR in CubeSolver: CubeFace " + "object needs to be initialized"));
        else
            cFace_ = cface;
        //Initializing variables
        n_ = cFace_.getSpaceDimension();
        m_ = cFace_.getFaceDimension();
        int nsimp = cFace_.getNumberOfSimplices();
        int nsface = cFace_.getNumberOfSimplexFaces();
        int nface = cFace_.getNumberOfFaces();
        solptr_ = new int[nsimp] [nsface];
        sptr_ = new int[nface];
        dims_ = m_ + 1;
        dime_ = 10;
    }
    //
    // Accessors
    //

    /** @return Returns the number of edges found */
    public int getNumberOfEdges() {
        return nedges_;
    }

    /** @return Returns the number of solutions found */
    public int getNumberOfSolutions() {
        return nsoln_;
    }

    /** @return Returns a reference to the edges array */
    public int[] [] getEdgesArray() {
        return edges_;
    }

    /** @return Returns a reference to the solution array */
    public double[] [] getSolutionArray() {
        return sol_;
    }

    /** @return Returns a reference to the solution pointer array */
    public int[] getSolutionPointerArray() {
        return sptr_;
    }

    /** @return Returns a reference to the solution pointer array */
    public CubeFace getCubeFaceObject() {
        return cFace_;
    }
    
    // Setters for child classes
    
    protected void setNEdges(int number) {
    	this.nedges_ = number;
    }
    
    protected int[][] getSolPtr() {
    	return solptr_;
    }
    
    protected int getM_() {
    	return m_;
    }
    
    protected int getN_() {
    	return n_;
    }
   
    //
    //  Members	
    //

    /**
     * <p> This routine finds the solution (if it exists) on each face in  face  and stores it in  sol_. </p>
     * <p> Two pointer arrays  (solptr_, sptr_)  indicate which column of sol_
     * contains the given solution in the corresponding column of  face. </p>
     * <p> (the pointers are set to  0  when there is no solution) </p>
     * @param exstfc Bit array indicating which faces in array "face" are really to be considered by cubsol.
     * Dimension of exstfc should be nface
     */
    public void cubsol(int[] exstfc, double[] [] foncub) {
        //local variables
        sol_ = new double[n_] [dims_];
        //find the solution on each face (define  nsoln_)
        mksoln(exstfc, foncub);
        //create the list of solutions for the simplices
        smpptr();
        
    }

    /**
     * This routine calls  affslv  to obtain the solutions on the faces in  face.
     * @param sol_ Array of solutions
     * @param exstfc Bit array indicating which faces in array "face" are really to be considered by cubsol.
     * Dimension of exstfc should be nface
     */
    private void mksoln(int[] exstfc, double[] [] foncub) {
        //local variables
        int flag, i, j, indf, k, v;
        int[] wrki = new int[m_];
        double[] [] u = new double[n_] [m_ + 1]; 
        double[] [] g = new double[m_] [m_];
        double[] gx = new double[m_];
        double[] x = new double[m_];
        double[] auxsol = new double[n_];
        double[][] solution = new double[n_][m_];
        int nface = cFace_.getNumberOfFaces();
        int[] [] face;
        face = cFace_.getFaceArray();
        double[] [] cvert;
        //int zeroCount = 0;
        cvert = cFace_.getVertexCoordinatesArray();
        //loop over the faces to find each solution (if there is one)
        nsoln_ = 0;
        for (indf = 0; indf < nface; indf++) {
        	sptr_[indf] = 0;
            //if the indf-face is not to be considered by mksoln, skip it
            if (exstfc[indf] != 0) {
            	//boolean zeroFound = false;                
                //initialize gx
                v = face[0] [indf];
                for (i = 0; i < m_; i++) {
                    gx[i] = foncub[i] [v];
                    
                    /*if (gx[i] == 0.0) {
                    	zeroFound = true;
                    } else if (gx[i] == Double.NEGATIVE_INFINITY) {
                    	gx[i] = -Double.MIN_VALUE;
                    } else if (gx[i] == Double.POSITIVE_INFINITY) {
                    	gx[i] = Double.MAX_VALUE;
                    }*/
                }
                //set the function values  at the face vertices
                for (k = 0; k < m_; k++) {
                    v = face[k + 1] [indf];
                    for (i = 0; i < m_; i++) {
                        g[i] [k] = foncub[i] [v];
                        /*if (g[i][k] == 0.0) {
                        	zeroFound = true;
                        } else if (g[i][k] == Double.NEGATIVE_INFINITY) {
                        	g[i][k] = -Double.MIN_VALUE;
                        } else if (g[i][k] == Double.POSITIVE_INFINITY) {
                        	g[i][k] = Double.MAX_VALUE;
                        }*/
                    }
                }
                
                /*if (zeroFound) {
                	zeroCount++;
                }*/
                //call the affine solver to solve in the standard simplex
                //if(!zeroFound || (zeroCount <= 1)) {
	                flag = affslv(x, gx, g, wrki);
	                //skip the rest if no solution ( note -- pointer initialized to 0 )
	                
	                // solve the system
	                // flag = calculateSolution(x, gx, g, m_, wrki);
	                
	                if (flag == 0) {
	                    //set the pointer to the solution
	                    nsoln_ = nsoln_ + 1;
	                    if (dims_ < nsoln_) {
	                        dims_ *= 2;
	                        for (j = 0; j < n_; j++) {
	                            sol_[j] = (double[]) doubleArray(sol_[j]);
	                        }
	                    }
	                    //set the face vertices
	                    for (k = 0; k < m_ + 1; k++) {
	                        v = face[k] [indf];
	                        for (i = 0; i < n_; i++) {
	                            u[i] [k] = cvert[v] [i];
	                        }
	                    }
	                    //transform the solution back from the standard simplex	                    
	                    double[] tmp = afftrn(nsoln_ - 1, u, x);
	                    
	                    if(tmp != null) {
	                    	sptr_[indf] = nsoln_;		                    
		                    for(int pont_solution = 0; pont_solution < tmp.length; pont_solution++) {
		                    	sol_[pont_solution] [nsoln_ - 1] = tmp[pont_solution];
		                    }
	                    } else {
	                    	nsoln_--;
	                    }
	                }
                //}
                                
            }
        }
      
    }

    /**
     * This routine finds the zero  x  of the affine function whose values at
     * the vertices of the standard m_-simplex are stored in the columns of  g.
     * It also checks whether the solution is inside the simplex and sets flag appropriately.
     * @param x
     * @param g0
     * @param g
     * @param wrki
     * @return  <ul> <p> -1      Error in solving the system </p> <p>  0      Valid solution inside simplex </p>
     * <p>  1      Valid solution outside simplex </p> <ul>
     */
   /* private void affslv(double[] x, double[] g0, double[] [] g, int[] wrki) {
        //local variables
        int i, j;
        //subtract  g0  from  g1, g2, ..., gm
        for (j = 0; j < m_; j++) {
            for (i = 0; i < m_; i++) {
                g[i] [j] = g[i] [j] - g0[i];
            }
        }
        //change sign of  g0
        for (i = 0; i < m_; i++) {
            g0[i] = -g0[i];
        }
        
    }*/
    
    private int affslv(double[] x, double[] g0, double[] [] g, int[] wrki) {
        //local variables
        double sum;
        int i, j, result;
        //subtract  g0  from  g1, g2, ..., gm
        for (j = 0; j < m_; j++) {
            for (i = 0; i < m_; i++) {
                g[i] [j] = g[i] [j] - g0[i];
            }
        }
        //change sign of  g0
        for (i = 0; i < m_; i++) {
            g0[i] = -g0[i];
        }
        //solve the system
        result = solver(x, g0, g, m_, wrki);
        //check for no solution
        
        if (result != 0)
            return result;
        //determine whether solution is inside
        sum = 0.0;
        for (i = 0; i < m_; i++) {
            if (x[i] < 0.0)
                //solution is outside
                    return 1;
            else
                sum = sum + x[i];
        }
        if (sum > 1.0)
            //solution is outside
                return 1;
        //fell through -- solution is inside
        return 0;
    }
    
    protected int analyzeResult(int result, double[] x) {
        //check for no solution
        if (result != 0)
            return result;
        //determine whether solution is inside
        double sum = 0.0;
        for (int i = 0; i < m_; i++) {
            if (x[i] < -0.000000001)
                //solution is outside
                    return 1;
            else
                sum = sum + x[i];
        }
        
        if (sum > 1.000000001)
        	// This is a problem, must be analyzed later...
            //solution is outside
                return 1;
        //fell through -- solution is inside
        return 0;
    }
    
    protected int calculateSolution(double[] x, double[] b, double[] [] a, int n1, int[] ipvt) {
    	int result, flag;
    	
    	result 	= solver(x,b,a,n1,ipvt);
    	flag 	= analyzeResult(result, x); 
    	
    	return flag;
    }
   
    /**
     * <p> This routine maps  x  in the standard m_-simplex to  y  in the
     * n_-dimensional m_-simplex with vertices  u(.,1), u(.,2), ...,  u(.,m_+1). </p>
     * <p> The map is the affine transformation which takes the vertices of the
     * standard simplex to those contained in the columns of  u. </p>
     * @param y
     * @param u
     * @param x
     */
    protected double[] afftrn(int ind, double[] [] u, double[] x) {
        int i, k;
        double[] solution = new double[n_];
                
        for (i = 0; i < n_; i++) { 
        	solution[i] = u[i] [0];
            for (k = 0; k < m_; k++) {
                 solution[i] = solution[i] + (u[i] [k + 1] - u[i] [0]) * x[k];
                /*if (sol_[i] [ind] == Double.NEGATIVE_INFINITY) {
                	sol_[i] [ind] = -Double.MIN_VALUE;
                } else if (sol_[i] [ind] == Double.POSITIVE_INFINITY) {
                	sol_[i] [ind] = Double.MAX_VALUE;
                } */
            }
        }        
                
        return solution;
    }

    /**
     * This routine solves the  n_ x n_  system  ax = b.
     * @param x
     * @param b
     * @param a
     * @param n_
     * @param ipvt
     * @return   <ul> <p>  0     ok system </p> <p> -1     singular system </p> <p> -2     bad value of  n_ </p> </ul>
     */
    private int solver(double[] x, double[] b, double[] [] a, int n1, int[] ipvt) {
        //local variables
        double det, anorm;
        double cond;
        double[] work = new double[10];
        int i;
        //local constants
        double eps = 0.00001;
        //check  n_
        switch (n1) {
            case 1:
                if (Math.abs(a[0] [0]) <= eps)
                    return -1;
                x[0] = b[0] / a[0] [0];
                return 0;
            case 2:
                det = a[0] [0] * a[1] [1] - a[0] [1] * a[1] [0];
                anorm = a[0] [0] * a[0] [0] + a[0] [1] * a[0] [1] + a[1] [0] * a[1] [0] + a[1] [1] * a[1] [1];
                if (Math.abs(det) <= (eps * anorm))
                    return -1;
                x[0] = (b[0] * a[1] [1] - b[1] * a[0] [1]) / det;
                x[1] = (a[0] [0] * b[1] - a[1] [0] * b[0]) / det;
                return 0;
            case 3:
                det = a[0] [0] * (a[1] [1] * a[2] [2] - a[1] [2] * a[2] [1]) - a[1] [0] *
                    (a[0] [1] * a[2] [2] - a[2] [1] * a[0] [2]) + a[2] [0] * (a[0] [1] * a[1] [2] - a[1] [1] * a[0] [2]);
                anorm = a[0] [0] * a[0] [0] + a[0] [1] * a[0] [1] + a[0] [2] * a[0] [2] + a[1] [0] * a[1] [0] + a[1] [1] *
                    a[1] [1] + a[1] [2] * a[1] [2] + a[2] [0] * a[2] [0] + a[2] [1] * a[2] [1] + a[2] [2] * a[2] [2];
                if (Math.abs(det) <= (eps * anorm))
                    return -1;
                x[0] = (b[0] * (a[1] [1] * a[2] [2] - a[2] [1] * a[1] [2]) - b[1] *
                    (a[0] [1] * a[2] [2] - a[2] [1] * a[0] [2]) + b[2] * (a[0] [1] * a[1] [2] - a[1] [1] * a[0] [2])) / det;
                x[1] = (-b[0] * (a[1] [0] * a[2] [2] - a[2] [0] * a[1] [2]) + b[1] *
                    (a[0] [0] * a[2] [2] - a[2] [0] * a[0] [2]) - b[2] * (a[0] [0] * a[1] [2] - a[1] [0] * a[0] [2])) / det;
                x[2] = (b[0] * (a[1] [0] * a[2] [1] - a[2] [0] * a[1] [1]) - b[1] *
                    (a[0] [0] * a[2] [1] - a[2] [0] * a[0] [1]) + b[2] * (a[0] [0] * a[1] [1] - a[1] [0] * a[0] [1])) / det;
                return 0;
        }
                
//      alternatively:
		//  call the  linpack  routine  sgefa  to find  lu
		//  call the  linpack  routine  sgesl  to solve the system
		if ((n1 >= 4)) {
			
			intW info = new intW(0);
			double xz[][];
			
			int sizeX = x.length;
			xz = new double[sizeX][1];			
			
			for(int pont_x = 0; pont_x < sizeX; pont_x++) {
				xz[pont_x][0] = b[pont_x];
			}
			
			DGESV.DGESV(n1, 1, a, ipvt, xz, info);
			
			for (int pont_x = 0; pont_x < sizeX; pont_x++) {
				x[pont_x] = xz[pont_x][0];
			}
			
			return 0;
		}
		else
			//bad value of  n1
			return -2;
    }
    
    /**
     * The following program is copied from the book "computer methods for
     * mathematical computations" by george e. forsythe, michael a. malcolm, and
     * cleve b. moler, prentice-hall, inc., englewood cliffs, n_.j., 1977.
     * @param ndim
     * @param n_
     * @param a
     * @param ipvt
     * @param work
     * @return
     */
    private double decomp(int ndim, int n1, double[] [] a, int[] ipvt, double[] work) {
        double ek, t, anorm, ynorm, znorm, cond;
        int nm1, i, j, k, kp1, kb, km1, m;
        double abs;
        ipvt[n1 - 1] = 1;
        if (n1 != 1) {
            nm1 = n1 - 1;
            anorm = 0.0;
            for (j = 0; j < n1; j++) {
                t = 0.0;
                for (i = 0; i < n1; i++) {
                    t = t + Math.abs(a[i] [j]);
                }
                if (t > anorm)
                    anorm = t;
            }
            for (k = 0; k <= nm1; k++) {
                kp1 = k + 1;
                m = k;
                for (i = kp1; i < n1; i++) {
                    if (Math.abs(a[i] [k]) > Math.abs(a[m] [k]))
                        m = i;
                }
                ipvt[k] = m;
                if (m != k) {
                    ipvt[n1 - 1] = -ipvt[n1 - 1];
                    t = a[m] [k];
                    a[m] [k] = a[k] [k];
                    a[k] [k] = t;
                    if (t != 0.0) {
                        for (i = kp1; i < n1; i++) {
                            a[i] [k] = -a[i] [k] / t;
                        }
                        for (j = kp1; j < n1; j++) {
                            t = a[m] [j];
                            a[m] [j] = a[k] [j];
                            a[k] [j] = t;
                            if (t != 0.0) {
                                for (i = kp1; i < n1; i++) {
                                    a[i] [j] = a[i] [j] + a[i] [k] * t;
                                }
                            }
                        }
                    }
                }
            }
            for (k = 0; k < n1; k++) {
                t = 0.0;
                if (k != 0) {
                    km1 = k - 1;
                    for (i = 0; i < km1; i++) {
                        t = t + a[i] [k] * work[i];
                    }
                }
                ek = 1.0;
                if (t < 0.0)
                    ek = -1.0;
                if (a[k] [k] == 0.0) {
                    cond = 1.0e+32;
                    return cond;
                }
                work[k] = -(ek + t) / a[k] [k];
            }
            for (kb = 0; kb <= nm1; kb++) {
                k = n1 - kb;
                t = 0.0;
                kp1 = k + 1;
                for (i = kp1; i < n1; i++) {
                    t = t + a[i] [k] * work[k];
                }
                work[k] = t;
                m = ipvt[k];
                if (m != k) {
                    t = work[m];
                    work[m] = work[k];
                    work[k] = t;
                }
            }
            ynorm = 0.0;
            for (i = 0; i < n1; i++) {
                ynorm = ynorm + Math.abs(work[i]);
            }
            solve(ndim, n1, a, work, ipvt);
            znorm = 0.0;
            for (i = 0; i < n1; i++) {
                znorm = znorm + Math.abs(work[i]);
            }
            cond = anorm * znorm / ynorm;
            if (cond < 1.0)
                cond = 1.0;
            return cond;
        }
        if (a[0] [0] != 0.0)
            return 1.0;
        return 1.0e+32;
    }

    private void solve(int ndim, int n1, double[] [] a, double[] b, int[] ipvt) {
        int kb, km1, nm1, kp1, i, k, m;
        double t;
        if (n1 != 1) {
            nm1 = n1 - 1;
            for (k = 0; k < nm1; k++) {
                kp1 = k + 1;
                m = ipvt[k];
                t = b[m];
                b[m] = b[k];
                b[k] = t;
                for (i = kp1; i < n1; i++) {
                    b[i] = b[i] + a[i] [k] * t;
                }
            }
            for (kb = 0; kb < nm1; kb++) {
                km1 = n1 - kb;
                k = km1 + 1;
                b[k] = b[k] / a[k] [k];
                t = -b[k];
                for (i = 0; i < km1; i++) {
                    b[i] = b[i] + a[i] [k] * t;
                }
            }
        }
        b[0] = b[0] / a[0] [0];
    }

    /**
     * This routine sets the pointers to  sol_  for each face in each simplex.
     * That is,  solptr_(i,j)  points to the solution in sol_ on the i-th face of the j-th simplex.
     */
    private void smpptr() {
        //local variables
        int ns, nf;
        int nsimp = cFace_.getNumberOfSimplices();
        int nsface = cFace_.getNumberOfSimplexFaces();
        int[] [] facptr = cFace_.getFacePointersArray();
        //loop over  facptr  to find solutions
        for (ns = 0; ns < nsimp; ns++) {
            for (nf = 0; nf < nsface; nf++) {
                solptr_[ns] [nf] = sptr_[facptr[ns] [nf]];
            }
        }
    }

    /** This routine builds the array of pointers to the edges_ in the solution polyhedron. */
    public void mkedge() {
        int nsimp = cFace_.getNumberOfSimplices();
        int nsface = cFace_.getNumberOfSimplexFaces();
        int[] [] fnbr = cFace_.getAdjacencyInfoArray();
        //Global variables initialization
        edges_ = new int[5] [dime_];
        smpedg_ = new int[nsimp] [2];
        //local variables
        int i, j, ns, spi, spj;
        //initialize
        nedges_ = 0;
        //loop over the simplices creating the edges_
        for (ns = 0; ns < nsimp; ns++) {
            smpedg_[ns] [0] = nedges_ + 1;
            //determine which neighboring faces have a solution edge
            for (i = 0; i < nsface - 1; i++) {
                spi = solptr_[ns] [i];
                if (spi != 0) {
                    for (j = i + 1; j < nsface; j++) {
                        if (fnbr[j] [i] != 0) {
                            spj = solptr_[ns] [j];
                            if (spj != 0) {
                                nedges_ = nedges_ + 1;
                                if (dime_ < nedges_) {
                                    dime_ *= 2;
                                    edges_[0] = (int[]) doubleArray(edges_[0]);
                                    edges_[1] = (int[]) doubleArray(edges_[1]);
                                    edges_[2] = (int[]) doubleArray(edges_[2]);
                                    edges_[3] = (int[]) doubleArray(edges_[3]);
                                    edges_[4] = (int[]) doubleArray(edges_[4]);
                                }
                                edges_[0] [nedges_ - 1] = spi;
                                edges_[1] [nedges_ - 1] = spj;
                                edges_[2] [nedges_ - 1] = ns;
                                edges_[3] [nedges_ - 1] = i;
                                edges_[4] [nedges_ - 1] = j;
                            }
                        }
                    }
                }
            }
            smpedg_[ns] [1] = nedges_;
        }
      
    }

    /**
     * This routine builds the array of pointers to the edges_ in the solution
     * polyhedron which are parallel to a coordinate plane.
     */
    private void mklevl() {
        //local variables
        int i, j, ns, spi, spj;
        int begi, begj, k, l;
        int nsimp = cFace_.getNumberOfSimplices();
        int nsface = cFace_.getNumberOfSimplexFaces();
        int[] [] facptr = cFace_.getFacePointersArray();
        int[] [] ptfatt = cFace_.getAttributePointersArray();
        int[] [] facatt = cFace_.getAttributeArray();
        int[] [] fnbr = cFace_.getAdjacencyInfoArray();
        //initialize
        nedges_ = 0;
        //loop over the simplices creating the edges_
        for (ns = 0; ns < nsimp; ns++) {
            smpedg_[ns] [0] = nedges_ + 1;
            //determine which neighboring faces have a solution edge
            for (i = 0; i < nsface - 1; i++) {
                spi = solptr_[ns] [i];
                if (spi != 0) {
                    for (j = i + 1; j < nsface; j++) {
                        if (fnbr[j] [i] != 0) {
                            spj = solptr_[ns] [j];
                            if (spj != 0) {
                                begi = ptfatt[facptr[ns] [i]] [0];
                                begj = ptfatt[facptr[ns] [j]] [0];
                                for (k = 0; k < ptfatt[facptr[ns] [i]] [1]; k++) {
                                    for (l = 0; l < ptfatt[facptr[ns] [j]] [1]; l++) {
                                        if (facatt[begi + k - 1] [0] != facatt[begj + l - 1] [0]) {
                                            //found an edge
                                            nedges_ = nedges_ + 1;
                                            if (dime_ < nedges_) {
                                                dime_ *= 2;
                                                edges_[0] = (int[]) doubleArray(edges_[0]);
                                                edges_[1] = (int[]) doubleArray(edges_[1]);
                                            }
                                            edges_[0] [nedges_] = spi;
                                            edges_[1] [nedges_] = spj;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            smpedg_[ns] [1] = nedges_;
        }
    }

    /**
     * Funcao para Debug!! Imprime array de inteiros trocando linhas com colunas.
     * <p><h3> Deve ser eliminada apos testes da classe </h3></p>
     * @param name Variable name
     * @param matrix The bidimensional array to be printed
     * @param k1 Size of the array
     * @param k2 Size of the array
     */
    private void printInt(String name, int[] [] matrix, int k1, int k2) {
        int i, j;
        System.out.println(name + ": ");
        for (j = 0; j < k2; j++) {
            for (i = 0; i < k1; i++) {
                System.out.print(matrix[i] [j] + " ");
            }
            System.out.println("");
        }
    }

    /**
     * Funcao para Debug!! Imprime array de inteiros trocando linhas com colunas.
     * <p><h3> Deve ser eliminada apos testes da classe </h3></p>
     * @param name Variable name
     * @param matrix The bidimensional array to be printed
     * @param k1 Size of the array
     * @param k2 Size of the array
     */
    private void printInt(String name, int[] matrix, int k1) {
        int j;
        System.out.println(name + ": ");
        for (j = 0; j < k1; j++) {
            System.out.print(matrix[j] + " ");
        }
        System.out.println(" ");
    }

    /**
     * Funcao para Debug!! Imprime array de doubles na ordem correta.
     * <p><h3> Deve ser eliminada apos testes da classe </h3></p>
     * @param name Variable name
     * @param matrix The bidimensional array to be printed
     * @param k1 Size of the array
     * @param k2 Size of the array
     */
    private void printDouble(String name, double[] [] matrix, int k1, int k2) {
        int i, j;
        System.out.println(name + ": ");
        for (i = 0; i < k1; i++) {
            for (j = 0; j < k2; j++) {
                System.out.print(matrix[i] [j] + " ");
            }
            System.out.println("");
        }
    }

    /**
     * Doubles the size of an array
     * @param source Array de origem
     * @return Returns an array containing the elements of source with twice its size
     */
    protected Object doubleArray(Object source) {
        int sourceLength = Array.getLength(source);
        Class arrayClass = source.getClass();
        Class componentClass = arrayClass.getComponentType();
        Object result = Array.newInstance(componentClass, sourceLength * 2);
        System.arraycopy(source, 0, result, 0, sourceLength);
        return result;
    }
}
