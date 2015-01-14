#include <fstream>
#include <iostream>

int main(int argc, const char *argv[]){
    if (argc < 2) return -1;

    std::ifstream in(argv[1]);

    while (!in.eof()){
        std::string s;
//        in >> s;
        std::getline(in, s);

        std::cout << "Line: \"" << s << "\"" << std::endl;
    }

    in.close();

    return 0;
}


