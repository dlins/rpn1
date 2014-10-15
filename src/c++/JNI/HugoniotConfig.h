/* 
 * File:   HugoniotConfig.h
 * Author: edsonlan
 *
 * Created on October 3, 2014, 3:36 PM
 */

#ifndef HUGONIOTCONFIG_H
#define	HUGONIOTCONFIG_H

#include <string>
#include <vector>
#include <iostream>

using namespace std;

class HugoniotConfig {
public:
  
    HugoniotConfig(const HugoniotConfig& orig);
    
    HugoniotConfig(const string & name,const vector<int> & caseIndices, const vector<string> &);
    
    
    virtual ~HugoniotConfig();
    
    string *  getName();
    
    vector<string> * getCaseNames();
    
    
     int getCase(const string & );
    
private:
    
    string * name_;
    vector<int> * casesIndexVector_;
    vector<string> * casesNamesVector_;
    

};

#endif	/* HUGONIOTCONFIG_H */

