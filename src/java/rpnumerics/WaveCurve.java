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

public class WaveCurve extends RPnCurve implements WaveCurveBranch, RpSolution {

    private int[] curveTypes_;
    private static int[] curvesIndex_;
    private int family_;
    private int direction_;
    private List<WaveCurveBranch> branchList_;
    private double ALFA;

    public WaveCurve(int family, int increase) {
        
        family_ = family;
        direction_ = increase;
        branchList_ = new ArrayList<WaveCurveBranch>();


    }

    public void add(WaveCurveBranch branch) {
        branchList_.add(branch);

    }

    public void remove(WaveCurveBranch branch) {
        branchList_.remove(branch);
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

        List<WaveCurveBranch> result = new ArrayList<WaveCurveBranch>();

        for (WaveCurveBranch branch : branchList_) {
            result.addAll(branch.getBranchsList());
        }

        return result;
    }

    public List<RealSegment> segments() {

        List temp = new ArrayList();

        for (int i = 0; i < getBranchsList().size(); i++) {
            for (int j = 0; j < ((FundamentalCurve) getBranchsList().get(i)).segments().size(); j++) {
                temp.add(((FundamentalCurve) getBranchsList().get(i)).segments().get(j));
            }
        }

        return temp;

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (WaveCurveBranch waveCurveBranch : branchList_) {


            for (WaveCurveBranch waveCurveBranch2 : waveCurveBranch.getBranchsList()) {

                FundamentalCurve orbit = (FundamentalCurve) waveCurveBranch2;

                stringBuilder.append("--------Inicio branch-------------"+orbit.getCurveType()+"\n");

                for (int i = 0; i < orbit.getPoints().length; i++) {

                    stringBuilder.append(orbit.getPoints()[i] + " " + orbit.getPoints()[i].getCorrespondingCurveIndex() + " " + orbit.getPoints()[i].getCorrespondingPointIndex()+"\n");

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
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/data" +curveIndex +".txt");
            BufferedWriter saida = new BufferedWriter(gravador);

            String direction = "Forward";
            if (getDirection()==Orbit.WAVECURVE_BACKWARD) direction = "Backward";

            saida.write("%% " +getClass().getSimpleName() + " Family:" +getFamily() + " Direction:" +direction +"\n");
            saida.write("%% xcoord1 ycoord1 xcoord2 ycoord2\n");

            for (int i = 0; i < segments().size(); i++) {
                saida.write(segments().get(i).toString() +"\n");
            }

            saida.close();
        }
        catch (IOException e) {
            System.out.println("Arquivos .txt de WaveCurve nao foram escritos.");
        }

        return buffer.toString();

    }
    

    public String create2DPointMatlabPlot(int x, int y, int identifier) {

        StringBuffer buffer = new StringBuffer();

        String color = null;

        if (getFamily()==0)
            color = "[0 0 1]";
        if (getFamily()==1)
            color = "[1 0 0]";

        x++;
        y++;

        // ---
        //buffer.append("data" +identifier +" = read_data_file('data" +identifier +".txt');\n");
        buffer.append("data" +identifier +" = importdata('data" +identifier +".txt');\n");
        buffer.append("disp('data" +identifier +".txt')\n");
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


}
