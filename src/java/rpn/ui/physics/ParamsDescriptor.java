/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui.physics;

import java.awt.Polygon;
import rpn.RPnProjDescriptor;
import wave.multid.Space;
import wave.multid.graphs.ClippedShape;
import wave.multid.view.ViewingTransform;
import wave.util.Boundary;
import wave.util.IsoTriang2DBoundary;

public class ParamsDescriptor {

    private ViewingTransform viewingTransform_;

    private final ClippedShape clippedShape_;
    private final Polygon boundaryView_;
    private final String[] paramNames_;

    public ParamsDescriptor(String[] paramsNames, int[] paramsOrientation, int [] paramsIndex,Boundary boundary) {

        Space paramsSpace = new Space("", 2);
        paramNames_ = paramsNames;
        clippedShape_ = new wave.multid.graphs.ClippedShape(boundary);
        
        
        boolean isIsoToEqui=false;
        if (boundary instanceof IsoTriang2DBoundary){
            isIsoToEqui=true;
        }
        
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(paramsSpace, "", 400, 400, paramsOrientation, isIsoToEqui);
        viewingTransform_ = projDescriptor.createTransform(clippedShape_);

        boundaryView_ = clippedShape_.createWindow(viewingTransform_.projectionMap()).dcView(viewingTransform_);

    }

    public Polygon getParamsBoundaryView() {
        return boundaryView_;
    }

    public ViewingTransform getViewingTransform() {
        return viewingTransform_;
    }

    public String[] getParamNames() {
        return paramNames_;
    }

}
