package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.markedhypercubes.MarkedCubeItem;
import wave.util.exceptions.*;
import wave.util.*;

public class MPGridFunctionOnGridEvaluator extends
		VectorialFunctionsOnGridUsingReducedMemory {
	
	private int numberOfFunctions;
	
	private Constraint[] plusConstraints;
	private Constraint[] minusConstraints;
	
	private GridFunction[] plusFunctions;
	private GridFunction[] minusFunctions;
	
	public MPGridFunctionOnGridEvaluator(int dimension,
										 Constraint[] constraint, 
										 MDVectorFunction[] functionsp, 
										 HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
			throws DimensionOutOfBounds, CanNotPerformCalculations {
		super(dimension, constraint, functionsp, hyperCubeErrorTreatment);
		
		if ((dimension % 2) != 0) {
			throw new DimensionOutOfBounds();
		}
		
		numberOfFunctions = ((MDVectorFunction) functionsp[0]).getResultComponentNumber();
		
		MDCompositeFunction function = ((MDCompositeFunction) ((MDVectorFunctionDecorator) functionsp[0]).getFunction());
		
		plusFunctions = function.getPlusFunctions();
		minusFunctions = function.getMinusFunctions();
		
		int numberOfConstraints = dimension / 2;
		
		plusConstraints = new Constraint[numberOfConstraints];
		minusConstraints = new Constraint[numberOfConstraints];
		
		for (int pont_constraint = 0; pont_constraint < numberOfConstraints; pont_constraint++) {
			minusConstraints[pont_constraint] = constraint[pont_constraint];
			plusConstraints[pont_constraint] = constraint[pont_constraint + numberOfConstraints];
		}
				
		for (int pont_solution = 0; pont_solution < plusFunctions.length; pont_solution++) {
			
			GridFunction[] buffer = new GridFunction[1];
			
			buffer[0] = plusFunctions[pont_solution];
			VectorialFunctionsOnMatrixGrid positiveSolution = getGridFunctionEvaluator(numberOfConstraints, 
																								 plusConstraints, 
																								 buffer, 
																								 hyperCubeErrorTreatment);
			plusFunctions[pont_solution].initFluxGrid(positiveSolution);
			
			buffer[0] = minusFunctions[pont_solution];
			VectorialFunctionsOnMatrixGrid negativeSolution = getGridFunctionEvaluator(numberOfConstraints, 
																								 minusConstraints, 
																								 buffer,
																								 hyperCubeErrorTreatment);
			minusFunctions[pont_solution].initFluxGrid(negativeSolution);			
		
		}
	}
	
	protected void buildArray(int parameter) throws DimensionOutOfBounds { 
		
		int dimension = this.myDimensionIs();
			
		IntegerInterval[] loopIntervals = new IntegerInterval[dimension];

		loopIntervals[0] = new IntegerInterval(0, 0);
		
		for (int dimension_pont = 2; dimension_pont <= dimension; dimension_pont++) {
			loopIntervals[dimension_pont - 1] = new IntegerInterval(0, this.getNumberOfDivisions(dimension_pont));
		}	
		
		MultipleLoop loop = setDomainMultipleLoop(loopIntervals);
		
		int loopSize = loop.getLoopSize();
		
		if (parameter == 1) {

			for(int loopHash = 0; loopHash < loopSize; loopHash++) {
								
				int[] coordinates =  loop.getIndex(loopHash);
				
				int[] position = new int[dimension + 1]; 
				
				coordinates[0] = 0;
				position[0] = 0;
				
				int pont_coordinates = 1;
				
				for (; pont_coordinates < dimension; pont_coordinates++) {
					   position[pont_coordinates] = coordinates[pont_coordinates];				
				}
				
				try {

                                        FunctionParameters parameters = new FunctionParameters(dimension);

					for (int dimension_pont = 0; dimension_pont < dimension; dimension_pont++) {
						parameters.setIndex(position[dimension_pont], dimension_pont + 1);
					}

                                        HyperCubeND cube = null;
                                        
                                        try {
                                                cube = this.getHyperCube(parameters);
                                        } catch (DimensionOutOfBounds e) {
                                                throw e;
                                        }

                                        int dimensionOfHyperCube = cube.getDimension();

                                        PointNDimension first = cube.getVertice(1);
                                        PointNDimension last = cube.getVertice(cube.getNumberOfVertices());

                                        int halfDimension = dimensionOfHyperCube / 2;

                                         boolean thereIsOverlap = true;

                                         double[][] points = new double[2][4];
                                        
                                        for (int N = 0; ((N < halfDimension) && thereIsOverlap); N++) {

                                            double fmN = first.getCoordinate(N + 1);
                                            double smN = last.getCoordinate(N + 1);

                                            double fpN = first.getCoordinate(N + 1 + halfDimension);
                                            double spN = last.getCoordinate(N + 1 + halfDimension);

                                            points[0][N] = fmN;
                                            points[0][N + halfDimension] = fpN;
                                            points[1][N] = smN;
                                            points[1][N + halfDimension] = spN;

                                           /* double deltaZero = Math.abs(fmN - fpN);
                                            double deltaY = Math.abs(spN - fpN);

                                            if (fpN >= fmN) {
                                                if (fpN <= smN) {
                                                    throw new CanNotPerformCalculations();
                                                }

                                            } else {
                                                if (deltaY >= deltaZero) {
                                                    throw new CanNotPerformCalculations();
                                                }
                                            }

                                            if ((((fpN - fmN) * (fpN - smN)) >= 0) &&
                                                (((spN - fmN) * (spN - smN)) >= 0)){

                                                throw new CanNotPerformCalculations();
                                            }*/

                                            double Lm = Math.abs(smN - fmN);
                                            double Lr = Math.abs(spN - fpN);

                                            double Cm = 0.5 * (fmN + smN);
                                            double Cr = 0.5 * (fpN + spN);

                                            double distC = Math.abs(Cm - Cr);

                                            thereIsOverlap = true;

                                            if ((distC > (Lm + Lr))) {
                                                thereIsOverlap = false;
                                            }

                                        }

                                         System.out.println("Primeiro ponto: " + points[0][0] + ", " + points[0][1] + ", " + points[0][2] + ", " + points[0][3]);
                                         System.out.println("Segundo ponto: " + points[1][0] + ", " + points[1][1] + ", " + points[1][2] + ", " + points[1][3]);

                                        if (thereIsOverlap) {
                                            throw new CanNotPerformCalculations();
                                        }

					FunctionParameters point = new FunctionParameters(coordinates);
					
					RealVector result = ((MDCompositeFunction) ((MDVectorFunctionDecorator) this.getFunctionsMap().get(String.valueOf(0))).getFunction()).value(point);
										
					for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
						position[dimension] = pont_function;
						getMatrix().setElement(position, new Double(result.getElement(pont_function)));
					}
					
				} catch (CanNotPerformCalculations e) {
					MarkedCubeItem markedCube = new MarkedCubeItem(this, new FunctionParameters(coordinates), e);
					try {
						this.getMatrix().setElement(position, markedCube);
					} catch (IndexOfArrayOutOfBounds e1) {
						throw new DimensionOutOfBounds();
					}
					//markHyperCube(markedCube);
				} catch (IndexOfArrayOutOfBounds e) {
					throw new DimensionOutOfBounds();
				}
			}

		}
		
		for(int loopHash = 0; loopHash < loopSize; loopHash++) {
						
			int[] coordinates = loop.getIndex(loopHash); 
			
			int[] position = new int[dimension + 1]; 
			
			coordinates[0] = parameter;
			position[0] = parameter % 2;
			
			int pont_coordinates = 1;
			
			for (; pont_coordinates < dimension; pont_coordinates++) {
				   position[pont_coordinates] = coordinates[pont_coordinates];				
			}
						
			try {
				
				FunctionParameters point = new FunctionParameters(coordinates);

				RealVector result = ((MDCompositeFunction) ((MDVectorFunctionDecorator) this.getFunctionsMap().get(String.valueOf(0))).getFunction()).value(point);
								
				for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
					position[dimension] = pont_function;
					getMatrix().setElement(position, new Double(result.getElement(pont_function)));
				}
				
			} catch (CanNotPerformCalculations e) {
				MarkedCubeItem markedCube = new MarkedCubeItem(this, new FunctionParameters(coordinates), e);
				try {
					this.getMatrix().setElement(position, markedCube);
				} catch (IndexOfArrayOutOfBounds e1) {
					throw new DimensionOutOfBounds();
				}
				//markHyperCube(markedCube);
			} catch (IndexOfArrayOutOfBounds e) {
				throw new DimensionOutOfBounds();
			}
		}

	}

	public Constraint[] getMinusConstraints() {
		return this.minusConstraints;
	}
	
	public Constraint[] getPlusConstraints() {
		return this.plusConstraints;
	}

	protected GridFunction[] getMinusFunctions() {
		return this.minusFunctions;
	}

	protected GridFunction[] getPlusFunctions() {
		return this.plusFunctions;
	}
	
}
