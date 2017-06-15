rm ./bin/template -r
javac -d ./bin ./src/**.java
cp ./src/template/ ./bin/template/ -r
cd bin
jar -cfe ../Brainfuck.jar Brainfuck ./
