/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.util.LinePlotted;
import rpnumerics.PhysicalBoundary;
import wave.multid.view.*;
import wave.multid.*;
import wave.util.RealSegment;
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
    
    
    public void edgeSelection (CoordsArray vertex1 , CoordsArray vertex2){

        
        PhysicalBoundary boundary  = (PhysicalBoundary) geomFactory().geomSource();
        
        Iterator segments = boundary.segments().iterator();
        while (segments.hasNext()) {
            RealSegment realSegment = (RealSegment)segments.next();

            CoordsArray segment = new CoordsArray(vertex1);
            segment.sub(vertex1, vertex2);

            
            
            RealVector segmentVector = new RealVector(segment.getCoords());
            RealVector realSegmentVector = new RealVector(realSegment.p1());
            realSegmentVector.sub(realSegment.p2());
            
            
            realSegmentVector.sub(segmentVector);
            
            
            if(realSegmentVector.norm()==0){
                
                System.out.println("Eh o lado: "+ vertex1 + vertex2);
                
            }
            
            
        }
        
        
        
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
