/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Configuration.h
 */

#ifndef _Configuration_H
#define _Configuration_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <map>
#include <string>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
using namespace std;

class Configuration {
private:


    map * configurationMap_;

public:

   Configuration();

    void addParamter(const string &, const string &)
    void addConfiguration(const string &, const Configuration &);

    const string & getParameter(const string &, const string &);


    virtual ~Configuration();



};

#endif //! _Configuration_H
