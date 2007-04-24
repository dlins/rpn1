/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

import java.lang.reflect.Array;

public class CubeFace {
    //
    // Members
    //

    /** The dimension of the space */
    private int n_;

    /** The dimension of the faces */
    private int m_;

    /** The number of faces in the basic simplex (n_+1 choose m_+1) */
    private int nsface_;

    /** The number of distinct faces (columns of  face_) */
    private int nface_;

    /** The number of vertices of the n_-cube (2**n_) */
    private int ncvert_;

    /** The number of simplices in the n_-cube (n_!) */
    private int nsimp_;

    /** Dimension of facatt_ */
    private int fdim_;

    /** Dimension of facept_ */
    private int faceptInd_;

    /** The initial size of the face_ array (the default value is 10) */
    private int dimf_;

    /**
     * <p> A 2-dimensional array each line of which contains the coordinates of one vertex of the standard n_-cube. </p>
     * <p> These are the binary representations of the numbers from  0  to  2**n_-1. </p> <p>For  n_=3:</p> <ul>
     * <p>    0 0 0 </p> <p>    0 0 1 </p> <p>    0 1 0 </p> <p>    0 1 1 </p> <p>    1 0 0 </p> <p>    1 0 1 </p>
     * <p>    1 1 0 </p> <p>    1 1 1 </p></ul>
     */
    private double[] [] cvert_;

    /**
     * <p> A 2-dimensional array each line of which contains the coordinates of the basic n_-simplex.</p> <p> For  n_=3:</p>
     * <ul> <p>      0 0 0 </p> <p>      0 0 1 </p> <p>      0 1 1 </p> <p>      1 1 1 </p></ul>
     */
    private int[] [] bsvert_;

    /**
     * <p> A 2-dimensional array each column of which contains a pertmutaion of the numbers from  1  to  n_. </p>
     * <p> Each column corresponds to a distinct simplex in the standard
     * hypercube by permutation of the coordinates of the vertices of the basic simplex. </p> <p> For  n_=3: </p> <ul>
     * <p> 1 2 1 2 3 3 </p> <p> 1 2 1 2 3 3 </p> <p> 3 3 2 1 2 1 </p></ul>
     * <p> The position of the 3-s above gives away the law of formation. </p>
     */
    private int[] [] perm_;

    /**
     * A 2-dimensional array each column of which contains the coordinates
     * of one m_-dimensional face_ of one of the simplices. Only distinct faces are stored.
     */
    private int[] [] face_;

    /**
     * A 2-dimensional array containing pointers to the array face_ so that duplicated faces can be referenced.
     * Each line points to the faces in a given simplex.
     */
    private int[] [] facptr_;

    /**
     * A 2-dimensional array each line of which contains a subset of size  m_+1  of the set  1, 2, ..., n_+1.
     * Each line corresponds to a face_ of the basic simplex.
     */
    private int[] [] comb_;

    /**
     * A 2-dimensional array containing the adjacency information for the faces in the basic simplex.
     * i.e.,  fnbr_[i][j]  is 1 (resp. 0) if the i-th and j-th faces are (resp. are not) adjacent in the basic simplex.
     * Here,  i,j  refer to the order determined by the array  comb_.
     */
    private int[] [] fnbr_;

    /**
     * A pointer array to  facept_.  for f = 1, ..., nface_, <p> ptrf_[f][0] = beginning line  in facept_ for face_ f. </p>
     * <p> ptrf_[f][1] = number of lines in facept_ for face_ f. </p>
     */
    private int[] [] ptrf_;

    /**
     * The inverse of  facptr_. <p> facept_[.][0] = index of simplex face_ for face_ f. </p>
     * <p> facept_[.][1] = index of simplex for face_ f. </p>
     */
    private int[] [] facept_;

    /**
     * A pointer array to  facatt_. <p> ptfatt_[f][0] = beginning line  in facatt_ for face_ f. </p>
     * <p> ptfatt_[f][1] = number of lines in facatt_ for face_ f. </p>
     */
    private int[] [] ptfatt_;

    /**
     * The array of attributes. <p> facatt_[.][0] = index of constant coordinate on a cube face_ (negative for x = 0). </p>
     * <p> facatt_[.][1] = index in  face_  of the opposite face_. </p>
     */
    private int[] [] facatt_;
    //
    // Constructors
    //

