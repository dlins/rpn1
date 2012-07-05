package rpn.component;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import wave.multid.*;
import wave.multid.view.*;

public class WaveCurveGeom extends OrbitGeom implements WaveCurveBranchGeom {


    private List<WaveCurveBranchGeom> childWCBGeom;


    public WaveCurveGeom(CoordsArray[] vertices, WaveCurveGeomFactory factory) {
        super(vertices, factory);

        //orbitsList_ = orbitsList;

        childWCBGeom = new ArrayList<WaveCurveBranchGeom>();

    }


    public List<WaveCurveBranchGeom> getOrbitGeom() {
        return childWCBGeom;
    }


    public void add(WaveCurveBranchGeom bGeom) {
        childWCBGeom.add(bGeom);
    }

    public void remove(WaveCurveBranchGeom bGeom) {
        childWCBGeom.remove(bGeom);
    }



    // No novo padrao: colocar uma lista de GeomObjView em WaveCurveView, como resultado do retorno de cada createView

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        // --- Original:
        return new WaveCurveView(this, transf,viewingAttr());
        // ---------------------------------------------------------------------


 
    }




   
}
