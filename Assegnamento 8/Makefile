SRC_FOLDER = src
JAR_FOLDER = jar
CLASS_FOLDER = class

all: MainClass.class
.PHONY: exec clean

MainClass.class:
	javac -classpath ./$(SRC_FOLDER):./$(JAR_FOLDER)/jackson-databind-2.9.7.jar:./$(JAR_FOLDER)/jackson-core-2.9.7.jar:./$(JAR_FOLDER)/jackson-annotations-2.9.7.jar ./$(SRC_FOLDER)/MainClass.java -d ./$(CLASS_FOLDER)
	
exec:
	java -classpath ./$(CLASS_FOLDER):./$(JAR_FOLDER)/jackson-databind-2.9.7.jar:./$(JAR_FOLDER)/jackson-core-2.9.7.jar:./$(JAR_FOLDER)/jackson-annotations-2.9.7.jar MainClass
	
clean:
	-rm ./$(CLASS_FOLDER)/*.class
	-rm output.json
