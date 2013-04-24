
#include "MultipleMatrix.h"

MultipleMatrix::MultipleMatrix() {	
	
}

MultipleMatrix::MultipleMatrix(int matrixLimits[], int dim) {
	numberOfMatrixes = dim;

    lengths = (int *) calloc(numberOfMatrixes, sizeof(int));
    
    matrixCreated = false;
    
    if (lengths != NULL) {

	    loopsize = 1; 
	
	    for (int pont = 0; pont < numberOfMatrixes; pont++) {
	      lengths[pont] = matrixLimits[pont];
	      loopsize *= lengths[pont];
	    }
	    
	    matrix = (void**) calloc(loopsize, sizeof(void *));
	    
	    if (matrix != NULL) {
	    	matrixCreated = true;
	    }
    }     
}

MultipleMatrix::~MultipleMatrix(void) {
	free(lengths);
	free(matrix); // testar para ver se ao estah destruindo tambem os elementos...
}

void MultipleMatrix::setMatrixLimits(int[] matrixLimits, int dim) {
	numberOfMatrixes = dim;

    lengths = (int *) calloc(numberOfMatrixes, sizeof(int));
    
    matrixCreated = false;
    
    if (lengths != NULL) {

	    loopsize = 1;
	
	    for (int pont = 0; pont < numberOfMatrixes; pont++) {
	      lengths[pont] = matrixLimits[pont];
	      loopsize *= lengths[pont];
	    }
	    
	    matrix = (void**) calloc(loopsize, sizeof(void *));
	    
	    if (matrix != NULL) {
	    	matrixCreated = true;
	    }
    }     
}

int MultipleMatrix::getPos(int* A) {	
	 
  	 if (matrixCreated) {
  	 	int sum = 0;
  
		  if (numberOfMatrixes != 1) {
		    int pont = (numberOfMatrixes - 1);
		    sum = A[pont] * lengths[pont - 1] + A[pont - 1];
		    pont--;
		    for (; pont >= 1; pont--) {
		      sum = sum * lengths[pont - 1] + A[pont - 1];
		    }
		  }
		  else {
		    sum = A[0];
		  }	    
	 	 return sum;
  	 } else {
  	 	return -1;
  	 }
  
}

int MultipleMatrix::setElement(int* coordinates, void* element) {	
	for(int pont_index = 0; pont_index < numberOfMatrixes; pont_index++) {
		if(coordinates[pont_index] > lengths[pont_index]) {
			return INPUT_OUT_OF_BOUNDS;
		}
	}
	
	int pos = getPos(coordinates);
	if (pos >= 0) {
		matrix[pos] = element;
	}
	
	return SUCCESSFUL_OPERATION;
}

int MultipleMatrix::getElement(void** element, int* coordinates) {
	for(int pont_index = 0; pont_index < numberOfMatrixes; pont_index++) {
		if(coordinates[pont_index] > lengths[pont_index]) {
			return INPUT_OUT_OF_BOUNDS;
		}
	}
	
	int pos = getPos(coordinates);
	if (pos >= 0) {	
		(*object) = matrix[pos];
	} else {
		return INPUT_OUT_OF_BOUNDS;
	}
		
}