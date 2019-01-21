/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexer Module
 */

// Might need to fix ASTERISK vs TIMES ???

import java.io.*;
import java.util.ArrayList;

class Lexer
{
    /**
     * Helper method for skipWhiteSpace()
     * @stream The stream from which chars are being read
     * @identifier The second char in a possible comment token (either '*' or '/')
     */
    private static void skipComment(PushbackInputStream stream, Character identifier) throws IOException
    {
        try
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
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void skipWhiteSpace(PushbackInputStream stream) throws IOException
    {
        Character ch = Character.valueOf( (char)stream.read() );

        while (stream.available() > 0)
        {
            if (ch == '/')
            {
                Character next = Character.valueOf( (char)stream.read() );

                if (next == '/' || next == '*')
                {
                    skipComment(stream, next);
                }
                else
                {
                    stream.unread(next);
                    return;
                }
            }
            else if ( !(Character.isWhitespace(ch)) )
            {
                stream.unread(ch);
                return;
            }

            ch = Character.valueOf( (char)stream.read() );
        }
    }

    /**
     * Lex method for returning Lexemes
     * @file The file to read from
     * return The Lexeme of the next token
     */
    public int lex(File file) throws IOException
    {
        Character ch;
        PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));

        skipWhiteSpace(stream);

        ch = Character.valueOf( (char)stream.read() );

        // if input failed return lexeme(ENDofINPUT);

        switch (ch)
        {
            case '(':
                //return new Lexeme(OPEN_PAREN);
                break;
            case ')':
                //return new Lexeme(CLOSE_PAREN);
                break;
            case ',':
                //return new Lexeme(COMMA);
                break;
            case '+':
                //return new Lexeme(PLUS);
                break;
            case '*':
                //return new Lexeme(TIMES);
                break;
            case '-':
                //return new Lexeme(MINUS);
                break;
            case '/':
                //return new Lexeme(DIVIDE);
                break;
            case '<':
                //return new Lexeme(LESS_THAN);
                break;
            case '>':
                //return new Lexeme(GREATER_THAN);
                break;
            case '=':
                //return new Lexeme(ASSIGN);
                break;
            case ';':
                //return new Lexeme(SEMICOLON);
                break;
            case '%':
                //return new Lexeme(MODULO);
                break;

            default:
                if ( Character.isDigit(ch) )
                {
                    // pushback(ch);
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
                    // return new Lexeme(UNKNOWN, ch);
                    System.out.println("Placeholder");
        }
        return 1;
    }
}
