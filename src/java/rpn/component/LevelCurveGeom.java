/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpn.component.util.LinePlotted;
import rpnumerics.EigenValueCurve;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class LevelCurveGeom extends SegmentedCurveGeom {

    //
    // Constructors
    public LevelCurveGeom(RealSegGeom[] segArray, LevelCurveGeomFactory factory) {

        super(segArray, factory);

    }

    //
    // Methods
    //
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new LevelCurveView(this, transf, viewingAttr());
    }

   
        
     @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
        EigenValueCurve fundamentalCurve = (EigenValueCurve) geomFactory().geomSource();
        double level = fundamentalCurve.getLevel();
        List<Object> wcObject = new ArrayList<Object>();
        wcObject.add(new RealVector(curvePoint.getCoords()));
        wcObject.add(new RealVector(wcPoint.getCoords()));
        wcObject.add(String.valueOf(level));

        LinePlotted speedAnnotation = new LinePlotted(wcObject, transform, new ViewingAttr(Color.white));

        addAnnotation(speedAnnotation);

    }
    
    
    
}
