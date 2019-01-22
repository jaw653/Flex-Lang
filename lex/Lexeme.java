/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexeme Class
 */

class Lexeme
{
    String type;
    String string;
    int integer;
    double real;
    char character;
    boolean bool;

    /**
     * Constructor method
     * @type The type of the new Lexeme
     */
    public Lexeme(String type)
    {
        type = this.type;
    }

    /**
     * Override constructor
     * @type The type of the new Lexeme
     * @str The string of the new Lexeme
     */
    public Lexeme(String type, Character ch)
    {
        type = this.type;
        character = ch;
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
        System.out.println(type);
    }
}