    /**
     * @param dim Dimension of the space
     * @param faceDim Dimension of the faces
     * @throws IllegalArgumentException
     */
    public CubeFace(int dim, int faceDim) throws IllegalArgumentException {
        //if the dimension is invallid
        if ((dim <= 0) || (faceDim <= 0)) throw new IllegalArgumentException();
        if (faceDim > dim) throw new IllegalArgumentException();
        //inicializing variables
        n_ = dim;
        m_ = faceDim;
        ncvert_ = (int)Math.pow(2.0, (double)n_);
        nsimp_ = factorial(n_);
        dimf_ = 10;
        //inicializing arrays
        cvert_ = new double[ncvert_] [n_];
        bsvert_ = new int[n_ + 1] [n_];
        perm_ = new int[n_] [nsimp_];
        // funcoes de inicializacao
        mkcube();
        //Debug!!!!!!!
        printDouble("cvert_", cvert_, ncvert_, n_);
        printInt("bsvert_", bsvert_, n_ + 1, n_);
        printInt("perm_", perm_, n_, nsimp_);
        //Fim do debug
        mkface();
        //Debug!!!!!!!
        printInt("comb_", comb_, m_ + 2, m_ + 1);
        printInt("face_", face_, m_ + 1, nface_);
        printInt("facptr_", facptr_, nsimp_, nsface_);
        printInt("fnbr_", fnbr_, nsface_, nsface_);
        //Fim do debug
        //Global variable initialization
        ptrf_ = new int[nface_] [2];
        ptfatt_ = new int[nface_] [2];
        mkflst();
        //Debug !!!
        printInt("ptrf_", ptrf_, nface_, 2);
        printInt("ptfatt_", ptfatt_, nface_, 2);
        printInt("facept_", facept_, faceptInd_, 2);
        printInt("factt", facatt_, fdim_, 2);
        //Fim do debug
    }
    //
    // Accessors
    //

    /** @return Returns the space dimension */
    public int getSpaceDimension() {
        return n_;
    }

    /** @return Returns the face_ dimension */
    public int getFaceDimension() {
        return m_;
    }

    /** @return Returns the number of vertices of the n_-cube (2**n_) */
    public int getNumberOfVertices() {
        return ncvert_;
    }

    /** @return Returns the number of distinct faces */
    public int getNumberOfFaces() {
        return nface_;
    }

    /** @return Returns the number of faces in the basic simplex */
    public int getNumberOfSimplexFaces() {
        return nsface_;
    }

    /** @return Returns the number of simplices in the n_-cube */
    public int getNumberOfSimplices() {
        return nsimp_;
    }

    /** @return Returns the attribute array */
    public int[] [] getAttributeArray() {
        return facatt_;
    }

    /** @return Returns a 2-dimensional array containing pointers to the attribute array */
    public int[] [] getAttributePointersArray() {
        return ptfatt_;
    }

    /**
     * @return Returns a 2-dimensional array containing pointers to the array face_ so that
     * duplicated faces can be referenced.
     */
    public int[] [] getFaceArray() {
        return face_;
    }

    /** @return Returns a 2-dimensional array containing pointers to the face_ array */
    public int[] [] getFacePointersArray() {
        return facptr_;
    }

    /** @return Returns a 2-dimensional array with coordinates of the vertices of the standard n_-cube. */
    public double[] [] getVertexCoordinatesArray() {
        return cvert_;
    }

    /** @return Returns a 2-dimensional array containing the adjacency information for the faces in the basic simplex. */
    public int[] [] getAdjacencyInfoArray() {
        return fnbr_;
    }

    /**
     * @param altFace The new face array
     * @throws IllegalArgumentException If the array has an invalid size.
     */
    public void setFaceArray(int[] [] altFace) throws IllegalArgumentException {
        //Check sizes
        if ((altFace.length != m_ + 1) || (altFace[0].length != nface_)) {
            throw new IllegalArgumentException("Face array with invalid size");
        }
        face_ = altFace;
    }

    /**
     * @param altVert The new vertex coordinates array
     * @throws IllegalArgumentException If the array has an invalid size.
     */
    public void setVertexCoordinatesArray(double[] [] altVert) throws IllegalArgumentException {
        //Check sizes
        if ((altVert.length != ncvert_) || (altVert[0].length < n_)) {
            throw new IllegalArgumentException("Face array with invalid size");
        }
        cvert_ = altVert;
    }
    //
    // Methods	
    //

