/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Space.h
 **/

#ifndef _Space_H
#define	_Space_H

//! Definition of class Space.
/*!
    
TODO:
NOTE :
    
@ingroup rpnumerics
 */

class Space {

private :
	const char * name_;
	int dim_;

public:
	Space(void);
	Space(const char * name, const int dim);
	~Space(void);
	
	const char * name(void) const;
	const int dim(void) const;
	void name(const char * name);
	void dim(const int dim);

};

inline Space::Space():name_("Space"),dim_(2) { }

inline Space::Space(const char * name, const int dim)
	: name_(name),
	  dim_(dim)
{
}

inline Space::~Space() { }

inline const char * Space::name() const { return name_; }

inline const int  Space::dim() const { return dim_; }

inline void Space::name(const char * name) { name_ = name; }

inline void Space::dim(const int dim) { dim_ = dim; }

#endif	/* _Space_H */
