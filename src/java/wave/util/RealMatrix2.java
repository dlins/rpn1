/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

import javax.vecmath.GMatrix;
import org.netlib.util.intW;
import org.netlib.lapack.DGEEV;
import org.netlib.lapack.DGEES;
import org.netlib.lapack.DGESV;
import org.netlib.lapack.DGETRF;
import java.util.StringTokenizer;

/* A general porpouses real nXn Matrix */

public class RealMatrix2 extends GMatrix {
    /* TODO
    these constantes should be in the error module or at least
    related to the error Module
    */

   private int stateSpaceDim;
   private  intW INFO;// = new intW(0);
   private int LWORK ;//= stateSpaceDim * 15;
   private  double[] WORK; //= new double[LWORK];
   private  boolean[] BWORK ;//= new boolean[stateSpaceDim];

    //
    // Constructors
    //
    public RealMatrix2(RealMatrix2 matrix) {

      super(matrix);
      stateSpaceDim=matrix.getNumRow();
      LWORK = stateSpaceDim * 15;
      WORK = new double[LWORK];
      BWORK = new boolean[stateSpaceDim];
      INFO = new intW(0);


    }


    public  RealMatrix2 (int row,int col,String data){

      super (row,col,fromString(data));

    }

    public RealMatrix2(int nrow, int ncol) {
      super(nrow, ncol);
      stateSpaceDim=nrow;
      LWORK = stateSpaceDim * 15;
      WORK = new double[LWORK];
      BWORK = new boolean[stateSpaceDim];
      INFO = new intW(0);

    }

    public RealMatrix2(int nrow, int ncol, double[] data) {
      super(nrow, ncol, data);
      stateSpaceDim=nrow;
      LWORK = stateSpaceDim * 15;
      WORK = new double[LWORK];
      BWORK = new boolean[stateSpaceDim];
      INFO = new intW(0);

    }

    //
    // Methods
    //
    public void scale(double t) {
        for (int i = 0; i < getNumRow(); i++)
            for (int j = 0; j < getNumCol(); j++)
                setElement(i, j, getElement(i, j) * t);
    }

    public double fNorm() {
        int m = getNumCol();
        int n = getNumRow();
        double sum = 0d;
        for (int j = 0; j < m; j++)
            for (int i = 0; i < n; i++)
                sum = sum + Math.pow(getElement(i, j), 2);
        return Math.sqrt(sum);
    }

    static public double[] [] convert(RealMatrix2 a) {
        int numOfRows = a.getNumRow();
        int numOfColumns = a.getNumCol();
        double[] [] result = new double[numOfRows] [numOfColumns];
        for (int i = 0; i < numOfRows; i++)
            for (int j = 0; j < numOfColumns; j++)
                result[i] [j] = a.getElement(i, j);
        return result;
    }

    static public RealMatrix2 convert(int nrow, int ncol, double[] [] array) {
        RealMatrix2 result = new RealMatrix2(nrow, ncol);
        for (int i = 0; i < nrow; i++)
            for (int j = 0; j < ncol; j++)
                result.setElement(i, j, array[i] [j]);
        return result;
    }

     public int fillSchurData(int stateSpaceDim, Object select, RealMatrix2 df, RealMatrix2 schurVec) {
        double[] [] dfModified = RealMatrix2.convert(df);
        double[] [] schurVecArray = new double[stateSpaceDim] [stateSpaceDim];
        double[] eigenValR = new double[stateSpaceDim];
        double[] eigenValI = new double[stateSpaceDim];
        intW schurDimW = new intW(0);
        DGEES.DGEES("V", "S", select, stateSpaceDim, dfModified, schurDimW, eigenValR, eigenValI, schurVecArray,
            WORK, LWORK, BWORK, INFO);
        for (int k = 0; k < stateSpaceDim; k++)
            for (int l = 0; l < stateSpaceDim; l++) {
                df.setElement(k, l, dfModified[k] [l]);
                schurVec.setElement(k, l, schurVecArray[k] [l]);
            }
        return schurDimW.val;
    }

