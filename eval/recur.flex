/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function recur(var n)
{
	print("Recur");

	if (n == 0)
	{
		var c = n + 1;
		recur(c);
	}
	else if (n == 1)
	{
		var c = n + 1;
		recur(c);
	}
}

define function run()
{
	recur(0);
}
