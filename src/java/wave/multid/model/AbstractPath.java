/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
* wave.multid.model.AbstractPath
*
* Composition Pattern applied to define
* a Wire Frame path
*/

package wave.multid.model;

import java.util.LinkedList;
import java.io.FileWriter;
import java.io.FileReader;
import wave.multid.*;
import wave.multid.map.*;

public class AbstractPath implements AbstractGeomObj {
    //
    // Constants
    //
    public static final int LIST_FIRST_INDEX = 0;
    //
    // Members
    //
    private LinkedList segmentList_;
    private Space space_;
    private BoundingBox boundary_;

    //
    // Constructors
    //
    public AbstractPath(Space s) {
        space_ = s;
        segmentList_ = new LinkedList();
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
    }

    public AbstractPath(AbstractSegment segment) {
        this(segment.getSpace());
        try {
            add(segment, false);
            boundary_.resize(segment.getDefinitionPoints());
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
    }

    public AbstractPath(AbstractPath copy) {
        this(copy.getSpace());
        CoordsArray[] copyCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        AbstractPathIterator iterator = copy.getPathIterator();
        while (!iterator.isDone()) {
            iterator.next();
            try {
                AbstractSegmentAtt copyAtt = iterator.currentSegment(copyCoords);
                AbstractSegment copySeg = new AbstractSegment(copyCoords, copyAtt);
                add(copySeg, false);
            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            } catch (WrongNumberOfDefPointsEx defex) {
                defex.printStackTrace();
            }
        }
    }

    //
    // Accessors/Mutators
    //
    public Space getSpace() { return space_; }

    public BoundingBox getBoundary() { return boundary_; }

    public AbstractPathIterator getPathIterator(Map map) throws DimMismatchEx {
      
        if (!getSpace().equals(map.getDomain()))
            throw new DimMismatchEx("MAPPING EXCEPTION");
        // TODO : check this approach... it's not changing
        // the model path itself !
        AbstractPath clonePath = new AbstractPath(this);
        clonePath.applyMap(map);
        return clonePath.getPathIterator();
    }

    public AbstractPathIterator getPathIterator() {
        return new AbstractPathIterator(segmentList_.listIterator(LIST_FIRST_INDEX));
    }

    //
    // Methods
    //
    public void reset() {
        segmentList_.clear();
        update();
    }

    public void add(AbstractPathIterator addList, boolean connect) throws DimMismatchEx {
        CoordsArray[] currCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        while (!addList.isDone()) {
            addList.next();
            AbstractSegmentAtt currAtt = addList.currentSegment(currCoords);
            try {
                AbstractSegment toAdd = new AbstractSegment(currCoords, currAtt);
                add(toAdd, connect);
            } catch (WrongNumberOfDefPointsEx wex) {
                wex.printStackTrace();
            }
        }
        update();
    }

    public void add(AbstractSegment toAdd, boolean connect) throws DimMismatchEx {
        if (!getSpace().equals(toAdd.getSpace()))
            // a new constructor should be added for DimMismatchEx
                throw new DimMismatchEx(toAdd.getDefinitionPoints() [0]);
        if ((connect) && (toAdd.getAttributes().getType() == AbstractSegment.SEG_MOVETO))
            toAdd.getAttributes().setType(AbstractSegment.SEG_LINETO);
        segmentList_.add(toAdd);
        update();
    }

    public void remove(AbstractSegment toRemove) {
        segmentList_.remove(toRemove);
    }

    public void remove(AbstractPathIterator removeList) {
        CoordsArray[] currCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        CoordsArray[] thisCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        while (!removeList.isDone()) {
            AbstractSegmentAtt currAtt = removeList.currentSegment(currCoords);
            try {
                AbstractSegment toRemove = new AbstractSegment(currCoords, currAtt);
                AbstractPathIterator thisIterator = getPathIterator();
                // now we search for an equivalent segment (it does not matter
                // if it's THE segment in case of touching borders
                while (!thisIterator.isDone()) {
                    thisIterator.next();
                    AbstractSegmentAtt thisAtt = thisIterator.currentSegment(thisCoords);
                    AbstractSegment thisSeg = new AbstractSegment(thisCoords, thisAtt);
                    if ((toRemove.equals(thisSeg) && thisSeg.getAttributes().getType() != AbstractSegment.SEG_MOVETO)) {
                        // WHY CALLING REMOVE ?
                        //                        thisIterator.remove();
                        // we are not removing SEG_MOVE_TO - mvera

/*			for (int i=LIST_FIRST_INDEX;i < segmentList_.size();i++)

                            if (((AbstractSegment)segmentList_.get(i)).equals(thisSeg) &&
                            	(thisSeg.getAttributes().getType() != AbstractSegment.SEG_MOVETO)) {
                            	segmentList_.remove(segmentList_.get(i));
                                break;
                            }*/

                        segmentList_.remove(thisSeg);
                        break;
                    }
                }
            } catch (WrongNumberOfDefPointsEx wex) {
                wex.printStackTrace();
            }
        }
        update();
    }

    public void print(FileWriter cout) { }

    public void load(FileReader cin) { }

    public void applyMap(Map map) throws DimMismatchEx {
        if (!getSpace().equals(map.getDomain()))
            throw new DimMismatchEx("MAPPING EXCEPTION");
        LinkedList newPath = new LinkedList();
        // MAX_DEF_POINTS is the num of points to define a segment
        CoordsArray[] segCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        CoordsArray[] mappedCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        AbstractPathIterator iterator = getPathIterator();
        while (!iterator.isDone()) {
            iterator.next();
            try {
                AbstractSegmentAtt mappedAtt = iterator.currentSegment(segCoords);
                for (int i = 0; i < segCoords.length; i++) {
                    mappedCoords[i] = new CoordsArray(map.getCodomain());
                    map.image(segCoords[i], mappedCoords[i]);
                }
                AbstractSegment mappedSeg = new AbstractSegment(mappedCoords, mappedAtt);
                newPath.add(mappedSeg);
            } catch (WrongNumberOfDefPointsEx wex) {
                wex.printStackTrace();
            }
        }
        synchronized(segmentList_) {
            segmentList_ = new LinkedList(newPath);
        }
        update();
    }

    public void closePath() {
        try {
            CoordsArray[] closeCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            closeCoords[0] = new CoordsArray(getSpace());
            closeCoords[1] = new CoordsArray(getSpace());
//            closeCoords[2] = new CoordsArray(getSpace());
//            closeCoords[3] = new CoordsArray(getSpace());
            add(new AbstractSegment(closeCoords, new AbstractSegmentAtt(AbstractSegment.SEG_CLOSE)), false);
        } catch (WrongNumberOfDefPointsEx ex) {
            ex.printStackTrace();
        } catch (DimMismatchEx dimex) {
            dimex.printStackTrace();
        }
    }

    protected void update() {
        // updates the bounding box only for now
        //        try {
        //			if (isModified()) {
        // updates with the proper dimension (in case we have applied a proj map)
        //          boundary_ = new BoundingBox(new CoordsArray(getSpace()),new CoordsArray(getSpace()));
        //          boundary_.resize(getPathIterator());
        //          THIS IS BUGGY !!!
        //		    }
        //        } catch (DimMismatchEx dex){dex.printStackTrace();}
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Path : \n" + getSpace() + '\n');
        buf.append("Path Segments : \n");
        AbstractPathIterator segIterator = getPathIterator();
        while (!segIterator.isDone()) {
            segIterator.next();
            CoordsArray[] segCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
            AbstractSegmentAtt segAtt = segIterator.currentSegment(segCoords);
            try {
                buf.append(new AbstractSegment(segCoords, segAtt));
            } catch (WrongNumberOfDefPointsEx wex) {
                wex.printStackTrace();
            }
        }
        return buf.toString();
    }
}
