# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Lexical Module Makefile

# Still need to add 'make run' functionality

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

test5: $(CLASSES)

##### Clean target #####
clean:
	rm *.class