     public void fillEigenData(int stateSpaceDim, RealMatrix2 df, double[] eigenValR, double[] eigenValI,
        RealVector[] eigenVec) {
            double[] [] dfModified = RealMatrix2.convert(df);
            double[] [] eigenVecArrayRight = new double[stateSpaceDim] [stateSpaceDim];
            double[] [] eigenVecArrayLeft = new double[stateSpaceDim] [stateSpaceDim];
            double[] eigenValRArray = new double[stateSpaceDim];
            double[] eigenValIArray = new double[stateSpaceDim];
            intW DimW = new intW(0);
            DGEEV.DGEEV("N", "V", stateSpaceDim, dfModified, eigenValR, eigenValI, eigenVecArrayLeft,
                eigenVecArrayRight, WORK, LWORK, INFO);
            for (int i = 0; i < stateSpaceDim; i++) {
                eigenVec[i] = new RealVector(stateSpaceDim);
                for (int j = 0; j < stateSpaceDim; j++)
                    eigenVec[i].setElement(j, eigenVecArrayRight[j] [i]);
            }
    }

    public double normInf() {
        // evaluates inf norm of a square matrix M
        // maximum row sum for absolute values of elements abs(m_ij)
        double sum = 0;
        double norm = 0;
        int i = 0;
        int j = 0;
        int n1 = getNumRow();
        int n2 = getNumCol();
        for (i = 0; i < n1; i++) {
            sum = 0;
            for (j = 0; j < n2; j++)
                sum = sum + Math.abs(getElement(i, j));
            if (norm < sum)
                norm = sum;
        }
        return norm;
    }


    public RealMatrix2 solve(RealMatrix2 b) {


        int N = b.getNumRow();

        if ((this.getNumRow() != getNumCol()) ||
            (getNumRow() != N))

            throw new IllegalArgumentException("wrong dimension...");


        int NRHS = b.getNumCol();
        double[][] A = convert(this);


        int LDA = N;
        int LDB = N;


        int[] IPIV = new int[N];

        double[][]B = new double[LDB][NRHS];

        B = convert(b);

        DGESV.DGESV(N,NRHS,A,IPIV,B,INFO);

        RealMatrix2 X = convert(LDB,NRHS,B);

        return X;

    }

    public RealVector solve(RealVector b) {

        RealMatrix2 B = new RealMatrix2(b.getSize(),1);
        B.setColumn(0,b);

        RealMatrix2 X = solve(B);
		RealVector x = new RealVector(b.getSize());
        X.getColumn(0,x);

        return x;
    }

    public void fillEigenVals(RealMatrix2 matrix, double[] eigenValR, double[] eigenValI) {


	// ALEXEI please check this !!! is the spaceDim in the num of Rows or Cols ??? +MARIO.
        int m = matrix.getNumRow();

        if (m != 2) {
            double[] [] dfModified = convert(matrix);
            double[] [] eigenVecArrayRight = new double[m] [m];
            double[] [] eigenVecArrayLeft = new double[m] [m];
            //            intW DimW = new intW(0);
            DGEEV.DGEEV("N", "N", m, dfModified, eigenValR, eigenValI, eigenVecArrayLeft,
                eigenVecArrayRight, WORK, LWORK, INFO);
        } else {
            double a = 0.5 * matrix.trace();
            double b = matrix.getElement(0, 0) * matrix.getElement(1, 1) -
                	   matrix.getElement(0, 1) * matrix.getElement(1, 0);

            double D = a * a - b;
            if (D < 0) {
                D = Math.sqrt(-D);
                eigenValR[0] = a;
                eigenValR[1] = a;
                eigenValI[0] = D;
                eigenValI[1] = -D;
            } else {
                D = Math.sqrt(D);

				//eigenValR[0] = a - D;
                eigenValR[0] = b/(a + D);

                eigenValR[1] = a + D;
                eigenValI[0] = 0;
                eigenValI[1] = 0;
            }
        }
    }

    public static RealMatrix2 kron(RealMatrix2 M1, RealMatrix2 M2) {
        // evaluates inf norm of a square matrix M
        // maximum row sum for absolute values of elements abs(m_ij)
        int Row1 = M1.getNumRow();
        int Col1 = M1.getNumCol();
        int Row2 = M2.getNumRow();
        int Col2 = M2.getNumCol();
        RealMatrix2 result = new RealMatrix2(Row1 * Row2, Col1 * Col2);
        RealMatrix2 tmp = new RealMatrix2(Row2, Col2);
        for (int i = 0; i < Row1; i++)
            for (int j = 0; j < Col1; j++) {
                tmp.set(M2);
                tmp.scale(M1.getElement(i, j));
                tmp.copySubMatrix(0, 0, Row2, Col2, (i - 1) * Row2, (j - 1) * Col2, result);
            }
        return result;
    }

