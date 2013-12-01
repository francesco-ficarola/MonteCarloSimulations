#!/bin/bash

__pwd=`dirname $0`

if [[ "$1" == "--debug" ]]; then
    shift
    __debug="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
fi

cd ${__pwd}
java ${__debug} -jar target/MonteCarlo.jar $@
