package rpn.component;

import rpnumerics.RarefactionOrbit;
import wave.multid.view.PolyLine;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import java.util.ArrayList;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import rpnumerics.OrbitPoint;

public class RarefactionOrbitView extends PolyLine {


//    private ArrayList arrowList_;
//    private OrbitPoint[] points_;


    public RarefactionOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
                                ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }


    public Shape createShape() {

        
         GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
          RarefactionOrbit source = (RarefactionOrbit) (((RpGeometry) getAbstractGeom()).geomFactory().
                                geomSource());

        try {
            composite.append(super.createShape(), false);
            
        } catch (DimMismatchEx ex) {

            ex.printStackTrace();

        }

        return composite;
    }
}
