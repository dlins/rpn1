/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpn.controller.LevelCurveController;
import rpn.controller.RpController;
import rpnumerics.*;
import wave.util.RealSegment;

public class LevelCurveGeomFactory extends RpCalcBasedGeomFactory {

    public LevelCurveGeomFactory(LevelCurveCalc calc) {
        super(calc);


    }

    //
    // Methods
    //
    public RpGeometry createGeomFromSource() {

        LevelCurve curve = (LevelCurve) geomSource();
        int resultSize = curve.segments().size();
        RealSegGeom[] realSegArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            realSegArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));
            realSegArray[i].viewingAttr().setColor(selectViewingAttribute(curve.getFamily()));
        }
        return new LevelCurveGeom(realSegArray, this);

    }



    @Override
    protected RpController createUI() {
        return new LevelCurveController();
    }

    private Color selectViewingAttribute(int family) {

        switch (family) {

            case 0:
                return Color.blue;

            case 1:

                return Color.red;


        }

        return null;

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
}
