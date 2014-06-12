/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import wave.multid.view.Viewing2DTransform;
import wave.multid.view.Viewing3DTransform;
import wave.multid.view.ViewingTransform;
import wave.multid.view.Iso2EquiTransform;
import wave.multid.Space;
import wave.multid.Multid;
import wave.multid.graphs.ClippedShape;
import wave.multid.graphs.ViewPlane;
import wave.multid.graphs.dcViewport;
import wave.multid.graphs.wcWindow;
import wave.multid.map.ProjectionMap;


public class RPnProjDescriptor {
    //
    // Constants
    //
    //
    // Members
    //

    private dcViewport viewport_;
    private ProjectionMap projMap_;
    private String label_;
    // as we have only one transformation for now
    private boolean iso2equi_;

    public RPnProjDescriptor(Space domain, String label, int w, int h, int[] projIndices, boolean iso2equi) {
        viewport_ = new dcViewport(w, h);
        Space projSpace = null;
        if (projIndices.length == 2) {
            projSpace = Multid.PLANE;
        } else if (projIndices.length == 3) {
            projSpace = Multid.SPACE;
        } else {
            throw new IllegalArgumentException("Invalid projection dim...");
        }
        projMap_ = new ProjectionMap(domain, projSpace, projIndices);
        label_ = label;
        iso2equi_ = iso2equi;

    }

    public dcViewport viewport() {
        return viewport_;
    }

    public ProjectionMap projMap() {
        return projMap_;
    }

    public boolean isIso2equi() {
        return iso2equi_;
    }

    public String label() {
        return label_;
    }

    public ViewingTransform createTransform(ClippedShape clipping) {
        
        
        wcWindow window = clipping.createWindow(projMap_);
        ViewPlane viewPlane = new ViewPlane(viewport_, window);
        if (projMap_.getCodomain().equals(Multid.PLANE)) {
            if (!iso2equi_) {
                return new Viewing2DTransform(projMap_, viewPlane);
            } else {
                return new Iso2EquiTransform(projMap_, viewPlane);
            }
        } else // 3D by default we will get the minimum Z coord proj plane
        {
            return new Viewing3DTransform(projMap_, viewPlane,
                    new double[]{0., 0., 0.}, clipping.getMinimums().getElement(projMap_.getCompIndexes()[2]));
        }
    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
        str.append("<PROJDESC ");
        str.append(" projlabel=\"" + label() + "\"");
        str.append(" projaxisid=\"");
        for (int i = 0; i < projMap().getCompIndexes().length; i++) {
            str.append(new Integer(projMap().getCompIndexes()[i]).toString() + " ");
        }
        str.append("\"");
        str.append(" vpwidth=\"" + new Double(viewport().getWidth()).intValue() + "\"");
        str.append(" vpheight=\"" + new Double(viewport().getHeight()).intValue() + "\">");
        str.append("</PROJDESC>\n");
        return str.toString();
    }
}
