/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import java.util.List;
import rpn.controller.PoincareController;
import rpn.controller.RpController;
import rpnumerics.Area;
import wave.util.SimplexPoincareSection;
import rpnumerics.PhasePoint;

public class PoincareSectionGeomFactory implements RpGeomFactory {
    //
    // Members
    //
    private RpGeometry geom_;
    private Object geomSource_;
    private RpController ui_;
    private boolean isGeomOutOfDate_;

    public PoincareSectionGeomFactory(SimplexPoincareSection pSection) {
        geomSource_ = pSection;
        updateGeom();
        isGeomOutOfDate_ = false;
        installController();
    }

    public RpController createUI() {
        return new PoincareController();
    }

    protected void installController() {
        setUI(createUI());
        //getUI().install(this);        // *** Esta chamada causa erro: ver metodo install(RpGeomFactory geom) em PoincareController
    }

    //
    // Accessors/Mutators
    //
    public Object geomSource() { return geomSource_; }

    public RpGeometry geom() { return geom_; }

    public void setUI(RpController ui) { ui_ = ui; }

    public RpController getUI() { return ui_; }

    public boolean isGeomOutOfDate() { return isGeomOutOfDate_; }

    public void setGeomOutOfDate(boolean flag) { isGeomOutOfDate_ = flag; }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {
        SimplexPoincareSection pSection = (SimplexPoincareSection)geomSource_;
        return new PoincareSectionGeom(MultidAdapter.converseRealVectorsToCoordsArray(pSection.getPoints()), this);
    }

    public void updateGeom() {
        geom_ = createGeomFromSource();
        isGeomOutOfDate_ = true;
    }


    public void updateGeom(Area area) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateGeom(List<Area> area, List<Integer> segmentsToRemove) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String toXML() { return "TODO: toXML()"; }
}
