#!/bin/bash

__pwd=`dirname $0`

cd ${__pwd}
java -jar target/MonteCarlo.jar
