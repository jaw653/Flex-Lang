/**
 * Author: Jake Wachs
 * YATF
 *
 * Testing the recognizer/parser for FLEX
 */

bundle "another.flex"
bundle "another1.flex"

define class Box
{
	var height;
	var width;
	var length;
	var weight;

	define function constructor(var h, var w, var l, var wt)
	{
		height = h;
		width = w;
		length = l;
		weight = wt;
	}

	define function setHeight(var h)
	{
		height = h;
	}

	define function setWidth(var w)
	{
		width = w;
	}

	define function setLength(var l)
	{
		length = l;
	}

	define function setWeight(var w)
	{
		weight = w;
	}

	define function getHeight()
	{
		return height;
	}

	define function getWidth()
	{
		return width;
	}

	define function getLength()
	{
		return length;
	}

	define function getWeight()
	{
		return weight;
	}
}

define function main()
{
	Box myBox = new Box()_;

	myBox.setHeight(5);
	myBox.setWeight(3.2);
	myBox.setWidth(4.3);
	myBox.setLength(9);

	var num;
	num = 5 + myBox.getHeight();
	num = myBox.getHeight() - 5;
	num = myBox.getHeight() / 5;
	num = 5 * myBox.getHeight();

	if (num < 5)
	{
		num += 3;
	}
	else if (num <= 5)
	{
		num -= 4;
	}
	else
	{
		num--;
		num++;
	}

	while (num > 20)
	{
		num--;
	}

	var i;
	for (i = 0; i < 5; i++)
	{
		num = num % 2;
	}
}
