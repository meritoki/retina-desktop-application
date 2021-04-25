sudo apt-get update
sudo apt-get -y install python
sudo apt-get install -y curl
curl https://bootstrap.pypa.io/pip/2.7/get-pip.py --output get-pip.py
sudo python get-pip.py
#sudo apt install -y python-pip
sudo apt-get install libpython-dev
pip install awscli
# pip install --upgrade cryptography --user
pip install -U panoptescli
sudo pip install --upgrade cryptography
sudo python -m easy_install --upgrade pyOpenSSL
# pip install -U pyopenssl
# pip install -U PyYAML
# export PY_USER_BIN=$(python -c 'import site; print(site.USER_BASE + "/bin")')
# export PATH=$PY_USER_BIN:$PATH
BASHRC=~/.bashrc
grep -qxF "export PY_USER_BIN=\$(python -c 'import site; print(site.USER_BASE + \"/bin\")')" $BASHRC || echo "export PY_USER_BIN=\$(python -c 'import site; print(site.USER_BASE + \"/bin\")')" >> $BASHRC
grep -qxF 'export PATH=$PY_USER_BIN:$PATH' $BASHRC || echo 'export PATH=$PY_USER_BIN:$PATH' >> $BASHRC
