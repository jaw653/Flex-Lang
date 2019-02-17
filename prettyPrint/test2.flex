/**
 * Author: Jake Wachs
 *
 * YATF
 */

bundle "test1.flex"
bundle "test3.flex"
bundle "anotherFile.flex"

define class Plane
{
	var fuelLevel = 0;
	var altitude;
	var speed;

	define function setFuel(var f)
	{
		fuelLevel = f;
	}

	define function getFuel()
	{
		return fuelLevel;
	}

	define function setAlt(var a)
	{
		altitude = a;
	}

	define function getAlt()
	{
		return altitude;
	}

	define function setSpeed(var s)
	{
		speed = s;
	}

	define function getSpeed()
	{
		return speed;
	}
}

define function main()
{
	var jet1 = new _Plane()_;

	jet1.setFuel(5);
	jet1.getFuel();

	jet1.setAlt(50000);
	jet1.getFuel();

	jet1.setSpeed(500);
	jet1.getSpeed();
}
