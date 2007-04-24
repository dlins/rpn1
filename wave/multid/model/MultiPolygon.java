/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

import wave.util.RealMatrix2;
import wave.multid.DimMismatchEx;
import wave.multid.view.*;
import wave.multid.*;
import wave.multid.map.Map;
import java.io.FileWriter;
import java.io.FileReader;
import java.awt.Color;
import wave.util.RealVector;

public class MultiPolygon extends MultiGeometryImpl {
    //
    // Members
    //
    private int numVertices_;

    //
    // Constructors
    //
    public MultiPolygon(CoordsArray[] vertices, ViewingAttr viewAttr) {
        super(vertices[0].getSpace(), viewAttr);
        numVertices_ = vertices.length;
        // a MultiPolygon is a MultiPolyLine + a CLOSE Segment
        MultiPolyLine line = new MultiPolyLine(vertices, viewAttr);
        try {
            append(line.getPathIterator(), false);
        } catch (DimMismatchEx dex) { dex.printStackTrace(); }
        closePath();
    }

    //
    // Accessors/Mutators
    //
    public int getNumVertices() { return numVertices_; }

    //
    // Methods
    //
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new PolyLine(this, transf, viewingAttr());
    }

    public boolean contains(CoordsArray point, RealMatrix2 vertices, RealMatrix2 norm) throws DimMismatchEx {
        int verticesNumCols = vertices.getNumCol();
        int normNumCols = norm.getNumCol();
        int verticesNumRows = vertices.getNumRow();
        int normNumRows = norm.getNumRow();
        if ((verticesNumCols != normNumCols) || (verticesNumRows != normNumRows) ||
            verticesNumRows != point.getSpace().getDim())
                throw new DimMismatchEx("vertices don't match norms");
        RealVector verticesCol = new RealVector(vertices.getNumRow());
        RealVector normCol = new RealVector(vertices.getNumRow());
        for (int i = 0; i < verticesNumCols; i++) {
            vertices.getColumn(i, verticesCol);
            norm.getColumn(i, normCol);
            verticesCol.sub(new RealVector(point.getCoords()));
            if (verticesCol.dot(normCol) > 0)
                return false;
        }
        return true;
    }

    public CoordsArray[] extractVertices() {
        AbstractPathIterator iterator = getPathIterator();
        CoordsArray[] vertices = new CoordsArray[numVertices_];
        int i = 0;
        while (!iterator.isDone()) {
            iterator.next();
            CoordsArray[] coords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            AbstractSegmentAtt att = iterator.currentSegment(coords);
            if (att.getType() != AbstractSegment.SEG_CLOSE)
                if (att.getType() == AbstractSegment.SEG_MOVETO)
                    vertices[i++] = new CoordsArray(coords[0]);
                else
                    vertices[i++] = new CoordsArray(coords[1]);
        }
        return vertices;
    }
}
