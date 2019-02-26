/**
 * Author: Jake Wachs
 *
 * YATF
 */

define function run()
{
	var arr = newArray(5);
	var val = setArray(arr, 0, 3);
	setArray(arr, 1, 100);
	setArray(arr, 2, 12);
	setArray(arr, 3, 4);
	setArray(arr, 4, 7);
	
	var x = getArray(arr, 0);
	var y = getArray(arr, 1);
	var z = getArray(arr, 2);
	var a = getArray(arr, 3);
	var b = getArray(arr, 4);

	print(x);
	print(y);
	print(z);
	print(a);
	print(b);
}
