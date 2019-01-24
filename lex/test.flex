/**
 * Author: jake wachs
 */

bundle 'sup.flex'

define class
{
}

define function main(var a, var b)
{
	var a = 5;
	var b = "hello world";
	
	a = a * 5;
	a = a + 5;
	a = a - 5;
	a = a / 5;
	a = a % 3;

	if (a == 5)
	{
		// comment
		while (a ==5)
		{}
		for (var i = 0; i < 5; i++)
		{}

		i--;
	}
	else if (a == 3)
	{
	}
	else if (a < 3)
	{
	}
	else if (a > 3)
	{
	}
	else
	{
	}

	return 1;
}
