/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.multid.model;

import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.view.ViewingAttr;
import wave.multid.DimMismatchEx;
import wave.multid.map.Map;
/** An extension of AbstractGeometryObj interface. This interface declares others basic methods and is the interface that the main multidimensional objects implements */ 

public interface MultiGeometry extends AbstractGeomObj {
    /*
     * PATH BASED IMMUTABLE GEOMETRIES
     */

    /** Returns the path used to define the multidimensional object. A multidimensional object can be understood as a sequence of segments in n dimensions . This segments  are stored in  AbstractPathIterator objects.*/

    AbstractPathIterator getPathIterator();

    /** Returns the path that defines the multidimensional object after the map transformation is applied */

    AbstractPathIterator getPathIterator(Map map) throws DimMismatchEx;

    /** Returns the visuals attributes of the multidimensional object. */

    ViewingAttr viewingAttr();

    /** Create a visual form of a multidimensional object. */

    GeomObjView createView(ViewingTransform transf) throws DimMismatchEx;
}