    public double determinant() {

        int numRow = getNumRow();
        int numCol = getNumCol();

        double result = 1;

        if ( (numRow == 2) && (numCol == 2) )
            result = this.getElement(0,0)*this.getElement(1,1)-this.getElement(0,1)*this.getElement(1,0);
        else
        if ( (numRow == 3) && (numCol == 3) )
            result = this.getElement(0,0)*this.getElement(1,1)*this.getElement(2,2)
                    +this.getElement(0,1)*this.getElement(1,2)*this.getElement(2,0)
                    +this.getElement(0,2)*this.getElement(1,0)*this.getElement(2,1)
                    -this.getElement(0,2)*this.getElement(1,1)*this.getElement(2,0)
                    -this.getElement(0,1)*this.getElement(1,0)*this.getElement(2,2)
                    -this.getElement(0,0)*this.getElement(1,2)*this.getElement(2,1);
        else {

            double[] [] A = convert(this);

            int[] IPIV = new int[numCol];
            if (numRow < numCol)
    	        IPIV = new int[numRow];

            int LDA = numRow;

            DGETRF.DGETRF(numRow,numCol,A,IPIV,INFO);

            RealMatrix2 LU = convert(numRow,numCol,A);

            for (int i = 0; i < LU.getNumCol(); i++)
                  result *= LU.getElement(i,i);

            for (int i = 0; i < IPIV.length; i++)
                if (IPIV[i] != i+1)
                  result = -result;
        }

        return result;
    }

    /**
     *  returns the vector consisting of
     *columns of the matrices Qx, Rx and
     * vecotr Xp
     * @param Qx RealMatrix2
     * @param Rx RealMatrix2
     * @param Xp RealVector
     * @return RealVector
     */

   public static RealVector Matrices2Vector(RealMatrix2 Qx, RealMatrix2 Rx, RealVector Xp) {
           int m = Qx.getNumRow();
           int k = Qx.getNumCol();
           RealVector result = new RealVector(m * k + k * (k + 1) / 2 + m);
           int a = 0;
           int i, j;
           for (i = 0; i < m; i++)
               for (j = 0; j < k; j++) {
                   result.setElement(a, Qx.getElement(i, j));
                   a = a + 1;
               }
           for (i = 0; i < k; i++)
               for (j = i; j < k; j++) {
                   result.setElement(a, Rx.getElement(i, j));
                   a = a + 1;
               }
           for (i = 0; i < m; i++) {
               result.setElement(a, Xp.getElement(i));
               a = a + 1;
           }
           return result;
       }

       /**
        *
        *  having a vector source, returns
        * the matrices Qx, Rx and vector Xp
        * as the inverse operation to Matrices2Vector function
        *
        * @param source RealVector
        * @param Qx RealMatrix2
        * @param Rx RealMatrix2
        * @param Xp RealVector
        */

       public static  void Vector2Matrices(RealVector source, RealMatrix2 Qx, RealMatrix2 Rx, RealVector Xp) {
       int k = Qx.getNumCol();
       int m = Qx.getNumRow();
       int a = 0;
       int i, j;
       for (i = 0; i < m; i++)
           for (j = 0; j < k; j++) {
               Qx.setElement(i, j, source.getElement(a));
               a = a + 1;
           }
       Rx.setZero();
       for (i = 0; i < k; i++)
           for (j = i; j < k; j++) {
               Rx.setElement(i, j, source.getElement(a));
               a = a + 1;
           }
       for (i = 0; i < m; i++) {
           Xp.setElement(i, source.getElement(a));
           a = a + 1;
       }

       }

