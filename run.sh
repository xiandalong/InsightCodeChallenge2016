#!/usr/bin/env bash
# This bash code is for compiling and running Java solution for Insight code challenge 2016

# I'll execute my programs, with the input directory venmo_input and output the files in the directory venmo_output
javac -classpath ./modules/json-simple-1.1.1.jar;./lib/jgrapht-core-0.9.2.jar ./src/median_degree.java
java  -classpath ./src;./modules/json-simple-1.1.1.jar;./lib/jgrapht-core-0.9.2.jar median_degree ./venmo_input/venmo-trans.txt ./venmo_output/output.txt



