#include "ODESolution.h"

int ODESolution::STOP_OUT_OF_BOUNDARY =1;
int ODESolution::STOP_ON_POINCARE_SECTION=2;
int ODESolution::MADE_MAXIMUM_STEP_NUMBER=0;


ODESolution::ODESolution(){
    
    
}

ODESolution::~ODESolution(){

    
    cout <<"Chamando destrutor de ODESolution"<<endl;
 coords_.clear();

}
