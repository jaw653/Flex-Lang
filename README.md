<p align="center">
	<img width="460" height="460" src="IMG_5594.jpg">
</p>

# FLEX Programming Language

FLEX is a dynamically typed programming language developed with the principals of RISC in mind. The idea is that every type of definition will follow the same format so as to minimize confusion and maximize consistency.


# Usage
Find below the Usage Details for FLEX

## Running FLEX code
To run a Flex program, all that is required is to use the flex executable as shown below
```
./flex <progName>.flex
```

## Defining Variables
Note that a real definition cannot begin with a period (ie, .5 must be changed to 0.5)
```
var myVar = 5;
var myVar = 5.5;
var myVar = "hello";
var myVar = "c";
```

Characters are simply strings of length 1 in FLEX. Booleans are not included as they are superfluous w/ the use of 1-true, 0-false integers.

## Basic operations
FLEX comes with many built-in operations for integers and real values, including Addition, Subtraction, Multiplication, Division, Modulo, and comparisons.
These come in the exact same form as C operations:
```
var a = 5 + 6;
a * 10;
...
```

## Defining Functions
```
define function fcName(var a, var b) {...}
```
Note that arguments are optional like any modern language

### Lambda Functions
Note that there is a trailing semicolon as a lambda function is part of a statement
```
var myFc = lambda() { print("hello, world"); };
```

## Defining Classes
```
define class MyClass {...}
```

### Instantiating Classes
```
var jClass = new _MyClass()_;
```

### Accessing object methods and variables
Member access is very similar to Java:
```
print(jClass.a);
```
Method calling also resembles java
```
jClass.methodName();
```
## Conditionals
Conditionals in FLEX are exactly like that of C syntax:
```
if (x < 5)
{
	print("hello world");
}
else if (...) { ... }
```

## Iteration/Looping
A simple while loop in FLEX again follows the syntax of C
```
while (x < 5)
{
	// do something
}
```

## Comments
Comments directly follow C
```
/* Multi-line Comment here */
// Single-line Comment here
```

## Arrays
Arrays are created via a few different built-in functions:
To create an array: ```var arr = newArray(5) //where 5 is the size of the new array```
To update an array index: ```setArray(arr, 0, 12);	//set arr[0] = 12```
To get an element of an array: ```getArray(arr, 0); //return the value at arr[0]```

## Command Line Arguments
Command line arguments are implicitly included in FLEX, so there is no need to retrieve them.
To access the number of command line arguments:
```
var numArgs = getArgCount();
```
To access an argument and a given index, i:
```
var arg = getArg(i);
```

## Reading from file
You can create filepointers in FLEX just like in other languages:
```
var fp = openReadFile(<filename>);	// Opens a file for reading
```
To read integers, do the following
```
var currInt = readInt(fp);		// Assuming fp has been initialized as above
```

