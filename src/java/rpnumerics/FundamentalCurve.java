/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rpn.RPnUIFrame;
import rpn.component.ClosestDistanceCalculator;
import wave.util.RealSegment;
import wave.util.RealVector;

public class FundamentalCurve extends Orbit implements WaveCurveBranch, RpSolution {

    private int familyIndex_;
    private int curveType_;
    private int curveIndex_;
    private boolean initialSubCurve_;
    private OrbitPoint referencePoint_;
    private List<OrbitPoint> points_;
    public FundamentalCurve(OrbitPoint[] points, int family, int increase) {
        super(points, increase);
        familyIndex_ = family;

        points_= new ArrayList<OrbitPoint>();
        
        for (int i = 0; i < points.length; i++) {
            OrbitPoint orbitPoint = points[i];
            points_.add(orbitPoint);
            
        }
        

        
    }

    public int getFamilyIndex() {
        return familyIndex_;
    }


    public int getCurveType() {
        return curveType_;
    }

    public int getCurveIndex() {
        return curveIndex_;
    }

    public void setCurveIndex(int curveIndex_) {
        this.curveIndex_ = curveIndex_;
    }



    public void setCurveType(int curveType_) {
        this.curveType_ = curveType_;
    }

    public boolean isInitialSubCurve() {
        return initialSubCurve_;
    }

    public void setInitialSubCurve(boolean initialSubCurve_) {
        this.initialSubCurve_ = initialSubCurve_;
    }





    public List<WaveCurveBranch> getBranchsList() {

        List<WaveCurveBranch> result = new ArrayList<WaveCurveBranch>();

        result.add(this);

        return result;

    }



    // ---------------------------- Acrescentei estes métodos em 18JAN2013 (Leandro)
    public String toMatlabData2D(int curveIndex) {

        StringBuffer buffer = new StringBuffer();

        try {
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/data" +curveIndex +".txt");
            BufferedWriter saida = new BufferedWriter(gravador);

            String direction = "Forward";
            if (getDirection()==Orbit.BACKWARD_DIR) direction = "Backward";

            saida.write("%% " +getClass().getSimpleName() + " Family:" +getFamilyIndex() + " Direction:" +direction +"\n");
            saida.write("%% xcoord1 ycoord1 xcoord2 ycoord2\n");

            for (int i = 0; i < segments().size(); i++) {
                RealSegment orbitPoint = (RealSegment) segments().get(i);
                saida.write(orbitPoint.toString() +"\n");
            }

            saida.close();
        }
        catch (IOException e) {
            System.out.println("Arquivos .txt de Orbit nao foram escritos.");
        }

        return buffer.toString();

    }


    public String create2DPointMatlabPlot(int x, int y, int identifier) {

        StringBuffer buffer = new StringBuffer();

        String color = null;

        if (getFamilyIndex()==0)
            color = "[0 0 1]";
        if (getFamilyIndex()==1)
            color = "[1 0 0]";

        x++;
        y++;

        buffer.append("data" +identifier +" = importdata('data" +identifier +".txt');\n");
        buffer.append("disp('data" +identifier +".txt')\n");

        buffer.append("plot(data" + identifier + "(:,");
        buffer.append(x);
        buffer.append("),");
        buffer.append("data" + identifier + "(:,");
        buffer.append(y);

        buffer.append("),'Color'" + "," + color + ")\n");

        buffer.append("hold on\n");

        RealVector xMin = RPNUMERICS.boundary().getMinimums();
        RealVector xMax = RPNUMERICS.boundary().getMaximums();

        buffer.append("axis([" + xMin.getElement(x - 1) + " " + xMax.getElement(x - 1) + " " + xMin.getElement(y - 1) + " " + xMax.getElement(y - 1) + "]);\n");

        return buffer.toString();

    }
    // -------------------------------------------------------------------------

    @Override
    public OrbitPoint getReferencePoint() {
        return referencePoint_;
    }

    @Override
    public void setReferencePoint(OrbitPoint referencePoint) {
        referencePoint_=referencePoint;
    }


    public List<OrbitPoint> getBranchPoints() {
        
        return  points_;
        
    }

    @Override
    public double getSpeed(OrbitPoint point) {
        

        
        int segmentIndex = findClosestSegment(new RealVector(point.getCoords()));

        OrbitPoint curvePoint = points_.get(segmentIndex);
        
        return curvePoint.getLambda();
    }



}
