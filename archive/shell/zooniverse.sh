  # echo -e "TEST\dedalo2018\https://www.zooniverse.org" | panoptes configure
# printf "%s\n" TEST dedalo2018 https://www.zooniverse.org | panoptes configure
cp config.yml ~/.panoptes/

# panoptes project create "Test" "Testing CLI Interface"
panoptes project modify --help
