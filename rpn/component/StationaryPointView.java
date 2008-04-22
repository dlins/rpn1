/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.view.PointMark;
import wave.multid.view.ViewingTransform;
import wave.multid.view.ViewingAttr;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.CoordsArray;
import wave.multid.Coords2D;
import wave.multid.DimMismatchEx;
import wave.util.Arrow;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import wave.util.RealVector;
import java.awt.Graphics2D;
import rpnumerics.StationaryPoint;

public class StationaryPointView extends PointMark {
    //
    // Constants
    //
    static public final double DEFAULT_ARROW_RATIO = 30d;
    static public final double DEFAULT_DOUBLE_ARROW_RATIO = 10d;
    static public final double DEFAULT_ARROW_HEAD_RATIO = 5d;
    public static Color DEFAULT_INWARD_COLOR = Color.blue;
    public static Color DEFAULT_OUTWARD_COLOR = Color.red;
    public static Color ELLIPTICAL_EIGEN_COLOR = Color.white;
    //
    // Members
    //
    private Arrow[] arrows_;

    //
    // Constructors
    //
    public StationaryPointView(MultiGeometryImpl geom, ViewingTransform transf, ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);
    }

    //
    // Accessors/Mutators
    //
    public double defaultArrowLength() {
        return getViewingTransform().viewPlane().getViewport().getWidth() / DEFAULT_ARROW_RATIO;
    }

    public double defaultDoubleArrowLength() {
        return defaultArrowLength() - DEFAULT_DOUBLE_ARROW_RATIO;
    }

    public double defaultArrowHeadSize() {
        return defaultArrowLength() / DEFAULT_ARROW_HEAD_RATIO;
    }

    //
    // Methods
    //
    public Shape createShape() {
    /* I'm assuming the eigen vectors to be directions related to the
     * stationary point coordinate itself rather than to origin
     *
     */

        // create the arrows (WC)
        StationaryPoint statPoint = (StationaryPoint)((RpGeometry)getAbstractGeom()).geomFactory().geomSource();
        GeneralPath composite = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        try {
            composite.append(super.createShape(), false);
            int stateSpaceDim = getAbstractGeom().getSpace().getDim();
            arrows_ = new Arrow[stateSpaceDim * 2]; // the opposite direction

      /*
       * CREATE DC COORDS
       */

            Coords2D start_dc = new Coords2D();
            getViewingTransform().viewPlaneTransform(new CoordsArray(statPoint.getPoint().getCoords()), start_dc);
            // the directions array will be ordered by eigenValuesR... the higher the
            // order the stronger the color
            Coords2D[] directions_dc = new Coords2D[stateSpaceDim];
            for (int i = 0, j = 0; i < stateSpaceDim; i++, j += 2) {
                directions_dc[i] = new Coords2D();
                RealVector eigentr = new RealVector(statPoint.getPoint().getCoords());
                for (int k = 0; k < stateSpaceDim; k++)
                    eigentr.setElement(k, eigentr.getElement(k) + statPoint.getEigenVec() [i].getElement(k));
                getViewingTransform().viewPlaneTransform(new CoordsArray(eigentr), directions_dc[i]);
                // apply a translation related to the start point...
                directions_dc[i].setElement(0, directions_dc[i].getX() - start_dc.getX());
                directions_dc[i].setElement(1, directions_dc[i].getY() - start_dc.getY());
                // outwards by default
                double arrowHeadSize = defaultArrowHeadSize();
                double arrowLength = defaultArrowLength();
                // the elliptical - no heads
                if (statPoint.getEigenValR() [i] == 0) {
                    arrowHeadSize = .1;
                    arrowLength *= 2;
                }
                else if (statPoint.getEigenValR() [i] < 0)
                    // inwards
                        arrowHeadSize *= -1;
                arrows_[j] = new Arrow(new RealVector(start_dc.getCoords()),
                    new RealVector(directions_dc[i].getCoords()), arrowHeadSize, arrowLength);
                arrows_[j + 1] = new Arrow(new RealVector(start_dc.getCoords()),
                    new RealVector(directions_dc[i].getCoords()), arrowHeadSize, -arrowLength);
            }
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
        return composite;
    }

    public void draw(Graphics2D g) {
        Color prev = g.getColor();
        g.setColor(getViewingAttr().getColor());
        super.draw(g);
        Color inWardColor = DEFAULT_INWARD_COLOR;
        Color outWardColor = DEFAULT_OUTWARD_COLOR;
        Color arrowColor = ELLIPTICAL_EIGEN_COLOR;
        int stateSpaceDim = getAbstractGeom().getSpace().getDim();
        int i = 0;
        int j = 0;
        do {
            StationaryPoint statPoint = (StationaryPoint)((RpGeometry)getAbstractGeom()).geomFactory().geomSource();
            // inwards
            if (statPoint.getEigenValR() [i] < 0) {
                arrowColor = inWardColor;
                if ((i + 1 < stateSpaceDim) && (statPoint.getEigenValR() [i] < statPoint.getEigenValR() [i + 1])) {
                    inWardColor = inWardColor.brighter();
                    inWardColor = inWardColor.brighter();
                }
            }
            // outwards
            if (statPoint.getEigenValR() [i] > 0) {
                arrowColor = outWardColor;
                if ((i + 1 < stateSpaceDim) && (statPoint.getEigenValR() [i] < statPoint.getEigenValR() [i + 1])) {
                    outWardColor = outWardColor.darker();
                    outWardColor = outWardColor.darker();
                }
            }
            g.setColor(arrowColor);
            arrows_[j].paintComponent(g);
            arrows_[j + 1].paintComponent(g);
            i++;
            j += 2;
        } while (i < stateSpaceDim);
        g.setColor(prev);
    }
}
