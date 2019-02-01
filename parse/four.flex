// test function myfc() {}

bundle "test2.flex"

define class MyClass
{
	var a;

	define another()
	{
		s = a;
	}
}

define function main()
{
	var c = new _MyClass()_;
	c.fc1();
	c.myfc(1, 2, 3);
	c.another();
}
