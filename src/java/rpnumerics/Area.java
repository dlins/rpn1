/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class Area {

    private RealVector topRight_;
    private RealVector downLeft_;
    private RealVector resolution_;

    public Area(RealVector resolution, RealVector topRight, RealVector downLeft) {
        topRight_ = topRight;
        downLeft_ = downLeft;
        resolution_ = resolution;

    }

    public RealVector getDownLeft() {
        return downLeft_;
    }

    public RealVector getResolution() {
        return resolution_;
    }

    public RealVector getTopRight() {
        return topRight_;
    }


    @Override
    public String toString(){

        StringBuffer buffer = new StringBuffer();

        buffer.append("<AREA>\n");
        buffer.append("<RESOLUTION>");
        buffer.append(resolution_.toString());
        buffer.append("<\\RESOLUTION>");
        buffer.append("\n");
        buffer.append("<TOP>");
        buffer.append(topRight_.toString());
        buffer.append("<\\TOP>");
        buffer.append("\n");
        buffer.append("<DOWN>");
        buffer.append(downLeft_.toString());
        buffer.append("<\\DOWN>");
        buffer.append("\n");
        buffer.append("<\\AREA>");

        return buffer.toString();

    }




}
