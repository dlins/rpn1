/* 
 * File:   RPnPluginManager.h
 * Author: edsonlan
 *
 * Created on September 23, 2008, 6:14 PM
 */

#ifndef _RPnPluginManager_H
#define	_RPnPluginManager_H

#include "RPnPlugin.h"
#include "PluginService.h"
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
    static map<string, map <string,map<string, string> * >  > *configMap_; // Representa a atual configuracao de plugins <pluginType,libname< <class,constructorMethod>>>
    static string * pluginDir_;
//    static void  search();
    
public:

    static void setPluginDir(const string);
    static void configPlugin(const string,const string,const string, const string);
    static RpnPlugin * getPluginInstance(const string);
    static void unload(RpnPlugin *, const string);
    virtual ~RPnPluginManager();

};



#endif	/* _RPnPluginManager_H */

