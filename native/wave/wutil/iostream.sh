#!/bin/ksh

filelist=`cat /tmp/sedlist`
for filename in $filelist
do
	ls $filename
	sed -f iostream.sed $filename > /tmp/swap_$filename
	cp -f /tmp/swap_$filename $filename
done
