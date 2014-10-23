/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraphND;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import rpn.parser.RPnDataModule;
import wave.util.RealVector;

public class Area {

    private RealVector topRight_;
    private RealVector downLeft_;
    private RealVector resolution_;

    
    public Area(AreaSelected wcPolygon) {
        this(new RealVector(2),wcPolygon);//No resolution needed
    }
    
    public Area(RealVector resolution, AreaSelected wcPolygon) {

        Path2D.Double path = wcPolygon.getWCObject();

        PathIterator pathIterator = path.getPathIterator(null);

        int index = 0;
        while (!pathIterator.isDone()) {

            double[] segmentArray = new double[2];

            int segment = pathIterator.currentSegment(segmentArray);
            if (segment != PathIterator.SEG_CLOSE) {
                if (index == 1) {
                    topRight_ = new RealVector(segmentArray);

                }

                if (index == 3) {
                    downLeft_ = new RealVector(segmentArray);
                }


            }
            index++;
            pathIterator.next();
        }

        resolution_ = resolution;
    }

    

    

    public Area(RealVector resolution, RealVector topRight, RealVector downLeft) {
        topRight_ = topRight;
        downLeft_ = downLeft;
        resolution_ = resolution;

    }

    public Area(RealVector topRight, RealVector downLeft) {
        topRight_ = topRight;
        downLeft_ = downLeft;
        System.out.println("Construtor de Area sem usar resolucao");
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

    public boolean isClosestCurve(RPnCurve curve) {
        //UserInputTable userInputList = UIController.instance().globalInputTable();
        //RealVector newValue = userInputList.values();
        //RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
        //RpGeometry geom = phaseSpace.findClosestGeometry(newValue);


        RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
        RpGeometry geom = phaseSpace.findClosestGeometry(GeometryGraphND.pMarca);

        return (curve == (RPnCurve)(geom.geomFactory().geomSource()));
    }


    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<AREA>\n");
        //buffer.append("<RESOLUTION>");
        //buffer.append(resolution_.toString());
        //buffer.append("<\\RESOLUTION>");
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
