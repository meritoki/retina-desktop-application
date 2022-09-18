sudo apt-get update
sudo apt-get -y install python
wget https://bootstrap.pypa.io/pip/2.7/get-pip.py
sudo python get-pip.py
sudo apt-get install libpython-dev
pip install awscli
pip install -U panoptescli
sudo pip install --upgrade cryptography
sudo python -m easy_install --upgrade pyOpenSSL
BASHRC=~/.bashrc
grep -qxF "export PY_USER_BIN=\$(python -c 'import site; print(site.USER_BASE + \"/bin\")')" $BASHRC || echo "export PY_USER_BIN=\$(python -c 'import site; print(site.USER_BASE + \"/bin\")')" >> $BASHRC
grep -qxF 'export PATH=$PY_USER_BIN:$PATH' $BASHRC || echo 'export PATH=$PY_USER_BIN:$PATH' >> $BASHRC
