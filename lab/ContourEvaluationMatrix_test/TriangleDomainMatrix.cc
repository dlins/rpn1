#include "TriangleDomainMatrix.h"

TriangleDomainMatrix::TriangleDomainMatrix(int divisionsp) : MultipleMatrix() {
	
	int* matrixLimits = (int*) calloc(2, sizeof(int));
	divisions = divisionsp;
	
	for(int pont_dimension = 0; pont_dimension < 2; pont_dimension++) {
		matrixLimits[pont_dimension] = divisions;
	}
	
	MultipleMatrix::setMatrixLimits(matrixLimits, 2);
}

TriangleDomainMatrix::~TriangleDomainMatrix(void) {
	
}

int TriangleDomainMatrix::setElement(int* coordinates, void* element) {	
	if (coordinates[1] >= (divisions - coordinates[0])) {
		return INPUT_OUT_OF_BOUNDS;
	}
	
	int pos = getPos(coordinates);
	if (pos >= 0) {
		matrix[pos] = element;
		return SUCCESSFUL_OPERATION;
	}
	
	return INPUT_OUT_OF_BOUNDS;
}

int TriangleDomainMatrix::getElement(void** element, int* coordinates) {
	if (coordinates[1] >= (divisions - coordinates[0])) {
		return INPUT_OUT_OF_BOUNDS;
	}
	
	int pos = getPos(coordinates);
	if (pos >= 0) {	
		(*object) = matrix[pos];
		return SUCCESSFUL_OPERATION;
	} else {
		return INPUT_OUT_OF_BOUNDS;
	}		
}