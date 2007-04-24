package rpnumerics.methods.contour;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.util.*;
import wave.exceptions.*;


public class GridGenerator4D extends GridGenerator{



	public GridGenerator4D(int dimension, 
						   Constraint[] constraint,
						   HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) throws DimensionOutOfBounds{

		super(dimension, constraint, hyperCubeErrorTreatment);

		// TODO Auto-generated constructor stub
	}

	public HyperCubeND getHyperCube(FunctionParameters parameters) throws DimensionOutOfBounds {

		// this function is specific fo N=4

		int index[] = {1, 3, 4, 2};

		int numberOfVertices = this.getNumberOfVertices();
		int dimension = this.myDimensionIs();

		HyperCubeND hypercube = null;
		PointNDimension[] hypercubeVertices = new PointNDimension[numberOfVertices];

		if (parameters.myDimensionIs() == dimension) {

			PointNDimension initial_point = getInitialPoint();

			for (int kl = 1; kl <= 4; kl++) {
				for (int kr = 1; kr <= 4; kr++) {
					PointNDimension hypercubePoint = new PointNDimension(dimension);

					// correspondece between the vertices

					int hyperCubeIndexes[] = new int[4];

					switch(kl) {
						case 1:
							hyperCubeIndexes[0] = parameters.getIndex(4);
							hyperCubeIndexes[1] = parameters.getIndex(3);
							break;
						case 2:
							hyperCubeIndexes[0] = parameters.getIndex(4) + 1;
							hyperCubeIndexes[1] = parameters.getIndex(3);
							break;
						case 3:
							hyperCubeIndexes[0] = parameters.getIndex(4) + 1;
							hyperCubeIndexes[1] = parameters.getIndex(3) + 1;
							break;
						case 4:
							hyperCubeIndexes[0] = parameters.getIndex(4);
							hyperCubeIndexes[1] = parameters.getIndex(3) + 1;
							break;
					}

					switch(kr) {
						case 1:
							hyperCubeIndexes[2] = parameters.getIndex(2);
							hyperCubeIndexes[3] = parameters.getIndex(1);
							break;
						case 2:
							hyperCubeIndexes[2] = parameters.getIndex(2) + 1;
							hyperCubeIndexes[3] = parameters.getIndex(1);
							break;
						case 3:
							hyperCubeIndexes[2] = parameters.getIndex(2) + 1;
							hyperCubeIndexes[3] = parameters.getIndex(1) + 1;
							break;
						case 4:
							hyperCubeIndexes[2] = parameters.getIndex(2);
							hyperCubeIndexes[3] = parameters.getIndex(1) + 1;
							break;
					}

					for (int dimension_i = 1; dimension_i<= 4; dimension_i++) {

						double variationOfDimension = 0;

						try {
							variationOfDimension = getVariation(dimension_i);
						} catch(Exception e) {

						}

						double value = initial_point.getCoordinate(dimension_i) +
						            (variationOfDimension  * (hyperCubeIndexes[dimension_i - 1] - 1));

						hypercubePoint.setCoordinate(value, dimension_i);
					}

					hypercubeVertices[(4*( index[(kl -1)]-1) + index[kr-1]) - 1] = hypercubePoint;
				}
			}

			try {
				hypercube = new HyperCubeND(dimension, hypercubeVertices);
			} catch (DimensionOutOfBounds e) {
				throw e;
			}

		} else {
			throw new DimensionOutOfBounds();
		}

		return hypercube;
	}

}
