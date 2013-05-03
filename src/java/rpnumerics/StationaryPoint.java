/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

//
// finds a stationary point of the vector field f
// using Newton method with the starting point x0
//
// Output description
//        DimN   Dimension of negative (attracting) invariant subspace
//        DimP   Dimension of positive (repelling) invariant subspace
//        eigenValR    Eigenvalues with negative real parts: Re
//        eigenValI    Eigenvalues with negative real parts: Im
//        eigenVec     Eigenvectors
//        schurFormN   Schur form with negative real part eigenvalues first
//        schurVecN    corresponding transformation matrix
//        schurFormP   Schur form with positive real part eigenvalues first
//        schurVecN    corresponding transformation matrix
//
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class StationaryPoint extends PhasePoint  {
    //
    // Members
    //

    private double[] eigenValR_;
    private double[] eigenValI_;
    private RealVector[] eigenVec_;
    private int DimP_;
    private RealMatrix2 schurFormP_;
    private RealMatrix2 schurVecP_;
    private int DimN_;
    private RealMatrix2 schurFormN_;
    private RealMatrix2 schurVecN_;
    private int integrationFlag_;
    private boolean isSaddle = false;
    private int type;

    //
    // Constructors
    //
    public StationaryPoint(StationaryPoint copy) {
        super(copy);
        eigenValR_ = copy.getEigenValR();
        eigenValI_ = copy.getEigenValI();
        eigenVec_ = copy.getEigenVec();
        DimP_ = copy.getDimP();
        schurFormP_ = copy.getSchurFormP();
        schurVecP_ = copy.getSchurVecP();
        DimN_ = copy.getDimN();
        schurFormN_ = copy.getSchurFormN();
        schurVecN_ = copy.getSchurVecN();
        integrationFlag_ = copy.getIntegrationFlag();
    }

    public StationaryPoint(PhasePoint point, double[] eigenValR, double[] eigenValI, RealVector[] eigenVec) {
        super(point);
        eigenValR_ = eigenValR;
        eigenValI_ = eigenValI;
        eigenVec_ = eigenVec;

        if (eigenValI[0] != 0 || eigenValI[1] != 0) {
            type = 2; // com autovalores complexos
        } else {
            if (eigenValR[0] * eigenValR[1] < 0.) {
                isSaddle = true;
                type = 0; //sela
            }

            if (eigenValR[0] > 0. && eigenValR[1] > 0.) {
                type = 1;     //repulsor
            }

            if (eigenValR[0] < 0. && eigenValR[1] < 0.) {
                type = -1;     //atrator
            }

        }



    }
    //
    // Accessors/Mutators
    //
    // TODO should be deprecated !

    public int getType() {
        return type;
    }

    public PhasePoint getPoint() {
        return this;
    }

    public double[] getEigenValR() {
        return eigenValR_;
    }

    public double[] getEigenValI() {
        return eigenValI_;
    }

    public RealVector[] getEigenVec() {
        return eigenVec_;
    }

    public int getDimP() {
        return DimP_;
    }

    public RealMatrix2 getSchurFormP() {
        return schurFormP_;
    }

    public RealMatrix2 getSchurVecP() {
        return schurVecP_;
    }

    public int getDimN() {
        return DimN_;
    }

    public RealMatrix2 getSchurFormN() {
        return schurFormN_;
    }

    public RealMatrix2 getSchurVecN() {
        return schurVecN_;
    }

    public int getIntegrationFlag() {
        return integrationFlag_;
    }

    public boolean isSaddle() {
        return isSaddle;
    }

    //
    // Methods
    //
    private PhasePoint[] orbitInitialPoints(RealVector dir) {

        PhasePoint[] array = new PhasePoint[2];

        double h = 1E-2;
        RealVector center = new RealVector(getCoords());
        RealVector point1 = new RealVector(2);
        RealVector point2 = new RealVector(2);

        for (int i = 0; i < 2; i++) {
            point1.setElement(0, h * dir.getElement(0) + center.getElement(0));
            point1.setElement(1, h * dir.getElement(1) + center.getElement(1));

            point2.setElement(0, -h * dir.getElement(0) + center.getElement(0));
            point2.setElement(1, -h * dir.getElement(1) + center.getElement(1));
        }

        array[0] = new PhasePoint(point1);
        array[1] = new PhasePoint(point2);

        return array;
    }

    public PhasePoint[] orbitDirectionFWD() {

        RealVector dir = new RealVector(2);

        for (int i = 0; i < 2; i++) {
            if (getEigenValR()[i] > 0.) {
                dir = new RealVector(getEigenVec()[i]);
            }
        }

        return orbitInitialPoints(dir);

    }

    public PhasePoint[] orbitDirectionBWD() {

        RealVector dir = new RealVector(2);

        for (int i = 0; i < 2; i++) {
            if (getEigenValR()[i] < 0.) {
                dir = new RealVector(getEigenVec()[i]);
            }
        }

        return orbitInitialPoints(dir);

    }

    public RealVector[] initialManifoldPoint() throws RpException {

        if (isSaddle) {
            RealVector[] array = new RealVector[4];

            double h = 1E-2;

            RealVector center = new RealVector(getPoint().getCoords());
            RealVector dir1 = new RealVector(getEigenVec()[0]);
            RealVector dir2 = new RealVector(getEigenVec()[1]);
            RealVector p1 = new RealVector(2);
            RealVector p2 = new RealVector(2);
            RealVector p3 = new RealVector(2);
            RealVector p4 = new RealVector(2);

            p1.setElement(0, h * (dir1.getElement(0)) + center.getElement(0));
            p1.setElement(1, h * (dir1.getElement(1)) + center.getElement(1));
            array[0] = p1;

            p2.setElement(0, -h * (dir1.getElement(0)) + center.getElement(0));
            p2.setElement(1, -h * (dir1.getElement(1)) + center.getElement(1));
            array[1] = p2;



            p3.setElement(0, h * (dir2.getElement(0)) + center.getElement(0));
            p3.setElement(1, h * (dir2.getElement(1)) + center.getElement(1));
            array[2] = p3;

            p4.setElement(0, -h * (dir2.getElement(0)) + center.getElement(0));
            p4.setElement(1, -h * (dir2.getElement(1)) + center.getElement(1));
            array[3] = p4;

            return array;
        } else {
            throw new RpException("Stationary Point is not Saddle!");
        }

    }

    @Override
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<STATPOINT>\n");
        buffer.append("\n");

        buffer.append("<EIGENVALR>");
        for (int i = 0; i < getEigenValR().length; i++) {

            buffer.append(getEigenValR()[i] + " ");

        }

        buffer.append("</EIGENVALR>\n");
        buffer.append("\n");

        buffer.append("<EIGENVALI>");

        for (int i = 0; i < getEigenValI().length; i++) {
            buffer.append(getEigenValI()[i] + " ");
        }

        buffer.append("</EIGENVALI>\n");
        buffer.append("\n");
        buffer.append("<EIGENVEC>\n");

        for (int i = 0; i < getEigenVec().length; i++) {

            buffer.append(getEigenVec()[i].toXML() + "\n");

        }
        buffer.append("\n");
        buffer.append("</EIGENVEC>\n");
        buffer.append("\n");
        buffer.append("<DIMP>" + getDimP() + "</DIMP>\n");
        buffer.append("\n");
        buffer.append("<DIMN>" + getDimN() + "</DIMN>\n");
        buffer.append("\n");
        buffer.append("<INTEGRATIONFLAG>" + getIntegrationFlag() + "</INTEGRATIONFLAG>\n");
        buffer.append("\n");
        buffer.append("<SCHURFORMN>\n" + getSchurFormN().toXML() + "</SCHURFORMN>\n");
        buffer.append("\n");
        buffer.append("<SCHURFORMP>\n" + getSchurFormP().toXML() + "</SCHURFORMP>\n");
        buffer.append("\n");
        buffer.append("<SCHURVECP>\n" + getSchurVecP().toXML() + "</SCHURVECP>\n");
        buffer.append("\n");
        buffer.append("<SCHURVECN>\n" + getSchurVecN().toXML() + "</SCHURVECN>\n");
        buffer.append("\n");
        buffer.append("</STATPOINT>\n");

        return buffer.toString();

    }

    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();

        if (calcReady) {


            buffer.append("<STATPOINT>\n");
            buffer.append("\n");

            buffer.append("<EIGENVALR>");
            for (int i = 0; i < getEigenValR().length; i++) {

                buffer.append(getEigenValR()[i] + " ");

            }

            buffer.append("</EIGENVALR>\n");
            buffer.append("\n");

            buffer.append("<EIGENVALI>");

            for (int i = 0; i < getEigenValI().length; i++) {
                buffer.append(getEigenValI()[i] + " ");
            }

            buffer.append("</EIGENVALI>\n");
            buffer.append("\n");
            buffer.append("<EIGENVEC>\n");

            for (int i = 0; i < getEigenVec().length; i++) {

                buffer.append(getEigenVec()[i].toXML() + "\n");

            }
            buffer.append("\n");
            buffer.append("</EIGENVEC>\n");
            buffer.append("\n");
            buffer.append("<DIMP>" + getDimP() + "</DIMP>\n");
            buffer.append("\n");
            buffer.append("<DIMN>" + getDimN() + "</DIMN>\n");
            buffer.append("\n");
            buffer.append("<INTEGRATIONFLAG>" + getIntegrationFlag()
                    + "</INTEGRATIONFLAG>\n");
            buffer.append("\n");
            buffer.append("<SCHURFORMN>\n" + getSchurFormN().toXML()
                    + "</SCHURFORMN>\n");
            buffer.append("\n");
            buffer.append("<SCHURFORMP>\n" + getSchurFormP().toXML()
                    + "</SCHURFORMP>\n");
            buffer.append("\n");
            buffer.append("<SCHURVECP>\n" + getSchurVecP().toXML() + "</SCHURVECP>\n");
            buffer.append("\n");
            buffer.append("<SCHURVECN>\n" + getSchurVecN().toXML() + "</SCHURVECN>\n");
            buffer.append("\n");
            buffer.append("</STATPOINT>\n");

        }

        return buffer.toString();

    }
    
    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
