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
}










/* This is what works */
/*
define function node(var x)
{
	var a = 0;
	return this;
}


define function run()
{
//	var a;
	var n = node(5);
	n.a = 20;
//	var box = new _Box()_;
}
*/
