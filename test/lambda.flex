/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function run()
{
	var x = lambda()
			{
				print("hello, lambda function running");
				return 5;
			};
	var y = x();
	
	print("var y is: ");
	print(y);
}
