/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

import wave.multid.*;

public class BoundingBox {
    //
    // Members
    //
    private CoordsArray minCorner_;
    private CoordsArray maxCorner_;
    private Space space_;

    //
    // Constructors
    //
    public BoundingBox(CoordsArray minValues, CoordsArray maxValues) throws DimMismatchEx {
        minCorner_ = new CoordsArray(minValues);
        maxCorner_ = new CoordsArray(maxValues);
        if (!(minValues.getSpace().equals(maxValues.getSpace())))
            throw new DimMismatchEx(minValues);
        else
            space_ = minCorner_.getSpace();
    }

    //
    // Accessors/Mutators
    //
    public Space getSpace() { return space_; }

    public CoordsArray getMinCorner() { return minCorner_; }

    public CoordsArray getMaxCorner() { return maxCorner_; }

    //
    // Methods
    //
    public boolean contains(CoordsArray check) throws DimMismatchEx {
        if (!(check.getSpace().equals(minCorner_.getSpace())))
            throw new DimMismatchEx(check);
        double[] minCornerVals = minCorner_.getCoords();
        double[] maxCornerVals = maxCorner_.getCoords();
        for (int i = 0; i < minCornerVals.length; i++) {
            if ((minCornerVals[i] > check.getCoords() [i]) || (maxCornerVals[i] < check.getCoords() [i]))
                return false;
        }
        return true;
    }

    public void resize(AbstractPathIterator iterator) throws DimMismatchEx {
        java.util.ArrayList listOfCoords = new java.util.ArrayList();
        while (!iterator.isDone()) {
            iterator.next();
            CoordsArray[] segCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            AbstractSegmentAtt att = iterator.currentSegment(segCoords);
            listOfCoords.add(segCoords);
        }
        CoordsArray[] resizeList = new CoordsArray[AbstractSegment.MAX_DEF_POINTS * listOfCoords.size()];
        int k = 0;
        for (int i = 0; i < listOfCoords.size(); i++)
            for (int j = 0; j < AbstractSegment.MAX_DEF_POINTS; j++)
                resizeList[k++] = ((CoordsArray[]) listOfCoords.get(i)) [j];
        resize(resizeList);
    }

    public void resize(BoundingBox boundary) throws DimMismatchEx {
        CoordsArray boundMinCorner = boundary.getMinCorner();
        CoordsArray boundMaxCorner = boundary.getMaxCorner();
        if (!contains(boundMinCorner) || !contains(boundMaxCorner)) {
            for (int i = 0; i < boundMinCorner.getCoords().length; i++) {
                minCorner_.setElement(i, Math.min(boundMinCorner.getCoords() [i], minCorner_.getCoords() [i]));
                maxCorner_.setElement(i, Math.max(boundMaxCorner.getCoords() [i], maxCorner_.getCoords() [i]));
            }
        }
    }

    public void resize(CoordsArray[] coordsSet) throws DimMismatchEx {
        for (int i = 0; i < coordsSet.length; i++) {
            if (!contains(coordsSet[i]))
                for (int j = 0; j < coordsSet[i].getCoords().length; j++) {
                    minCorner_.setElement(j, Math.min(coordsSet[i].getCoords() [j], minCorner_.getCoords() [j]));
                    maxCorner_.setElement(j, Math.max(coordsSet[i].getCoords() [j], maxCorner_.getCoords() [j]));
                }
        }
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(minCorner_);
        buff.append(maxCorner_);
        return buff.toString();
    }
}
