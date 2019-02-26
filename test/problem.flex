/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function run()
{
	var argCount = getArgCount();

	if (argCount < 2)
	{
		print("incorrect number of command line args");
	}
	else if (argCount >= 2)
	{
		var filename = getArg(1);
		print(filename);

		var fp = openReadFile(filename);
		
		// therefore need an end of file function?

		var total = 0;
		while (fileEnd() == 0)		//While more ints to read
		{
			var currInt = readInt(fp);
			total = total + currInt;
		}

		print("total is: ");
		print(total);
	}
}
