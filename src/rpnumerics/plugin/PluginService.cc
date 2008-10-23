#include "PluginService.h"

PluginService::PluginService(const string libFileName) {


    libFileName_ = new string(libFileName);

    pluginlib_ = dlopen(libFileName_->c_str(), RTLD_LAZY);
     if (!pluginlib_) {
        cerr << "Cannot load library: " << dlerror() << '\n';
     
    }
    
    
    //    libNames_ = new vector<string>;
    //    pluginsNames_ = new vector<string>;

    //    search();

}

PluginService::~PluginService() {
    delete libFileName_;
    dlclose(pluginlib_);
    //    delete pluginsNames_;
    //    delete libNames_;
}

//void PluginService::search() { //TODO Assuming file name starting with lib and with .so extension
//
//    libNames_->clear();
//    DIR * dirp = opendir(pluginsDir_->c_str());
//    struct dirent* dp;
//
//    while (dp = readdir(dirp)) {
//
//        string fileName(dp->d_name);
//        size_t found = fileName.find_last_of(".");
//        string libString(fileName.substr(0, 3));
//        string extensionSting(fileName.substr(found + 1));
//
//        if (libString.compare("lib") == 0 && extensionSting.compare("so") == 0) {
//            libNames_->push_back(string(fileName));
//        }
//    }
//
//    cout << "Plugins found:" << "\n";
//    for (int i = 0; i < libNames_->size(); i++) {
//        cout << libNames_->at(i) << "\n";
//
//    }
//
//    cout << libNames_->size() << "\n";
//    closedir(dirp);
//
//}

RpnPlugin * PluginService::load(const string createMethod) {
    cout << "Service load" << "\n";
    create_t* create_plugin = (create_t*) dlsym(pluginlib_, createMethod.c_str());
    return create_plugin();
}

void PluginService::unload(RpnPlugin * plugin) {
    cout <<"Service unload"<<"\n";
    destroy_t* destroy_plugin = (destroy_t*) dlsym(pluginlib_, "destroy");

    destroy_plugin(plugin);



}
