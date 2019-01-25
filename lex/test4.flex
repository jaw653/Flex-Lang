/**
 * Author: Jake Wachs
 * YATF
 */

define class LightBulb
{
	var brightness;
	var location;
	var resistance;

	define function LightBulb(var b, var l, var r)
	{
		brightness = b;
		location = l;
		resistance = r;
	}

	define function setBrightness(var b)
	{
		brightness = b;
	}
	
	define function getBrightness()
	{
		return brightness;
	}

	define function displayInfo()
	{
	}
}

define function main()
{
	/* Instantiate a new bulb object and display it */
	var lamp = new _LightBulb(5, 5, 3.2)_;
	lamp.displayInfo();

	return 1;
}
