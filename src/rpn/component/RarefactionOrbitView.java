package rpn.component;

import wave.multid.view.PolyLine;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.view.ViewingAttr;
import java.util.ArrayList;
import java.awt.Shape;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;

public class RarefactionOrbitView extends PolyLine {


    private ArrayList arrowList_;
    private OrbitPoint[] points_;


    public RarefactionOrbitView(MultiGeometryImpl geom, ViewingTransform transf,
                                ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }


    public Shape createShape() {

        Orbit source = (Orbit) (((RpGeometry) getAbstractGeom()).geomFactory().
                                geomSource());


        System.out.println("Chamando create view");




        return null;


    }
}
