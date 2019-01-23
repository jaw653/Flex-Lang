# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Lexical Module Makefile

# Still need to add 'make run' functionality

OPTS = -Xlint
CLASSES = Lexeme.class Lexer.class Scanner.class

all: $(CLASSES)

Lexeme.class: Lexeme.java
	javac $(OPTS) Lexeme.java

Lexer.class: Lexer.java
	javac $(OPTS) Lexer.java

Scanner.class: Scanner.java
	javac $(OPTS) Scanner.java

clean:
	rm *.class
