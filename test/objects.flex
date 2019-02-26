/**
 * Author: Jake Wachs
 *
 * YATF
 */

define class myClass
{
	var a = 10;
	var b = 7;
	var c = "I'm a variable";

	define function tester()
	{
		print("hello, world!");
	}
}


define function run()
{
	var m = new _myClass()_;
	m.tester();
	var x = m.a;
	print(x);


	print(m.c);

	var y = m.b;
	print("m.b (and therefore y) is:");
	print(y);
}

