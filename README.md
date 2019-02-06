# FLEX Programming Language

FLEX is a dynamically typed programming language developed with the principals of RISC in mind. The idea is that every type of definition will follow the same format so as to minimize confusion and maximize consistency.

<!--
# Make run test case correct outputs (temporarily placed here in makefile)
All proper outputs are echoed when running tests.
test1: should fail (illegal). No "function" keyword.
test2: should pass (legal).
test3: should fail (illegal). Incorrect class instantiation, no leading underscore.
test4: should pass (legal).
test5: should fail (illegal). No ending semicolon.
-->
# Usage
Find below the Usage Details for FLEX

## Defining Functions
```
define function fcName(var a, var b) {...}
```
Note that arguments are optional like any modern language

## Defining Classes
```
define class MyClass {...}
```

## Defining Variables
Note that a real definition cannot begin with a period (ie, .5 must be changed to 0.5)
```
var myVar = 5;
var myVar = 5.5;
var myVar = "hello";
var myVar = "c";
```


