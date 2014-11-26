
 #include "EvaluationsGrid.h"

EvaluationsGrid::EvaluationsGrid(double** limitsp, int divisionsp[], int dimp) {
	limits = limitsp;
	divisions = divisionsp;
	dim = dimp;
	
	evaluationsMatrix = new MultipleMatrix(divisions, dim);
}

EvaluationsGrid::~EvaluationsGrid(void) {

}

int EvaluationsGrid::getNumberOfDivisions(int divisionsArray[]) {
	for (int pont_division = 0; pont_division < dim; pont_division++) {
		divisionsArray[pont_division] = divisions[pont_division];
	}
}

int EvaluationsGrid::getLimits(double limitsArray[][2]) {	
	for(int pont_limits = 0; pont_limits < dim; pont_limits++) {
		limitsArray[pont_limits][0] = limits[pont_limits][0];
		limitsArray[pont_limits][1] = limits[pont_limits][1];
	}
}

int EvaluationsGrid::getCoordinates(double coordinatesArray[], int indexesArray[]) {
	
	for(int pont_index = 0; pont_index < dim; pont_index++) {
		
		if (indexesArray[pont_index] > divisions[pont_index]) {
			return INPUT_OUT_OF_BOUNDS;
		}
		
		double[2] boundaries;
		boundaries[0] = limits[pont_index][0];
		boundaries[1] = limits[pont_index][1];

		double interval = boundaries[1] - boundaries[0];
		double increment = interval / divisionsp[pont_index];

		coordinatesArray[pont_index] = boundaries[0] + (increment * indexesArray[pont_index]);
	}
	
	return SUCCESSFUL_OPERATION;

}

int EvaluationsGrid::getIndexes(int indexesArray[], double coordinatesArray[]) {
	for(int pont_index = 0; pont_index < dim; pont_index++) {
		
		if ((coordinatesArray[pont_index] < limits[pont_index][0]) || (coordinatesArray[pont_index] > limits[pont_index][1])) {
			return INPUT_OUT_OF_BOUNDS;
		}
		
		double[2] boundaries;
		boundaries[0] = limits[pont_index][0];
		boundaries[1] = limits[pont_index][1];

		double interval = boundaries[1] - boundaries[0];
		double increment = interval / divisionsp[pont_index];
		double errorMargin = (increment * 0.08);
		int round = 0;
		
		double pointPosition = ((coordinatesArray[pont_index] - boundaries[0]) % increment);

		if ( pointPosition >= errorMargin) {			
			if (pointPosition >= (increment - errorMargin) {
				round = 1;
			} else {
				return POINT_DOESNT_MATCH_GRID;
			}
		}
			
		indexesArray[pont_index] = (((coordinatesArray[pont_index] - boundaries[0]) / increment) + round);
	}

	return SUCCESSFUL_OPERATION;

}

int EvaluationsGrid::setValueAtIndex(void* object, int indexArray[]) {
	
	for(int pont_index = 0; pont_index < dim; pont_index++) {		
		if (indexesArray[pont_index] > divisions[pont_index]) {
			return INPUT_OUT_OF_BOUNDS;
		}
	}
	
	int result = evaluationsMatrix.setElement(indexArray, object); 
	
	if (result != SUCCESSFUL_OPERATION) {
		return result;
	}
	
	return SUCCESSFUL_OPERATION;
}

int EvaluationsGrid::setValueAtCoordinate(void* object, double coordinatesArray[]) {

	int[] indexArray;	
	indexArray = (int *) calloc(dim, sizeof(int));
	
	if ( indexArray == NULL) {
		return CAN_NOT_PERFORM_OPERATION;
	}
	
	int result = getIndexes(indexArray, coordinatesArray);
	
	if ( result != SUCCESSFUL_OPERATION) {
		return result;
	}
	
	return setValueAtIndex(object, indexArray);
}

int EvaluationsGrid::getValueAtIndex(void** object, int indexArray[]) {
	for(int pont_index = 0; pont_index < dim; pont_index++) {		
		if (indexesArray[pont_index] > divisions[pont_index]) {
			return INPUT_OUT_OF_BOUNDS;
		}
	}
	
	int result = evaluationsMatrix.getElement(indexArray, object); 
	
	if (result != SUCCESSFUL_OPERATION) {
		return result;
	}
	
	return SUCCESSFUL_OPERATION;
}

int EvaluationsGrid::getValueAtCoordinate(void** object, double coordinatesArray[]) {
		int[] indexArray;	
	indexArray = (int *) calloc(dim, sizeof(int));
	
	if ( indexArray == NULL) {
		return CAN_NOT_PERFORM_OPERATION;
	}
	
	int result = getIndexes(indexArray, coordinatesArray);
	
	if ( result != SUCCESSFUL_OPERATION) {
		return result;
	}
	
	return getValueAtIndex(object, indexArray);
}
  	