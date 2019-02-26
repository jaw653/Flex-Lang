# Author: Jake Wachs
# CS 403, Programming Languages
# The University of Alabama
#
# Parent Makefile


OPTS = -Xlint
CLASSES = lex/Types.class lex/Lexeme.class lex/Lexer.class recognize/Recognizer.class recognize/GrammarCheck.class env/Environment.class parse/Parser.class prettyPrint/PrettyPrinter.class eval/Evaluator.class


all: $(CLASSES)

run: $(CLASSES)
	make error1
	make error1x
	@echo ""
	make error2
	make error2x
	@echo ""
	make error3
	make error3x
	@echo ""
	make error4
	make error4x
	@echo ""
	make error5
	make error5x
	@echo ""
	make arrays
	make arraysx
	@echo ""
	make conditionals
	make conditionalsx
	@echo ""
	make recursion
	make recursionx
	@echo ""
	make iteration
	make iterationx
	@echo ""
	make functions
	make functionsx
	@echo ""
	make lambda
	make lambdax
	@echo ""
	make objects
	make objectsx
	@echo ""
	make problem
	make problemx


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

##### Lusth Test Cases #####
error1:
	cat test/error1.flex
error1x: $(CLASSES)
	-./flex test/error1.flex

error2:
	cat test/error2.flex
error2x: $(CLASSES)
	-./flex test/error2.flex

error3:
	cat test/error3.flex
error3x: $(CLASSES)
	-./flex test/error3.flex

error4:
	cat test/error4.flex
error4x: $(CLASSES)
	-./flex test/error4.flex

error5:
	cat test/error5.flex
error5x: $(CLASSES)
	-./flex test/error5.flex

arrays:
	cat test/arrays.flex
arraysx: $(CLASSES)
	./flex test/arrays.flex

conditionals:
	cat test/conditionals.flex
conditionalsx: $(CLASSES)
	./flex test/conditionals.flex

recursion:
	cat test/recursion.flex
recursionx: $(CLASSES)
	./flex test/recursion.flex

iteration:
	cat test/iteration.flex
iterationx: $(CLASSES)
	./flex test/iteration.flex

functions:
	cat test/functions.flex
functionsx: $(CLASSES)
	./flex test/functions.flex

lambda:
	cat test/lambda.flex
lambdax: $(CLASSES)
	./flex test/lambda.flex

objects:
	cat test/objects.flex
objectsx: $(CLASSES)
	./flex test/objects.flex

problem:
	cat test/problem.flex
	cat test/ints.txt
problemx: $(CLASSES)
	./flex test/problem.flex test/ints.txt


##### Personal Test Cases #####
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

test6: $(CLASSES)
	./flex eval/test6.flex

test7: $(CLASSES)
	./flex eval/test7.flex sample.txt

test8: $(CLASSES)
	./flex eval/test8.flex

test9: $(CLASSES)
	./flex eval/test9.flex

test10: $(CLASSES)
	./flex eval/test10.flex

##### Clean Command #####
clean:
	-rm recognize/*.class
	rm lex/*.class
	rm env/*.class
	rm parse/*.class
	rm prettyPrint/*.class
	rm eval/*.class
