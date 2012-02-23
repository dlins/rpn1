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
    protected RpGeometry createGeomFromSource() {

        HugoniotCurve curve = (HugoniotCurve) geomSource();
        System.out.println("curve.getXZero() : " +curve.getXZero());
        
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

        StringBuilder buffer = new StringBuilder();

        HugoniotCurveCalcND calc = (HugoniotCurveCalcND)rpCalc();

        String commandName = geomSource().getClass().getName();
        commandName = commandName.toLowerCase();
        commandName = commandName.replaceAll(".+\\.", "");

        buffer.append("<COMMAND name=\""+commandName+"\""+ " inputpoint=\"" + hugoniotCurve.getXZero().toString() + "\" " + calc.getParams().toString()+  ">\n");

        buffer.append(((HugoniotCurve) geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();
   

    }
}