    /**
     *  @param num integer greater than or equal to zero
     *  @return Returns the factorial of num
     */
    private int factorial(int num) {
        if (num <= 0)
            return 1;
        int result = 1;
        for (int i = num; i > 0; i--) {
            result = result * i;
        }
        return result;
    }

    /**
     * This routine creates the standard hypercube vertices (in cvert_),
     * the basic simplex vertices (in bsvert_), and the permutations of 1, 2, ..., n_  (in permutations).
     */
    private void mkcube() {
        int i, index, j, k;
        // hypercube vertices
        for (index = 0; index < ncvert_; index++) {
            k = index;
            for (i = 0; i < n_; i++) {
                cvert_[index] [n_ - 1 - i] = k % 2;
                k = k / 2;
            }
        }
        // basic simplex vertices
        for (j = 0; j <= n_; j++) {
            for (i = 0; i < n_; i++) {
                bsvert_[j] [i] = 0;
            }
        }
        for (j = 1; j <= n_; j++) {
            for (i = n_ - j; i < n_; i++) {
                bsvert_[j] [i] = 1;
            }
        }
        // permutations (define  nsimp_)
        mkperm();
    }

    /**
     *  This routine creates the array  perm_  containing the permutations
     * of the numbers from 1 to  n_.  It also sets  nsimp_  to be  n_  factorial.
     */
    private void mkperm() {
        int i, j, k, kmfact, l, shift;
        // initialize
        nsimp_ = 1;
        perm_[0] [0] = 0;
        if (n_ == 1)
            return;
        // loop to build permutations
        for (k = 1; k < n_; k++) {
            kmfact = nsimp_;
            // place  k  in row  k
            for (j = 0; j < kmfact; j++) {
                perm_[k] [j] = k;
            }
            // place  k  in the other rows
            for (l = 0; l < k; l++) {
                shift = 0;
                for (i = 0; i < k; i++) {
                    if (i == k - 1 - l)
                        shift = 1;
                    for (j = 0; j < kmfact; j++) {
                        perm_[i + shift] [j + nsimp_] = perm_[i] [j];
                    }
                }
                for (j = 0; j < kmfact; j++) {
                    perm_[k - 1 - l] [j + nsimp_] = k;
                }
                nsimp_ = nsimp_ + kmfact;
            }
        }
    }

    /**
     * This routine creates the list  face_  of all m_-dimensional faces
     * of the  n_!  simplices obtained by permuting the coordinates of
     * the basic simplex.  to do this it needs the array  comb_  of all
     * subsets of size  m_+1  from a set of size  n_+1.  since  face_  contains
     * only distinct faces, a pointer array  facptr_  is needed to
     * determine which column of  face_  corresponds to each permutation and combination.
     */
    private void mkface() {
        //combinations (define  nsface_)
        mkcomb(n_ + 1, m_ + 1);
        //make the arrays  face_, facptr_ (define  nface_)
        mkfcfp();
        //compute the neighbor array
        mkfnbr();
    }

    /**
     *  This routine makes a list of all  m_  element subsets of an n_  element set.  It also sets  nsface_  to be the number
     * of subsets found. Each subset is stored in a column of  comb_.  the elements
     * in a column are in increasing order, and the columns are ordered lexicographically.
     *  @param n_ Set size
     *  @param m_ Subset size
     */
    private void mkcomb(int n, int m) {
        //local variables
        int i, j, pos, count;
        //global variable initialization
        comb_ = new int[m + 1] [m];
        //initialize
        nsface_ = 1;
        for (i = 0; i < m; i++) {
            comb_[nsface_ - 1] [i] = i;
        }
        //the main loop
        while (true) {
            count = 0;
            //search for the last position to change
            for (i = 0; i < m; i++) {
                pos = m - 1 - i;
                if (comb_[nsface_ - 1] [pos] == n - i - 1) {
                    count++;
                }
                else {
                    //position found.  store new combination.
                    nsface_ = nsface_ + 1;
                    //increment the value at the position
                    comb_[nsface_ - 1] [pos] = comb_[nsface_ - 2] [pos] + 1;
                    //copy the preceding values
                    for (j = 0; j < pos; j++) {
                        comb_[nsface_ - 1] [j] = comb_[nsface_ - 2] [j];
                    }
                    //modify the succeeding values
                    for (j = pos + 1; j < m; j++) {
                        comb_[nsface_ - 1] [j] = comb_[nsface_ - 1] [j - 1] + 1;
                    }
                    //break
                    i = m;
                }
            }
            if (count == m)
                //fell through.  done.
                    return;
        }
    }

