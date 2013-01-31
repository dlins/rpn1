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

    public HugoniotCurveGeomFactory(HugoniotCurveCalc calc) {
        super(calc);


    }

    @Override
    protected RpController createUI() {
        return new HugoniotController();
    }

    //
    // Methods
    //
    public RpGeometry createGeomFromSource() {

        HugoniotCurve curve = (HugoniotCurve) geomSource();
        
        
//        System.out.println("tamanho dentro do factory: "+curve.segments().size());

        HugoniotSegGeom.DIRECTION = curve.getDirection();

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

}
