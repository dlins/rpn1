/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpn.component.HugoniotSegGeom;
import rpn.component.util.ControlClick;
import rpn.component.util.GeometryUtil;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;
import wave.util.RealVector;

public class SegmentedCurve extends RPnCurve implements RpSolution {

    private List<HugoniotSegment> hugoniotSegments_;
    public double distancia = 0;                            //** declarei isso (Leandro)

    public SegmentedCurve(List<HugoniotSegment> hugoniotSegments) {
        super(coordsArrayFromRealSegments(hugoniotSegments), new ViewingAttr(Color.red));


        hugoniotSegments_ = hugoniotSegments;

        System.out.println("Printando segmentos da curva: ");
//        for (HugoniotSegment hugoniotSegment : hugoniotSegments) {
//
//            System.out.println(hugoniotSegment);
//
//        }
    }

    //** inseri este método (Leandro)
    @Override
    public int findClosestSegment(RealVector targetPoint, double alpha) {

        RealVector target = new RealVector(targetPoint);
        RealVector closest = null;
        RealVector segmentVector = null;
        alpha = 0;
        int closestSegment = 0;
        double closestDistance = -1;

        List hugoniotSegList = segments();

        double[] dist = new double[segments().size()];  //guarda todas as distancias entre o targetPoint e os segmentos da curva
        double distmin = 0, distprox;

        for (int i = 0; i < segments().size(); i++) {

            HugoniotSegment segment = (HugoniotSegment) hugoniotSegList.get(i);
            segmentVector = new RealVector(segment.rightPoint());
            segmentVector.sub(segment.leftPoint());  // origem do vetor na origem do sistema, vetor continua no espaco

            for (int k = 0; k < target.getSize(); k++) {                       /// Teste para calcular na projecao
                if (target.getElement(k) == 0.) {
                    segmentVector.setElement(k, 0.);
                }
            }

            closest = new RealVector(target);

            closest.sub(segment.leftPoint());   //*** ATENCAO: deve ser mudado para leftPoint()

            alpha = closest.dot(segmentVector)
                    / segmentVector.dot(segmentVector);


            if (alpha < 0) {
                alpha = 0;
            }
            if (alpha > 1) {
                alpha = 1;
            }
            segmentVector.scale(alpha);

            closest.sub(segmentVector);

            for (int k = 0; k < target.getSize(); k++) {                       /// Teste para calcular na projecao
                if (target.getElement(k) == 0.) {
                    closest.setElement(k, 0.);
                }
            }

            dist[i] = closest.norm();   //*****!!!!!!!!!

        }

        distmin = dist[0];

        for (int i = 1; i < dist.length; i++) {
            distprox = dist[i];
            if (distprox <= distmin) {
                distmin = distprox;
                closestSegment = i;
            }
        }

        distancia = distmin;
        //System.out.println("Distancia:" +distancia);

        return closestSegment;   // da ultima curva testada

    }
    //*************************************************************************

