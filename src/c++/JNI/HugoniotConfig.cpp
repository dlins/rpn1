/* 
 * File:   HugoniotConfig.cpp
 * Author: edsonlan
 * 
 * Created on October 3, 2014, 3:36 PM
 */

#include "HugoniotConfig.h"





HugoniotConfig::HugoniotConfig(const string & name,const vector<int> & caseIndices, const vector<string> & caseNames):name_(new string(name)),casesIndexVector_(new vector<int>(caseIndices)),
        casesNamesVector_(new vector<string>(caseNames)) {


    
}

vector<string> * HugoniotConfig::getCaseNames(){
    return casesNamesVector_;
}

string * HugoniotConfig::getName(){
    return name_;
}


//void HugoniotConfig::addCase(const string caseName, const int caseIndex){
//
//    casesNamesVector_->push_back(caseName);
//    casesIndexVector_->push_back(caseIndex);
//
//   
//    
//}

int HugoniotConfig::getCase(const string & caseName){
    
    for (int i = 0; i < casesNamesVector_->size(); i++) {
        
        if(casesNamesVector_->at(i).compare(caseName)==0)
            
            return casesIndexVector_->at(i);

    }
    
    
}






HugoniotConfig::HugoniotConfig(const HugoniotConfig& orig) {
}

HugoniotConfig::~HugoniotConfig() {
    delete casesIndexVector_;
    delete casesNamesVector_;
    delete name_;
}

