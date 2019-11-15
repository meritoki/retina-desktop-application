WORKSPACE=/home/jorodriguez/Documents/Meritoki/File/Project/Retina/Workspace/desktop-application
echo $WORKSPACE
cd $WORKSPACE
VERSION=0.4.201911
JAR=target/desktop-$VERSION.jar
java -jar $JAR $@
cd -
