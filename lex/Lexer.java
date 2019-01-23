/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexer Module
 */

// Might need to fix ASTERISK vs TIMES ???
// skipwhitespace() needs to also account for spaces within a string like "hello world"
// should my new Lexemes() in the switch statement be "quoted" ?

import java.io.*;
import java.util.ArrayList;

class Lexer
{
    File file;
    PushbackInputStream stream;
    boolean inQuote = false;

    /**
     * Default constructor
     * @stream Input stream passed in from main
     */
    public Lexer(PushbackInputStream stream) throws IOException
    {
        this.stream = stream;
    }

    /**
     * Helper method for skipWhiteSpace()
     * @identifier The second char in a possible comment token (either '*' or '/')
     */
    private void skipComment(Character identifier) throws IOException
    {
        Character curr = Character.valueOf( (char)stream.read() );

        if (identifier == '*')
        {
            while (stream.available() > 0)
            {
                if (curr == '*')
                {
                    Character next = Character.valueOf( (char)stream.read() );

                    if (next == '/')
                        return;
                }

                curr = Character.valueOf( (char)stream.read() );
            }
        }
        else
        {
            while (stream.available() > 0)
            {
                if (curr == '\n')
                    return;

                curr = Character.valueOf( (char)stream.read() );
            }
        }
    }

    /**
     * Method to continue reading from input stream if whitespace or comment is found
     */
    private void skipWhiteSpace() throws IOException
    {
        Character ch = Character.valueOf( (char)stream.read() );

        while (stream.available() > 0)
        {
            if (ch == '/')
            {
                Character next = Character.valueOf( (char)stream.read() );

                if (next == '/' || next == '*')
                {
                    skipComment(next);
                }
                else
                {
                    stream.unread(next);
                    return;
                }
            }
            else if ( !(Character.isWhitespace(ch)) )
            {
                if (ch == '\"')
                {
                    if (inQuote)
                        inQuote = false;
                    else
                        inQuote = true;
                }

                stream.unread(ch);
                return;
            }
            else if (Character.isWhitespace(ch))
            {
                if (ch == ' ' && inQuote)
                {
                    stream.unread(ch);
                    return;
                }
            }

            ch = Character.valueOf( (char)stream.read() );
        }
    }

    /**
     * placeholder
     * @return Placeholder
     */
    private Lexeme lexNumber() throws IOException
    {
        boolean real = false;
        Character ch;
        String buffer = "";

        ch = Character.valueOf( (char)stream.read() );
        if (ch == '.')
            real = true;

        while ( stream.available() > 0 && (Character.isDigit(ch) || ch == '.') )
        {
            buffer += ch;

            if (ch == '.' && !real)
                // return new Lexeme(BADNUM, buffer);
                System.out.println("placeholder for above");

            if (ch == '.')
                real = true;

            ch = Character.valueOf( (char)stream.read() );
        }

        stream.unread(ch);
        if (real)
            //return new Lexeme(REAL, toReal(buffer));
            System.out.println("placeholder for above");
        else
            // return new Lexeme(INTEGER, toInt(buffer));
            System.out.println("placeholder for above");
    }

    /**
     * Gets next non-whitespace token from stream
     * return The Lexeme of the next token
     */
    public Lexeme lex() throws IOException
    {
        Character ch;

        skipWhiteSpace();

        ch = Character.valueOf( (char)stream.read() );
        System.out.println("ch is: " + ch);

        // if read fails return ENDofINPUT Lexeme

        switch (ch)
        {
            case '(':
                return new Lexeme("OPEN_PAREN");
            case ')':
                return new Lexeme("CLOSE_PAREN");
            case ',':
                return new Lexeme("COMMA");
            case '+':
                return new Lexeme("PLUS");
            case '*':
                return new Lexeme("TIMES");
            case '-':
                return new Lexeme("MINUS");
            case '/':
                return new Lexeme("DIVIDE");
            case '<':
                return new Lexeme("LESS_THAN");
            case '>':
                return new Lexeme("GREATER_THAN");
            case '=':
                return new Lexeme("ASSIGN");
            case ';':
                return new Lexeme("SEMICOLON");
            case '%':
                return new Lexeme("MODULO");

            default:
                if ( Character.isDigit(ch) )
                {
                    stream.unread(ch);
                    // return lexNumber();
                }
                else if ( Character.isLetter(ch) )
                {
                    // pushback(ch);
                    // return lexVariableOrKeyword();
                }
                else if (ch == '\"')
                {
                    // return lexString();
                }
                else
                    return new Lexeme("UNKNOWN", ch);
                    // System.out.println("Placeholder");
        }
        return new Lexeme("UNKNOWN", ch);                   // FIXME: might need to edit this guy. Just put him here until I could get away without a compile warning for no return statement

        // return new Lexeme("test");
    }


}
