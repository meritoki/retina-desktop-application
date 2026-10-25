WORKSPACE=$(pwd)
echo WORKSPACE $WORKSPACE
cp retina.src.sh retina.sh
echo "WORKSPACE=${WORKSPACE}" | cat - retina.sh > /tmp/out && mv /tmp/out retina.sh
chmod +x retina.sh
sudo rm -f /usr/local/bin/retina
#exit 0
sudo ln -s $WORKSPACE/retina.sh /usr/local/bin/retina
