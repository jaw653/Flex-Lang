# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Parent Makefile


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
test1: $(CLASSES)
	@echo "Original file:"
	@cat prettyPrint/test1.flex
	@echo "Pretty Printed version of the original:"
	@./pp prettyPrint/test1.flex > test1.pp.1
	@cat test1.pp.1
	@echo "Pretty Printed version of the pretty printed version:"
	@./pp test1.pp.1 > test1.pp.2
	@cat test1.pp.2
	diff -s -q test1.pp.1 test1.pp.2

test2: $(CLASSES)
	@echo "Original file:"
	@cat prettyPrint/test2.flex
	@echo "Pretty Printed version of the original:"
	@./pp prettyPrint/test2.flex > test2.pp.1
	@cat test2.pp.1
	@echo "Pretty Printed version of the pretty printed version:"
	@./pp test2.pp.1 > test2.pp.2
	@cat test2.pp.2
	diff -s -q test2.pp.1 test2.pp.2

test3: $(CLASSES)
	@echo "Original file:"
	@cat prettyPrint/test3.flex
	@echo "Pretty Printed version of the original:"
	@./pp prettyPrint/test3.flex > test3.pp.1
	@cat test3.pp.1
	@echo "Pretty Printed version of the pretty printed version:"
	@./pp test3.pp.1 > test3.pp.2
	@cat test3.pp.2
	diff -s -q test3.pp.1 test3.pp.2 


##### Clean Command #####
clean:
	rm recognize/*.class
	rm lex/*.class
	rm env/*.class
	rm parse/*.class
	rm prettyPrint/*.class
	rm *.1
	rm *.2
