/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.view;

import wave.multid.model.AbstractGeomObj;
import java.awt.Graphics2D;

/** The main class of the view package. This class declares the basic methods to create a visual form of a multidimensional object. A GeomObjView object contains the attributes and the transformation associated with a visual form of a multidimensional object. */

public interface GeomObjView {

    /** Returns the geometrics proprieties of a multidimensional object. */ 

    AbstractGeomObj getAbstractGeom();

    /** Set geometrics proprieties to a multidimensional object. */

    void setAbstractGeom(AbstractGeomObj abstractGeom);

    /** Returns the view transform that is necessary to obtain a visual form of a multidimensional object. */

    ViewingTransform getViewingTransform();

    /** Set a view transform to a multidimensional object to get it visual form.  */

    void setViewingTransform(ViewingTransform transf);

    /** Returns the visual attributes of a multidimensional object. */

    ViewingAttr getViewingAttr();

    /** Set a view a attribute to multidimensional object. */

    void setViewingAttr(ViewingAttr viewAttr);

    /** Draw a  multidimensional object */

    void draw(Graphics2D g);

    /** Updates the visual form of a multidimensional object */

    void update();
}
