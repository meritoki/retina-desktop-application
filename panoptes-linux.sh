sudo apt-get update
sudo apt-get -y install python3
sudo apt-get install python3-pip -y
sudo apt-get install libpython3-dev -y
pip3 install awscli
pip3 install -U panoptescli
sudo pip3 install --upgrade cryptography
sudo python3 -m easy_install --upgrade pyOpenSSL
BASHRC=~/.bashrc
grep -qxF "export PY_USER_BIN=\$(python3 -c 'import site; print(site.USER_BASE + \"/bin\")')" $BASHRC || echo "export PY_USER_BIN=\$(python3 -c 'import site; print(site.USER_BASE + \"/bin\")')" >> $BASHRC
grep -qxF 'export PATH=$PY_USER_BIN:$PATH' $BASHRC || echo 'export PATH=$PY_USER_BIN:$PATH' >> $BASHRC
