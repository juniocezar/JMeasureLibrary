javac -d build lac/*
cd build
jar -cvf lib.jar lac
~/shape.Android/build-tools/27.0.3/dx --dex --output=classes.dex lib.jar
~/shape.Android/build-tools/27.0.3/aapt add androidlib.jar classes.dex
