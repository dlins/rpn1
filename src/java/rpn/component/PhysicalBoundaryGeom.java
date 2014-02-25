/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import rpn.component.util.LinePlotted;
import wave.multid.view.*;
import wave.multid.*;
import wave.util.RealVector;

public class PhysicalBoundaryGeom extends SegmentedCurveGeom {

    private ViewingAttr viewAtt_;

    public PhysicalBoundaryGeom(RealSegGeom[] segArray, PhysicalBoundaryFactory factory) {

        super(segArray, factory);
        viewAtt_ = new ViewingAttr(Color.gray);

    }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new PhysicalBoundaryView(this, transf, viewingAttr());
    }

    @Override
    public ViewingAttr viewingAttr() {
        return viewAtt_;
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);
        double sum=0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < curvePoint.getDim(); i++) {
            sum+=curvePoint.getElement(i);
            String number = formatter.format(curvePoint.getElement(i));
            stringBuilder.append(number);
            stringBuilder.append("    ");
        }

        stringBuilder.append(formatter.format(1-sum));
        
        
        List<Object> wcObject = new ArrayList<Object>();
        wcObject.add(new RealVector(curvePoint.getCoords()));
        wcObject.add(new RealVector(wcPoint.getCoords()));
        wcObject.add(String.valueOf(stringBuilder.toString()));

        LinePlotted speedAnnotation = new LinePlotted(wcObject, transform, new ViewingAttr(Color.white));

        addAnnotation(speedAnnotation);

    }
}
