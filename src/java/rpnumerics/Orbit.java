/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealVector;


import wave.multid.view.ViewingAttr;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import rpn.RPnUIFrame;
import rpn.component.MultidAdapter;
import wave.multid.CoordsArray;
import wave.util.RealSegment;

public class Orbit extends RPnCurve implements RpSolution {
    //
    // Members
    //

    public static final int FORWARD_DIR = 20;
    public static final int BACKWARD_DIR = 22;
    public static final int BOTH_DIR = 0;

    private OrbitPoint[] points_;
    private int increase_;
    private boolean interPoincare_;


    private List<? extends RealSegment> segments_;

    private double ALFA;


    //
    // Constructor
    //
    public Orbit(RealVector[] coords, double[] times, int increase) {
        super(MultidAdapter.converseRealVectorsToCoordsArray(coords), new ViewingAttr(Color.white));

        increase_ = increase;
        points_ = orbitPointsFromRealVectors(coords, times);

        segments_ = MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseRPnCurveToCoordsArray(this));

        interPoincare_ = false;

    }

    public Orbit(OrbitPoint[] points,  int increase) {
        super(MultidAdapter.converseOrbitPointsToCoordsArray(points), new ViewingAttr(Color.white));
        increase_ = increase;
        points_ = points;
        CoordsArray [] arrayTeste = MultidAdapter.converseOrbitPointsToCoordsArray(points);
        segments_ = MultidAdapter.converseCoordsArrayToRealSegments(arrayTeste);

        interPoincare_ = false;

    }

    public Orbit(Orbit orbit) {

        super(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), new ViewingAttr(Color.white));

        increase_ = orbit.getDirection();
        points_ = orbit.getPoints();

        segments_ = MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseRPnCurveToCoordsArray(this));

        interPoincare_ = false;


    }

    private static OrbitPoint[] orbitPointsFromRealVectors(RealVector[] coords,
            double[] times) {
        OrbitPoint[] result = new OrbitPoint[times.length];
        for (int i = 0; i < times.length; i++) {
            result[i] = new OrbitPoint(coords[i], times[i]);
        }
        return result;
    }



    public boolean isInterPoincare() {
        return interPoincare_;
    }

    public void setInterPoincare(boolean interPoincare) {
        interPoincare_ = interPoincare;
    }

  

    //
    // Methods
    //
    // there is a possibility that the concatenation of
    // Orbits not exist...
//    static public Orbit cat(Orbit curve1, Orbit curve2) {//TODO Reimplements . Bugged !
//        Orbit swap = new Orbit(curve1.getPoints(), RpSolution.DEFAULT_NULL_FLAG);
//        swap.cat(curve2);
//        return swap;
//    }






    public void cat(Orbit curve) {//TODO Reimplements . Bugged !
        // opposite time directions assumed...
        OrbitPoint[] swap = new OrbitPoint[points_.length
                + curve.getPoints().length - 1];
        double deltat = lastPoint().getLambda() - curve.lastPoint().getLambda();
        for (int i = 0, j = curve.getPoints().length - 2; i < swap.length; i++) {
            if (i >= points_.length) {
                swap[i] = curve.getPoints()[j--];
                swap[i].setLambda(swap[i].getLambda() + deltat);
            } else {
                swap[i] = (OrbitPoint) points_[i];
            }
        }
        System.arraycopy(swap, 0, points_, 0, swap.length);
    }


    public void append(Orbit orbit){

        int pointsOldSize=points_.length;


        OrbitPoint[] temp = new OrbitPoint[pointsOldSize];

        System.arraycopy(points_, 0, temp, 0, pointsOldSize);


        points_= new OrbitPoint[pointsOldSize+orbit.getPoints().length];


        for (int i = 0; i < temp.length; i++) {
            points_[i] = new OrbitPoint(temp[i]);

        }

        for (int i = 0; i < orbit.getPoints().length; i++) {

            points_[i+pointsOldSize] = new OrbitPoint(orbit.getPoints()[i]);

        }

    }



    public static Orbit concat(Orbit backward, Orbit forward) {
        // opposite time directions assumed...
        OrbitPoint[] swap = new OrbitPoint[backward.getPoints().length
                + forward.getPoints().length - 1];


        for (int i = 0, j = backward.getPoints().length - 1; i < swap.length; i++) {
            if (i >= backward.getPoints().length) {
                swap[i] = (OrbitPoint) forward.getPoints()[i - backward.getPoints().length + 1];
            } else {
                swap[i] = backward.getPoints()[j--];

            }
        }

        return new Orbit(swap, Orbit.BOTH_DIR);

    }



    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n points = ");
        for (int i = 0; i < points_.length; i++) {
            buf.append("[" + i + "] = " + points_[i] + "  ");
            buf.append("\n");
        }
        return buf.toString();
    }

    public String toXML() {

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < getPoints().length; i++) {

            buffer.append(getPoints()[i].toXML());

        }
        return buffer.toString();

    }

    //*********************************** Alterei este método em 17/08 (Leandro)
    public String toMatlabData(int curveIndex) {

        StringBuffer buffer = new StringBuffer();

        try {
            //FileWriter gravador = new FileWriter("/home/moreira/Documents/data" +curveIndex +".txt");
            FileWriter gravador = new FileWriter(RPnUIFrame.dir + "/data" +curveIndex +".txt");
            BufferedWriter saida = new BufferedWriter(gravador);

            for (int i = 0; i < points_.length; i++) {
                OrbitPoint orbitPoint = points_[i];
                saida.write(orbitPoint.toString() +"\n");
            }

            saida.close();
        }
        catch (IOException e) {
            System.out.println("Arquivos .txt de Orbit nao foram escritos.");
        }

        return buffer.toString();

    }
    //**************************************************************************

    //
    // Accessors/Mutators
    //
    public OrbitPoint[] getPoints() {
        return points_;
    }

    public OrbitPoint lastPoint() {
        return (OrbitPoint) points_[points_.length - 1];
    }

    public OrbitPoint firstPoint() {
        return (OrbitPoint) points_[0];
    }

    public int getDirection() {
        return increase_;
    }

    public void setIntegrationFlag(int flag) {
        increase_ = flag;
    }

