/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
import rpnumerics.RarefactionOrbit;
import rpnumerics.RarefactionOrbitCalc;
import wave.util.RealVector;

public class RarefactionOrbitGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionOrbitGeomFactory(RarefactionOrbitCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        RarefactionOrbit orbit = (RarefactionOrbit) geomSource();

        return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), this);

    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
        String tdir = "pos";
        if (((RarefactionOrbitCalc) rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR) {
            tdir = "neg";
        }
        str.append("<RAREFACIONORBITCALC tdirection=\"" + tdir + "\" calcready=\"" + rpn.parser.RPnDataModule.RESULTS + "\">\n");
        if (!rpn.parser.RPnDataModule.RESULTS) {
            str.append(((Orbit) geomSource()).getPoints()[0].toXML());
        }
        str.append(((RarefactionOrbit) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
        str.append("</RAREFACTIONORBITCALC>\n");
        return str.toString();
    }

    public String toMatlab() {

        StringBuffer buffer = new StringBuffer();
        RarefactionOrbit orbit = (RarefactionOrbit) geomSource();


        String plotString = "figure; set(gca, 'Color',[0 0 0]" + ");hold on\n";
        buffer.append("%%\nclose all;clear all;\n");
        buffer.append(orbit.toMatlabData());


        buffer.append("%%\n% begin plot x y\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(plotString);
        buffer.append(toMatlabPlot(2, 1));


        buffer.append("\n%%\n% begin plot x z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(plotString);
        buffer.append(toMatlabPlot(3, 1));


        buffer.append("\n%%\n% begin plot y z\n");

        buffer.append(plotString);
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(toMatlabPlot(3, 2));

//        System.out.println(orbit.toMatlabData());
//
//        System.out.println("plot(data(:,1),data(:2))");

        return buffer.toString();



    }

    private String toMatlabPlot(int x, int y) {

        StringBuffer buffer = new StringBuffer();

        RarefactionOrbit orbit = (RarefactionOrbit) geomSource();

        System.out.println("Flag: " + orbit.getFamilyIndex());

        int family = orbit.getFamilyIndex();

        String color;

        if (family == 1) {
            color = "[1 0 0]";
        } else {
            color = "[0 0 1]";
        }


        buffer.append("plot(data(:,");
        buffer.append(x);
        buffer.append("),");
        buffer.append("data(:,");
        buffer.append(y);

        buffer.append("),'Color'" + "," + color + ")\n");

        RealVector xMin = RPNUMERICS.boundary().getMinimums();
        RealVector xMax = RPNUMERICS.boundary().getMaximums();

        buffer.append("axis([" + xMin.getElement(x-1) + " " + xMax.getElement(x-1) + " " + xMin.getElement(y-1) + " " + xMax.getElement(y-1) + "]);\n");

        buffer.append(createAxisLabel2D(x-1, y-1));
        return buffer.toString();

    }

    private String createAxisLabel2D(int x, int y) {

        String axisName[] = new String[3];

        axisName[0] = "s";
        axisName[2] = "T";
        axisName[1] = "u";


        StringBuffer buffer = new StringBuffer();
        buffer.append("xlabel('");
        buffer.append(axisName[x] + "')\n");
        buffer.append("ylabel('" + axisName[y] + "')\n");

        return buffer.toString();



    }
}
