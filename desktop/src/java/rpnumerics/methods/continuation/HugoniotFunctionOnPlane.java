package rpnumerics.methods.continuation;

import wave.util.*;

public class HugoniotFunctionOnPlane implements VectorFunction {
    //
    // Members
    //
    private VectorFunction f_;
    private RealVector point_;
    private RealVector norm_;

    public HugoniotFunctionOnPlane(VectorFunction f, RealVector point, RealVector norm) {
      f_ = f;
      point_ = point;
      norm_ = norm;
    }

    public RealVector value(RealVector U) {
      RealVector HF = f_.value(U);
      RealVector result = new RealVector(HF.getSize() + 1);
      for (int i = 0; i < HF.getSize(); i++)
        result.setElement(i, HF.getElement(i));
      RealVector deltaU = new RealVector(U);
      deltaU.sub(point_);
      result.setElement(HF.getSize(), norm_.dot(deltaU));
      return result;
    }

    public RealMatrix2 deriv(RealVector U) {
      RealMatrix2 HdF = new RealMatrix2(f_.deriv(U));
      RealMatrix2 result = new RealMatrix2(HdF.getNumCol(), HdF.getNumCol());
      RealVector row = new RealVector(HdF.getNumCol());
      for (int i = 0; i < HdF.getNumRow(); i++) {
        HdF.getRow(i, row);
        result.setRow(i, row);
      }
      row = new RealVector(norm_);
      result.setRow(HdF.getNumRow(), row);
      return result;
    }
  }
