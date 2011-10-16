#! /bin/sh

cd "`dirname "$0"`"

mkdir ../reference/javadoc/

javadoc -d ../reference/javadoc -classpath .:/Applications/Processing.app/Contents/Resources/Java/core.jar *.java