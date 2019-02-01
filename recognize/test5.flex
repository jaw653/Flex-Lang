/**
 * Author: Jake Wachs
 * YATF
 */
bundle "one.flex"
bundle "anotha.flex"

define class myClass
{
	var a;

	define function another()
	{
		s = a;
	}
}

define function main()
{
	var a = new _myClass()_;
	a.another()
}

