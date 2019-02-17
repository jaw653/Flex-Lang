# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Parent Makefile
# Does the main rule need to be 'make parser', 'make recognizer',
#	or does it not matter because I'll have a shell script for executing?

# cat the source code file before running
# make sure to have ./ before executable

OPTS = -Xlint
CLASSES = lex/Types.class lex/Lexeme.class lex/Lexer.class recognize/Recognizer.class recognize/GrammarCheck.class env/Environment.class parse/Parser.class prettyPrint/PrettyPrinter.class

all: $(CLASSES)

run: $(CLASSES)
	@echo "Running test file..."
	./pp prettyPrint/test1.flex
	./pp prettyPrint/test2.flex
	./pp prettyPrint/test3.flex

##### Classes #####
lex/Types.class: lex/Types.java
	javac $(OPTS) lex/Types.java

lex/Lexeme.class: lex/Lexeme.java
	javac $(OPTS) lex/Lexeme.java

lex/Lexer.class: lex/Lexer.java
	javac $(OPTS) lex/Lexer.java

recognize/Recognizer.class: recognize/Recognizer.java
	javac $(OPTS) recognize/Recognizer.java

recognize/GrammarCheck.class: recognize/GrammarCheck.java
	javac $(OPTS) recognize/GrammarCheck.java

env/Environment.class: env/Environment.java
	javac $(OPTS) env/Environment.java

parse/Parser.class: parse/Parser.java
	javac $(OPTS) parse/Parser.java

prettyPrint/PrettyPrinter.class: prettyPrint/PrettyPrinter.java
	javac $(OPTS) prettyPrint/PrettyPrinter.java

##### Test Cases #####
# test1: $(CLASSES)
#	@echo "Running test1, should fail"
#	@cat recognize/test1.flex
#	-./recognizer recognize/test1.flex
#	@echo "\n=========================\n"

#test2: $(CLASSES)
#	@echo "Running test2, should pass"
#	@cat recognize/test2.flex
#	-./recognizer recognize/test2.flex
#	@echo "\n=========================\n"

#test3: $(CLASSES)
#	@echo "Running test3, should fail"
#	@cat recognize/test3.flex
#	-./recognizer recognize/test3.flex
#	@echo "\n=========================\n"

#test4: $(CLASSES)
#	@echo "Running test4, should pass"
#	@cat recognize/test4.flex
#	-./recognizer recognize/test4.flex
#	@echo "\n=========================\n"

#test5: $(CLASSES)
#	@echo "Running test5, should fail"
#	@cat recognize/test5.flex
#	-./recognizer recognize/test5.flex
#	@echo "\n"

##### Clean Command #####
clean:
	rm recognize/*.class
	rm lex/*.class
	rm env/*.class
	rm parse/*.class
	rm prettyPrint/*.class
