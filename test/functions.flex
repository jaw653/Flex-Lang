/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function f(var x)
{
	return lambda(var y) { return x + y; };
}

define function g(var func)
{
	func();
}

define function run()
{
	var a = f(3);
	var b = f(11);

	var q = a(5);
	var r = b(6);
	var s = a(10);

	print(q);
	print(r);
	print(s);

	g(lambda() { print("hello"); });
}

