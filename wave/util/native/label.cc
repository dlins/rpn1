#ifdef __GNUC__
#pragma implementation
#endif
#include <string.h>
#include "label.h"
#if (__GNUC__ >= 3 \
	|| (defined(__SUNPRO_CC) && __SUNPRO_CC >= 0x520))
using std::streamoff;
using std::streampos;
#endif

int label_debugging_level = 0;

static const int BUFFER_SIZE = 1024;
static char buffer[BUFFER_SIZE];

static int
find_char(istream &is, char sought)
{
#ifndef label_NO_DEBUG
	if (label_debugging_level >= 3)
		cerr << "label(): find_char('" << sought << "'):" << endl;
#endif
	if (!is)
		return 0;

	char c;
	while (is.get(c) && !is.eof()) {
#ifndef label_NO_DEBUG
		if (label_debugging_level >= 3)
			cerr << "c = " << (int) c << " = '" << c << "'" << endl;
#endif
		if (c == sought) {
			is.putback(c);
#ifndef label_NO_DEBUG
			if (label_debugging_level >= 3)
				cerr << "found '" << c << "'" << endl;
#endif
			return 1;
		}
	}
#ifndef label_NO_DEBUG
	if (label_debugging_level >= 3)
		cerr << "failed to find '" << sought << "'" << endl;
#endif
	return 0;
}

static istream &
find_label(istream &is, const char *label)
{
	if (!is)
		return is;

#ifndef label_NO_DEBUG
	if (label_debugging_level >= 1)
		cerr << "label(): searching for '" << label << "'" << endl;
#endif

	int len = strlen(label);
	if (len == 0)
		return is;
	if (len+1 > BUFFER_SIZE) {
#ifndef label_NO_DEBUG
		if (label_debugging_level >= 1)
			cerr << "label(): strlen(label)+1 > "
				<< BUFFER_SIZE << endl;
#endif
		is.clear(ios::failbit|is.rdstate());
		return is;
	}

	streampos current_position = is.tellg();

	// first pass through istream,
	// from current position to the end

	while (find_char(is, label[0])) {
		is.get(buffer, len+1);
#ifndef label_NO_DEBUG
		if (label_debugging_level >= 2)
			cerr << "label(): get() buffer: '"
				<< buffer << "'" << endl;
#endif
		if (strncmp(buffer, label, len) == 0) {
#ifndef label_NO_DEBUG
			if (label_debugging_level >= 1)
				cerr << "label(): found it" << endl;
#endif
			return is;
		}
#ifndef label_NO_DEBUG
		else if (label_debugging_level >= 2)
			cerr << "label(): not a match" << endl;
#endif
		streamoff offset = - (int) strlen(buffer) + 1;
		if (!is.seekg(offset, ios::cur))
			return is;
	}

	// second pass through istream,
	// from beginning to the end

#ifndef label_NO_DEBUG
	if (label_debugging_level >= 1)
		cerr << "label(): second pass through file" << endl;
	if (label_debugging_level >= 2) {
		cerr << "label(): after first pass: ";
		cerr << "is.rdstate() = " << is.rdstate() << endl;
	}
#endif
	is.clear(~(ios::eofbit|ios::failbit)&is.rdstate());
#ifndef label_NO_DEBUG
	if (label_debugging_level >= 2) {
		cerr << "label(): after clearing eofbit and failbit: ";
		cerr << "is.rdstate() = " << is.rdstate() << endl;
	}
#endif
	if (!is.seekg(0, ios::beg))
		return is;
#ifndef label_NO_DEBUG
	if (label_debugging_level >= 2) {
		cerr << "label(): after seekg() to beginning: ";
		cerr << "is.rdstate() = " << is.rdstate() << endl;
	}
#endif
	while (find_char(is, label[0])) {
		is.get(buffer, len+1);
#ifndef label_NO_DEBUG
		if (label_debugging_level >= 2)
			cerr << "label(): get() buffer: '"
				<< buffer << "'" << endl;
#endif
		if (strncmp(buffer, label, len) == 0) {
#ifndef label_NO_DEBUG
			if (label_debugging_level >= 1)
				cerr << "label(): found it" << endl;
#endif
			return is;
		}
		streamoff offset = - (int) strlen(buffer) + 1;
		if (!is.seekg(offset, ios::cur))
			return is;
	}

#ifndef label_NO_DEBUG
	if (label_debugging_level >= 2) {
		cerr << "label(): after second pass: ";
		cerr << "is.rdstate() = " << is.rdstate() << endl;
	}
#endif
	is.clear(~(ios::eofbit|ios::failbit)&is.rdstate());
#ifndef label_NO_DEBUG
	if (label_debugging_level >= 2) {
		cerr << "label(): after clearing eofbit and failbit: ";
		cerr << "is.rdstate() = " << is.rdstate() << endl;
	}
#endif
	if (!is.seekg(current_position))
		return is;
#ifndef label_NO_DEBUG
	if (label_debugging_level >= 2) {
		cerr << "label(): after seekg() to current position: ";
		cerr << "is.rdstate() = " << is.rdstate() << endl;
	}
#endif
	is.clear(ios::failbit|is.rdstate());
#ifndef label_NO_DEBUG
	if (label_debugging_level >= 1)
		cerr << "label(): failed to find '" << label << "'" << endl;
#endif

	return is;
}

label_IMANIP(_CCP)
label(_CCP lab)
{
	return label_IMANIP(_CCP)(find_label, lab);
}

static istream &
expect_label(istream &is, const char *lab)
{
	// TODO: don't use label(); rather, check that next string is correct
	if (!(is >> label(lab)))
		is.clear(ios::badbit|is.rdstate());
	return is;
}

label_IMANIP(_CCP)
expect(_CCP lab)
{
	return label_IMANIP(_CCP)(expect_label, lab);
}
