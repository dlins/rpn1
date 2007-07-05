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

public class Orbit implements RpSolution {
    //
    // Members
    //
    private OrbitPoint[] points_;
    private int intFlag_;

    private double[] times_;
    //
    // Constructor
    //

    public Orbit(OrbitPoint[] points, int flag) {

        intFlag_ = flag;
        points_=points;
    }

    public Orbit(Orbit orbit) {

        intFlag_ = orbit.getIntegrationFlag();
        points_=orbit.getPoints();
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

    //
    // Methods

   
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
        buffer.append("<ORBIT flag=\"" + getIntegrationFlag() + "\">\n");
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

            buffer.append("<ORBIT flag=\"" + getIntegrationFlag() + "\">\n");
            for (int i = 0; i < points_.length; i++) {

                buffer.append("<ORBITPOINT time=\"" +
                              ((OrbitPoint) points_[i]).getTime() + "\">");
                buffer.append(points_[i].toXML());
                buffer.append("</ORBITPOINT>\n");

            }
            buffer.append("</ORBIT>\n");
        } else {}
        return buffer.toString();

    }
}
