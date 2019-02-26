/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function f(var x)
{
	print("recur");
	if (x <= 5)
	{
		f(x+1);
	}
}

define function run()
{
	f(1);
}
