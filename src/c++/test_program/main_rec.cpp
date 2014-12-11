#include <iostream>
#include <vector>
#include "RealVector.h"

void subdivide(const RealVector &pmin, 
               const RealVector &pmax,
               const std::vector<int> &subdivision,
               std::vector<RealVector> &point){

    int size = pmin.size();
    double delta = (pmax(0) - pmin(0))/(double)(subdivision[0] - 1);
    RealVector temp(size);

    if (size == 1){
        for (int i = 0; i < subdivision[0]; i++) {
            temp(0) = pmin(0) + delta*(double)i;
            point.push_back(temp);
        }
    }
    else {
        RealVector temp_pmin(size - 1), temp_pmax(size - 1);
        std::vector<int> temp_subdivision(size - 1);
        for (int i = 0; i < temp_pmin.size(); i++){
            temp_pmin(i) = pmin(i + 1);
            temp_pmax(i) = pmax(i + 1);
            temp_subdivision[i] = subdivision[i + 1];
        }

        std::vector<RealVector> temp_point;
        subdivide(temp_pmin, temp_pmax, temp_subdivision, temp_point);

        for (int i = 0; i < subdivision[0]; i++) {
            temp(0) = pmin(0) + delta*(double)i;

            for (int j = 0; j < temp_point.size(); j++){
                for (int k = 0; k < size - 1; k++) temp(k + 1) = temp_point[j](k);
                point.push_back(temp);
            }
        }
    }

    return;
}

int main(){
    int n = 3;

    RealVector pmin(n), pmax(n);
    std::vector<int> subdivision(n);

    for (int i = 0; i < n; i++){
        pmin(i) = (double)i;
        subdivision[i] = i + 3;
    }

    pmax = pmin + 1.0;
    
    std::vector<RealVector> point;
    subdivide(pmin, pmax, subdivision, point);

    for (int i = 0; i < point.size(); i++) std::cout << "point[" << i << "] = " << point[i] << std::endl;

    return 0;
}

