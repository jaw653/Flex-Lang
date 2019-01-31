// test function myfc() {}

define class MyClass
{
	var a;

	define function fc1()
	{
	}

	// next add a function with a list of variable parameters
	define function myfc(var a, var b, var c)
	{
		s = a;
		s2 = b;
		s3 = c;
	}

	define function another()
	{
	}
}

define function main()
{
	var c = new _MyClass()_;
	c.fc1();
	c.myfc(1, 2, 3);
	c.another();
}
