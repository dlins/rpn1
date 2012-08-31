/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import wave.util.RealSegment;

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
            for (int j = 0; j < ((WaveCurveOrbit) getBranchsList().get(i)).segments().size(); j++) {
                temp.add(((WaveCurveOrbit) getBranchsList().get(i)).segments().get(j));
            }
        }

        return temp;

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (WaveCurveBranch waveCurveBranch : branchList_) {


            for (WaveCurveBranch waveCurveBranch2 : waveCurveBranch.getBranchsList()) {

                WaveCurveOrbit orbit = (WaveCurveOrbit) waveCurveBranch2;

                stringBuilder.append("--------Inicio branch-------------"+orbit.getCurveType()+"\n");

                for (int i = 0; i < orbit.getPoints().length; i++) {

                    stringBuilder.append(orbit.getPoints()[i] + " " + orbit.getPoints()[i].getCorrespondingCurveIndex() + " " + orbit.getPoints()[i].getCorrespondingPointIndex()+"\n");

                }

                stringBuilder.append("--------Fim branch-------------\n");
            }
        }
        return stringBuilder.toString();

    }
}
