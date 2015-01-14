package wave.util;

import wave.util.exceptions.DimensionOutOfBounds;
public class HyperCubeND {
// classe para definir um cubo ndimensoes

	private PointNDimension[] vertices;
	private int numberOfVertices = 0;
	private int dimension;

	public HyperCubeND(int dimension, PointNDimension[] verticesp) throws DimensionOutOfBounds{
		super();
		// TODO Auto-generated constructor stub
		this.dimension = dimension;
		this.numberOfVertices = (int) Math.pow(2, dimension);

		if (this.numberOfVertices == verticesp.length) {

			this.vertices = new PointNDimension[this.numberOfVertices];

			for(int i = 0;i < this.numberOfVertices; i++) {
				vertices[i] = verticesp[i];
			}

		} else {
			throw new DimensionOutOfBounds();
		}

	}


	public int getNumberOfVertices() {
		return vertices.length;
	}

	public int getDimension(){
		return this.dimension;
	}

	public PointNDimension getVertice(int index) throws DimensionOutOfBounds {

		if (index <= numberOfVertices && index >= 1) {
			return vertices[index-1];
		} else {
			throw new DimensionOutOfBounds();
		}
	}

	public static int calculateNumberOfVertices(int dimension) {
		return (int) Math.pow(2, dimension);
	}

	public static int calculateNumberOfFaces(int dimension) {
		return 0;
	}

	public static HyperCubeND generateBasicHyperCube(int dimension) {

		int numberOfVerticesOfBasicHyberCube = calculateNumberOfVertices(dimension);

		PointNDimension[] hypercubeVertices = new PointNDimension[numberOfVerticesOfBasicHyberCube];

		for (int index = 1; index <= numberOfVerticesOfBasicHyberCube; index++) {

			int k = index -1;

			PointNDimension temp = new PointNDimension(dimension);

			for (int i= 1; i <= dimension; i++ ) {

				try {
					temp.setCoordinate((k % 2), i);
				} catch (Exception e) {

				}

				k = k/2;
			}

			hypercubeVertices[index-1] = temp;
		}

		HyperCubeND hypercube = null;

		try {
			hypercube = new HyperCubeND(dimension, hypercubeVertices);
		} catch (Exception e) {

		}

		return hypercube;
	}

}
