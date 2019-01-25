/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * Grammar program
 */


bundle "example.h"
bundle "file.h"


define class plane
{
    var remainingFuel;
    var speed;

    define function getFuel()
    {
        return remainingFuel;
    }

    define function setFuel(x)
    {
        remainingFuel = x;
    }
}


define function sayHi()
{
}


define function manyArgs(a, b, c)
{
}


define function main()
{
    var a = 5;
    var b = 10;
    var c = 5.5;
    var d;
    var myBool = false;
    var f = "hello world";

    var sum = a + b;
    var product = sum * b;
    var difference = product - 3;
    var dividend = difference / 4;
    a--;

    if (dividend > 2019)
    {
        while (dividend > 2019)
        {
            sum = (sum + 50);
            dividend = dividend / 10;
        }

        if (a <= b)
        {
        }

        else if (a >= b)
        {
        }

        var x = b % 3;
        x += 1;
        x -= 1;
    }
    else
    {
        var i;
        for (i = 0; i < 10; i++)
        {
            sayHi();
        }
    }

    var boeing = new _plane()_;
    boeing.getFuel();

    var j = 0;
    j++;
    j--;

    return 1;
}