    /** This routine makes the arrays  face_  and  facptr_. It also defines the variable  nface_. */
    private void mkfcfp() {
        //local variables
        int c, i, j, index, p, posit, v;
        int[] stor = new int[n_ + 1];
        //global variable initialization
        facptr_ = new int[nsimp_] [nsface_];
        face_ = new int[m_ + 1] [dimf_];
        //loop over all faces to create  face_  and  facptr_
        nface_ = 0;
        for (p = 0; p < nsimp_; p++) {
            for (c = 0; c < nsface_; c++) {
                //set indices for each vertex
                for (v = 0; v < m_ + 1; v++) {
                    index = 0;
                    for (i = 0; i < n_; i++) {
                        index = 2 * index + bsvert_[comb_[c] [v]] [perm_[i] [p]];
                    }
                    stor[v] = index;
                }
                //store the face_ if it is distinct
                if (nface_ > 0)
                    posit = search(stor, face_, m_ + 1, nface_);
                else
                    posit = -1;
                if (posit == -1) {
                    nface_ = nface_ + 1;
                    if (dimf_ < nface_) {
                        dimf_ *= 2;
                        for (j = 0; j < m_ + 1; j++) {
                            face_[j] = (int[]) doubleArray(face_[j]);
                        }
                    }
                    for (i = 0; i < m_ + 1; i++) {
                        face_[i] [nface_ - 1] = stor[i];
                    }
                    posit = nface_ - 1;
                }
                //set the pointer to the current face_
                facptr_[p] [c] = posit;
            }
        }
        return;
    }

    /**
     * This routine searches the columns of a2  for the column array a1.
     * @param a1 The column to be searched
     * @param a2 The array to be analized
     * @param n1 Number of columns of the array
     * @param n2 Number of lines of the array
     * @returns  <p> -1  ----------> a1  not found in  a2 </p> <p> column no.  --> a1  found in  a2 </p>
     */
    private int search(int[] a1, int[] [] a2, int n1, int n2) {
        //local variables
        int i, j;
        //check for empty  a2
        if (n2 == 0)
            return -1;
        for (j = 0; j < n2; j++) {
            for (i = 0; i < n1; i++) {
                if (a1[i] != a2[i] [j])
                    i = n1 + 2;
            }
            if (i == n1)
                return j;
        }
        //fell through.  a1  not found.
        return -1;
    }

    /**
     * This routine makes the adjacency array  fnbr_  for the faces in
     * the basic simplex. Two faces in comb_ are adjacent if they differ in exactly one vertex.
     */
    private void mkfnbr() {
        //local variables
        int i, j, ndif, nf;
        int[] stor = new int[n_ + 1];
        //global variable initialization
        fnbr_ = new int[nsface_] [nsface_];
        //zero out  fnbr_
        for (j = 0; j < nsface_; j++) {
            for (i = 0; i < nsface_; i++) {
                fnbr_[j] [i] = 0;
            }
        }
        //loop over the faces
        for (nf = 0; nf < nsface_ - 1; nf++) {
            //set comparison array
            for (i = 0; i < n_ + 1; i++) {
                stor[i] = 0;
            }
            for (i = 0; i < m_ + 1; i++) {
                stor[comb_[nf] [i]] = 1;
            }
            //compare with the later faces
            for (j = nf + 1; j < nsface_; j++) {
                ndif = 0;
                for (i = 0; i < m_ + 1; i++) {
                    if (stor[comb_[j] [i]] == 0)
                        ndif = ndif + 1;
                }
                if (ndif == 1) {
                    fnbr_[nf] [j] = 1;
                    fnbr_[j] [nf] = 1;
                }
            }
        }
    }

