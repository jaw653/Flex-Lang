/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexeme Class
 */

 // need to create a constructor for taking in reals, ints, bools - not just tokens
 // is it ok that I got rid of BOOLEAN from my grammar and here b/c I realized it could be done with an int?
 // do I need to set the other types to null or can I just left unset in overloaded constructors?



class Lexeme implements Types
{
    String type;
    String sval;
    int ival;
    double rval;
    char cval;

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
     * Basic getter function for type field
     * @return The type field of the Lexeme
     */
    public String getType()
    {
        return type;
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
            System.out.println(this.type + " " + this.ival);
        }
        else if (this.type == REAL)
        {
            System.out.println(this.type + " " + this.rval);
        }
        else if (this.type == CHARACTER)
        {
            System.out.println(this.type + " " + this.cval);
        }
        else
            System.out.println(this.type);
    }
}
