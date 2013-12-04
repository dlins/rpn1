/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import wave.util.RealMatrix2;
import wave.multid.view.*;
import wave.multid.*;
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
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
        closePath();
    }

    //
    // Accessors/Mutators
    //
    public int getNumVertices() {
        return numVertices_;
    }

    //
    // Methods
    //
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new PolyLine(this, transf, viewingAttr());
    }

    public boolean contains(CoordsArray point, RealMatrix2 vertices, RealMatrix2 norm) throws DimMismatchEx {
        int verticesNumCols = vertices.getNumCol();
        int normNumCols = norm.getNumCol();
        int verticesNumRows = vertices.getNumRow();
        int normNumRows = norm.getNumRow();
        if ((verticesNumCols != normNumCols) || (verticesNumRows != normNumRows)
                || verticesNumRows != point.getSpace().getDim()) {
            throw new DimMismatchEx("vertices don't match norms");
        }
        RealVector verticesCol = new RealVector(vertices.getNumRow());
        RealVector normCol = new RealVector(vertices.getNumRow());
        for (int i = 0; i < verticesNumCols; i++) {
            vertices.getColumn(i, verticesCol);
            norm.getColumn(i, normCol);
            verticesCol.sub(new RealVector(point.getCoords()));
            if (verticesCol.dot(normCol) > 0) {
                return false;
            }
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
            if (att.getType() != AbstractSegment.SEG_CLOSE) {
                if (att.getType() == AbstractSegment.SEG_MOVETO) {
                    vertices[i++] = new CoordsArray(coords[0]);
                } else {
                    vertices[i++] = new CoordsArray(coords[1]);
                }
            }
        }
        return vertices;
    }

   

    public MultiPolygon convexPolygon() {

        ArrayList<RealVector> vertices =  new ArrayList<RealVector>();
        CoordsArray[] extractVertices = extractVertices();
        
        for (int i = 0; i < extractVertices.length; i++) {
            CoordsArray coordsArray = extractVertices[i];

            vertices.add(new RealVector(coordsArray.getCoords()));
            
        }
        
        List<RealVector> convexVertices = convexHull(vertices);
        
        CoordsArray [] convexCoods = new CoordsArray[convexVertices.size()];
        for (int i = 0; i < convexVertices.size(); i++) {
            RealVector realVector = convexVertices.get(i);
            
            convexCoods [i] = new CoordsArray(realVector);
            
            
        }
        
        return new MultiPolygon(convexCoods, viewingAttr());
        
    }


    private native List<RealVector> convexHull(List<RealVector> coords);
    
    
    public String toXML(){
        
        
        StringBuffer buffer = new StringBuffer();

        buffer.append("<DOMAINSELECTION>\n");
        
        CoordsArray [] vertices = extractVertices();
        
        for (int i = 0; i < vertices.length; i++) {
            CoordsArray coordsArray = vertices[i];
            RealVector realVector = new RealVector(coordsArray.getCoords());
            buffer.append(realVector.toXML());

        }
       
        buffer.append("\n</DOMAINSELECTION>");


        return buffer.toString();
        
        
        
    }
}
