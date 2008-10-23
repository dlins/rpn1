/* 
 * File:   RPnPluginManager.h
 * Author: edsonlan
 *
 * Created on September 23, 2008, 6:14 PM
 */

#ifndef _RPnPluginManager_H
#define	_RPnPluginManager_H

#include "RpnPlugin.h"
#include <sys/types.h>
#include <dirent.h>
#include <dlfcn.h>
#include <iostream>
#include <map>

using std::cout;
using std::cerr;
using std::string;
using std::map;


class RPnPluginManager {
private:

    static map<string, map<string,string> * > *libMap_;
    static string * pluginDir_;
    
    static map<string,string> * accessClasses(const string);
    static void  search();
    
public:



    static void setPluginDir(const string);
    static void addClass(const string,const string, const string);
    static string getConstructorMethod(const string,const string);
  
    virtual ~RPnPluginManager();

};



#endif	/* _RPnPluginManager_H */

