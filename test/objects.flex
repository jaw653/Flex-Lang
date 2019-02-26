/**
 * Author: Jake Wachs
 *
 * YATF
 */

define class myClass
{
	var a = 10;
	var b = 0;
	var c = "hello world";

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
}

