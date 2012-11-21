  package rpnumerics.methods.contour;

import java.awt.*;
import java.io.*;

import wave.util.*;
import wave.multid.model.*;
import wave.multid.view.*;
import wave.util.exceptions.*;
import rpnumerics.RpCurve;
import rpnumerics.methods.contour.exceptions.*;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorFound;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.markedhypercubes.MarkedCubeItem;

public class ContourND implements Serializable {
	
	// members were declared protected, instead of being declared private, just for convience
	// This is better than write getters and setters for all these properties, since they will
	// not suffer any changes in the future.
	
	private String nameOfMethod = "";
	
	/** The dimension of the space, that is the old n_ */
	protected int dimension;
	
	/** The dimension of the faces, it is the old m_ */
	protected int numberOfEquations; 
	protected int numberOfVertices;

        private boolean triangularDomain = false;
	
	private HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment;
	
	//
	// Members
	//
	
	/** Interface that implements the funtion to be approximated */
	protected CubeFunction function[];
	

	/** Object containing the data structures needed by CubeSolver */
	private CubeFace cFace_;

	/** Object containing methods to find solutions in a hcube */
	private CubeSolver cSolver_;
		
	/**
	 * Segment to be sent
	 */
			
	public ContourND(CubeFunction[] functionp, 
					 int dimensionp, 
					 HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) throws IllegalArgumentException {

		propertiesInitialization(functionp, dimensionp);
		
		try {
			validationTest(); 
		} catch (IllegalArgumentException e) {
			throw e;
		}
				
		initMainObjects();
		
		this.hyperCubeErrorTreatment = hyperCubeErrorTreatment;
		
		setNameOfMethod("ContourND");	
	}
	
	public RpCurve curvND(double[] rect,int[] res) throws CanNotPerformCalculations {
				
		// intervalos em rect
		// numero de divisoes estah em res para cada dimensao
		// res => array com o numero de divisoes de cada dimensao
		// rectl => array com os limites do dominio da funcao.
		
		//	pegar intervalos e outros dados  para calcular
		
		ContourCurve curve = new ContourCurve();
		
		if ((rect.length == (dimension * 2)) && (res.length == dimension)) {
			
			try {
					

		    	double[][] foncub_ = new double[numberOfEquations][numberOfVertices];
		    					
				Constraint[] constraints = initializeConstraints(res, rect);
								
				GridGenerator solution = initializeSolutionConstraints(dimension, 
																	   constraints, 
																	   function, 
																	   hyperCubeErrorTreatment);
							
				MultipleLoop loop = initializeLoop(res);
				
				int size = loop.getLoopSize();
				
				for(int loopHash = 0; loopHash < size; loopHash++) {
		    		
		    		int[] position = loop.getIndex(loopHash);
		    		
					FunctionParameters parameters = new FunctionParameters(dimension);
					
					for (int dimension_pont = 0; dimension_pont < dimension; dimension_pont++) {
						parameters.setIndex(position[dimension_pont], dimension_pont + 1);
					}
					
					try {

                                            jumpHyperCube(parameters, res);
						
					foncub_ = evaluateFunctions(solution, parameters);
						
						double [][] sol_;
				    	int [][] edges_; 
						
						sol_ = solving(foncub_);
						
						edges_ = makeEdges(); 	
						
						try{
				    		int nedges = cSolver_.getNumberOfEdges();
					
							if ( nedges > 0 ) {
								
								ContourPolyline[] polylines = copyEdges(solution, nedges,parameters, sol_, edges_);
						    	
								for (int pont_polyline = 0; pont_polyline < polylines.length; pont_polyline++) {
					    			curve.addPolyline(polylines[pont_polyline]);
					    		}
							} else {
								throw new SolutionNotFound();
							}
							
					    } catch (DimensionOutOfBounds e) {
					    	throw new CanNotPerformCalculations();
						} catch (ThereIsNoFeasibleSolution e) {
							throw new CanNotPerformCalculations();
						} catch (HyperCubeErrorFound e) {
							this.hyperCubeErrorTreatment.markHyperCube(new MarkedCubeItem( solution, parameters, e));
			    		} 
												
					} catch (SolutionNotFound e) {
						
					} catch (CanNotPerformCalculations e) {
						
		    		}
		    	}
								    			
			} catch (Exception e) {
				e.printStackTrace();
				throw new CanNotPerformCalculations();			
			}
		}
		
		return setRPnCurve(curve);
	}
		
	public HyperCubeErrorTreatmentBehavior getHyperCubeErrorTreatment() {
		return this.hyperCubeErrorTreatment;
	}
	
	public boolean wasThereErrors() {
		return this.hyperCubeErrorTreatment.isThereMarkedHyperCubes();
	}
	
