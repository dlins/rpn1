/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rpn.RPnUIFrame;
import rpn.component.HugoniotSegGeom;
import rpn.command.ClassifierCommand;
import rpn.command.VelocityCommand;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;
import wave.util.RealVector;

public class SegmentedCurve extends RpCurve implements RpSolution {


    private List<? extends RealSegment> segments_;

    private double ALFA;


    public SegmentedCurve(List<? extends RealSegment> segmentsList) {
        super(coordsArrayFromRealSegments(segmentsList), new ViewingAttr(Color.red));
        segments_ = segmentsList;

    }


    //******************************* Acrescentei este método em 17/08 (Leandro)
    public void toMatlabReadFile() {
        try {
            //FileWriter gravador = new FileWriter("/home/moreira/Documents/read_data_file.m");
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/read_data_file.m");
            BufferedWriter saida = new BufferedWriter(gravador);
            saida.write("function [A] = read_data_file(name)\n");
            saida.write("% Function [A] = read_data_file(name)\n");
            saida.write("%\n");
            saida.write("% Read into A the contents of the file.\n\n");
            saida.write("    fid = fopen(name, 'r');\n");
            saida.write("    tline = fgetl(fid);\n");
            saida.write("    noc = length(str2num(tline));\n");
            saida.write("    A = fscanf(fid, '%lg %lg', [noc inf]);\n");
            saida.write("    fclose(fid);\n");
            saida.write("    A = A';\n\n");
            saida.write("end");
            saida.close();

        } catch (IOException e) {
            System.out.println("Não deu para escrever o arquivo");
        }
    }
    //*************************************************************************

