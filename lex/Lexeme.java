/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexeme Class
 */
package lex;

public class Lexeme implements Types
{
    String type;
    String sval;
    int ival;
    double rval;
    char cval;
    Lexeme car;
    Lexeme cdr;
	int[] arr;
	int arrSize;		// Keeps track of how many elements are in the array

    /**
     * Constructor method
     * @type The type of the new Lexeme
     */
    public Lexeme(String type)
    {
        this.type = type;
    }

    /**
     * Overloaded constructor
     * @type The type of the new Lexeme
     * @sval The string of the new Lexeme
     */
    public Lexeme(String type, String sval)
    {
        this.type = type;
        this.sval = sval;
    }

    /**
     * Overloaded constructor
     * @type The type of the new Lexeme
     * @ival The int of the new Lexeme
     */
    public Lexeme(String type, int ival)
    {
        this.type = type;
        this.ival = ival;
    }

    /**
     * Overloaded constructor
     * @type The type of the new Lexeme
     * @rval The real of the new Lexeme
     */
    public Lexeme(String type, double rval)
    {
        this.type = type;
        this.rval = rval;
    }

    /**
     * Overloaded constructor
     * @type The type of the new Lexeme
     * @cval The char of the new Lexeme
     */
    public Lexeme(String type, char cval)
    {
        this.type = type;
        this.cval = cval;
    }

	/**
	 * Overloaded constructor, allows for booleans to be passed as ints
	 * @type The type of the new Lexeme
	 * @b The boolean to be converted to int
	 */
	public Lexeme(String type, boolean b)
	{
		this.type = type;
		
		if (b == true)
			this.ival = 1;
		else
			this.ival = 0;
	}

    /**
     * Left getter method for linked list
     * @return The Lexeme to the left
     */
    public Lexeme getCar()
    {
        return car;
    }

    /**
     * Right getter method for linked list
     * @return The Lexeme to the right
     */
    public Lexeme getCdr()
    {
        return cdr;
    }

    /**
     * Left setter method for linked list
     * @l Lexeme to set to the left of the current object
     */
    public void setCar(Lexeme l)
    {
        car = l;
    }

    /**
     * Right setter method for linked list
     * @r Lexeme to set to the right of the current object
     */
    public void setCdr(Lexeme r)
    {
        cdr = r;
    }

	/**
	 * Setter method for array allocation
	 */
	public void allocateArr(int size)
	{
		arr = new int[size];
	}

    /**
     * Basic getter function for type field
     * @return The type field of the Lexeme
     */
    public String getType()
    {
        return type;
    }

    /**
     * Basic getter function for the name of an ID Lexeme
     * @return The name of an ID Lexeme
     */
    public String getName()
    {
        return sval;
    }

    /**
     * Basic getter function for the value of an int Lexeme
     * @return The value of the ival
     */
    public int getInt()
    {
        return ival;
    }

    /**
     * Basic getter function for the value of a real Lexeme
     * @return The value of the rval
     */
    public double getReal()
    {
        return rval;
    }

    /**
     * Displays Lexeme type to stdout
     */
    public void display()
    {
        if (this.type == STRING)
        {
            System.out.println(this.type + " " + this.sval);
        }
        else if (this.type == INTEGER)
        {
            System.out.print(this.type + " " + this.ival);
        }
        else if (this.type == REAL)
        {
            System.out.println(this.type + " " + this.rval);
        }
        else if (this.type == CHARACTER)
        {
            System.out.println(this.type + " " + this.cval);
        }
		else if (this.type == ID)
		{
			System.out.print(this.type + " " + this.sval);
		}
		else if (this.type == ARRAY)
		{
			System.out.print("[");
			for (int i = 0; i < this.arrSize; i++)
			{
				System.out.print(this.arr[i]);
				if (i < this.arrSize - 1)
					System.out.print(",");
			}
				
			System.out.print("]\n");
		}
		else if (this.type == V)
			System.out.print(this.type + " " + this.ival);
        else
            System.out.print(this.type);
    }
}
