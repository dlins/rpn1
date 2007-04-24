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
import wave.util.*;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class StationaryPoint extends PhasePoint implements RpSolution {
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

    public StationaryPoint(PhasePoint point, double[] eigenValR, double[] eigenValI, RealVector[] eigenVec, int DimP,
        RealMatrix2 schurFormP, RealMatrix2 schurVecP, int DimN, RealMatrix2 schurFormN,
        RealMatrix2 schurVecN, int integrationFlag) {
            super(point);
            eigenValR_ = eigenValR;
            eigenValI_ = eigenValI;
            eigenVec_ = eigenVec;
            DimP_ = DimP;
            schurFormP_ = schurFormP;
            schurVecP_ = schurVecP;
            DimN_ = DimN;
            schurFormN_ = schurFormN;
            schurVecN_ = schurVecN;
            integrationFlag_ = integrationFlag;
    }

    //
    // Accessors/Mutators
    //
    // TODO should be deprecated !
    public PhasePoint getPoint() { return this; }

    public double[] getEigenValR() { return eigenValR_; }

    public double[] getEigenValI() { return eigenValI_; }

    public RealVector[] getEigenVec() { return eigenVec_; }

    public int getDimP() { return DimP_; }

    public RealMatrix2 getSchurFormP() { return schurFormP_; }

    public RealMatrix2 getSchurVecP() { return schurVecP_; }

    public int getDimN() { return DimN_; }

    public RealMatrix2 getSchurFormN() { return schurFormN_; }

    public RealMatrix2 getSchurVecN() { return schurVecN_; }

    public int getIntegrationFlag() { return integrationFlag_; }

    //
    // Methods
    //

    public String toXML(){

      StringBuffer buffer = new StringBuffer();

      buffer.append("<STATPOINT>\n");
      buffer.append("\n");

      buffer.append("<EIGENVALR>");
      for (int i = 0 ; i < getEigenValR().length;i++){

        buffer.append(getEigenValR()[i]+" ");

      }

      buffer.append("</EIGENVALR>\n");
      buffer.append("\n");

      buffer.append("<EIGENVALI>");

      for (int i = 0 ; i < getEigenValI().length;i++){
        buffer.append(getEigenValI()[i]+" ");
      }

      buffer.append("</EIGENVALI>\n");
      buffer.append("\n");
      buffer.append("<EIGENVEC>\n");

      for (int i=0;i <getEigenVec().length;i++){

        buffer.append(getEigenVec()[i].toXML()+"\n");

      }
      buffer.append("\n");
      buffer.append("</EIGENVEC>\n");
      buffer.append("\n");
      buffer.append("<DIMP>"+getDimP()+"</DIMP>\n");
      buffer.append("\n");
      buffer.append("<DIMN>"+getDimN()+"</DIMN>\n");
      buffer.append("\n");
      buffer.append("<INTEGRATIONFLAG>"+getIntegrationFlag()+"</INTEGRATIONFLAG>\n");
      buffer.append("\n");
      buffer.append("<SCHURFORMN>\n"+getSchurFormN().toXML()+"</SCHURFORMN>\n");
      buffer.append("\n");
      buffer.append("<SCHURFORMP>\n"+getSchurFormP().toXML()+"</SCHURFORMP>\n");
      buffer.append("\n");
      buffer.append("<SCHURVECP>\n"+getSchurVecP().toXML()+"</SCHURVECP>\n");
      buffer.append("\n");
      buffer.append("<SCHURVECN>\n"+getSchurVecN().toXML()+"</SCHURVECN>\n");
      buffer.append("\n");
      buffer.append("</STATPOINT>\n");

      return buffer.toString();

    }

    public String toXML(boolean calcReady){
      StringBuffer buffer = new StringBuffer();

      if (calcReady){


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
        buffer.append("<INTEGRATIONFLAG>" + getIntegrationFlag() +
                      "</INTEGRATIONFLAG>\n");
        buffer.append("\n");
        buffer.append("<SCHURFORMN>\n" + getSchurFormN().toXML() +
                      "</SCHURFORMN>\n");
        buffer.append("\n");
        buffer.append("<SCHURFORMP>\n" + getSchurFormP().toXML() +
                      "</SCHURFORMP>\n");
        buffer.append("\n");
        buffer.append("<SCHURVECP>\n" + getSchurVecP().toXML() + "</SCHURVECP>\n");
        buffer.append("\n");
        buffer.append("<SCHURVECN>\n" + getSchurVecN().toXML() + "</SCHURVECN>\n");
        buffer.append("\n");
        buffer.append("</STATPOINT>\n");

      }

        return buffer.toString();

    }

    public String toString() {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("Point = " + getPoint());
        return sBuffer.toString();
    }
}
