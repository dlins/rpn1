/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.model;

import java.io.FileWriter;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Iterator;

import wave.multid.view.*;
import wave.multid.*;
import wave.multid.map.Map;

public class AbstractScene implements AbstractGeomObj {
    //
    // Members
    //

    protected ArrayList geomList_;
    private String name_;
    private ArrayList viewList_;
    private BoundingBox boundary_;
    private Space space_;

    //
    // Constructors
    //
    public AbstractScene(String name, Space space) {
        name_ = name;
        space_ = space;
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
        geomList_ = new ArrayList();
        viewList_ = new ArrayList();

    }

    //
    // Accessors/Mutators
    //
    public String getName() {
        return name_;
    }

    public Iterator getGeomObjIterator() {
//        System.out.println("geomList_.size() em AbstractScene : " +geomList_.size());       //*** Informa a quantidade de curvas
        return geomList_.iterator();
    }

    public BoundingBox getBoundary() {
        return boundary_;
    }

    public Space getSpace() {
        return space_;
    }

    //
    // Methods
    //
    public void join(MultiGeometry geom) {
        try {
            geomList_.add(geom);
            boundary_.resize(geom.getPathIterator());
        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < viewList_.size(); i++) {
            ((Scene) viewList_.get(i)).addViewFor(geom);
        }

    }

    public void print(FileWriter cout) {
    }

    public void load(FileReader cin) {
    }

    public void remove(int geometryIndex) {

        MultiGeometry geom = (MultiGeometry) geomList_.remove(geometryIndex);

        try {
            boundary_.resize(geom.getPathIterator());
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
        for (int i = 0; i < viewList_.size(); i++) {
            ((Scene) viewList_.get(i)).removeViewOf(geom);
        }

    }

    public void remove(MultiGeometry geom) {

        geomList_.remove(geom);

        try {
            boundary_.resize(geom.getPathIterator());
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
        for (int i = 0; i < viewList_.size(); i++) {
            ((Scene) viewList_.get(i)).removeViewOf(geom);
            ((Scene) viewList_.get(i)).update();
        }
        geom = null;

    }

    public void applyMap(Map map) {
        Iterator geomIterator = getGeomObjIterator();
        while (geomIterator.hasNext()) {
            MultiGeometry geom = (MultiGeometry) geomIterator.next();
            try {
                geom.applyMap(map);
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            }
        }
        update();
    }

    public void clear() {

        for (Object object : geomList_) {
            for (int i = 0; i < viewList_.size(); i++) {
                ((Scene) viewList_.get(i)).removeViewOf((MultiGeometry) object);
            }

        }

        geomList_.clear();

//        update();
    }

    public void update() {
        for (int i = 0; i < viewList_.size(); i++) {
            ((Scene) viewList_.get(i)).update();
        }
    }

    public Scene createScene(ViewingTransform transf, ViewingAttr viewAttr) throws DimMismatchEx {
        Scene newScene = new Scene(this, transf, viewAttr);
        viewList_.add(newScene);
        return newScene;
    }
}
