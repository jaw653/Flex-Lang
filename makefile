# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Parent Makefile


OPTS = -Xlint
CLASSES = lex/Types.class lex/Lexeme.class lex/Lexer.class recognize/Recognizer.class recognize/GrammarCheck.class env/Environment.class parse/Parser.class prettyPrint/PrettyPrinter.class eval/Evaluator.class


all: $(CLASSES)

run: $(CLASSES)
	make test1
	make test2


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

eval/Evaluator.class: eval/Evaluator.java
	javac $(OPTS) eval/Evaluator.java


##### Test Cases #####
test1: $(CLASSES)
	./flex eval/test1.flex

test2: $(CLASSES)
	./flex eval/test2.flex

test3: $(CLASSES)
	./flex eval/test3.flex

test4: $(CLASSES)
	./flex eval/test4.flex

test5: $(CLASSES)
	./flex eval/test5.flex


##### Clean Command #####
clean:
	rm recognize/*.class
	rm lex/*.class
	rm env/*.class
	rm parse/*.class
	rm prettyPrint/*.class
	rm *.1
	rm *.2
	rm eval/*.class
