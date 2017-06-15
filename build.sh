rm ./bin/template -r
javac -d ./bin ./src/**.java
#mkdir ./bin/template
cp ./src/template/ ./bin/template/ -r
jar -cfe Brainfuck.jar Brainfuck ./bin
