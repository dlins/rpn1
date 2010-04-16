/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;


import wave.multid.view.ViewingAttr;
import java.awt.Color;
import rpn.component.MultidAdapter;
import rpn.component.OrbitGeom;

public class Orbit extends RPnCurve implements RpSolution {
    //
    // Members
    //
    private OrbitPoint[] points_;
    private int intFlag_;
    //
    // Constructor
    //
    public Orbit(RealVector[] coords, double[] times, int flag) {
        super(MultidAdapter.converseRealVectorsToCoordsArray(coords), new ViewingAttr(Color.white));

        intFlag_ = flag;
        points_ = orbitPointsFromRealVectors(coords, times);
    }

    public Orbit(OrbitPoint[] points, int flag) {
        super(MultidAdapter.converseOrbitPointsToCoordsArray(points), new ViewingAttr(Color.white));
        intFlag_ = flag;
        points_ = points;
    }

    public Orbit(Orbit orbit) {

        super(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), new ViewingAttr(Color.white));

        intFlag_ = orbit.getIntegrationFlag();
        points_ = orbit.getPoints();
    }

    private static OrbitPoint[] orbitPointsFromRealVectors(RealVector[] coords,
            double[] times) {
        OrbitPoint[] result = new OrbitPoint[times.length];
        for (int i = 0; i < times.length; i++) {
            result[i] = new OrbitPoint(coords[i], times[i]);
        }
        return result;
    }

    public int findClosestSegment(wave.util.RealVector coords, double alpha) {
        return 0;
    }
  
    //
    // Methods
    //
    // there is a possibility that the concatenation of
    // Orbits not exist...
    static public Orbit cat(Orbit curve1, Orbit curve2) {//TODO Reimplements . Bugged !
        Orbit swap = new Orbit(curve1.getPoints(), RpSolution.DEFAULT_NULL_FLAG);
        swap.cat(curve2);
        return swap;
    }

    public void cat(Orbit curve) {//TODO Reimplements . Bugged !
        // opposite time directions assumed...
        OrbitPoint[] swap = new OrbitPoint[points_.length +
                curve.getPoints().length - 1];
        double deltat = lastPoint().getTime() - curve.lastPoint().getTime();
        for (int i = 0, j = curve.getPoints().length - 2; i < swap.length; i++) {
            if (i >= points_.length) {
                swap[i] = curve.getPoints()[j--];
                swap[i].setTime(swap[i].getTime() + deltat);
            } else {
                swap[i] = (OrbitPoint) points_[i];
            }
        }
        System.arraycopy(swap, 0, points_, 0, swap.length);
    }

    public static Orbit concat(Orbit backward, Orbit forward) {
        // opposite time directions assumed...
        OrbitPoint[] swap = new OrbitPoint[backward.getPoints().length +
                forward.getPoints().length - 1];

        double timeAdjust = -backward.getPoints()[0].getTime();
        
        for (int i = 0, j = backward.getPoints().length - 1; i < swap.length; i++) {
            if (i >= backward.getPoints().length) {
                swap[i] = (OrbitPoint) forward.getPoints()[i - backward.getPoints().length + 1];
            } else {
                swap[i] = backward.getPoints()[j--];
                swap[i].setTime(swap[i].getTime() + timeAdjust);

            }
        }

        return new Orbit(swap, OrbitGeom.BOTH_DIR);

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
        StringBuffer buffer = new StringBuffer();

        String timedir = "pos";
        if (getIntegrationFlag() == OrbitGeom.BACKWARD_DIR) {
            timedir = "neg";
        }

        buffer.append("<ORBIT timedirection=\"" + timedir + "\">\n");

        for (int i = 0; i < points_.length; i++) {

            buffer.append("<ORBITPOINT time=\"" +
                    ((OrbitPoint) points_[i]).getTime() + "\">");
            buffer.append(points_[i].toXML());
            buffer.append("</ORBITPOINT>\n");
        }
        buffer.append("</ORBIT>\n");

        return buffer.toString();
    }

    public String toXML(boolean calcReady) {
        StringBuffer buffer = new StringBuffer();
        if (calcReady) {
            buffer.append("<ORBIT timedirection=\"" + intFlag_ + "\"" + ">\n");
            for (int i = 0; i < points_.length; i++) {


                buffer.append("<ORBITPOINT time=\"" +
                        ((OrbitPoint) points_[i]).getTime() + "\">");
                buffer.append(points_[i].toXML());
                buffer.append("</ORBITPOINT>\n");
            }
            buffer.append("</ORBIT>\n");
        }
        return buffer.toString();
    }


      //
    // Accessors/Mutators
    //
    public OrbitPoint[] getPoints() {
        return points_;
    }

    public OrbitPoint lastPoint() {
        return (OrbitPoint) points_[points_.length - 1];
    }

    public int getIntegrationFlag() {
        return intFlag_;
    }

    public void setIntegrationFlag(int flag) {
        intFlag_ = flag;
    }



}
