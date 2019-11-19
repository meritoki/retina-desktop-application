sudo apt-get update
sudo apt-get -y install python
sudo apt install -y python-pip
# pip install --upgrade cryptography --user
pip install -U panoptescli

# pip install -U pyopenssl
# pip install -U PyYAML
# export PY_USER_BIN=$(python -c 'import site; print(site.USER_BASE + "/bin")')
# export PATH=$PY_USER_BIN:$PATH
BASHRC=~/.bashrc
grep -qxF "export PY_USER_BIN=\$(python -c 'import site; print(site.USER_BASE + \"/bin\")')" $BASHRC || echo "export PY_USER_BIN=\$(python -c 'import site; print(site.USER_BASE + \"/bin\")')" >> $BASHRC
grep -qxF 'export PATH=$PY_USER_BIN:$PATH' $BASHRC || echo 'export PATH=$PY_USER_BIN:$PATH' >> $BASHRC
