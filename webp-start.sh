#!/bin/bash
basepath=$(cd `dirname $0`; pwd)
java -jar $basepath/webp-1.0.1.jar
open -a "/Applications/Safari.app" http://localhost:8080
#open -a "/Applications/Google Chrome.app" http://localhost:8080