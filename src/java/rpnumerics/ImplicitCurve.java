package rpnumerics;

import wave.util.*;
import java.util.*;

public class ImplicitCurve {
    //
    // Members
    //
    private VectorFunction f_;
    private int dim_;
    private double[] minimums_;
    private double[] domainSize_;
    private int[] gridSize_;
    private ArrayList segments_;

    public ImplicitCurve(VectorFunction f, double[] minimums, double[] maximums, int[] gridSize) {
        dim_ = minimums.length;
        f_ = f;
        minimums_ = minimums;
        domainSize_ = new double[dim_];
        for (int i = 0; i < dim_; i++) domainSize_[i] = maximums[i] - minimums[i];
        gridSize_ = gridSize;
        segments_ = new ArrayList();
        BooleanMultiArray signs;
        int[] subDim = new int[dim_];
        for (int i = 0; i < dim_ - 1; i++)
            subDim[i] = gridSize[i] + 1;
        subDim[dim_ - 1] = 2;
        signs = InitialFill(subDim);
        int[] cubeIndex = new int[dim_];
        for (int i = 0; i < dim_; i++) cubeIndex[i] = 0;
        int lastCoord = 0;
        while (lastCoord < gridSize[dim_ - 1]) {
            while (cubeIndex[dim_ - 1] == lastCoord) {
                cubeIndex[dim_ - 1] = 0;
                if (CubeIntersect(signs, cubeIndex)) {
                    cubeIndex[dim_ - 1] = lastCoord;
                    FindIntersectionsInCube(cubeIndex);
                }
                cubeIndex[dim_ - 1] = lastCoord;
                IncreaseCubeIndex(cubeIndex);
            }
            lastCoord++;
            if (lastCoord < gridSize[dim_ - 1])
                Fill(signs, lastCoord + 1);
        }
    }

    private BooleanMultiArray InitialFill(int[] subDim) {
        BooleanMultiArray signs = new BooleanMultiArray(subDim, dim_ - 1);
        int[] index = new int[dim_];
        for (int i = 0; i < dim_; i++) index[i] = 0;
        RealVector point = new RealVector(dim_);
        RealVector value = new RealVector(dim_ - 1);
        while (index[dim_ - 1] < 2) {
            point = GridPoint(index);
            value = f_.value(point);
            boolean[] sign = new boolean[dim_ - 1];
            for (int i = 0; i < dim_ - 1; i++)
                if (value.getElement(i) >= 0)
                    sign[i] = true;
                else
                    sign[i] = false;
            signs.set(index, sign);
            IncreaseIndex(index);
        }
        return signs;
    }

    private void Fill(BooleanMultiArray signs, int lastCoord) {
        boolean[] sign;
        RealVector point = new RealVector(dim_);
        RealVector value = new RealVector(dim_ - 1);
        int[] index = new int[dim_];
        for (int i = 0; i < dim_ - 1; i++) index[i] = 0;
        index[dim_ - 1] = lastCoord;
        while (index[dim_ - 1] == lastCoord) {
            index[dim_ - 1] = 1;
            sign = signs.get(index);
            index[dim_ - 1] = 0;
            signs.set(index, sign);
            index[dim_ - 1] = lastCoord;
            point = GridPoint(index);
            value = f_.value(point);
            for (int i = 0; i < dim_ - 1; i++)
                if (value.getElement(i) >= 0)
                    sign[i] = true;
                else
                    sign[i] = false;
            index[dim_ - 1] = 1;
            signs.set(index, sign);
            index[dim_ - 1] = lastCoord;
            IncreaseIndex(index);
        }
    }

    private void IncreaseIndex(int[] index) {
        int i = 0;
        while ((i < dim_) && (index[i] == gridSize_[i])) {
            index[i] = 0;
            i++;
        }
        if (i < dim_) index[i] ++;
        else
            index[dim_ - 1] = gridSize_[dim_ - 1] + 1;
    }

    private void IncreaseCubeIndex(int[] index) {
        int i = 0;
        while ((i < dim_) && (index[i] == gridSize_[i] - 1)) {
            index[i] = 0;
            i++;
        }
        if (i < dim_) index[i] ++;
        else
            index[dim_ - 1] = gridSize_[dim_ - 1] + 1;
    }

    private RealVector GridPoint(int[] index) {
        RealVector vector = new RealVector(dim_);
        for (int i = 0; i < dim_; i++)
            vector.setElement(i, minimums_[i] + domainSize_[i] * index[i] / gridSize_[i]);
        return vector;
    }

    private boolean CubeIntersect(BooleanMultiArray signs, int[] index) {
        boolean[] result = new boolean[dim_ - 1];
        boolean[] init = signs.get(index);
        boolean[] sign = new boolean[dim_ - 1];
        int[] index2 = new int[dim_];
        int[] indexFull = new int[dim_];
        for (int i = 0; i < dim_ - 1; i++) result[i] = false;
        for (int i = 0; i < dim_; i++) index2[i] = 0;
        index2[0] = 1;
        int twoPowDim = (int)Math.pow(2, dim_);
        for (int ii = 0; ii < twoPowDim - 1; ii++) {
            for (int i = 0; i < dim_; i++)
                indexFull[i] = index[i] + index2[i];
            sign = signs.get(indexFull);
            for (int i = 0; i < dim_ - 1; i++)
                if (sign[i] != init[i]) result[i] = true;
            int j = 0;
            while ((j < dim_) && (index2[j] == 1)) {
                index2[j] = 0;
                j++;
            }
            if (j < dim_) index2[j] ++;
        }
        boolean finalResult = true;
        for (int i = 0; i < dim_ - 1; i++)
            if (result[i] == false) finalResult = false;
        return finalResult;
    }

    private void FindIntersectionsInCube(int[] cubeIndex) {
        RealVector point1 = GridPoint(cubeIndex);
        segments_.add(new RealSegment(point1, point1));
    }

    public ArrayList Curve() {
        return segments_;
    }
}
