SRC_FOLDER = src
CLASS_FOLDER = class

all: TimeServerClass.class TimeClientClass.class
.PHONY: execServer execClient clean

TimeServerClass.class:
	javac -classpath ./$(SRC_FOLDER) ./$(SRC_FOLDER)/TimeServerClass.java -d ./$(CLASS_FOLDER)
	
TimeClientClass.class:
	javac -classpath ./$(SRC_FOLDER) ./$(SRC_FOLDER)/TimeClientClass.java -d ./$(CLASS_FOLDER)
	
execServer:
	java -classpath ./$(CLASS_FOLDER) TimeServerClass 239.255.1.3 6789
	
execClient:
	java -classpath ./$(CLASS_FOLDER) TimeClientClass 239.255.1.3 6789
	
clean:
	-rm ./$(CLASS_FOLDER)/*.class
