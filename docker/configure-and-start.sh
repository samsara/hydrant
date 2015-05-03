#!/bin/bash


export CONFIG_FILE=/opt/hydrant/config/hydrant.config

#Uncomment the data sources that we are using
#E.G environment variable USE_TWITTER_SRC will uncomment all lines that begin
#with ;;USE_TWITTER_SRC
perl -pe 's/^;;([A-Za-z0-9_]+) /defined $ENV{$1} ? "":  $&/eg' < ${CONFIG_FILE}.tmpl > ${CONFIG_FILE}.tmpl.rightsources


# replace variables in template with environment values
echo "TEMPLATE: generating configuation."
perl -pe 's/%%([A-Za-z0-9_]+)%%/defined $ENV{$1} ? $ENV{$1} : $&/eg' < ${CONFIG_FILE}.tmpl.rightsources > $CONFIG_FILE

# check if all UNcommented properties have been replaced, commented properties are ignored
if grep -qoP '^[^;]+%%[^%]+%%' $CONFIG_FILE ; then
    echo "ERROR: Not all variable have been resolved,"
    echo "       please set the following variables in your environment:"
    grep -oP '%%[^%]+%%' $CONFIG_FILE | sed 's/%//g' | sort -u
    set
    exit 1
fi


exec /usr/bin/supervisord -c /etc/supervisor/supervisord.conf
