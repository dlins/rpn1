#ifndef _doubleArray_h
#ifdef __GNUC__
#pragma interface
#endif
#define _doubleArray_h 1

// This may look like C code, but it is really -*- C++ -*-
/*
Copyright (C) 1988 Free Software Foundation
    written by Doug Lea (dl@rocky.oswego.edu)

This file is part of the GNU C++ Library.  This library is free
software; you can redistribute it and/or modify it under the terms of
the GNU Library General Public License as published by the Free
Software Foundation; either version 2 of the License, or (at your
option) any later version.  This library is distributed in the hope
that it will be useful, but WITHOUT ANY WARRANTY; without even the
implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU Library General Public License for more details.
You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the Free Software
Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
*/

// DELETE: #include <builtin.h>
// ADD:
typedef void (*one_arg_error_handler_t)(const char*);
typedef void (*two_arg_error_handler_t)(const char*, const char*);
// END ADD
// DELETE: #include "double.defs.h"

#ifndef _double_typedefs
#define _double_typedefs 1
typedef void (*doubleProcedure)(double );
typedef double  (*doubleMapper)(double );
typedef double  (*doubleCombiner)(double , double );
typedef int  (*doublePredicate)(double );
typedef int  (*doubleComparator)(double , double );
#endif


class doubleArray
{
protected:
  int                   len;
  double                   *s;

                        doubleArray(int l, double* d);
public:
                        doubleArray ();
                        doubleArray (int l);
                        doubleArray (int l, double  fill_value);
// CHANGE:
// OLD:                        doubleArray (doubleArray&);
                        doubleArray (const doubleArray&);
// END CHANGE
                        ~doubleArray ();

// CHANGE:
// OLD:  doubleArray &              operator = (doubleArray & a);
  doubleArray &              operator = (const doubleArray & a);
// END CHANGE
  doubleArray                at(int from = 0, int n = -1);

  int                   capacity();
  void                  resize(int newlen);

// NOTE:
// In the GNU version, operator[] has range_checking, whereas
// elem() does not; in the present code, the reverse is true.

  double&                  operator [] (int n);
// ADD:
  double                   operator [] (int n) const;
// END ADD
  double&                  elem(int n);
// ADD:
  double                   elem(int n) const;
// END ADD

  friend doubleArray         concat(doubleArray & a, doubleArray & b);
  friend doubleArray         map(doubleMapper f, doubleArray & a);
  friend doubleArray         merge(doubleArray & a, doubleArray & b, doubleComparator f);
  friend doubleArray         combine(doubleCombiner f, doubleArray & a, doubleArray & b);
  friend doubleArray         reverse(doubleArray & a);

  void                  reverse();
  void                  sort(doubleComparator f);
  void                  fill(double  val, int from = 0, int n = -1);

  void                  apply(doubleProcedure f);
  double                   reduce(doubleCombiner f, double  base);
  int                   index(double  targ);

  friend int            operator == (doubleArray& a, doubleArray& b);
  friend inline int     operator != (doubleArray& a, doubleArray& b);

  void                  error(const char* msg);
// CHANGE:
// OLD: void                  range_error();
  void                  range_error() const;
// END CHANGE
};

extern void default_doubleArray_error_handler(const char*);
extern one_arg_error_handler_t doubleArray_error_handler;

extern one_arg_error_handler_t
        set_doubleArray_error_handler(one_arg_error_handler_t f);


inline doubleArray::doubleArray()
{
  len = 0; s = 0;
}

inline doubleArray::doubleArray(int l)
{
  s = new double [len = l];
}


inline doubleArray::doubleArray(int l, double* d) :len(l), s(d) {}


inline doubleArray::~doubleArray()
{
  delete [] s;
}


inline double& doubleArray::operator [] (int n)
{
// DELETE:  if ((unsigned)n >= (unsigned)len)
// DELETE:    range_error();
  return s[n];
}

// ADD:
inline double doubleArray::operator [] (int n) const
{
// DELETE:  if ((unsigned)n >= (unsigned)len)
// DELETE:    range_error();
  return s[n];
}
// END ADD

inline double& doubleArray::elem(int n)
{
// ADD:
  if ((unsigned)n >= (unsigned)len)
    range_error();
// END ADD
  return s[n];
}

// ADD:
inline double doubleArray::elem(int n) const
{
  if ((unsigned)n >= (unsigned)len)
    range_error();
  return s[n];
}
// END ADD


inline int doubleArray::capacity()
{
  return len;
}



inline int operator != (doubleArray& a, doubleArray& b)
{
  return !(a == b);
}

#endif
