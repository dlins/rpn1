/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpn.controller.PoincareController;
import rpn.controller.RpController;
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
        getUI().install(this);
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

    public String toXML() {
        StringBuffer str = new StringBuffer();
        str.append("<POINCAREDATA>\n");
        SimplexPoincareSection pSection = (SimplexPoincareSection)geomSource_;
        for (int i = 0; i < pSection.getPoints().length; i++)
            str.append(new PhasePoint(pSection.getPoints() [i]).toXML());
        str.append("</POINCAREDATA>\n");
        return str.toString();
    }
}
