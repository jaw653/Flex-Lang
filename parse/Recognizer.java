/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Recognizer Class
 */

// Do I need an optArgList() and optArgListPending() because it would just be empty or argList()
// Should asterisk and times both be in operatorPending()?

package parse;

import lex.*;
import java.io.*;


public class Recognizer implements Types
{
    Lexeme currLexeme;
    PushbackInputStream stream;


    /**
     * Default constructor
     */
    public Recognizer(Lexeme curr, PushbackInputStream stream)
    {
        currLexeme = curr;
        this.stream = stream;
    }

    /**
     * Checks Type of current Lexeme against argument
     * @type The Type to be checked against
     * @return true if Types match
     */
    private boolean check(String type)
    {
        return currLexeme.getType() == type;
    }

    /**
     * Sets currLexeme to the next Lexeme in the list
     */
    private void advance() throws IOException
    {
        Lexer i = new Lexer(stream);
        currLexeme = i.lex();
    }

    /**
     * Kills program if currLexeme is of incorrect Type
     * @type The Type to compare with currLexeme's Type
     */
    private void matchNoAdvance(String type)
    {
        if ( !check(type) )
        {
            System.out.println("Syntax error");
            System.exit(-1);
        }
    }

    /**
     * Advances if currLexeme is of correct Type
     * @type The correct Type to compare with currLexeme's Type
     */
    private void match(String type) throws IOException
    {
        matchNoAdvance(type);
        advance();
    }

/***** Non-terminals *****/
    /**
     * Root of recursive descent parsing
     */
    public void program() throws IOException
    {
        def();
        if (programPending())
        {
            def();
        }
    }

    /**
     * Generic definition non-terminal method
     */
    public void def() throws IOException
    {
        varDef();
        functionDef();
        classDef();
        importDef();
    }

    /**
     * Variable Defintion non-terminal method
     */
    public void varDef() throws IOException
    {
        match(VAR);
        match(ID);

        if ( check(ASSIGN) )
        {
            match(ASSIGN);
            expression();
        }

        match(SEMICOLON);
    }

    /**
     * Function definition non-terminal method
     */
    public void functionDef() throws IOException
    {
        match(DEFINE);
        match(FUNCTION);
        match(ID);
        match(OPEN_PAREN);

        if ( argListPending() )
        {
            argList();
        }

        match(CLOSE_PAREN);

        block();
    }

    /**
     * Class definition non-terminal method
     */
    public void classDef() throws IOException
    {
        match(DEFINE);
        match(CLASS);
        match(ID);
        block();
    }

    /**
     * Import definition non-terminal method
     */
    public void importDef() throws IOException
    {
        match(BUNDLE);
        match(STRING);
    }

    /**
     * Expression non-terminal method
     */
    public void expression() throws IOException
    {
        unary();
        if ( operatorPending() )
        {
            operator();
            expression();
        }
    }

    /**
     * Argument List non-terminal method
     */
    public void argList() throws IOException
    {
        match(VAR);
        match(ID);

        if ( check(COMMA) )
        {
            match(COMMA);
            argList();
        }
    }

    /**
     * Statement block non-terminal method
     */
    public void block()
    {

    }

    /**
     * Unary non-terminal method
     */
    public void unary()
    {
    }

    /**
     * Operator non-terminal method
     */
    public void operator()
    {
    }


/***** Non-terminal pending methods *****/
    public boolean programPending()
    {
        return defPending();
    }

    public boolean defPending()
    {
        return ( varDefPending() || functionDefPending() ||
            classDefPending() || importDefPending() );
    }

    public boolean varDefPending()
    {
        return check(VAR);
    }

    public boolean functionDefPending()
    {
        return check(DEFINE);
    }

    public boolean classDefPending()
    {
        return check(DEFINE);
    }

    public boolean importDefPending()
    {
        return check(BUNDLE);
    }

    public boolean argListPending()
    {
        return check(VAR);
    }

    public boolean operatorPending() throws IOException
    {
        return ( check(PLUS) || check(TIMES) || check(DIVIDE) || check(MINUS) ||
            check(GREATER_THAN) || check(LESS_THAN) || check(EQUAL_TO) || check(GT_EQUAL) ||
            check(LT_EQUAL) || check(MODULO) || check(PLUS_EQUAL) || check(MINUS_EQUAL) ||
            check(ASSIGN) );                                             //FIXME: should asterisk and times both be here?
    }

/***** Terminals *****/

}
