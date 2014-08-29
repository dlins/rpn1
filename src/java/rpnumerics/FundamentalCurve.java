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
import wave.util.RealSegment;
import wave.util.RealVector;

public class FundamentalCurve extends Orbit implements WaveCurveBranch, RpSolution, RpDiagramCalc {

    private int familyIndex_;
    private int curveType_;
    private int curveIndex_;
    private boolean initialSubCurve_;

    private double[] xi_;

    private List<OrbitPoint> points_;

    public FundamentalCurve(OrbitPoint[] points, int family, int increase) {
        super(points, increase);
        familyIndex_ = family;

        points_ = new ArrayList<OrbitPoint>();

        for (int i = 0; i < points.length; i++) {
            OrbitPoint orbitPoint = points[i];
            points_.add(orbitPoint);

        }

        xi_ = createArcLength();

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

    public void setXi(double[] xi) {
        xi_ = xi;
    }

    public double[] getXi() {
        return xi_;
    }

    @Override
    public double[] getCoordByArcLength(double x) throws RpException {

        double[] xi = getXi();

        for (int n = 0; n < xi.length; n++) {

            if (xi[n] == x) {

                OrbitPoint point = getBranchPoints().get(n);

                return point.getCoords().toDouble();
            }

        }

        throw new RpException(x + " arc lenght is not in this curve");
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
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/data" + curveIndex + ".txt");
            BufferedWriter saida = new BufferedWriter(gravador);

            String direction = "Forward";
            if (getDirection() == Orbit.BACKWARD_DIR) {
                direction = "Backward";
            }

            saida.write("%% " + getClass().getSimpleName() + " Family:" + getFamilyIndex() + " Direction:" + direction + "\n");
            saida.write("%% xcoord1 ycoord1 xcoord2 ycoord2\n");

            for (int i = 0; i < segments().size(); i++) {
                RealSegment orbitPoint = (RealSegment) segments().get(i);
                saida.write(orbitPoint.toString() + "\n");
            }

            saida.close();
        } catch (IOException e) {
            System.out.println("Arquivos .txt de Orbit nao foram escritos.");
        }

        return buffer.toString();

    }

    public String create2DPointMatlabPlot(int x, int y, int identifier) {

        StringBuffer buffer = new StringBuffer();

        String color = null;

        if (getFamilyIndex() == 0) {
            color = "[0 0 1]";
        }
        if (getFamilyIndex() == 1) {
            color = "[1 0 0]";
        }

        x++;
        y++;

        buffer.append("data" + identifier + " = importdata('data" + identifier + ".txt');\n");
        buffer.append("disp('data" + identifier + ".txt')\n");

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

    public List<OrbitPoint> getBranchPoints() {

        return points_;

    }

    @Override
    public double getSpeed(OrbitPoint point) {

        int segmentIndex = findClosestSegment(new RealVector(point.getCoords()));

        OrbitPoint curvePoint = points_.get(segmentIndex);

        return curvePoint.getSpeed();
    }

    @Override
    public RpSolution createDiagramSource() throws RpException {

      

//        List<RealVector> speedLineCoordsList = new ArrayList<RealVector>();
        List<DiagramLine> diagramLineList = new ArrayList<DiagramLine>();

        int curveType = 0;

        if (this instanceof RarefactionCurve) {
            curveType = 2;
        }

        if (this instanceof ShockCurve) {
            curveType = 1;
        }

        if (this instanceof CompositeCurve) {
            curveType = 3;
        }

        List<List<RealVector>> eigenValueLists = new ArrayList<List<RealVector>>();

        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
            //Creating lists for eigen values 
            ArrayList<RealVector> eigenValueCoords = new ArrayList<RealVector>();

            eigenValueLists.add(eigenValueCoords);

            //Creating reference point eigen values lines
            List<RealVector> referenceEigenValueList = new ArrayList<RealVector>();

            RealVector p1 = new RealVector(2);
            RealVector p2 = new RealVector(2);

            p1.setElement(0, xi_[0]);
            p1.setElement(1, getReferencePoint().getEigenValues()[i]);

            p2.setElement(0, xi_[xi_.length - 1]);
            p2.setElement(1, getReferencePoint().getEigenValues()[i]);

            referenceEigenValueList.add(p1);
            referenceEigenValueList.add(p2);

            ArrayList<List<RealVector>> referenceEigenValueLineList = new ArrayList<List<RealVector>>();
            referenceEigenValueLineList.add(referenceEigenValueList);

            DiagramLine eigenValueLine = new DiagramLine(referenceEigenValueLineList);
            
            
            eigenValueLine.setName("Reference point eigen value: "+ i);

            eigenValueLine.setType(0, curveType);

            diagramLineList.add(eigenValueLine);

        }
        
        
        List<RealVector> speedLineCoordsList = new ArrayList<RealVector>();

        for (int i = 0; i < getBranchPoints().size(); i++) {
            OrbitPoint point = getBranchPoints().get(i);

            double xi = xi_[i];

            // Filling Speed


            RealVector speedDiagramCoord = new RealVector(2);

            speedDiagramCoord.setElement(0, xi);
            speedDiagramCoord.setElement(1, point.getSpeed());

            speedLineCoordsList.add(speedDiagramCoord);

            //Filling Eigen values
            for (int j = 0; j < eigenValueLists.size(); j++) {
                List<RealVector> list = eigenValueLists.get(j);

                RealVector eigenCoord = new RealVector(2);

                eigenCoord.setElement(0, xi);
                eigenCoord.setElement(1, point.getEigenValues()[j]);

                list.add(eigenCoord);

            }

        }

        //Creating lines for eigen values
        for (int i = 0; i < RPNUMERICS.domainDim(); i++) {

            DiagramLine eigenValueLine = new DiagramLine("Eigen value family "+i);
            eigenValueLine.addPart(eigenValueLists.get(i));
            eigenValueLine.setType(0, curveType);

            diagramLineList.add(eigenValueLine);

        }
        
        //Crating speed line 

        DiagramLine speedLine = new DiagramLine("Speed");
        


        speedLine.addPart(speedLineCoordsList);

        speedLine.setType(0, curveType);

        diagramLineList.add(speedLine);

        Diagram diagram = new Diagram(diagramLineList);

        return diagram;

    }

    @Override
    public RpSolution updateDiagramSource() throws RpException {
        return createDiagramSource();
    }

    private double[] createArcLength() {

        double[] arcLength = new double[getBranchPoints().size()];

        arcLength[0] = 0;

        for (int n = 0; n < getBranchPoints().size() - 1; n++) {
            OrbitPoint p1 = getBranchPoints().get(n);

            OrbitPoint p2 = getBranchPoints().get(n + 1);

            double distance = p1.getCoords().distance(p2);

            arcLength[n + 1] = arcLength[n] + distance;

        }

        return arcLength;
    }

}
