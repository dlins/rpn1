#ifndef _label_h
#define _label_h
#ifdef __GNUC__
#pragma interface
#endif

#include <iostream>

using namespace std;


#ifdef __STDC__
#define label_IMANIP(typ)     imanip_ ## typ
#else // SUN C++ 1.0 does not have an ANSI preprocessor!?
#define label_IMANIP(typ)     imanip_/**/typ
#endif /* __STDC__ */

#define label_IOMANIPdeclare(typ)                                             \
class label_IMANIP(typ) {                                                     \
        istream& (*_fn)(istream&, typ);                                       \
        typ _ag;                                                              \
public:                                                                       \
        label_IMANIP(typ)(istream& (*_f)(istream&, typ), typ _z ) :           \
                _fn(_f), _ag(_z) { }                                          \
        friend istream& operator>>(istream& _s, const label_IMANIP(typ)& _f) {\
                return(*_f._fn)(_s, _f._ag); }                                \
        }

typedef const char *_CCP;
label_IOMANIPdeclare(_CCP);

label_IMANIP(_CCP) label(_CCP lab);
label_IMANIP(_CCP) expect(_CCP lab);

// Control macros:
//
//	label_NO_DEBUG	-- turn off all debugging

extern int label_debugging_level;	// 0, 1, 2, or 3

#endif /* _label_h */
