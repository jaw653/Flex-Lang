/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function run()
{
	var x = 3;
	var y = 8;

	if (x < y)
	{
		print("cond0: x is less than y!");
	}
	else if (x > y)
	{
		print("cond0: y is less than x!");		// Should not run
	}

	x = x + 10;

	if (x < y)
	{
		print("cond1: x is less than y!");		// Should not run
	}
	else if (x > y)
	{
		print("cond1: y is less than x!");
	}

}
