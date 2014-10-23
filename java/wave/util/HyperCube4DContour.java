package wave.util;

import wave.util.exceptions.DimensionOutOfBounds;
public class HyperCube4DContour extends HyperCubeND {

	public HyperCube4DContour(PointNDimension[] verticesp)
			throws DimensionOutOfBounds {
		super(4, verticesp);
		// TODO Auto-generated constructor stub
	}

	public static HyperCube4DContour generateBasicHyperCube() {

		int index[] = {1, 3, 4, 2};

		int numberOfVertices = 16;
		int dimension = 4;

		HyperCube4DContour hypercube = null;
		PointNDimension[] hypercubeVertices = new PointNDimension[numberOfVertices];


		for (int kl = 1; kl <= 4; kl++) {
			for (int kr = 1; kr <= 4; kr++) {
				PointNDimension hypercubePoint = new PointNDimension(4);

				// correspondece between the vertices

				int hyperCubeIndexes[] = new int[4];

				switch(kl) {
					case 1:
						hyperCubeIndexes[0] = 0;
						hyperCubeIndexes[1] = 0;
						break;
					case 2:
						hyperCubeIndexes[0] = 1;
						hyperCubeIndexes[1] = 0;
						break;
					case 3:
						hyperCubeIndexes[0] = 1;
						hyperCubeIndexes[1] = 1;
						break;
					case 4:
						hyperCubeIndexes[0] = 0;
						hyperCubeIndexes[1] = 1;
						break;
				}

				switch(kr) {
					case 1:
						hyperCubeIndexes[2] = 0;
						hyperCubeIndexes[3] = 0;
						break;
					case 2:
						hyperCubeIndexes[2] = 1;
						hyperCubeIndexes[3] = 0;
						break;
					case 3:
						hyperCubeIndexes[2] = 1;
						hyperCubeIndexes[3] = 1;
						break;
					case 4:
						hyperCubeIndexes[2] = 0;
						hyperCubeIndexes[3] = 1;
						break;
				}

				for (int dimension_i = 1; dimension_i<= dimension; dimension_i++) {

					double value = hyperCubeIndexes[dimension_i - 1];

					try {
						hypercubePoint.setCoordinate(value, dimension_i);
					} catch (Exception e) {

					}
				}

				hypercubeVertices[(4*( index[(kl -1)]-1) + index[kr-1]) - 1] = hypercubePoint;
			}
		}

		try {
			hypercube = new HyperCube4DContour(hypercubeVertices);
		} catch (DimensionOutOfBounds e) {

		}

		return hypercube;

	}

}