       /** provides the vector form z' = result
        * for the right-hand side of equations
        * Q' R + Q R' = F Q R
        *QT' Q + QT Q' = 0
        *
        * @param Q RealMatrix2
        * @param R RealMatrix2
        * @param F RealMatrix2
        * @param dQ RealMatrix2
        * @param dR RealMatrix2
        */
       public static void QRFunction(RealMatrix2 Q, RealMatrix2 R, RealMatrix2 F, RealMatrix2 dQ, RealMatrix2 dR) {
       RealMatrix2 Qcopy = new RealMatrix2(Q);
       RealMatrix2 Rcopy = new RealMatrix2(R);
       RealMatrix2 Fcopy = new RealMatrix2(F);
       int k = Q.getNumCol();
       int m = Q.getNumRow();
       int dim = m * k + k * (k + 1) / 2;
       double tmp;
       RealMatrix2 C = new RealMatrix2(dim, dim);
       C.setZero();
       RealVector x = new RealVector(dim);
       // Q'R+QR' = FQR
       RealMatrix2 RcopyT = new RealMatrix2(Rcopy);
       RcopyT.transpose();
       for (int i = 0; i < m; i++)
           for (int j = 0; j < k; j++)
               for (int n = 0; n <= j; n++) {
                   //Q'R
                   C.setElement(i * k + j, i * k + n, Rcopy.getElement(n, j));
                   C.setElement(i * k + j, k * m + j * (j + 1) / 2 + n, Qcopy.getElement(i, n));
               }
       RealMatrix2 FQR = new RealMatrix2(m, k);
       FQR.mul(Fcopy, Qcopy);
       FQR.mul(Rcopy);
       //FQR
       for (int i = 0; i < m; i++)
           for (int j = 0; j < k; j++)
               x.setElement(i * k + j, FQR.getElement(i, j));
       // Q'T Q+QT Q' = 0
       for (int i = 0; i < k; i++)
           for (int j = 0; j <= i; j++)
               for (int n = 0; n < m; n++) {
                   //Q'T Q
                   C.setElement(k * m + i * (i + 1) / 2 + j, n * k + i,
                       C.getElement(k * m + i * (i + 1) / 2 + j, n * k + i) + Qcopy.getElement(n, j));
                   //QT Q'
                   C.setElement(k * m + i * (i + 1) / 2 + j, n * k + j,
                       C.getElement(k * m + i * (i + 1) / 2 + j, n * k + j) + Qcopy.getElement(n, i));
               }
       // solve C result = x
       RealVector result = new RealVector(wave.util.MathUtil.linearSolver(C, x));
       for (int i = 0; i < m; i++)
           for (int j = 0; j < k; j++)
               dQ.setElement(i, j, result.getElement(i * k + j));
       dR.setZero();
       for (int i = 0; i < k; i++)
           for (int j = i; j < k; j++) {
               dR.setElement(i, j, result.getElement(k * m + j * (j + 1) / 2 + i));
           }
   }










    public static RealMatrix2 matrixExponent(RealMatrix2 M) {
        // evaluates exp(M) for a square matrix M
        //  Matrix exponential via Pade approximation.
        //   See Golub and Van Loan, Matrix Computations, Algorithm
        // 11.3-1.
        // Scale A by power of 2 so that its norm is < 1/2 .
        RealMatrix2 expM = new RealMatrix2(M);
        RealMatrix2 A = new RealMatrix2(M);
        double logBase = Math.log(A.normInf()) / Math.log(2); // logarithm with base 2
        double logBase_ = Math.floor(logBase) + 1d;
        double s = Math.max(0d, logBase_ + 1d);
        A.scale(Math.pow(0.5d, s));
        // Pade approximation for exp(A)
        RealMatrix2 X = new RealMatrix2(A);
        RealMatrix2 X2 = new RealMatrix2(A);
        double c = 0.5;
        RealMatrix2 E = new RealMatrix2(A);
        E.scale(-c);
        E.identityMinus();
        RealMatrix2 D = new RealMatrix2(A);
        D.scale(c);
        D.identityMinus();
        double q = 6;
        boolean p = true;
        int k = 0;
        for (k = 2; k <= q; k++) {
            c = c * (q - k + 1d) / (k * (2d * q - k + 1d));
            X2 = new RealMatrix2(X);
            X.mul(A, X2);
            RealMatrix2 cX = new RealMatrix2(X);
            cX.scale(c);
            E.add(cX);
            if (p)
                D.add(cX);
            else
                D.sub(cX);
            p = !p;
        }
        D.invert();
        D.mul(E);
        for (k = 1; k <= s; k++) {
            X2 = new RealMatrix2(D);
            D.mul(X2, X2);
        }
        expM = new RealMatrix2(D);
        return expM;
    }


    public String toXML(){

      StringBuffer buffer = new StringBuffer();
      RealVector rowBuffer = new RealVector(getNumCol());
      buffer.append("<REALMATRIX columns=\"" + getNumCol() +"\" "+"rows=\""+getNumRow()+"\" "+">");
      for (int i=0;i < getNumRow();i++){
        getRow(i,rowBuffer);
        buffer.append(rowBuffer.toString());
      }
      buffer.append("</REALMATRIX>\n");
      return buffer.toString();
    }




    private static double [] fromString (String data){


        StringTokenizer tokenizer = new StringTokenizer(data);
        double doubleList[] = new double[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens())
            doubleList[i++] = (new Double(tokenizer.nextToken())).doubleValue();

         return doubleList;

}




    public int getLWORK(){return LWORK;}

    public double [] getWORK(){return WORK;}

    public intW getINFO(){return INFO;}











}
