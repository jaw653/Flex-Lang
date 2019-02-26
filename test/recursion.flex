/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function f(var x)
{
	print("recur f with x of value: ");
	print(x);

	if (x <= 5)
	{
		f(x+1);
	}
}

define function run()
{
	f(1);
}
