#ifdef __GNUC__
#pragma implementation
#endif
#include <iostream>
#include "Vector.h"

Vector &
Vector::operator*=(double s)
{
#ifdef Vector_DEBUG
	cerr << "Vector::operator*=(double) called\n";
#endif
	double *p = coords;
	double *end = coords + size();
	while (p < end)
		*p++ *= s;
	return *this;
}

Vector &
Vector::operator+=(const Vector &b)
{
#ifdef Vector_DEBUG
	cerr << "Vector::operator+=(const Vector &) called\n";
#endif
#ifndef Vector_NO_CHECKING
	if (b.size() != size())
		THROW(Vector::SizeMismatch());
#endif
	double *p = coords;
	double *end = coords + size();
	double *r = b.coords;
	while (p < end)
		*p++ += *r++;
	return *this;
}

Vector &
Vector::operator-=(const Vector &b)
{
#ifdef Vector_DEBUG
	cerr << "Vector::operator-=(const Vector &) called\n";
#endif
#ifndef Vector_NO_CHECKING
	if (b.size() != size())
		THROW(Vector::SizeMismatch());
#endif
	double *p = coords;
	double *end = coords + size();
	double *r = b.coords;
	while (p < end)
		*p++ -= *r++;
	return *this;
}

istream &
operator>>(istream &is, Vector &vector)
{
	for (int i = 0; i < vector.size(); i++)
		is >> vector(i);
	return is;
}

ostream &
operator<<(ostream &os, const Vector &vector)
{
	if (vector.size() == 0) {
		os << "zero-length Vector\n";
	}
	else {
		os << vector(0);
		for (int i = 1; i < vector.size(); i++)
			os << " " << vector(i);
	}
	return os;
}