	protected void propertiesInitialization(CubeFunction[] functionp, int dimensionp) {
		dimension = dimensionp;
		numberOfEquations = functionp.length;		
		numberOfVertices = HyperCubeND.calculateNumberOfVertices(dimension);		
    							
		function = new CubeFunction[numberOfEquations];	
		
		for (int pont_function = 0; pont_function < functionp.length; pont_function++) {			
			function[pont_function] = functionp[pont_function];			
		}
		
	}
	
	protected void validationTest() throws IllegalArgumentException {
		if ((dimension - numberOfEquations) > 1 || (numberOfEquations == 0)) {
			// It is not a curve
			throw (new IllegalArgumentException("ERROR in Contour4D: CubeFunction " + "Dimension is out of Bounds"));
		}
		
		// testar se ponteiros de funcoes sao null...
	}
	
	protected int [][] makeEdges() {
		cSolver_.mkedge();
		return  cSolver_.getEdgesArray(); 	
	}
	   
	protected ContourPolyline[] copyEdges(GridGenerator solution, int nedges, FunctionParameters parameters, double [][] sol_, int [][] edges_) throws DimensionOutOfBounds, 
																																					   ThereIsNoFeasibleSolution, 
																																					   HyperCubeErrorFound {
    	if(solution.myDimensionIs() == parameters.myDimensionIs()) {
        	
    		ContourEdgesMultiPolyline edges = null;
    		
    		try {
    			edges = new ContourEdgesMultiPolyline(nedges,
    												  edges_,
    			 									  sol_,
    												  (FunctionParameters) parameters.clone(),
    												  solution);
    		} catch(ThereIsNoFeasibleSolution e) {
    			e.printStackTrace();
    			throw e;
    		} catch (DimensionOutOfBounds e) {
    			e.printStackTrace();
    			throw e;
    		} catch (HyperCubeErrorFound e) {
    			e.printStackTrace();
    			throw e;
    		}
    		    		
    		return edges.getContourPolylines();    					
    		
    	} else {
    		throw new  DimensionOutOfBounds();
    	}
    }
    
    protected int invertIndex(int index, int dimension) {
    	/*
    	 * this function move the higher indexes of the array to low indexes. Inverts the array
    	 * to make it compatible.
    	 * 
    	 * Example: 1 2 3 4 makes them 4 3 2 1
    	 */
    	
    	int half = dimension / 2;
    	int rest = dimension % 2;
    	
    	int pont_half = half + 1;
    	
    	int result = 0;
    	
    	if (rest != 0) {
    		result = pont_half + (pont_half - index);
    	} else {
    		if (index <= half) {
    			result = pont_half + (half - index);
    		} else {
    			result = half - (index - pont_half);
    		}    		
    	}
    	
    	return result;
    }
    
    // Template Pattern functions
    
    protected void initMainObjects() {    			
    	cFace_ = new CubeFace(dimension, numberOfEquations);
		cSolver_ = new CubeSolver(cFace_);
    }
    
    protected Constraint[] initializeConstraints(int res[], double rect[]) throws PointsAreNotSameDimension{
    	Constraint[] constraints = new Constraint[dimension];
    	    	
    	try {
			for (int dimension_pont = 0; dimension_pont < dimension; dimension_pont++) {
				
				constraints[dimension_pont] = new Constraint(new DoubleInterval(rect[dimension_pont*2], rect[dimension_pont*2 +1]), 
														     res[dimension_pont]);
			}
    	} catch (PointsAreNotSameDimension e) {
    		throw e;
    	}
		
		return constraints;
    }
    
    protected GridGenerator initializeSolutionConstraints(int dimension, 
    													  Constraint[] constraints, 
    													  CubeFunction[] function,
    													  HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) 
    																						throws DimensionOutOfBounds,
    																							   CanNotInitializeGrid {
    	
    	GridGeneratorUsingReducedMemory solutionp;
    	
    	try {
    		solutionp = new GridGeneratorUsingReducedMemory(dimension, constraints, function, hyperCubeErrorTreatment);
    	} catch (DimensionOutOfBounds e) {
    		throw e;
    	}
    	
    	return solutionp;
    }
    
    protected double[][] basicHypercubeGeneration() throws DimensionOutOfBounds {
    	int numberOfVerticesofStandardCube = HyperCubeND.calculateNumberOfVertices(dimension);
		
    	// 	get the standard n-cube, put in the old structure vert
		    	
		double[][] vert = new double[numberOfVerticesofStandardCube][dimension];				
		
		HyperCubeND standard = HyperCubeND.generateBasicHyperCube(dimension);
		
		try {
			for (int vertice_pont = 1; vertice_pont <= numberOfVerticesofStandardCube; vertice_pont++) {
				PointNDimension point = standard.getVertice(vertice_pont);
				for (int coordinate_pont=1; coordinate_pont <= dimension; coordinate_pont++) {
					vert[vertice_pont-1][coordinate_pont-1] = point.getCoordinate(invertIndex(coordinate_pont, dimension));
				}
			}
		} catch (DimensionOutOfBounds e) {
			throw e;
		}
		
		return vert;
    }
    
