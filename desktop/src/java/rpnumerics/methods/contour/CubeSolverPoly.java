package rpnumerics.methods.contour;

import java.util.*;

public class CubeSolverPoly extends CubeSolver {
		
	public CubeSolverPoly(CubeFace cface) {
		super(cface);
		
	}

	public int[][] mkpoly() {
		
		// that function was based on the function mkpoly from the file hcube.F
		// at  /impa/proj/RP/wave_2005-07/rp/contour/
		
		int numberOfSimplices 			= cFace_.getNumberOfSimplices(); 			// nsimp
		int numberOfFaces 				= cFace_.getNumberOfSimplexFaces();			// nsface
		int[][] AdjcencyInfo			= cFace_.getAdjacencyInfoArray();
		int numberOfPolygons 			= 0;										// npolys										// nvertt
		int [][] solutionPointerArray 	= this.getSolPtr(); 						// solptr

		Vector polygonList = new Vector();											// polyv
		
		int[][] polygons = null;
		
		for(int pont_simplex = 1; pont_simplex <= numberOfSimplices; pont_simplex++) {
			//int pont_vertice = numberOfVertices;								// nverts
			
			// find the first solution in this simplex and count the number
			// of solutions 
			
			int solutionCounter 	= 0;				// solcnt
			int previous 			= 0;				// prev
			int current  			= 0;				// cur
			
			int numberOfVertices 	= 0;	
			
			Vector vertices = new Vector();											
			
			for(int pont_face = 1; pont_face <= numberOfFaces; pont_face++) {
				int solutionPointerTemp = solutionPointerArray[pont_simplex - 1][pont_face - 1]; // spi
				if( solutionPointerTemp != 0) {
					if(solutionCounter == 0) {
						numberOfVertices++;
						vertices.add(new Integer(solutionPointerTemp));
						previous = -1;
						current  = pont_face;
					}
					solutionCounter++;
				}				
			}
			
			if (solutionCounter != 0) {
				for (int pont_solution = 2; pont_solution <= solutionCounter; pont_solution++) {
					boolean solutionFound = false;
					for(int pont_face = 1; ((pont_face <= numberOfFaces) && !solutionFound); pont_face++) {
						if (pont_face != previous && pont_face != current) {
							int solutionPointerTemp = solutionPointerArray[pont_simplex - 1][pont_face - 1]; // spi
							if((solutionPointerTemp != 0) && (AdjcencyInfo[current - 1][pont_face - 1] != 0)) {
								numberOfVertices++;								
								vertices.add(new Integer(solutionPointerTemp));
								previous = current;
								current  = pont_face;
								solutionFound = true;
							}
						}
					}
				}
					
				numberOfPolygons++;
				
				int verticesSize = vertices.size();
				Object[] verticesArray = vertices.toArray();
				int[] polygonVertices = new int[verticesSize];
				
				for(int pont_vertice = 1; pont_vertice <= verticesSize; pont_vertice++) {
					polygonVertices[pont_vertice - 1] = ((Integer) verticesArray[pont_vertice - 1]).intValue();
				}
				
				polygonList.add(polygonVertices);
			}
						
		}
		
		int polygonSize = polygonList.size();
		this.setNEdges(polygonSize);
		
		if( polygonSize > 0) {
			
			polygons = new int[polygonSize][];
			
			Iterator i = polygonList.iterator();
			
			for (int pont_polygon = 1; pont_polygon <= polygonSize; pont_polygon++) {
				polygons[pont_polygon - 1] = (int[]) i.next();
			}
			
			return polygons;
		} 
		
		return null;
	}
	
}
