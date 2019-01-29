# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Parent Makefile

OPTS = -Xlint
CLASSES = Types.class Lexeme.class Lexer.class Recognizer.class Parser.class

recognizer: $(CLASSES)

Types.class: lex/Types.java
	javac $(OPTS) lex/Types.java

Lexeme.class: lex/Lexeme.java
	javac $(OPTS) lex/Lexeme.java

Lexer.class: lex/Lexer.java
	javac $(OPTS) lex/Lexer.java

Recognizer.class: parse/Recognizer.java
	javac $(OPTS) parse/Recognizer.java

Parser.class: parse/Parser.java
	javac $(OPTS) parse/Parser.java

clean:
	rm *.class
