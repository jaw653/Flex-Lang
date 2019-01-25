/**
 * Author: Jake Wachs
 * YATF (Yet another test file)
 */

define class Box
{
	var height;
	var width;
	var length;
	var weight;

	define function setHeight(var x)
	{
		height = x;
	}
}

define function main()
{
	var BoxA = new _Box()_;
	BoxA.setHeight(5);

	return 1;
}
