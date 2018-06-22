#!/bin/bash
basepath=$(cd `dirname $0`; pwd)
java -jar $basepath/webp-1.0.2.jar
open http://localhost:8080