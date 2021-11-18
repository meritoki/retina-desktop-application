/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
brew install python@2
brew install wget
wget https://bootstrap.pypa.io/pip/2.7/get-pip.py
sudo python get-pip.py
sudo -H pip install awscli
sudo -H pip install -U panoptescli
sudo -H pip install --upgrade cryptography
sudo python -m easy_install --upgrade pyOpenSSL
