/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexer Module
 */

// make sure bundle uses double qoutes in grammar
// make sure you have and use all TERMINALS from grammar
// add line-knowledge for error-throwing

import java.io.*;

class Lexer implements Types
{
    File file;
    PushbackInputStream stream;
    int lineNum = 1;

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
                {
                    lineNum += 1;
                    return;
                }

                curr = Character.valueOf( (char)stream.read() );
            }
        }
    }

    /**
     * Method to continue reading from input stream
     * if whitespace or comment is found
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
                    stream.unread(ch);
                    return;
                }
            }

            else if ( !(Character.isWhitespace(ch)) )
            {
                stream.unread(ch);
                return;
            }
            else if (ch == '\n')
            {
                lineNum += 1;
            }

            ch = Character.valueOf( (char)stream.read() );
        }
    }

    /**
     * Lex specifically for numbers
     * @return Lexeme of number found
     */
    private Lexeme lexNumber() throws IOException
    {
        boolean real = false;
        Character ch;
        String buffer = "";

        ch = Character.valueOf( (char)stream.read() );
        if (ch == '.')
        {
            real = true;
        }

        while ( stream.available() > 0 && (Character.isDigit(ch) || ch == '.') )
        {
            buffer += ch;

            if (ch == '.' && !real)
            {
                return new Lexeme(BADNUM, buffer);
            }
            if (ch == '.')
            {
                real = true;
            }

            ch = Character.valueOf( (char)stream.read() );
        }

        stream.unread(ch);
        if (real)
        {
            return new Lexeme(REAL, Double.parseDouble(buffer));
        }
        else
        {
            return new Lexeme(INTEGER, Integer.parseInt(buffer));
        }
    }

    /**
     * Lex function specifically for variables and keywords
     * @return Lexeme of var or keyword found
     */
    private Lexeme lexVarOrKeyword() throws IOException
    {
        Character ch;
        String buffer = "";

        ch = Character.valueOf( (char)stream.read() );

        while (stream.available() > 0 && (Character.isLetter(ch) || Character.isDigit(ch)) )
        {
            buffer += ch;
            ch = Character.valueOf( (char)stream.read() );
        }

        stream.unread(ch);

        // figure out if buffer is variable or keyword
        if ( buffer.equals("var") )
            return new Lexeme(VAR);
        else if ( buffer.equals("define") )
            return new Lexeme(DEFINE);
        else if ( buffer.equals("function") )
            return new Lexeme(FUNCTION);
        else if ( buffer.equals("bundle") )
            return new Lexeme(BUNDLE);
        else if ( buffer.equals("return") )
            return new Lexeme(RETURN);
        else if ( buffer.equals("if") )
            return new Lexeme(IF);
        else if ( buffer.equals("else") )
            return new Lexeme(ELSE);
        else if ( buffer.equals("while") )
            return new Lexeme(WHILE);
        else if ( buffer.equals("for") )
            return new Lexeme(FOR);
        else if ( buffer.equals("class") )
            return new Lexeme(CLASS);
        else
            return new Lexeme(ID, buffer);
    }

    /**
     * Lex specifically for a double quoted string
     * @return Lexeme of String found
     */
    private Lexeme lexString() throws IOException
    {
        String buffer = "";
        Character ch = Character.valueOf( (char)stream.read() );

        while (ch != '\"')
        {
            buffer += ch;

            ch = Character.valueOf( (char)stream.read() );
        }

        return new Lexeme(STRING, buffer);
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
        // System.out.println("ch is: " + ch);              //FIXME: for testing purposes only

        // if read fails return ENDOFINPUT Lexeme

        switch (ch)
        {
            case '(':
                return new Lexeme(OPEN_PAREN);
            case ')':
                return new Lexeme(CLOSE_PAREN);
            case ',':
                return new Lexeme(COMMA);
            case '+':
                ch = Character.valueOf( (char)stream.read() );

                if (ch == '+')
                {
                    return new Lexeme(INCREMENT);
                }

                stream.unread(ch);
                return new Lexeme(PLUS);
            case '*':
                return new Lexeme(TIMES);
            case '-':
                ch = Character.valueOf( (char)stream.read() );

                if (ch == '-')
                    return new Lexeme(DECREMENT);

                stream.unread(ch);
                return new Lexeme(MINUS);
            case '/':
                return new Lexeme(DIVIDE);
            case '<':
                return new Lexeme(LESS_THAN);
            case '>':
                return new Lexeme(GREATER_THAN);
            case '=':
                ch = Character.valueOf( (char)stream.read() );

                if (ch == '=')
                    return new Lexeme(EQUAL_TO);

                stream.unread(ch);
                return new Lexeme(ASSIGN);
            case ';':
                return new Lexeme(SEMICOLON);
            case '%':
                return new Lexeme(MODULO);
            case '_':
                return new Lexeme(UNDERSCORE);
            case '\'':
                return new Lexeme(SINGLE_QUOTE);
            case '{':
                return new Lexeme(OPEN_BRACE);
            case '}':
                return new Lexeme(CLOSE_BRACE);
            default:
                if ( Character.isDigit(ch) )
                {
                    stream.unread(ch);
                    return lexNumber();
                }

                else if ( Character.isLetter(ch) )
                {
                    stream.unread(ch);
                    return lexVarOrKeyword();
                }

                else if (ch == '\"')
                {
                    return lexString();
                }
                else
                    return new Lexeme(UNKNOWN, ch);

        }

    //    return new Lexeme(UNKNOWN, ch);                   // FIXME: might need to edit this guy. Just put him here until I could get away without a compile warning for no return statement
    }


}
