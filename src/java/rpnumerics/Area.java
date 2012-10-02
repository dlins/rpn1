/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.awt.Polygon;
import java.awt.Rectangle;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import rpn.parser.RPnDataModule;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class Area {

    private RealVector topRight_;
    private RealVector downLeft_;
    private RealVector resolution_;

    public Area(RealVector resolution, java.awt.geom.Area wcPolygon) {

        Rectangle wcPolygonBox = wcPolygon.getBounds();


        double[] topRightArray = {wcPolygonBox.getX() + wcPolygonBox.getWidth(), wcPolygonBox.getY()};
        double[] downLeftArray = {wcPolygonBox.getX(), wcPolygonBox.getY()+  wcPolygonBox.getHeight()};


        topRight_ = new RealVector(topRightArray);
        downLeft_ = new RealVector(downLeftArray);
        
        System.out.println("topRight: "+topRight_);
        System.out.println("downLeft: "+downLeft_);
        
        
        
        
        resolution_ = resolution;
    }

    public Area(RealVector resolution, Polygon dcPolygon, ViewingTransform viewingTransform) {

        Rectangle dcPolygonBox = dcPolygon.getBounds();

        Coords2D topRightDCoords = new Coords2D(dcPolygonBox.x + dcPolygonBox.width, dcPolygonBox.y);
        Coords2D downLeftDCoords = new Coords2D(dcPolygonBox.x, dcPolygonBox.y + dcPolygonBox.height);

        CoordsArray topRightWCCoords = new CoordsArray(new Space(" ", 2));
        CoordsArray downLeftWCCoords = new CoordsArray(new Space(" ", 2));

        viewingTransform.dcInverseTransform(topRightDCoords, topRightWCCoords);
        viewingTransform.dcInverseTransform(downLeftDCoords, downLeftWCCoords);

        topRight_ = new RealVector(topRightWCCoords.getCoords());
        downLeft_ = new RealVector(downLeftWCCoords.getCoords());
        resolution_ = resolution;
    }

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

    public boolean isClosestCurve(RPnCurve curve) {
        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();
        RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;

        RpGeometry geom = phaseSpace.findClosestGeometry(newValue);

        return (curve == (RPnCurve) (geom.geomFactory().geomSource()));
    }

    @Override
    public String toString() {

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
