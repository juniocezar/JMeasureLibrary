#!/bin/bash

echo "Removing possible previous versions of the libraries"
rm release/jmeasure-linuxlib.jar release/jmeasure-androidlib.jar

echo ""
echo "Compiling source code"
javac -d release src/lac/*

cd release
echo ""
echo "Creating jar file"
jar -cvf jmeasure-linuxlib.jar lac

echo ""
echo "Building dex file"
~/shape.Android/build-tools/27.0.3/dx --dex --output=classes.dex jmeasure-linuxlib.jar

echo ""
echo "Generating dalvikvm jar file"
~/shape.Android/build-tools/27.0.3/aapt add jmeasure-androidlib.jar classes.dex

echo ""
echo "Removing extra files"
rm -v -r lac classes.dex


if [[ -z "$1" ]]; then
  echo ""
  echo "This script may be used to run the sample benchmark with the"
  echo "following command line: "
fi
