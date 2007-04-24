/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

import java.util.ArrayList;
import java.util.Iterator;

public class GridIterator implements Iterator {
    static public final int NULL_INDEX = -1;
    private GridBucket[] gridBuckets_;
    private GridProfile[] profiles_;
    private int bucketsCounter_ = 0;
    private int currentIndex_ = 0;

    public GridIterator(MultiGrid grid,int dim) {
        /*
         * we will assume that profile[0]
         * maps coords[0]
         */

        int numOfBuckets = 1;
        for (int i = 0; i < grid.profiles().length; i++)
            numOfBuckets *= grid.profiles() [i].numOfNodes();
        gridBuckets_ = new GridBucket[numOfBuckets];
        profiles_ = grid.profiles();
//        dim = rpnumerics.RPNUMERICS.domainDim();
        double[] coords = new double[dim];
        LoopConstruct[] loops = new LoopConstruct[dim];
        loops[0] = new LoopConstruct(new BucketCreator(new LoopIterator(loops)), 0, profiles_[0].numOfNodes(), 1);
        for (int i = 1; i < loops.length; i++)
            loops[i] = new LoopConstruct(loops[i - 1], 0, profiles_[i].numOfNodes(), 1);
        // starts
        loops[loops.length - 1].run();
    }

    class BucketCreator implements Runnable {
        private Iterator iterator_;

        public BucketCreator(Iterator iterator) {
            iterator_ = iterator;
        }

        public void run() {
            int[] loopIndices = (int[]) iterator_.next();
            double[] center_coords = new double[loopIndices.length];
            for (int i = 0; i < center_coords.length; i++)
                center_coords[i] = profiles_[i].delta() * loopIndices[i] + profiles_[i].minimum() + profiles_[i].delta() / 2.;
            gridBuckets_[bucketsCounter_++] = new GridBucket(center_coords);
        }
    }


    static class GridBucket {
        RealVector center_;
        boolean traversed_;

        GridBucket(double[] center) {
            center_ = new RealVector(center);
            traversed_ = false;
        }

        public boolean isTraversed() { return traversed_; }

        public void setTraversed(boolean traversed) { traversed_ = traversed; }

        public RealVector center() { return center_; }
    }


    public boolean hasNext() {
        int numOfBuckets = gridBuckets_.length;
        for (int i = currentIndex_; i < numOfBuckets; i++)
            if (!gridBuckets_[i].isTraversed()) {
                // not visited yet !
                currentIndex_ = i;
                return true;
            }
        currentIndex_ = NULL_INDEX;
        return false;
    }

    public Object next() {
        if (currentIndex_ == NULL_INDEX)
            return null;
        remove();
        return gridBuckets_[currentIndex_].center();
    }

    public void remove() {
        gridBuckets_[currentIndex_].setTraversed(true);
    }

    public void remove(RealVector coords) throws IllegalArgumentException {
        remove(locate(coords));
    }

    public void remove(int index) {
        gridBuckets_[index].setTraversed(true);
    }

    public int locate(RealVector coords) throws IllegalArgumentException {
        /*
         * we will assume a loop construct where
         * the higher the profile index the outter
         * is the loop nested.
         */

        // check input first...
        boolean out = false;
        for (int i = 0; i < coords.getSize(); i++)
            if ((coords.getElement(i) > profiles_[i].maximum()) || (coords.getElement(i) < profiles_[i].minimum())) {
                out = true;
                throw new IllegalArgumentException("OUT OF BOUNDS");
            }
        if (!out) {
            int[] shifts = new int[coords.getSize()];
            for (int i = 0; i < shifts.length; i++) {
                shifts[i] = 0;
                double pointer = profiles_[i].minimum();
                while (pointer < coords.getElement(i)) {
                    shifts[i] ++;
                    pointer += profiles_[i].delta();
                }
                shifts[i] --;
            }
            int index = 0;
            for (int i = shifts.length - 1; i > 0; i--) {
                double n_backwards = 1;
                for (int j = i - 1; j >= 0; j--)
                    n_backwards *= profiles_[j].numOfNodes();
                index += shifts[i] * n_backwards;
            }
            index += shifts[0];
            return index;
        }
        return NULL_INDEX;
    }
}
