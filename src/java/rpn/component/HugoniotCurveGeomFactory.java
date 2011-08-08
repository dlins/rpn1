/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import rpn.controller.HugoniotController;
import rpn.controller.RpController;

public class HugoniotCurveGeomFactory extends RpCalcBasedGeomFactory {

    private boolean continuationMethod_;

    public HugoniotCurveGeomFactory(HugoniotCurveCalc calc) {
        super(calc);
        continuationMethod_ = false;

    }

    public HugoniotCurveGeomFactory(ShockCurveCalc calc) {
        super(calc);
        continuationMethod_ = true;

    }

    @Override
    protected RpController createUI() {
        return new HugoniotController();
    }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        HugoniotCurve curve = (HugoniotCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        return new HugoniotCurveGeom(hugoniotArray, this);

    }

    private String createAxisLabel3D(int x, int y, int z) {

        String axisName[] = new String[3];

        axisName[0] = "s";
        axisName[1] = "T";
        axisName[2] = "u";


        StringBuffer buffer = new StringBuffer();
        buffer.append("xlabel('");
        buffer.append(axisName[x] + "')\n");
        buffer.append("ylabel('" + axisName[y] + "')\n");

        buffer.append("zlabel('" + axisName[z] + "')\n");

        return buffer.toString();



    }

    public String toMatlab(int curveIndex) {
        return null;
    }

    public String toXML() {

        HugoniotCurve hugoniotCurve = (HugoniotCurve) geomSource();


        StringBuffer buffer = new StringBuffer();


        if (continuationMethod_ == false) {
            buffer.append("<COMMAND name=\"hugoniot\" inputpoint=\"" + hugoniotCurve.getXZero().toString() + "\"" + ">\n");
        } else {
            ShockCurveCalc shockCalc = (ShockCurveCalc) rpCalc();
            int direction = shockCalc.tDirection();

            if (direction == OrbitGeom.FORWARD_DIR) {
                buffer.append("<COMMAND name=\"shockforward\" inputpoint=\"" + hugoniotCurve.getXZero().toString() + "\"" + ">\n");
            }


            if (direction == OrbitGeom.BACKWARD_DIR) {
                buffer.append("<COMMAND name=\"shockbackward\" inputpoint=\"" + hugoniotCurve.getXZero().toString() + "\"" + ">\n");
            }

            if (direction == OrbitGeom.BOTH_DIR) {
                buffer.append("<COMMAND name=\"shockboth\" inputpoint=\"" + hugoniotCurve.getXZero().toString() + "\"" + ">\n");
            }

        }


//        buffer.append(((HugoniotCurve) geomSource()).toXML());//TODO Save with calculations

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
