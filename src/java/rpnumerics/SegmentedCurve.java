/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;
import wave.util.RealVector;

public class SegmentedCurve extends RPnCurve implements RpSolution {

    private List<HugoniotSegment> hugoniotSegments_;

    public SegmentedCurve(List<HugoniotSegment> hugoniotSegments) {
        super(coordsArrayFromRealSegments(hugoniotSegments), new ViewingAttr(Color.red));
        hugoniotSegments_ = hugoniotSegments;
    }

    private static CoordsArray[] coordsArrayFromRealSegments(List segments) {

        ArrayList tempCoords = new ArrayList(segments.size());
        for (int i = 0; i < segments.size(); i++) {
            RealSegment segment = (RealSegment) segments.get(i);
            tempCoords.add(new CoordsArray(segment.p1()));
            tempCoords.add(new CoordsArray(segment.p2()));

        }

        CoordsArray[] coords = new CoordsArray[tempCoords.size()];
        for (int i = 0; i < tempCoords.size(); i++) {
            coords[i] = (CoordsArray) tempCoords.get(i);
        }
        tempCoords = null;
        return coords;

    }

    public String toMatlabData(int identifier) {

        StringBuffer buffer = new StringBuffer();
        System.out.println(hugoniotSegments_.size());


        buffer.append("%% xcoord ycoord zcoord firstPointShockSpeed secondPointShockSpeed leftEigenValue0 leftEigenValue1 rightEigenValue0 rightEigenValue1\n");

        buffer.append("data" + identifier + "= [\n");
        for (int i = 0; i < hugoniotSegments_.size(); i++) {

            HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
                    i));
            RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
                    hSegment.rightPoint());
            double leftSigma = hSegment.leftSigma();
            double rightSigma = hSegment.rightSigma();
            buffer.append(rSegment.toString() + "   " + leftSigma + " " + rightSigma + " " + hSegment.getLeftLambdaArray()[0] + " " + hSegment.getLeftLambdaArray()[1] + " " + hSegment.getRightLambdaArray()[0] + " " + hSegment.getRightLambdaArray()[1] + "\n");

        }



        buffer.append("];\n");

        buffer.append("type" + identifier + "=[\n");

        for (int i = 0; i < hugoniotSegments_.size(); i++) {
            HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(
                    i));
            buffer.append((hSegment.getType() + 1) + ";\n");
        }
        buffer.append("];\n");

        return buffer.toString();
    }

    public String createMatlabPlotLoop(int x, int y, int identifier) {
        x++;
        y++;//Adjusting to Matlab's indices

        int dimension = RPNUMERICS.domainDim();
        StringBuffer buffer = new StringBuffer();

        buffer.append("for i=1: length(data" + identifier + ")\n");
        buffer.append("plot([ data" + identifier);

        buffer.append("(i" + "," + x + ") ");
        buffer.append("data" + identifier + "(i," + (x + dimension) + ")],");

        buffer.append("[ data" + identifier);

        buffer.append("(i" + "," + y + ") ");
        buffer.append("data" + identifier + "(i," + (y + dimension) + ")],");
        buffer.append("'Color',");
        buffer.append("[toc(type" + identifier + "(i), 1) toc(type" + identifier + "(i), 2) toc(type" + identifier + "(i), 3)])\n");

        buffer.append("hold on\n");

        buffer.append("end\n");

        return buffer.toString();

    }

     public static String createSegmentedMatlabPlotLoop(int x, int y, int identifier) {
        x++;
        y++;//Adjusting to Matlab's indices

        RealVector xMin = RPNUMERICS.boundary().getMinimums();
        RealVector xMax = RPNUMERICS.boundary().getMaximums();

        int dimension = RPNUMERICS.domainDim();
        StringBuffer buffer = new StringBuffer();

        buffer.append("for i=1: length(data" + identifier + ")\n");
        buffer.append("plot([ data" + identifier);

        buffer.append("(i" + "," + x + ") ");
        buffer.append("data" + identifier + "(i," + (x + dimension) + ")],");

        buffer.append("[ data" + identifier);

        buffer.append("(i" + "," + y + ") ");
        buffer.append("data" + identifier + "(i," + (y + dimension) + ")],");
        buffer.append("'Color',");
        buffer.append("[toc(type" + identifier + "(i), 1) toc(type" + identifier + "(i), 2) toc(type" + identifier + "(i), 3)])\n");
        buffer.append("hold on\n");
        buffer.append("end\n");
        buffer.append("set(gca, 'Color',[0 0 0]);\n");
        x--;
        y--;
        buffer.append("axis([" + xMin.getElement(x) + " " + xMax.getElement(x) + " " + xMin.getElement(y) + " " + xMax.getElement(y) + "]);\n");
        buffer.append(createAxisLabel2D(x, y));
        return buffer.toString();

    }



     public static String createAxisLabel2D(int x, int y) {

        String axisName[] = new String[3];

        axisName[0] = "s";
        axisName[1] = "T";
        axisName[2] = "u";


        StringBuffer buffer = new StringBuffer();
        buffer.append("xlabel('");
        buffer.append(axisName[x] + "')\n");
        buffer.append("ylabel('" + axisName[y] + "')\n");

        return buffer.toString();

    }

    public String createSegment3DPlotMatlabPlot(int identifier) {

        StringBuffer buffer = new StringBuffer();


        buffer.append("for i=1: length(data" + identifier + ")\n");
        buffer.append("plot3([ data" + identifier);

        buffer.append("(i" + ", 1  ) ");
        buffer.append("data" + identifier + "(i,4)],");

        buffer.append("[ data" + identifier);

        buffer.append("(i" + ",2 ) ");
        buffer.append("data" + identifier + "(i,5" + ")],");
        buffer.append("[data" + identifier + "(i, 3) data" + identifier + "(i, 6)],");
        buffer.append("'Color',");
        buffer.append("[toc(type" + identifier + "(i), 1) toc(type" + identifier + "(i), 2) toc(type" + identifier + "(i), 3)])\n");

        buffer.append("hold on\n");
        buffer.append("end\n");
        RealVector xMin = RPNUMERICS.boundary().getMinimums();
        RealVector xMax = RPNUMERICS.boundary().getMaximums();


        buffer.append("axis([" + xMin.getElement(0) + " " + xMax.getElement(0) + " " + xMin.getElement(1) + " " + xMax.getElement(1) + " " + xMin.getElement(2) + " " + xMax.getElement(2) + "]);\n");
        buffer.append("view([20 30])\n");
        buffer.append("set(gca, 'Color',[0 0 0])\n");
        buffer.append("xlabel('s')\nylabel('T')\nzlabel('u')\n");

        return buffer.toString();
    }

    //
    // Accessors/Mutators
    //
    public List segments() {
        return hugoniotSegments_;
    }
}