    /** Find facept_ and facatt_ sizes */
    private void findInds() {
        //local variables
        int f, i, pos, s, v;
        double sum;
        pos = 0;
        for (f = 0; f < nface_; f++) {
            for (s = 0; s < nsimp_; s++) {
                for (i = 0; i < nsface_; i++) {
                    if (facptr_[s] [i] == f) {
                        pos = pos + 1;
                    }
                }
            }
        }
        //Global variable initialization
        faceptInd_ = pos + 1;
        facept_ = new int[pos + 1] [2];
        pos = 0;
        for (f = 0; f < nface_; f++) {
            for (i = 0; i < n_; i++) {
                sum = 0.0;
                for (v = 0; v < m_ + 1; v++) {
                    sum = sum + cvert_[face_[v] [f]] [i];
                }
                if (sum == 0.0) {
                    pos = pos + 1;
                }
                else if (sum == (double)(m_ + 1)) {
                    pos = pos + 1;
                }
            }
        }
        fdim_ = pos;
        //Global variable initialization
        facatt_ = new int[fdim_] [2];
    }

    /**
     * This routine has two purposes: <ul> <p> (1) Invert the array  facptr_  using two arrays: facept_, ptrf_. </p>
     * <p> (2) Create the attribute arrays facatt_ and ptfatt_. These encode the information about which simplex faces
     * reside on cube faces. </p> </ul>
     */
    private void mkflst() {
        int[] wrk = new int[m_ + 1];
        findInds();
        //local variables
        int f, i, next, pos, s, v;
        double sum;
        for (i = 0; i < faceptInd_; i++) {
            facept_[i] [0] = -1;
            facept_[i] [1] = -1;
        }
        //invert facptr_
        next = 0;
        for (f = 0; f < nface_; f++) {
            pos = next;
            for (s = 0; s < nsimp_; s++) {
                for (i = 0; i < nsface_; i++) {
                    if (facptr_[s] [i] == f) {
                        facept_[pos] [0] = i;
                        facept_[pos] [1] = s;
                        pos = pos + 1;
                    }
                }
            }
            ptrf_[f] [0] = next;
            ptrf_[f] [1] = pos - next;
            next = pos;
        }
        //determine face_ attributes
        next = 0;
        for (f = 0; f < nface_; f++) {
            pos = next;
            for (i = 0; i < n_; i++) {
                sum = 0.0;
                for (v = 0; v < m_ + 1; v++) {
                    sum = sum + cvert_[face_[v] [f]] [i];
                }
                if (sum == 0.0) {
                    facatt_[pos] [0] = -(i + 1);
                    facatt_[pos] [1] = mkoppf(f, i, 0, wrk);
                    pos = pos + 1;
                }
                else if (sum == (double)(m_ + 1)) {
                    facatt_[pos] [0] = +(i + 1);
                    facatt_[pos] [1] = mkoppf(f, i, 1, wrk);
                    pos = pos + 1;
                }
            }
            ptfatt_[f] [0] = next;
            ptfatt_[f] [1] = pos - next;
            next = pos;
        }
    }

    /**
     * This function determines which face_ is opposite face_  f  in
     * a cube.  It is assumed that face_  f  lies on a cube face_.
     * @param f Face number
     * @param xind Indicates which component is constant.
     * @param value Is that constant value.
     * @param oppvrt Auxiliar vector
     * @returns See the search method return values
     */
    private int mkoppf(int f, int xind, int value, int[] oppvrt) {
        //local variables
        int oppval, sign, v;
        //set the opposite face_ transformation
        if (value == 0)
            sign = +1;
        else
            sign = -1;
        oppval = sign * (int)Math.pow(2, n_ - (xind + 1));
        //transform each vertex
        for (v = 0; v < m_ + 1; v++) {
            oppvrt[v] = face_[v] [f] + oppval;
        }
        //find the index of the opposite face_
        return search(oppvrt, face_, m_ + 1, nface_);
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
        for (i = 0; i < k1; i++) {
            for (j = 0; j < k2; j++) {
                System.out.print(matrix[i] [j] + " ");
            }
            System.out.println("");
        }
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
    private Object doubleArray(Object source) {
        int sourceLength = Array.getLength(source);
        Class arrayClass = source.getClass();
        Class componentClass = arrayClass.getComponentType();
        Object result = Array.newInstance(componentClass, sourceLength * 2);
        System.arraycopy(source, 0, result, 0, sourceLength);
        return result;
    }
}
