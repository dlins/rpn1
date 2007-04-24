/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public abstract class QuadNDParams extends FluxParams {
    //
    // Constants
    //
    int m_;
    double[] A_;
    double[] [] B_;
    double[] [] [] C_;

    //
    // Constructors
    //
    protected QuadNDParams(QuadNDParams copy) {
        super(copy);
    }

    protected QuadNDParams(String ID, int m, double[] A, double[] [] B, double[] [] [] C,int index) {
        super(ID, new RealVector(m),index);
        m_ = m;
        RealVector params = new RealVector(m + m * m + m * m * m);
        for (int i = 0; i < m; i++) {
            params.setElement(i, A[i]);
            for (int j = 0; j < m; j++) {
                params.setElement(m + i * m + j, B[i] [j]);
                for (int k = 0; k < m; k++) {
                    params.setElement(m + m * m + i * m * m + j * m + k, C[i] [j] [k]);
                }
            }
        }
        setParams(params);
    }

    //
    // Accessors/Mutators
    //
    public double[] getA() {
        double[] A = new double[m_];
        for (int i = 0; i < m_; i++)
            A[i] = getParams().getElement(i);
        return A;
    }

    public double[] [] getB() {
        double[] [] B = new double[m_] [m_];
        for (int i = 0; i < m_; i++)
            for (int j = 0; j < m_; j++)
                B[i] [j] = getParams().getElement(m_ + i * m_ + j);
        return B;
    }

    public double[] [] [] getC() {
        double[] [] [] C = new double[m_] [m_] [m_];
        for (int i = 0; i < m_; i++)
            for (int j = 0; j < m_; j++)
                for (int k = 0; k < m_; k++)
                    C[i] [j] [k] = getParams().getElement(m_ + m_ * m_ + i * m_ * m_ + j * m_ + k);
        return C;
    }

    public void setAParam(int i, double value) {
        setParam(i, value);
    }

    public void setBParam(int i, int j, double value) {
        setParam(m_ + i * m_ + j, value);
    }

    public void setCParam(int i, int j, int k, double value) {
        setParam(m_ + m_ * m_ + i * m_ * m_ + j * m_ + k, value);
    }

    public int spaceDim() { return m_; }
}
