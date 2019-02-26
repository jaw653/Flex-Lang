/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function run()
{
	var numArgs = getArgCount();
	var cmdArg = getArg(0);
	var arg2 = getArg(1);
//	var arg3 = getArg(2);
//	var argg = getArg(3);

	var file = openReadFile(arg2);
	var x = readInt(file);

}