    protected int[] initializeExstfc() {
    	
    	// initialize exstfc as 1
	// that must always be done, no explanation for that
    	
    	int numberOfFaces = cFace_.getNumberOfFaces();
		int[] exstfc = new int[numberOfFaces];
		
		setExstfc(exstfc, numberOfFaces);
		
		return exstfc;
    }
    
    protected MultipleLoop initializeLoop(int[] res) {
    	IntegerInterval[] loopIntervals = new IntegerInterval[dimension];

		for (int dimension_pont = 0; dimension_pont < dimension; dimension_pont++) {					
			loopIntervals[dimension_pont] = new IntegerInterval(1, res[invertIndex(dimension_pont + 1, dimension) - 1]);
		}				
		
		return new MultipleLoop(loopIntervals);
    }
    
    protected double[][] evaluateFunctions(GridGenerator solution, FunctionParameters parameters) throws SolutionNotFound,
    																									 DimensionOutOfBounds,
    																									 CanNotPerformCalculations {
    	
    	double[][] foncub_ = new double[numberOfEquations][numberOfVertices];
    	
		int numberOfVertices = solution.getNumberOfVertices();
		
		double[] feasibleSolution = new double[numberOfVertices];						
			
		for (int function_pointer = 1; function_pointer <= numberOfEquations; function_pointer++) {
			
			try {
				feasibleSolution = solution.solveHyperCube(parameters, function[function_pointer - 1]);
			} catch (CanNotPerformCalculations e) {
				throw e;
			} catch (DimensionOutOfBounds e) {
				throw e;
			}
			
			if (feasibleSolution == null) {
				throw new SolutionNotFound();
			} else {		    							
				foncub_[function_pointer - 1] = feasibleSolution;
			}
		}
		
		return foncub_;
    } 
      
    protected double [][] solving(double[][] foncub_) {
    	double[][] vert = null;
    	
    	try { 
    		vert = basicHypercubeGeneration();
    	} catch (DimensionOutOfBounds e) {
    		e.printStackTrace();
    	}
    	
    	cFace_.setVertexCoordinatesArray(vert);	
    	
    	int[] exstfc = initializeExstfc();
		
		cSolver_.cubsol(exstfc, foncub_);		
		return cSolver_.getSolutionArray();
    }
          
    // End of Template Pattern functions
    
    protected final void setCFace(CubeFace cFace_) {
    	this.cFace_ = cFace_;
    } 
    
    protected CubeFace getCFace() {
    	return this.cFace_;
    }
    
    protected final void setCSolver(CubeSolver cSolver_) {
    	this.cSolver_ = cSolver_;
    }
    
    protected CubeSolver getCSolver() {
    	return this.cSolver_;
    }
    
    private void setExstfc(int[] exstfc, int numberOfFaces) {
    	for (int face_pont = 0; face_pont <= (numberOfFaces - 1); face_pont++) {
			exstfc[face_pont] = 1;
	}		
    }
    
    protected void setNameOfMethod(String method) {
    	this.nameOfMethod = method;
    }
    
    protected RpCurve setRPnCurve (ContourCurve curve) {
    	
    	RpCurve rpncurve = null;
		
		if (curve.numberOfSegments() != 0 ) {
			rpncurve = new RpCurve(curve, new ViewingAttr(Color.yellow)) {

                @Override
                public java.util.List segments() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

            };
		}		
    	
    	return rpncurve;
    }
    
    public String getNameOfMethod() {
    	return this.nameOfMethod;
    }

    private void jumpHyperCube(FunctionParameters parameters, int[] res) throws CanNotPerformCalculations {    
    
         try {

             if (triangularDomain) {
                int myDimension = parameters.myDimensionIs();

                int even = myDimension % 2;
                int dim_loop = myDimension / 2;

                if ( even == 0) {
                    for (int pont_loop = 0; pont_loop < dim_loop; pont_loop++) {
                        int first = parameters.getIndex(2*pont_loop + 1);
                        int second = parameters.getIndex(2*pont_loop + 2);
                        if ((first + second) > res[0]) {
                                // System.out.println("Leaving..." + first + " " + second);
                            throw new CanNotPerformCalculations();
                        }

                    }
                }
             }
        } catch (Exception e) {

        }
    }

    public void setTriangularDomainOn() {
        triangularDomain = true;
    }

    public void setTriangularDomainOff() {
        triangularDomain = false;
    }
}

	

