#!/bin/sh

ACTIVE_PROFILE="dev"

SCRIPT=`realpath -s $0`
SCRIPTPATH=`dirname $SCRIPT`

($SCRIPTPATH/runReceiver.sh & ) \
  && ($SCRIPTPATH/runReceiver.sh & ) \
  && ($SCRIPTPATH/runReceiver.sh & ) \
  && ($SCRIPTPATH/runReceiver.sh & ) \
  && ($SCRIPTPATH/runSender.sh & ) \
  && ($SCRIPTPATH/runSender.sh & ) \
  && echo 'Hello world'