//    public String createPoint3DMatlabPlot(int identifier) {
//
//        StringBuffer buffer = new StringBuffer();
//
//        buffer.append("data" +identifier +" = read_data_file('data" +identifier +".txt');\n");        //*** Leandro
//        buffer.append("disp('data" +identifier +".txt')\n");                                          //*** Leandro
//
//        String color = null;
//        if (this instanceof RarefactionOrbit) {
//            RarefactionOrbit rarefactionOrbit = (RarefactionOrbit) this;
//            int family = rarefactionOrbit.getFamilyIndex();
//            if (family == 1) {
//                color = "[1 0 0]";
//            } else {
//                color = "[0 0 1]";
//            }
//
//        }
//
//        buffer.append("plot3(data" + identifier + "(:,1),data" + identifier + "(:,2),data" + identifier + "(:,3),'Color'," + color + ")\n");
//        buffer.append("hold on\n");
//
//        RealVector xMin = RPNUMERICS.boundary().getMinimums();
//        RealVector xMax = RPNUMERICS.boundary().getMaximums();
//
//        buffer.append("axis([" + xMin.getElement(0) + " " + xMax.getElement(0) + " " + xMin.getElement(1) + " " + xMax.getElement(1) + " " + xMin.getElement(2) + " " + xMax.getElement(2) + "]);\n");
//
//        buffer.append("xlabel('s')\nylabel('T')\nzlabel('u')\n");
//        buffer.append("pause\n");
//
//        return buffer.toString();
//
//    }

//    public String create2DPointMatlabPlot(int x, int y, int identifier) {
//
//        StringBuffer buffer = new StringBuffer();Orbit orbitFWD = (Orbit) nativeCalc(start_, RPNUMERICS.getShockProfile().getXZero(), RPNUMERICS.getShockProfile().getSigma(), Orbit.FORWARD_DIR ,poincareSection_);
//
//        String color = null;
//
//        if (this instanceof RarefactionOrbit) {
//            RarefactionOrbit rOrbit = (RarefactionOrbit) this;
//
//            int family = rOrbit.getFamilyIndex();
//            if (family == 1) {
//                color = "[1 0 0]";
//            } else {
//                color = "[0 0 1]";
//            }
//        }
//
//
//        x++;
//        y++;
//
//        buffer.append("plot(data" + identifier + "(:,");
//        buffer.append(x);
//        buffer.append("),");
//        buffer.append("data" + identifier + "(:,");
//        buffer.append(y);
//
//        buffer.append("),'Color'" + "," + color + ")\n");
//
//        buffer.append("hold on\n");
//
//        RealVector xMin = RPNUMERICS.boundary().getMinimums();
//        RealVector xMax = RPNUMERICS.boundary().getMaximums();
//
//        buffer.append("axis([" + xMin.getElement(x - 1) + " " + xMax.getElement(x - 1) + " " + xMin.getElement(y - 1) + " " + xMax.getElement(y - 1) + "]);\n");
//
//        buffer.append(createAxisLabel2D(x - 1, y - 1));
//        return buffer.toString();
//
//    }

    private static String createAxisLabel2D(int x, int y) {

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

    @Override
    public List segments() {
        return segments_;
    }


    
}
