package wave.util;

import wave.multid.*;
import wave.util.exceptions.DimensionOutOfBounds;


public class PointNDimension {
	
  private int dimension;
  private RealVector coords_;

  public PointNDimension() {
    super();

  }

  public PointNDimension(double[] coords) {
	  dimension = coords.length;
	  coords_ = new RealVector(coords);
  }
  
  public PointNDimension(RealVector coords) {

    coords_ = coords;
    dimension = coords_.getSize();

  }

public PointNDimension(Space space){

  dimension= space.getDim();

  coords_ =new RealVector (dimension);

  coords_.zero();

}

public PointNDimension(CoordsArray coords) {
	coords_ = new RealVector(coords.getCoords());
	dimension = coords_.getSize();
}

  public PointNDimension(int dimension) {
    this.dimension = dimension;
    coords_ = new RealVector(dimension);
    coords_.zero();
  }



  public void add(RealVector vector) throws DimensionOutOfBounds{

    if (dimension != vector.getSize()){
      throw new DimensionOutOfBounds();
    }

    else{

      coords_.add(vector);

    }


  }


 public void add(double [] vector) throws DimensionOutOfBounds{

    if (dimension != vector.length){
      throw new DimensionOutOfBounds();
    }

    else{

      double [] data = coords_.toDouble();
      for (int i = 0;i < data.length;i++){

        data[i]+=vector[i];

      }


      coords_= new RealVector(data);

    }


  }

  protected void setDimension(int dimension) {

    this.dimension = dimension;
    coords_ = new RealVector(dimension);
  }

  public void setCoordinate(double value, int dimension) throws
      DimensionOutOfBounds {

    if ( (dimension >= 1) || (dimension <= this.dimension)) {
      coords_.setElement(dimension - 1, value);
    }
    else {
      throw new DimensionOutOfBounds();
    }
  }

  public double getCoordinate(int dimension) throws DimensionOutOfBounds {
    if ( (dimension >= 1) || (dimension <= this.dimension)) {
      return coords_.getElement(dimension - 1);
    }
    else {
      throw new DimensionOutOfBounds();
    }
  }

  public int myDimensionIs() {
    return this.dimension;
  }

  private boolean equals(PointNDimension evaluated) throws  DimensionOutOfBounds {

	  if (this.myDimensionIs() == evaluated.myDimensionIs()) {
      for (int pointer = 1; pointer <= this.myDimensionIs(); pointer++) {
        try {
          if (this.getCoordinate(pointer) != evaluated.getCoordinate(pointer))
            return false;
        }
        catch (Exception e) {
          throw new DimensionOutOfBounds();
        }
      }
    }
    else {
      throw new DimensionOutOfBounds();
    }

    return true;
  }
  
  public boolean equals(Object evaluated) {
	  
	  boolean tmp = true;
	  
	  try {
		  tmp = this.equals((PointNDimension) evaluated);
	  } catch (Exception e) {
		  
	  }
	  
	  return tmp;
  }
  
  public Object clone() {
	  return new PointNDimension(coords_);
  }

  public CoordsArray toCoordsArray() {
	  double[] tmp = new double[dimension];
	  
	  for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
		  try {
			  tmp[pont_dimension] = this.getCoordinate(pont_dimension + 1);
		  } catch (DimensionOutOfBounds e) {
			  e.printStackTrace();			  
		  }		  
	  }
	  	  
	  return new CoordsArray(tmp);
  }
  
  public RealVector toRealVector() {	  	  
	  return new RealVector(coords_);
  }
  
  public boolean isSameDimension(PointNDimension evaluated) {
    return (evaluated.myDimensionIs() == this.dimension);
  }

}
