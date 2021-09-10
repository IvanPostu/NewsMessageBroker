#!/bin/sh

ACTIVE_PROFILE="dev"

SCRIPT=`realpath -s $0`
SCRIPTPATH=`dirname $SCRIPT`
ROOT_PROJECT_DIR="$SCRIPTPATH/.."

java -jar "$ROOT_PROJECT_DIR/sender-app/target/sender-app-1.0-SNAPSHOT-jar-with-dependencies.jar"
