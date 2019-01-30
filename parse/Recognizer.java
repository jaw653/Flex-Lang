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
        //System.out.println("curr type is: " + currLexeme.getType() +
        //    " checking against: " + type);
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
            System.out.println("Syntax error, " + currLexeme.getType() + " is not of type " + type);
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
        // System.out.println("currLexeme is now: " + currLexeme.getType());
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
        if ( varDefPending() )
        {
            //System.out.println("variable definition");
            varDef();
        }
        else if ( definedPending() )
        {
            defined();
        }
        else if ( importDefPending() )
        {
            //System.out.println("import definition");
            importDef();
        }
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

    public void defined() throws IOException
    {
        match(DEFINE);

        if ( check(FUNCTION) )
        {
            match(FUNCTION);
            match(ID);
            match(OPEN_PAREN);

            if ( paramListPending() )
                paramList();

            match(CLOSE_PAREN);
        }
        else
        {
            match(CLASS);
            match(ID);
        }

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
    public void paramList() throws IOException
    {
        match(VAR);
        match(ID);

        if ( check(COMMA) )
        {
            match(COMMA);
            paramList();
        }
    }

    /**
     * Statement block non-terminal method
     */
    public void block() throws IOException
    {
        match(OPEN_BRACE);
        // optStatements
        if ( statementsPending() )
        {
			// System.out.println("entered statements pending conditional");
            statements();
        }

        if ( returnStatementPending() )
        {
            returnStatement();
        }
        // optReturn
		// System.out.println("testing testing one two three");
        match(CLOSE_BRACE);
    }

    /**
     * Unary non-terminal method
     */
    public void unary() throws IOException
    {
        if ( check(ID) )
            idStart();
        else if ( check(INTEGER) )
            match(INTEGER);
        else if ( check(REAL) )
            match(REAL);
        else if ( check(CHARACTER) )        //FIXME: don't think I'm accounting for a character anymore
            match(CHARACTER);
        else if ( check(STRING) )
            match(STRING);
        /*                                  //FIXME: array not currently defined
        else if ( check(ARRAY) )
            match(ARRAY);
        */
        else if ( check(NOT) )
        {
            match(NOT);
            unary();
        }
        else if ( check(OPEN_PAREN) )
        {
            match(OPEN_PAREN);
            expression();
            match(CLOSE_PAREN);
        }
/*                                          //FIXME: no UMINUS in types, maybe just get rid of it from the grammar
        else if ( check(UMINUS) )
        {

        }
*/
        else if ( check(NEW) )
        {
            match(NEW);
            match(UNDERSCORE);
            match(ID);
            match(OPEN_PAREN);

            //optExprList
            if ( exprListPending() )
                exprList();

            match(CLOSE_PAREN);
            match(UNDERSCORE);
        }
    }

    /**
     * Operator non-terminal method
     */
    public void operator() throws IOException
    {
        if ( check(PLUS) )
            match(PLUS);
        else if ( check(TIMES) )
            match(TIMES);
        else if ( check(DIVIDE) )
            match(DIVIDE);
        else if ( check(MINUS) )
            match(MINUS);
        else if ( check(GREATER_THAN) )
            match(GREATER_THAN);
        else if ( check(LESS_THAN) )
            match(LESS_THAN);
        else if ( check(EQUAL_TO) )
            match(EQUAL_TO);
        else if ( check(GT_EQUAL) )
            match(GT_EQUAL);
        else if ( check(LT_EQUAL) )
            match(LT_EQUAL);
        else if ( check(MODULO) )
            match(MODULO);
        else if ( check(PLUS_EQUAL) )
            match(PLUS_EQUAL);
        else if ( check(MINUS_EQUAL) )
            match(MINUS_EQUAL);
        else if ( check(ASSIGN) )
            match(ASSIGN);
//        else if ( check(ASTERISK) )
//            match(ASTERISK);                            //FIXME: should this and times be here?
    }

    /**
     * Statements non-terminal method
     */
    public void statements() throws IOException        //FIXME: does this need to have statements in it?
    {
        statement();

        if ( statementsPending() )
        {
            statement();
        }
    }

    public void statement() throws IOException
    {
        // System.out.println("hello there");
        if ( expressionPending() )
        {
            expression();
            match(SEMICOLON);
        }
        else if ( ifStatementPending() )
        {
            ifStatement();
        }
        else if ( whileLoopPending() )
        {
            whileLoop();
        }
        else if ( forLoopPending() )
        {
            forLoop();
        }
        else if ( definedPending() )
        {
            // System.out.println("here");
            defined();
        }
        else if ( varDefPending() )
        {
            varDef();
        }
    }

    public void ifStatement() throws IOException
    {
        match(IF);
        match(OPEN_PAREN);

        expression();                               //FIXME: Might need an expressionPending() here?

        match(CLOSE_PAREN);

        block();

        elseIf();
        // optElse
    }

    public void elseIf() throws IOException
    {
        match(ELSE);

        if ( check(IF) )
        {
            ifStatement();
        }
        else
        {
            block();
        }
    }

    public void whileLoop() throws IOException
    {
        match(WHILE);
        match(OPEN_PAREN);

        expression();

        match(CLOSE_PAREN);

        block();
    }

    public void forLoop() throws IOException
    {
        match(FOR);
        match(OPEN_PAREN);

        expression();
        match(SEMICOLON);

        expression();
        match(SEMICOLON);

        expression();
        match(CLOSE_PAREN);

        block();
    }

    public void returnStatement() throws IOException
    {
        match(RETURN);
        expression();
        match(SEMICOLON);
    }

    public void exprList() throws IOException
    {
        expression();

        if ( check(COMMA) )
        {
            match(COMMA);
            exprList();
        }
    }

	public void idStart() throws IOException
	{
		match(ID);

		if ( check(ASSIGN) )
		{
			match(ASSIGN);
			unary();
		}
		else if ( check(OPEN_PAREN) )
		{
			match(OPEN_PAREN);

			if ( exprListPending() )
			{
				exprList();
			}

			match(CLOSE_PAREN);
		}
		else if ( check(INCREMENT) )
			match(INCREMENT);
		else if ( check(DECREMENT) )
			match(DECREMENT);
		else if ( check(PERIOD) )
		{
			match(PERIOD);
			match(ID);
			match(OPEN_PAREN);

			if ( exprListPending() )
			{
				exprList();
			}

			match(CLOSE_PAREN);
		}
	}


/***** Non-terminal pending methods *****/
    public boolean programPending() throws IOException
    {
        return defPending();
    }

    public boolean defPending() throws IOException
    {
        return ( varDefPending() || definedPending() || importDefPending() );
    }

    public boolean varDefPending()
    {
        return check(VAR);
    }

    public boolean definedPending() throws IOException
    {
        return check(DEFINE);
    }

    public boolean importDefPending()
    {
        return check(BUNDLE);
    }

    public boolean paramListPending()
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

    public boolean statementsPending() throws IOException
    {
        return statementPending();
    }

    public boolean statementPending() throws IOException
    {
        if ( expressionPending() )
            return true;
        else if ( ifStatementPending() )
            return true;
        else if ( whileLoopPending() )
            return true;
        else if ( forLoopPending() )
            return true;
        else if ( definedPending() )
            return true;
        else if ( varDefPending() )
            return true;
		else if ( unaryPending() )
			return true;
		else if ( idStartPending() )
			return true;
        else
        {
            return false;
        }
    }

    public boolean expressionPending()
    {
        return unaryPending();
    }

    public boolean ifStatementPending()
    {
        return check(IF);
    }

    public boolean whileLoopPending()
    {
        return check(WHILE);
    }

    public boolean forLoopPending()
    {
        return check(FOR);
    }

    public boolean returnStatementPending()
    {
        return check(RETURN);
    }

    public boolean exprListPending()
    {
        return expressionPending();
    }

    public boolean elseIfPending()
    {
        return check(ELSE);
    }

	public boolean unaryPending()
	{
		return ( idStartPending() || check(INTEGER) || check(REAL) || check(STRING) ||
			check(NOT) || check(OPEN_PAREN) || check(MINUS) || check(NEW) );
	}

	public boolean idStartPending()
	{
		return check(ID);
	}
}
