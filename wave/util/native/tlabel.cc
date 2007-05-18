#include <iostream>
#include <fstream.h>
#include "label.h"

int
read_int(istream &is, const char *search_label, int default_value)
{
	is >> label(search_label);
#ifndef label_NO_DEBUG
	cerr << "read_int(): after label(): ";
	cerr << "is.rdstate() = " << is.rdstate() << endl;
#endif
	if (!is) {	// search_label not found
		is.clear();
		return default_value;
	}

	int i;
	is >> i;
#ifndef label_NO_DEBUG
	cerr << "read_int(): after insertion of int: ";
	cerr << "is.rdstate() = " << is.rdstate()
		<< ", i = " << i << endl;
#endif
	return i;
}

int
main(int argc, char **argv)
{
	label_debugging_level = 3;

	ifstream ifs;
	if (argc >= 2) {
		ifs.open(argv[1]);
// OLD:		cin = ifs;
		(void) cin.rdbuf(ifs.rdbuf());
	}

	ofstream ofs;
	if (argc >= 3) {
		ofs.open(argv[2]);
// OLD:		cout = ofs;
		(void) cout.rdbuf(ofs.rdbuf());
	}

	ofstream efs;
	if (argc >= 4) {
		efs.open(argv[3]);
// OLD:		cerr = efs;
		(void) cerr.rdbuf(efs.rdbuf());
	}

	int i;

	i = read_int(cin, "label 1: ", -11);
	cout << "label 1: " << i << endl;

	i = read_int(cin, "label 2: ", -22);
	cout << "label 2: " << i << endl;

	i = read_int(cin, "label 3: ", -33);
	cout << "label 3: " << i << endl;

	i = read_int(cin, "label 4: ", -44);
	cout << "label 4: " << i << endl;

	return 0;
}
