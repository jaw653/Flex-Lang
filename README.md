<p align="center">
	<img width="460" height="460" src="IMG_5594.jpg">
</p>

# FLEX Programming Language

FLEX is a dynamically typed programming language developed with the principals of RISC in mind. The idea is that every type of definition will follow the same format so as to minimize confusion and maximize consistency.

# Pretty Print Make Run Outputs:
## Test1 Output
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

##Test2 Output
Original file:
/**
 * Author: Jake Wachs
 *
 * YATF
 */

bundle "test1.flex"
bundle "test3.flex"
bundle "anotherFile.flex"

define class Plane
{
	var fuelLevel = 0;
	var altitude;
	var speed;

	define function setFuel(var f)
	{
		fuelLevel = f;
	}

	define function getFuel()
	{
		return fuelLevel;
	}

	define function setAlt(var a)
	{
		altitude = a;
	}

	define function getAlt()
	{
		return altitude;
	}

	define function setSpeed(var s)
	{
		speed = s;
	}

	define function getSpeed()
	{
		return speed;
	}
}

define function main()
{
	var jet1 = new _Plane()_;

	jet1.setFuel(5);
	jet1.getFuel();

	jet1.setAlt(50000);
	jet1.getFuel();

	jet1.setSpeed(500);
	jet1.getSpeed();
}
Pretty Printed version of the original:
bundle "test1.flex"
bundle "test3.flex"
bundle "anotherFile.flex"
define class Plane
{
var fuelLevel = 0;
define function setFuel(var f)
{
fuelLevel = f;
}

define function getFuel()
{
return fuelLevel;
}

define function setAlt(var a)
{
altitude = a;
}

define function getAlt()
{
return altitude;
}

define function setSpeed(var s)
{
speed = s;
}

define function getSpeed()
{
return speed;
}

}

define function main()
{
var jet1 = new _Plane()_;
jet1.setFuel(5);
jet1.getFuel();
jet1.setAlt(50000);
jet1.getFuel();
jet1.setSpeed(500);
jet1.getSpeed();
}

Pretty Printed version of the pretty printed version:
bundle "test1.flex"
bundle "test3.flex"
bundle "anotherFile.flex"
define class Plane
{
var fuelLevel = 0;
define function setFuel(var f)
{
fuelLevel = f;
}

define function getFuel()
{
return fuelLevel;
}

define function setAlt(var a)
{
altitude = a;
}

define function getAlt()
{
return altitude;
}

define function setSpeed(var s)
{
speed = s;
}

define function getSpeed()
{
return speed;
}

}

define function main()
{
var jet1 = new _Plane()_;
jet1.setFuel(5);
jet1.getFuel();
jet1.setAlt(50000);
jet1.getFuel();
jet1.setSpeed(500);
jet1.getSpeed();
}

diff -s -q test2.pp.1 test2.pp.2
Files test2.pp.1 and test2.pp.2 are identical

##Test3 Output
Original file:
define function test()
{
	thing = x;
}
Pretty Printed version of the original:
define function test()
{
thing = x;
}

Pretty Printed version of the pretty printed version:
define function test()
{
thing = x;
}

diff -s -q test3.pp.1 test3.pp.2 
Files test3.pp.1 and test3.pp.2 are identical


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


