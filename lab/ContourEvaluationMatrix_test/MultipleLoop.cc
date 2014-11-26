
#include "MultipleLoop.h"

MultipleLoop::MultipleLoop(int** loopLimits, int dim) {
	numberOfLoops = dim;

	limits = (int *) calloc(numberOfLoops, sizeof(int));
	pt1 = (int *) calloc(numberOfLoops, sizeof(int));

	loopsize = 1;

	for (int pont = 0; pont < numberOfLoops; pont++) {
		int* tmpPoint = loopLimits[pont];
		pt1[pont] = tmpPoint[0];
		int pt2 = tmpPoint[1];


		limits[pont] = pt2 - pt1[pont] + 1;
		loopsize *= limits[pont];
	}
}
  
MultipleLoop::~MultipleLoop(void) {
	free(limits);
	free(pt1);
}
    
int MultipleLoop::getLoopSize() {
	return loopsize;
}

int* MultipleLoop::getIndex(int pos) {
	int temp = pos;
	int *index = (int *) calloc(numberOfLoops, sizeof(int));

	for(int pont = 0; pont < numberOfLoops; pont++){
		index[pont] = (temp % limits[pont]) + pt1[pont];
		temp = temp / limits[pont];
	}

	return index;
}