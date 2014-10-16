/* 
 * File:   WaveCurveConfig.cpp
 * Author: edsonlan
 * 
 * Created on October 13, 2014, 1:09 PM
 */

#include "WaveCurveConfig.h"

WaveCurveConfig::WaveCurveConfig(const vector<string> & caseNames,const vector<int> & caseFlags): casesFlagVector_(new vector<int>(caseFlags)),
        casesNamesVector_(new vector<string>(caseNames)) {
    
    
}


int WaveCurveConfig::flag(const string & caseName){
 
    for (int i = 0; i < casesNamesVector_->size(); i++) {
       
        if(casesNamesVector_->at(i).compare(caseName)==0)
            
            return casesFlagVector_->at(i);

    }
      
}


const vector<string> & WaveCurveConfig::getNames(){
    
    
    return *casesNamesVector_;
    
    
    
    
}




WaveCurveConfig::WaveCurveConfig(const WaveCurveConfig& orig) {
}

WaveCurveConfig::~WaveCurveConfig() {
    
    delete casesFlagVector_;
    delete casesNamesVector_;
}

