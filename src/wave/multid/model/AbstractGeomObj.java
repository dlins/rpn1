/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

import java.io.FileWriter;
import java.io.FileReader;
import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.multid.Space;
import wave.multid.map.Map;
import java.awt.Color;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
 /** The main class of the model package . This interface declares the main methods used to create the mutidimensional objects. */
public interface AbstractGeomObj {

    /** Loads an abstract object from a file. */
   
    void load(FileReader cin);

    /** Writes an abstract object into a file. */

    void print(FileWriter cout);

    /** Returns the box that describes the multidimensional object.*/

    BoundingBox getBoundary();

    /** Returns the space of the multidimensional object. For space we can understand as what is the dimension of the object */
    Space getSpace();

    /** Apply a map transformation to a multidimensional object. With this transformation we can change the object by a scaling , a rotation, a translation, etc */
    void applyMap(Map map) throws DimMismatchEx;
}
