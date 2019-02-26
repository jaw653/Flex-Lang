/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function run()
{
	var i = 0;
	while (i < 5)
	{
		print(i);
		
		var j = 0;
		while (j < 3)
		{
			print("inner loop"); 	// Prints 3 "inner loops" for each outer loop num
			j = j+1;
		}

		i = i + 1;
	}
}
