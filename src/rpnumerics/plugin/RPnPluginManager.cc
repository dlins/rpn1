#include "RPnPluginManager.h"

map<string, map<string, string> * > * RPnPluginManager:: libMap_=NULL;
string * RPnPluginManager::pluginDir_=new string();

RPnPluginManager::~RPnPluginManager() {
    delete pluginDir_;
    delete libMap_;

}

void RPnPluginManager::search() { //TODO Assuming file name starting with lib and with .so extension


    libMap_->clear();
    DIR * dirp = opendir(pluginDir_->c_str());
    struct dirent* dp;

    while ((dp = readdir(dirp))) {

        string fileName(dp->d_name);
        size_t found = fileName.find_last_of(".");
        string libString(fileName.substr(0, 3));
        string extensionSting(fileName.substr(found + 1));

        if (libString.compare("lib") == 0 && extensionSting.compare("so") == 0) {

            map<string, string> * mapClasses = new map <string, string > ();
            libMap_->operator[](fileName) = mapClasses;
        }
    }
    cout <<libMap_->size() << " plugin(s) found:" << "\n";

    map<string, map <string, string> *>::iterator it=libMap_->begin();
    
    while (it!=libMap_->end()){
        cout<<it->first<<"\n";
        it++;
    }


    closedir(dirp);

}

void RPnPluginManager::setPluginDir(const string pluginDir) {
    delete pluginDir_;
    delete libMap_;
    pluginDir_ = new string(pluginDir);
    libMap_ = new map<string, map<string, string> * >();
    search();

}


map<string, string> * RPnPluginManager::accessClasses(const string libName) {

    map<string, map <string, string> *>::iterator it;

    it = libMap_->find(libName);

    if (it == libMap_->end()) {

        cout << "Library " << libName << " not found" << "\n";
        return NULL;


    } else {
        return it->second;
    }


}

void RPnPluginManager::addClass(const string libName, const string className, const string constructorMethod) {

    map<string, string> * classesMap = accessClasses(libName);
    classesMap->operator[](className) = constructorMethod;
    cout << "Quantidade de classes: " << classesMap->size() << "\n";

}

string RPnPluginManager::getConstructorMethod(const string libName, const string className) {

    map<string, string> * classesMap = accessClasses(libName);

    map<string, string>::iterator it;

    it = classesMap->find(className);

    if (it == classesMap->end()) {

        cout << "Class " << className << " not found" << "in "<<libName<<" library" "\n";
        return NULL;


    } else {

        return it->second;

    }

}
