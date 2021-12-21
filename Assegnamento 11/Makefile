SRC_FOLDER = src
CLASS_FOLDER = class

all: ServerMainClass.class ClientMainClass.class
.PHONY: execServer execClient clean

ServerMainClass.class:
	javac -classpath ./$(SRC_FOLDER) ./$(SRC_FOLDER)/ServerMainClass.java -d ./$(CLASS_FOLDER)
	
ClientMainClass.class:
	javac -classpath ./$(SRC_FOLDER) ./$(SRC_FOLDER)/ClientMainClass.java -d ./$(CLASS_FOLDER)
	
execServer:
	java -classpath ./$(CLASS_FOLDER) ServerMainClass
	
execClient:
	java -classpath ./$(CLASS_FOLDER) ClientMainClass CONGRESS-SERVER 6789
	
clean:
	-rm ./$(CLASS_FOLDER)/*.class
