/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexeme Class
 */

 // need to create a constructor for taking in reals, ints, bools - not just tokens
 // is it ok that I got rid of BOOLEAN from my grammar and here b/c I realized it could be done with an int?


class Lexeme
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
        type = this.type;
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
        System.out.println(this.type);
    }
}
