/* 
 * File:   WaveCurveConfig.h
 * Author: edsonlan
 *
 * Created on October 13, 2014, 1:09 PM
 */

#ifndef WAVECURVECONFIG_H
#define	WAVECURVECONFIG_H

#include <vector>
#include <string>
using namespace std;


class WaveCurveConfig {
public:
    WaveCurveConfig(const vector<string> & caseNames,const vector<int> & caseFlags);
    WaveCurveConfig(const WaveCurveConfig& orig);
    
    int flag(const string &);
    
    
    const vector<string> & getNames();
    
    
    virtual ~WaveCurveConfig();
private:

    
     vector<int> * casesFlagVector_;
    vector<string> * casesNamesVector_;
};

#endif	/* WAVECURVECONFIG_H */

