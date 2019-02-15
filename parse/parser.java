/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Parser class implementation of Recognizer
 */

 package recognize;

 import lex.*;
 import java.io.*;


 public class Recognizer implements Types
 {
     Lexer i;
     Lexeme currLexeme;


     /**
      * Default constructor
      */
     public Recognizer(Lexeme curr, PushbackInputStream stream) throws IOException
     {
         currLexeme = curr;
         i = new Lexer(stream);
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
             int line = i.getLineNum();
             if (line > 2)
                 line += 2;
             System.out.println("Illegal syntax error > line " + line );
             System.exit(-1);
         }
     }

     /**
      * Advances if currLexeme is of correct Type
      * @type The correct Type to compare with currLexeme's Type
      */
     private String match(String type) throws IOException
     {
        matchNoAdvance(type);
        advance();
        return type;
     }

    /**
     * Construct method for binding together Lexemes
     * @type The type of the new Lexeme
     * @l The pointer to the left of the Lexeme
     * @r The pointer to the right of the Lexeme
     */
    private Lexeme cons(String type, Lexeme l, Lexeme r)
    {
        Lexeme ret = new Lexeme(type);
        ret.setCar(l);
        ret.setCdr(r);

        return ret;
    }

 /***** Non-terminals *****/
    /**
      * Root of recursive descent parsing
     */
    public Lexeme program() throws IOException
    {
        def();
        if (programPending())
        {
            return program();
        }

    }

    /**
     * Generic definition non-terminal method
     */
    public Lexeme def() throws IOException
    {
        if ( varDefPending() )
        {
            return varDef();
        }
        else if ( definedPending() )
        {
            return defined();
        }
        else if ( importDefPending() )
        {
            return importDef();
        }
    }

    /**
     * Variable Defintion non-terminal method
     */
    public Lexeme varDef() throws IOException
    {
        String id;
        Lexeme expr;

        match(VAR);
        id = match(ID);

        if ( check(ASSIGN) )
        {
            match(ASSIGN);
            expr = expression();
        }

        match(SEMICOLON);

        return cons(VAR, new Lexeme(I, id), expr);
    }

    public Lexeme defined() throws IOException
    {
        boolean isFunction = false;

        String id;
        Lexeme paramList, block;

        match(DEFINE);

        if ( check(FUNCTION) )
        {
            isFunction = true;

            match(FUNCTION);
            id = match(ID);
            match(OPEN_PAREN);

            if ( paramListPending() )
                paramList = paramList();

            match(CLOSE_PAREN);
        }
        else
        {
            match(CLASS);
            id = match(ID);
        }

        block = block();

        if (isFunction == true)
            return cons(FUNCDEF, new Lexeme(I, id), cons(GLUE, paramList, block));

        return cons(CLASSDEF, new Lexeme(I, id), block);
    }

    /**
     * Import definition non-terminal method
     */
    public Lexeme importDef() throws IOException
    {
        String file;

        match(BUNDLE);
        file = match(STRING);

        return cons(IMPORTDEF, new Lexeme(I, file), null);
    }

    /**
     * Expression non-terminal method
     */
    public Lexeme expression() throws IOException
    {
        Lexeme unary, op, expr;

        boolean opPending = false;

        unary = unary();
        if ( operatorPending() )
        {
            op = operator();
            expr = expression();
        }

        if (opPending == true)
            return cons(EXPRDEF, unary, cons(GLUE, op, expr));

        return cons(EXPRDEF, unary, null);
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
             statements();
         }

         if ( returnStatementPending() )
         {
             returnStatement();
         }

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
         else if ( check(CHARACTER) )
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
     public void statements() throws IOException
     {

         statement();

         if ( statementsPending() )
         {
             statements();
         }
     }

     public void statement() throws IOException
     {
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

         expression();

         match(CLOSE_PAREN);

         block();

         elseIf();
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
         return ( expressionPending() || ifStatementPending() || whileLoopPending() ||
             forLoopPending() || definedPending() || varDefPending() ||
             unaryPending() || idStartPending() );
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