    @SuppressWarnings("static-access")
    public String toMatlabData(int identifier) {

        toMatlabReadFile();

        StringBuffer buffer = new StringBuffer();
        System.out.println(segments_.size());

        //********************************************************************** (inicia Leandro)
        if (identifier == 0) {
            buffer.append("%% Dados para marcadores\n");

            // coordenadas das strings de classificacao ------------------------
            buffer.append("dataString=[\n");

            for (int k = 0; k < ClassifierCommand.xStr.size(); k++) {
                double x = (Double)(ClassifierCommand.xStr.get(k));
                double y = (Double)(ClassifierCommand.yStr.get(k));

                if (x!=0.  &&  y!=0.) {
                    buffer.append(x + "   " + y + ";\n");
                }

            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // coordenadas das setas das strings de classificacao --------------
            buffer.append("dataSeta=[\n");

            for (int k = 0; k < ClassifierCommand.xSeta.size(); k++) {
                double x = (Double)(ClassifierCommand.xSeta.get(k));
                double y = (Double)(ClassifierCommand.ySeta.get(k));

                if (x!=0.  &&  y!=0.) {
                    buffer.append(x + "   " + y + ";\n");
                }

            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // strings de classificacao ----------------------------------------
            buffer.append("typeString=[\n");

            for (int k = 0; k < ClassifierCommand.xStr.size(); k++) {
                double x = (Double)(ClassifierCommand.xStr.get(k));
                double y = (Double)(ClassifierCommand.yStr.get(k));

                if (x!=0.  &&  y!=0.) {
                    int s1 = (Integer) (ClassifierCommand.tipo.get(k));
                    buffer.append("'");
                    buffer.append(HugoniotSegGeom.s[s1]);
                    buffer.append("'" + ";\n");
                    //buffer.append(s1 + ";\n");
                }

            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // coordenadas das strings de velocidade ---------------------------
            buffer.append("dataVel=[\n");

            for (int k = 0; k < VelocityCommand.xVel.size(); k++) {
                double x = (Double)(VelocityCommand.xVel.get(k));
                double y = (Double)(VelocityCommand.yVel.get(k));

                if (x!=0.  &&  y!=0.) {
                    buffer.append(x + "   " + y + ";\n");
                }

            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // coordenadas das setas das strings de velocidade -----------------
            buffer.append("dataSetaVel=[\n");

            for (int k = 0; k < VelocityCommand.xSetaVel.size(); k++) {
                double x = (Double)(VelocityCommand.xSetaVel.get(k));
                double y = (Double)(VelocityCommand.ySetaVel.get(k));

                if (x!=0.  &&  y!=0.) {
                    buffer.append(x + "   " + y + ";\n");
                }

            }

            buffer.append("];\n");
            //------------------------------------------------------------------


            // strings de velocidade -------------------------------------------
            buffer.append("velString=[\n");

            for (int k = 0; k < VelocityCommand.xVel.size(); k++) {
                double x = (Double)(VelocityCommand.xVel.get(k));
                double y = (Double)(VelocityCommand.yVel.get(k));

                if (x!=0.  &&  y!=0.) {
                    buffer.append(VelocityCommand.vel.get(k) + ";\n");
                }

            }

            buffer.append("];\n");
            //------------------------------------------------------------------


        }
        //********************************************************************** (finaliza Leandro)

        try {
            //FileWriter gravador = new FileWriter("/home/moreira/Documents/data" + identifier + ".txt");
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/data" +identifier +".txt");
            BufferedWriter saida = new BufferedWriter(gravador);

            for (int i = 0; i < segments_.size(); i++) {

                HugoniotSegment hSegment = ((HugoniotSegment) segments_.get(i));
                RealSegment rSegment = new RealSegment(hSegment.leftPoint(), hSegment.rightPoint());
                double leftSigma = hSegment.leftSigma();
                double rightSigma = hSegment.rightSigma();

                double R = 0.;
                double G = 0.;
                double B = 0.;

                //-------------
                R = HugoniotSegGeom.viewAttrSelection(hSegment).getColor().getRed() / 255.;
                G = HugoniotSegGeom.viewAttrSelection(hSegment).getColor().getGreen() / 255.;
                B = HugoniotSegGeom.viewAttrSelection(hSegment).getColor().getBlue() / 255.;

                //------------

//                if (geometry_ instanceof HugoniotCurveGeom) {
//
//                    R = HugoniotSegGeom.viewAttrSelection(hSegment).getColor().getRed() / 255.;
//                    G = HugoniotSegGeom.viewAttrSelection(hSegment).getColor().getGreen() / 255.;
//                    B = HugoniotSegGeom.viewAttrSelection(hSegment).getColor().getBlue() / 255.;
//                } else {
//                    R = (geometry_.viewingAttr().getColor().getRed()) / 255.;
//                    G = (geometry_.viewingAttr().getColor().getGreen()) / 255.;
//                    B = (geometry_.viewingAttr().getColor().getBlue()) / 255.;
//                }

                saida.write(rSegment.toString()
                        + "   " + leftSigma + " " + rightSigma
                        + " " + hSegment.getLeftLambdaArray()[0] + " " + hSegment.getLeftLambdaArray()[1]
                        + " " + hSegment.getRightLambdaArray()[0] + " " + hSegment.getRightLambdaArray()[1]
                        + " " + R + " " + G + " " + B + "\n");

            }
            saida.close();

//        buffer.append("%% xcoord ycoord zcoord firstPointShockSpeed secondPointShockSpeed leftEigenValue0 leftEigenValue1 rightEigenValue0 rightEigenValue1\n");
//
//        buffer.append("data" + identifier + "= [\n");
//        for (int i = 0; i < segments_.size(); i++) {
//
//            HugoniotSegment hSegment = ((HugoniotSegment) segments_.get(i));
//            RealSegment rSegment = new RealSegment(hSegment.leftPoint(),
//                    hSegment.rightPoint());
//            double leftSigma = hSegment.leftSigma();
//            double rightSigma = hSegment.rightSigma();
//            buffer.append(rSegment.toString() + "   " + leftSigma + " " + rightSigma + " " + hSegment.getLeftLambdaArray()[0] + " " + hSegment.getLeftLambdaArray()[1] + " " + hSegment.getRightLambdaArray()[0] + " " + hSegment.getRightLambdaArray()[1] + "\n");


        } catch (IOException e) {
            System.out.println("Arquivos .txt de SegmentedCurve não foram escritos.");
        }

        //**********************************************************************

//        buffer.append("];\n");
//
//        buffer.append("type" + identifier + "=[\n");
//
//        for (int i = 0; i < segments_.size(); i++) {
//            HugoniotSegment hSegment = ((HugoniotSegment) segments_.get(i));
//            buffer.append((hSegment.getType() + 1) + ";\n");
//        }
//        buffer.append("];\n");

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
        buffer.append("[data" + identifier + "(i, 13) data" + identifier + "(i, 14) data" + identifier + "(i, 15)])\n");

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
        buffer.append("[data" + identifier + "(i, 13) data" + identifier + "(i, 14) data" + identifier + "(i, 15)])\n");
        buffer.append("hold on\n");
        buffer.append("end\n");

        //********************************************************************** (inicia Leandro)

        if ((identifier == 0) && (x == 2) && (y == 1)) {

            // plot das strings de classificacao e suas setas -----------------
            buffer.append("if (bool == 1)\n");
            buffer.append("[rowS, colS] = size(dataString);\n");
            buffer.append("for k=1: rowS\n");

            buffer.append("text(dataString(k,1), "
                    + "dataString(k,2), "
                    + "horzcat(typeString(k,1),"
                    + "typeString(k,2),"
                    + "typeString(k,3),"
                    + "typeString(k,4)), "
                    + "'Color', [0 0 0], 'FontSize', 16)\n");

            buffer.append("line([dataString(k,1) " + "dataSeta(k,1)], "
                    + "[dataString(k,2) " + "dataSeta(k,2)], "
                    + "'Color', [0 0 0])\n");

            buffer.append("end\n");
            buffer.append("end\n");
            //-----------------------------------------------------------------


            // plot das strings de velocidade e suas setas -----------------

            buffer.append("if (bool2 == 1)\n");
            buffer.append("[rowV, colV] = size(dataVel);\n");
            buffer.append("for k=1: rowV\n");

            buffer.append("text(dataVel(k,1), "
                    + "dataVel(k,2), "
                    + "num2str(velString(k), '%.4e'), "
                    + "'Color', [0 0 0], 'FontSize', 12)\n");

            buffer.append("line([dataVel(k,1) " + "dataSetaVel(k,1)], "
                    + "[dataVel(k,2) " + "dataSetaVel(k,2)], "
                    + "'Color', [0 0 0])\n");

            buffer.append("end\n");
            buffer.append("end\n");

            //-----------------------------------------------------------------
        }
        //********************************************************************** (finaliza Leandro)

        buffer.append("set(gca, 'Color',[1 1 1]);\n");
        x--;
        y--;
        buffer.append("axis([" + xMin.getElement(x) + " " + xMax.getElement(x) + " " + xMin.getElement(y) + " " + xMax.getElement(y) + "]);\n");
        buffer.append(createAxisLabel2D(x, y));
        return buffer.toString();

    }

    public String createSegment3DPlotMatlabPlot(int identifier) {

        StringBuffer buffer = new StringBuffer();

        buffer.append("data" +identifier +" = read_data_file('data" +identifier +".txt');\n");      //*** Leandro
        buffer.append("disp('data" +identifier +".txt')\n");                                        //*** Leandro

        buffer.append("for i=1: length(data" + identifier + ")\n");
        buffer.append("plot3([ data" + identifier);

        buffer.append("(i" + ", 1  ) ");
        buffer.append("data" + identifier + "(i,4)],");

        buffer.append("[ data" + identifier);

        buffer.append("(i" + ",2 ) ");
        buffer.append("data" + identifier + "(i,5" + ")],");
        buffer.append("[data" + identifier + "(i, 3) data" + identifier + "(i, 6)],");
        buffer.append("'Color',");
        buffer.append("[data" + identifier + "(i, 13) data" + identifier + "(i, 14) data" + identifier + "(i, 15)])\n");

        buffer.append("hold on\n");
        buffer.append("end\n");
        RealVector xMin = RPNUMERICS.boundary().getMinimums();
        RealVector xMax = RPNUMERICS.boundary().getMaximums();


        buffer.append("axis([" + xMin.getElement(0) + " " + xMax.getElement(0) + " " + xMin.getElement(1) + " " + xMax.getElement(1) + " " + xMin.getElement(2) + " " + xMax.getElement(2) + "]);\n");
        buffer.append("view([20 30])\n");
        buffer.append("set(gca, 'Color',[1 1 1])\n");
        buffer.append("xlabel('s')\nylabel('T')\nzlabel('u')\n");
        buffer.append("pause\n");

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
        return segments_;
    }
}