    public String toMatlabData(int identifier) {              //** Imprime no output.m os dados das curvas e a classificacao dos segmentos (preenche os campos data e type)

        StringBuffer buffer = new StringBuffer();
        System.out.println(hugoniotSegments_.size());

        //********************************************************************** (inicia Leandro)
        if (identifier == 0) {
            buffer.append("%% Dados para marcadores\n");

            // coordenadas das strings de classificacao ------------------------
            buffer.append("dataString=[\n");

            for (int k = 0; k < ControlClick.xStr.size(); k++) {
                buffer.append(ControlClick.xStr.get(k) + "   " + ControlClick.yStr.get(k) + ";\n");
            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // coordenadas das setas das strings de classificacao --------------
            buffer.append("dataSeta=[\n");

            for (int k = 0; k < ControlClick.xSeta.size(); k++) {
                buffer.append(ControlClick.xSeta.get(k) + "   " + ControlClick.ySeta.get(k) + ";\n");
            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // strings de classificacao ----------------------------------------
            buffer.append("typeString=[\n");

            for (int k = 0; k < ControlClick.xStr.size(); k++) {
                int s1 = (Integer) (GeometryUtil.tipo.get(k));
                buffer.append("'");
                buffer.append(HugoniotSegGeom.s[s1]);
                buffer.append("'" + ";\n");
                //buffer.append(s1 + ";\n");
            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // coordenadas das strings de velocidade ---------------------------
            buffer.append("dataVel=[\n");

            for (int k = 0; k < ControlClick.xVel.size(); k++) {
                buffer.append(ControlClick.xVel.get(k) + "   " + ControlClick.yVel.get(k) + ";\n");
            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // coordenadas das setas das strings de velocidade -----------------
            buffer.append("dataSetaVel=[\n");

            for (int k = 0; k < ControlClick.xSetaVel.size(); k++) {
                buffer.append(ControlClick.xSetaVel.get(k) + "   " + ControlClick.ySetaVel.get(k) + ";\n");
            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // strings de velocidade -------------------------------------------
            buffer.append("velString=[\n");

            for (int k = 0; k < ControlClick.xVel.size(); k++) {
                buffer.append(GeometryUtil.vel.get(k) + ";\n");
            }

            buffer.append("];\n");
            //------------------------------------------------------------------


        }
        //********************************************************************** (finaliza Leandro)

        buffer.append("%% xcoord ycoord zcoord firstPointShockSpeed secondPointShockSpeed leftEigenValue0 leftEigenValue1 rightEigenValue0 rightEigenValue1\n");

        buffer.append("data" + identifier + "= [\n");
        for (int i = 0; i < hugoniotSegments_.size(); i++) {

            HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(i));
            RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
                    hSegment.rightPoint());
            double leftSigma = hSegment.leftSigma();
            double rightSigma = hSegment.rightSigma();
            buffer.append(rSegment.toString() + "   " + leftSigma + " " + rightSigma + " " + hSegment.getLeftLambdaArray()[0] + " " + hSegment.getLeftLambdaArray()[1] + " " + hSegment.getRightLambdaArray()[0] + " " + hSegment.getRightLambdaArray()[1] + "\n");

        }

        buffer.append("];\n");

        buffer.append("type" + identifier + "=[\n");

        for (int i = 0; i < hugoniotSegments_.size(); i++) {
            HugoniotSegment hSegment = ((HugoniotSegment) hugoniotSegments_.get(i));
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

    public static String createSegmentedMatlabPlotLoop(int x, int y, int identifier) {               // metodo original
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

        //********************************************************************** (inicia Leandro)

        if ((identifier == 0) && (x == 2) && (y == 1)) {

            // plot das strings de classificacao e suas setas -----------------
            buffer.append("if (bool == 1)\n");
            buffer.append("[rowS, colS] = size(dataString)\n");
            buffer.append("for k=1: rowS\n");

            buffer.append("text(dataString(k,1), "
                    + "dataString(k,2), "
                    + "horzcat(typeString(k,1),"
                    + "typeString(k,2),"
                    + "typeString(k,3),"
                    + "typeString(k,4)), "
                    //+ "'Color', [1 1 1], 'FontSize', 16)\n");
                    + "'Color', [0 0 0], 'FontSize', 16)\n");

            buffer.append("line([dataString(k,1) " + "dataSeta(k,1)], "
                    + "[dataString(k,2) " + "dataSeta(k,2)], "
                    //+ "'Color', [1 1 1])\n");
                    + "'Color', [0 0 0])\n");

            buffer.append("end\n");
            buffer.append("end\n");
            //-----------------------------------------------------------------


            // plot das strings de velocidade e suas setas -----------------

            buffer.append("if (bool2 == 1)\n");
            buffer.append("[rowV, colV] = size(dataVel)\n");
            buffer.append("for k=1: rowV\n");

            buffer.append("text(dataVel(k,1), "
                    + "dataVel(k,2), "
                    + "num2str(velString(k), '%.4e'), "
                    //+ "'Color', [1 1 1], 'FontSize', 12)\n");
                    + "'Color', [0 0 0], 'FontSize', 12)\n");

            buffer.append("line([dataVel(k,1) " + "dataSetaVel(k,1)], "
                    + "[dataVel(k,2) " + "dataSetaVel(k,2)], "
                    //+ "'Color', [1 1 1])\n");
                    + "'Color', [0 0 0])\n");

            buffer.append("end\n");
            buffer.append("end\n");

            //-----------------------------------------------------------------
        }
        //********************************************************************** (finaliza Leandro)

        //buffer.append("set(gca, 'Color',[0 0 0]);\n");
        buffer.append("set(gca, 'Color',[1 1 1]);\n");
        x--;
        y--;
        buffer.append("axis([" + xMin.getElement(x) + " " + xMax.getElement(x) + " " + xMin.getElement(y) + " " + xMax.getElement(y) + "]);\n");
        buffer.append(createAxisLabel2D(x, y));
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
        //buffer.append("set(gca, 'Color',[0 0 0])\n");
        buffer.append("set(gca, 'Color',[1 1 1])\n");
        buffer.append("xlabel('s')\nylabel('T')\nzlabel('u')\n");

        return buffer.toString();
    }

    private static String createAxisLabel2D(int x, int y) {         //** Define os eixos para o output.m

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

    //
    // Accessors/Mutators
    //
    public List segments() {
        return hugoniotSegments_;
    }
}
