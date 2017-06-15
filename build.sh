#!/bin/bash

if [ ! -d ./bin/ ] ; then
  mkdir ./bin
fi
javac -d ./bin ./src/**.java
cd bin
jar -cfe ../Brainfuck.jar Brainfuck ./
