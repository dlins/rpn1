/*
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

public class WaveCurve extends RPnCurve implements WaveCurveBranch, RpSolution, RpDiagramCalc {

    private int[] curveTypes_;
    private static int[] curvesIndex_;
    private int family_;
    private int direction_;
    private List<WaveCurveBranch> branchList_;
    private OrbitPoint referencePoint_;
    private double ALFA;
    private List<OrbitPoint> points_;


    public WaveCurve(int family, int increase) {

        family_ = family;
        direction_ = increase;
        branchList_ = new ArrayList<WaveCurveBranch>();
        points_ = new ArrayList<OrbitPoint>();

    }

    public void add(WaveCurveBranch branch) {
        branchList_.add(branch);

        points_.addAll(branch.getBranchPoints());

    }

    public void remove(WaveCurveBranch branch) {
        branchList_.remove(branch);

        points_.removeAll(branch.getBranchPoints());

    }

    public int[] getCurveTypes() {
        return curveTypes_;
    }

    public static int[] getCurvesIndex() {
        return curvesIndex_;
    }

    public int getFamily() {
        return family_;
    }

    public int getDirection() {
        return direction_;
    }

    public List<WaveCurveBranch> getBranchsList() {

        return branchList_;

    }

    @Override
    public List<RealSegment> segments() {

        List temp = new ArrayList();

        for (WaveCurveBranch object : getBranchsList()) {
            temp.addAll(object.segments());
        }

        return temp;

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (WaveCurveBranch waveCurveBranch : branchList_) {

            for (WaveCurveBranch waveCurveBranch2 : waveCurveBranch.getBranchsList()) {

                FundamentalCurve orbit = (FundamentalCurve) waveCurveBranch2;

                stringBuilder.append("--------Inicio branch-------------" + orbit.getCurveType() + "\n");

                for (int i = 0; i < orbit.getPoints().length; i++) {

                    stringBuilder.append(orbit.getPoints()[i] + " " + orbit.getPoints()[i].getCorrespondingCurveIndex() + " " + orbit.getPoints()[i].getCorrespondingPointIndex() + "\n");

                }

                stringBuilder.append("--------Fim branch-------------\n");
            }
        }
        return stringBuilder.toString();

    }

    // ------------------------ Acrescentei estes métodos em 15JAN2013 (Leandro)
    // ------- Isso foi feito para atender a uma necessidade emergencial do Cido
    public String toMatlabData2D(int curveIndex) {

        StringBuffer buffer = new StringBuffer();

        try {
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/data" + curveIndex + ".txt");
            BufferedWriter saida = new BufferedWriter(gravador);

            String direction = "Forward";
            if (getDirection() == Orbit.WAVECURVE_BACKWARD) {
                direction = "Backward";
            }

            saida.write("%% " + getClass().getSimpleName() + " Family:" + getFamily() + " Direction:" + direction + "\n");
            saida.write("%% xcoord1 ycoord1 xcoord2 ycoord2\n");

            for (int i = 0; i < segments().size(); i++) {
                saida.write(segments().get(i).toString() + "\n");
            }

            saida.close();
        } catch (IOException e) {
            System.out.println("Arquivos .txt de WaveCurve nao foram escritos.");
        }

        return buffer.toString();

    }

    

    @Override
    public double [] getCoordByArcLength(double x) throws RpException{

        for (WaveCurveBranch branch : getBranchsList()) {

            for (WaveCurveBranch innerBranch : branch.getBranchsList()) {

                FundamentalCurve fundamentalCurve = (FundamentalCurve) innerBranch;

                double[] xi = fundamentalCurve.getXi();

                for (int n = 0; n < xi.length; n++) {

                    if (xi[n] == x) {
                        
                    OrbitPoint point = fundamentalCurve.getBranchPoints().get(n);

                    return point.getCoords().toDouble();
                    }


                }

            }

        }
        
        throw  new RpException(x +" arc length is not in this curve");
    }

    public String create2DPointMatlabPlot(int x, int y, int identifier) {

        StringBuffer buffer = new StringBuffer();

        String color = null;

        if (getFamily() == 0) {
            color = "[0 0 1]";
        }
        if (getFamily() == 1) {
            color = "[1 0 0]";
        }

        x++;
        y++;

        // ---
        //buffer.append("data" +identifier +" = read_data_file('data" +identifier +".txt');\n");
        buffer.append("data" + identifier + " = importdata('data" + identifier + ".txt');\n");
        buffer.append("disp('data" + identifier + ".txt')\n");
        // ---

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
        referencePoint_ = referencePoint;
    }

    @Override
    public List<OrbitPoint> getBranchPoints() {

        return points_;

    }

    @Override

    public double getSpeed(OrbitPoint point) {

        int segmentIndex = findClosestSegment(new RealVector(point.getCoords()));
        OrbitPoint get = points_.get(segmentIndex);
        return get.getSpeed();
    }

    @Override
    public RpSolution createDiagramSource() {

        Diagram diagram = (Diagram) nativeDiagramCalc(family_, getId());

        return diagram;
    }

    private native RpSolution nativeDiagramCalc(int family, int curveID);

    @Override
    public RpSolution updateDiagramSource() {
        return createDiagramSource();
    }

}
