SRC_FOLDER = src
TEST_FOLDER = test

all: MainClass

MainClass: ./$(SRC_FOLDER)/MainClass.java
	javac -sourcepath ./$(SRC_FOLDER) ./$(SRC_FOLDER)/MainClass.java

.PHONY: test0 test1 test2 test3

test0: MainClass
	cat ./$(TEST_FOLDER)/input0.txt | java -cp ./$(SRC_FOLDER) MainClass
	
test1: MainClass
	cat ./$(TEST_FOLDER)/input1.txt | java -cp ./$(SRC_FOLDER) MainClass

test2: MainClass
	cat ./$(TEST_FOLDER)/input2.txt | java -cp ./$(SRC_FOLDER) MainClass

test3: MainClass
	cat ./$(TEST_FOLDER)/input3.txt | java -cp ./$(SRC_FOLDER) MainClass
