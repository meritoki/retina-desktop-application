mvn deploy:deploy-file -DgroupId=com.meritoki.retina.application.desktop \
    -DartifactId=desktop \
    -Dversion=0.0.201910 \
    -Dpackaging=jar \
    -Dfile=target/desktop-0.0.201910.jar \
    -DrepositoryId=retina \
    -DgeneratePom=false \
    -Durl=http://localhost:8083/repository/retina/
