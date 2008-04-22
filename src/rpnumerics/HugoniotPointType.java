/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

public class HugoniotPointType {
    public static final int DEFAULT_NO = 0;
    private int negativeRealPartNoLeft_ = DEFAULT_NO;
    private int zeroRealPartNoLeft_ = DEFAULT_NO;
    private int positiveRealPartNoLeft_ = DEFAULT_NO;
    private int negativeRealPartNoRight_ = DEFAULT_NO;
    private int zeroRealPartNoRight_ = DEFAULT_NO;
    private int positiveRealPartNoRight_ = DEFAULT_NO;

    public HugoniotPointType(double[] eigenValRLeft, double[] eigenValRRight) {
        int numericalZero = 0;
        int m = eigenValRRight.length;
        for (int k = 0; k < m; k++) {
            // counting eigenvalues real part signs for left state
            // (initial point U_0)
            if (eigenValRLeft[k] < -numericalZero) {
                negativeRealPartNoLeft_++;
            } else if (eigenValRLeft[k] <= numericalZero)
                zeroRealPartNoLeft_++;
            else
                positiveRealPartNoLeft_++;
            // counting eigenvalues real part signs for right state
            // (point on Hugoniot curve)
            if (eigenValRRight[k] < -numericalZero)
                negativeRealPartNoRight_++;
            else if (eigenValRRight[k] <= numericalZero)
                zeroRealPartNoRight_++;
            else
                positiveRealPartNoRight_++;
        }
    }

    public HugoniotPointType(int negativeRealPartNoLeft, int zeroRealPartNoLeft, int positiveRealPartNoLeft,
        int negativeRealPartNoRight, int zeroRealPartNoRight, int positiveRealPartNoRight) {
            negativeRealPartNoRight_ = negativeRealPartNoRight;
            zeroRealPartNoRight_ = zeroRealPartNoRight;
            positiveRealPartNoRight_ = positiveRealPartNoRight;
            negativeRealPartNoLeft_ = negativeRealPartNoLeft;
            zeroRealPartNoLeft_ = zeroRealPartNoLeft;
            positiveRealPartNoLeft_ = positiveRealPartNoLeft;
    }

    public int negativeRealPartNoLeft() { return negativeRealPartNoLeft_; }

    public int negativeRealPartNoRight() { return negativeRealPartNoRight_; }

    public int zeroRealPartNoLeft() { return zeroRealPartNoLeft_; }

    public int zeroRealPartNoRight() { return zeroRealPartNoRight_; }

    public int positiveRealPartNoLeft() { return positiveRealPartNoLeft_; }

    public int positiveRealPartNoRight() { return positiveRealPartNoRight_; }


    public String toString(){


      StringBuffer buffer = new StringBuffer();

      buffer.append(negativeRealPartNoLeft()+" ");
      buffer.append(negativeRealPartNoRight()+" ");
      buffer.append(zeroRealPartNoLeft()+" ");
      buffer.append(zeroRealPartNoRight()+" ");
      buffer.append(positiveRealPartNoLeft()+" ");
      buffer.append(positiveRealPartNoRight());

      return buffer.toString();


    }



    public boolean equals(HugoniotPointType test) {
        // equality with respect to signs of eigenvalue real parts
        if ((test.positiveRealPartNoLeft() == positiveRealPartNoLeft()) &&
            (test.negativeRealPartNoLeft() == negativeRealPartNoLeft()) &&
            (test.zeroRealPartNoLeft() == zeroRealPartNoLeft()) && (test.positiveRealPartNoRight() == positiveRealPartNoRight()) &&
            (test.negativeRealPartNoRight() == negativeRealPartNoRight()) &&
            (test.zeroRealPartNoRight() == zeroRealPartNoRight()))
                return true;
        return false;
    }
}
