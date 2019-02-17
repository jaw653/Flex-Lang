# FLEX Programming Language

FLEX is a dynamically typed programming language developed with the principals of RISC in mind. The idea is that every type of definition will follow the same format so as to minimize confusion and maximize consistency.

# Pretty Print Make Run Outputs:
## Test1 Output'''
Original file:
/**
 * Author: Jake Wachs
 *
 * YATF
 */

bundle "test1.flex"

/*
define class myClass
{
	define function test(var a, var b, var c)
	{
		var x = a +5;
		x = a + b;
		x = a + b + c;
		x += 5;
	}
}
*/

define function myFunction()
{
	var x = 5;
	var y = 7;

	if (x == y)
	{}

	if (x == y)
	{
		while (x == y)
		{
			x += 1;
		}
	}

	else
	{
		var i = 0;

		for (i = 0; i < 5; i++)
		{
			x -= 3;
		}
	}

}

Pretty Printed version of the original:
bundle "test1.flex"
define function myFunction()
{
var x = 5;
var y = 7;
if (x == y)
{
}

if (x == y)
{
while (x == y)
{
x -= 1;
}

}

else 
{
var i = 0;
for (i = 0; i < 5; i++)
{
x -= 3;
}

}

}

Pretty Printed version of the pretty printed version:
bundle "test1.flex"
define function myFunction()
{
var x = 5;
var y = 7;
if (x == y)
{
}

if (x == y)
{
while (x == y)
{
x -= 1;
}

}

else 
{
var i = 0;
for (i = 0; i < 5; i++)
{
x -= 3;
}

}

}

diff -s -q test1.pp.1 test1.pp.2
Files test1.pp.1 and test1.pp.2 are identical

'''

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


