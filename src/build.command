#! /bin/sh

cd "`dirname "$0"`"

if
    javac -classpath /Applications/Processing.app/Contents/Resources/Java/core.jar -source 5 -target 5 -d . *.java
then
    jar -cvf ../library/fvlib.jar volatileprototypes/fvlib/
    rm -rf volatileprototypes/
    sh ./generatedoc.command
fi