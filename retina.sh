WORKSPACE=/home/jorodriguez/Documents/Meritoki/File/Project/Retina/Workspace/desktop-application
echo $WORKSPACE
cd $WORKSPACE
VERSION=0.7.202005
JAR=target/desktop-$VERSION.jar
java -jar $JAR $@
cd -
