#include "RPnPluginManager.h"

map<string, map <string, map<string, string> * > > * RPnPluginManager::configMap_ = NULL;

string * RPnPluginManager::pluginDir_ = new string();

RPnPluginManager::~RPnPluginManager() {

    delete pluginDir_;
    delete configMap_;

}

void RPnPluginManager::unload(RpnPlugin * plugin, const string pluginType) {

    map<string, map <string, map <string, string> *> >::iterator it;

    it = configMap_->find(pluginType);

    if (it == configMap_->end()) {

        cout << "Plugin " << pluginType << " not configured yet (UNLOAD) " << "\n";


    } else {

        map <string, map<string, string> * > pluginTypeMap = it->second;

        map <string, map<string, string> * >::iterator pluginMapIterator = pluginTypeMap.begin();

        string dirPath(*pluginDir_);
        
        dirPath += pluginMapIterator->first;
        PluginService service(dirPath);
        
//        cout << "Unload Lib: " << dirPath << "\n";

        service.unload(plugin);
    }
}

RpnPlugin * RPnPluginManager::getPluginInstance(const string pluginType) {

    map<string, map <string, map <string, string> *> >::iterator it;

    it = configMap_->find(pluginType);
    

    

    if (it == configMap_->end()) {

        cout << "Plugin " << pluginType << " not configured yet (LOAD) " << "\n";
        return NULL;

    } else {

        map <string, map<string, string> * > pluginTypeMap = it->second;

        map <string, map<string, string> * >::iterator pluginMapIterator = pluginTypeMap.begin();

        map <string, string> * classMap = pluginMapIterator->second;

        map <string, string> ::iterator classIterator = classMap->begin();

        string dirPath(*pluginDir_);

        dirPath += pluginMapIterator->first;

//        cout << "Load Lib: " << dirPath << "\n";
//        cout << "Load Method: " << classIterator->second<<"\n";

        PluginService service(dirPath);

        return service.load(classIterator->second);

    }
}

void RPnPluginManager::setPluginDir(const string pluginDir) {
    delete pluginDir_;
    delete configMap_;
    pluginDir_ = new string(pluginDir);
    configMap_ = new map<string, map <string, map<string, string> * > > ();


}

void RPnPluginManager::configPlugin(const string pluginType, const string libName, const string className, const string constructorMethod) {
    
    map<string, string> * classMap = new map<string, string > ();

    classMap->operator[](className) = constructorMethod;

    configMap_->erase(pluginType);

    configMap_->operator[](pluginType)[libName] = classMap;

}
