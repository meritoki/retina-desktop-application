echo WORKSPACE $WORKSPACE
cd $WORKSPACE
VERSION=1.0.202208
JAR=target/retina-$VERSION.jar
java -jar $JAR $@
cd -
