# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Lexical Module Makefile


OPTS = -Xlint
CLASSES = Lexeme.class Lexer.class Scanner.class

##### Main scanner target #####
scanner: $(CLASSES)

Lexeme.class: Lexeme.java
	javac $(OPTS) Lexeme.java

Lexer.class: Lexer.java
	javac $(OPTS) Lexer.java

Scanner.class: Scanner.java
	javac $(OPTS) Scanner.java

##### Test targets #####
test1: $(CLASSES)
	scanner test.flex

test2: $(CLASSES)
	scanner test2.flex

test3: $(CLASSES)
	scanner test3.flex

test4: $(CLASSES)
	scanner test4.flex

test5: $(CLASSES)
	scanner test5.flex

##### Run targets #####
run: $(CLASSES)
	make test1
	make test2
	make test3
	make test4
	make test5

##### Clean target #####
clean:
	rm *.class
