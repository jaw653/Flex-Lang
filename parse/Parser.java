/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Parser class implementation of Recognizer
 */

 package parse;

 import lex.*;
 import java.io.*;


 public class Parser implements Types
 {
     Lexer i;
     Lexeme currLexeme;


     /**
      * Default constructor
      */
     public Parser(Lexeme curr, PushbackInputStream stream) throws IOException
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
// System.out.println("currLexeme is: " + currLexeme.getType() + ", should be: " + type);
         return currLexeme.getType() == type;
     }

     /**
 	 * Display method for parse tree
 	 * @tree The root Lexeme parse tree to display
 	 */
 	private void displayTree(Lexeme tree)
 	{
 		if (tree.getCar() != null)
 		{System.out.print("\n{Left"); displayTree(tree.getCar()); System.out.print("Left}\n");}
 		System.out.print("<" + tree.getType() + ">");
 		if (tree.getCdr() != null)
 		{System.out.print("\n{Right"); displayTree(tree.getCdr()); System.out.print("Right}\n");}
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
     private Lexeme match(String type) throws IOException
     {
        Lexeme save = currLexeme;
        matchNoAdvance(type);
        advance();
        return save;
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
        Lexeme def, prog = null;

        def = def();
        if (programPending())
        {
            prog = program();
        }

        return cons(PROG, def, prog);
    }

    /**
     * Generic definition non-terminal method
     */
    public Lexeme def() throws IOException
    {
        Lexeme def = null;

        if ( varDefPending() )
        {
            def = varDef();
        }
        else if ( definedPending() )
        {
            def = defined();
        }
        else if ( importDefPending() )
        {
            def = importDef();
        }

        return cons(DEF, def, null);
    }

    /**
     * Variable Defintion non-terminal method
     */
    public Lexeme varDef() throws IOException
    {
        Lexeme tree = null;
        Lexeme id, objId = null, exprList = null, expr = null;

        match(VAR);
        id = match(ID);

        if ( check(ASSIGN) )
        {
            match(ASSIGN);

            if (check(NEW))
            {
                match(NEW);
                match(UNDERSCORE);
                objId = match(ID);
                match(OPEN_PAREN);
                if (exprListPending())
                {
                    exprList = exprList();
                }
                match(CLOSE_PAREN);
                match(UNDERSCORE);

                tree = cons(CLASS_INSTANTIATION, id, cons(GLUE, objId, exprList));
            }
            else
            {
				if (expressionPending())
	                expr = expression();
                tree = cons(VARDEF, id, expr);
            }
        }
		else
		{
			tree = cons(VARDEF, id, expr);
		}

        match(SEMICOLON);

        return tree;
    }

    /**
     * Parse method for all entities starting with "define"
     */
    public Lexeme defined() throws IOException
    {
        Lexeme id, paramList = null, block = null, ret = null;

		match(DEFINE);

        if ( check(FUNCTION) )
        {
            match(FUNCTION);
            id = match(ID);
            match(OPEN_PAREN);

            if ( paramListPending() )
                paramList = paramList();

            match(CLOSE_PAREN);

			block = block();

			if (!id.getName().equals("run"))
				ret = cons(FUNCDEF, id, cons(GLUE, paramList, block));
			else
				ret = cons(RUNDEF, id, cons(GLUE, paramList, block));
        }
        else
        {
            match(CLASS);
            id = match(ID);

			block = block();

			ret = cons(CLASSDEF, id, block);
        }

//        block = block();

        return ret;
    }

    /**
     * Import definition non-terminal method
     */
    public Lexeme importDef() throws IOException
    {
        Lexeme file;

        match(BUNDLE);
        file = match(STRING);

        return cons(IMPORTDEF, file, null);
    }

    /**
     * Expression non-terminal method
     */
    public Lexeme expression() throws IOException
    {
        Lexeme tree;
        Lexeme unary, op = null, expr = null;

        unary = unary();

        if ( operatorPending() )
        {
            op = operator();
            expr = expression();

            tree = cons(EXPRDEF, unary, cons(GLUE, op, expr));
        }
        else
            tree = cons(EXPRDEF, unary, null);

        return tree;
    }

    /**
     * Parameter List non-terminal method
     */
    public Lexeme paramList() throws IOException
    {
        Lexeme id;
        Lexeme pList = null;

        match(VAR);
        id = match(ID);

        if ( check(COMMA) )
        {
            match(COMMA);
            pList = paramList();
        }

        return cons(PARAMLIST, id, pList);
    }

    /**
     * Statement block non-terminal method
     */
    public Lexeme block() throws IOException
    {
        Lexeme stmnts = null, ret = null;

        match(OPEN_BRACE);

        if ( statementsPending() )
        {
            stmnts = statements();
        }

        if ( returnStatementPending() )
        {
            ret = returnStatement();
        }

        match(CLOSE_BRACE);

		return cons(BLOCK, stmnts, ret);
    }

    /**
     * Unary non-terminal method
     */
    public Lexeme unary() throws IOException
    {
        Lexeme tmp = null, tmp1 = null, tree = null;
		Lexeme block = null, paramList = null;

        if ( check(ID) )
        {
            tree = cons(UNARY, idStart(), null);        //FIXME: should the cdr be null?
        }
        else if ( check(INTEGER) )
            tree = cons(UNARY, match(INTEGER), null);
        else if ( check(REAL) )
            tree = cons(UNARY, match(REAL), null);
        else if ( check(CHARACTER) )
            tree = cons(UNARY, match(CHARACTER), null);
        else if ( check(STRING) )
            tree = cons(UNARY, match(STRING), null);
		else if ( check(LAMBDA) )
		{
            match(LAMBDA);
            match(OPEN_PAREN);

            if ( paramListPending() )
                paramList = paramList();
            
			match(CLOSE_PAREN);

			block = block();
			
			tree = cons(LAMBDA, null, cons(GLUE, paramList, block));
		}
        else if ( check(NOT) )
        {
            tmp = match(NOT);
            tmp1 = unary();
            tree = cons(UNARY, tmp, tmp1);
        }
        else if ( check(OPEN_PAREN) )
        {
            match(OPEN_PAREN);
            tmp1 = expression();
            match(CLOSE_PAREN);
            tree = cons(UNARY, tmp1, null);
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
            tmp = match(ID);
            match(OPEN_PAREN);

            //optExprList
            if ( exprListPending() )
                tmp1 = exprList();

            match(CLOSE_PAREN);
            match(UNDERSCORE);

            tree = cons(CLASS_INSTANTIATION, tmp, tmp1);
        }

        return tree;
    }

    /**
     * Operator non-terminal method
     */
    public Lexeme operator() throws IOException
    {
        Lexeme tree = null;

        if ( check(PLUS) )
            tree = match(PLUS);
        else if ( check(TIMES) )
            tree = match(TIMES);
        else if ( check(DIVIDE) )
            tree = match(DIVIDE);
        else if ( check(MINUS) )
            tree = match(MINUS);
        else if ( check(INCREMENT) )
            tree = match(INCREMENT);
        else if ( check(DECREMENT) )
            tree = match(DECREMENT);
        else if ( check(GREATER_THAN) )
            tree = match(GREATER_THAN);
        else if ( check(LESS_THAN) )
            tree = match(LESS_THAN);
        else if ( check(EQUAL_TO) )
            tree = match(EQUAL_TO);
		else if ( check(NOT_EQUAL) )
			tree = match(NOT_EQUAL);
		else if ( check(NEGATE) )
			tree = match(NEGATE);
        else if ( check(GT_EQUAL) )
            tree = match(GT_EQUAL);
        else if ( check(LT_EQUAL) )
            tree = match(LT_EQUAL);
        else if ( check(MODULO) )
            tree = match(MODULO);
        else if ( check(PLUS_EQUAL) )
            tree = match(PLUS_EQUAL);
        else if ( check(MINUS_EQUAL) )
            tree = match(MINUS_EQUAL);
        else if ( check(ASSIGN) )
            tree = match(ASSIGN);
 //        else if ( check(ASTERISK) )
 //            match(ASTERISK);                            //FIXME: should this and times be here?
        return tree;
    }

    /**
     * Statements non-terminal method
     */
    public Lexeme statements() throws IOException
    {
        Lexeme tree = null;
        Lexeme stmnt, stmnts = null;

        stmnt = statement();

        if ( statementsPending() )
        {
            stmnts = statements();
        }

        return cons(STATEMENTS, stmnt, stmnts);
    }

    /**
     * Statment non-terminal method
     */
    public Lexeme statement() throws IOException
    {
        Lexeme tmp = null;

        if ( expressionPending() )
        {
            tmp = expression();

			match(SEMICOLON);
        }
        else if ( ifStatementPending() )
        {
            tmp = ifStatement();
        }
        else if ( whileLoopPending() )
        {
            tmp = whileLoop();
        }
        else if ( forLoopPending() )
        {
            tmp = forLoop();
        }
        else if ( definedPending() )
        {
            tmp = defined();
        }
        else if ( varDefPending() )
        {
            tmp = varDef();
        }
		else
			System.out.println("tmp not assigned");
		return cons(STATEMENT, tmp, null);
    }

    /**
     * if statement non-terminal method
     */
    public Lexeme ifStatement() throws IOException
    {
        Lexeme expr, blck, elif = null;

        match(IF);
        match(OPEN_PAREN);

        expr = expression();

        match(CLOSE_PAREN);

        blck = block();

        if (check(ELSE))
            elif = elseIf();

        return cons(IFSTMNT, expr, cons(GLUE, blck, elif));
    }

    /**
     * else if non-terminal method
     */
    public Lexeme elseIf() throws IOException
    {
        Lexeme ifStmnt = null, blck = null;

        match(ELSE);

        if ( check(IF) )
        {
            ifStmnt = ifStatement();
        }
/*
        else
        {
            blck = block();
       	}
*/
        return cons(ELSEIF, ifStmnt, null);
    }

    /**
     * while loop non-terminal method
     */
    public Lexeme whileLoop() throws IOException
    {
        Lexeme expr, blck;

        match(WHILE);
        match(OPEN_PAREN);

        expr = expression();

        match(CLOSE_PAREN);

        blck = block();

        return cons(WHILELOOP, expr, blck);
    }

    /**
     * for looop non-terminal method
     */
    public Lexeme forLoop() throws IOException
    {
        Lexeme tree;
        Lexeme expr0, expr1, expr2, blck;

        match(FOR);
        match(OPEN_PAREN);

        expr0 = expression();
        match(SEMICOLON);

        expr1 = expression();
        match(SEMICOLON);

        expr2 = expression();
/*
System.out.println("expr2: " + expr2.getType() + ", ");
System.out.println("expr2car: " + expr2.getCar().getType());
System.out.println("expr2carcar: " + expr2.getCar().getCar().getType());
System.out.println("expr2carcar val: " + expr2.getCar().getCar().getName());
System.out.println("expr2carcdr val: " + expr2.getCar().getCdr().getName());
*/

        match(CLOSE_PAREN);

        blck = block();

        tree = cons(FORLOOP, cons(GLUE, expr0, expr1), cons(GLUE, expr2, blck));
        //displayTree(tree);
        return tree;
     }

    /**
     * Return statment non-terminal method
     */
    public Lexeme returnStatement() throws IOException
    {
        Lexeme expr;

        match(RETURN);
        expr = expression();
        match(SEMICOLON);

        return cons(RETSTMNT, expr, null);
    }

    /**
     * Expression list non-terminal method
     */
    public Lexeme exprList() throws IOException
    {
        Lexeme expr, exprList = null;

        expr = expression();

        if ( check(COMMA) )
        {
            match(COMMA);
            exprList = exprList();
        }

        return cons(EXPRLIST, expr, exprList);
    }

    /**
     * Non-terminal method for entities beginning with id
     */
 	public Lexeme idStart() throws IOException
 	{
        Lexeme tree = null;
        Lexeme id, unry = null, exprList = null, mthdName = null;

 		id = match(ID);

		if ( operatorPending() )
 		{
 			Lexeme op = operator();
 			unry = unary();

			tree = cons(op.getType(), id, unry);
 		}
 		else if ( check(OPEN_PAREN) )
 		{
 			match(OPEN_PAREN);

 			if ( exprListPending() )
 			{
 				exprList = exprList();
 			}

 			match(CLOSE_PAREN);

            tree = cons(FUNCCALL, id, exprList);
 		}
 		else if ( check(INCREMENT) )
 			tree = cons(IDSTART, id, match(INCREMENT));
 		else if ( check(DECREMENT) )
 			tree = cons(IDSTART, id, match(DECREMENT));
 		else if ( check(PERIOD) )
 		{
 			match(PERIOD);
 			mthdName = match(ID);

			if (check(OPEN_PAREN))
			{
				match(OPEN_PAREN);

 				if ( exprListPending() )
 				{
 					exprList = exprList();
 				}

 				match(CLOSE_PAREN);

            	tree = cons(OBJMETHOD, id, cons(GLUE, mthdName, exprList));
			}
			else
				tree = cons(OBJACCESS, id, mthdName);		// A dot access such as obj.a
 		}
        else
            tree = cons(IDSTART, id, null);

        return tree;
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
             check(ASSIGN) || check(INCREMENT) || check(DECREMENT) );                                             //FIXME: should asterisk and times both be here?
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
// System.out.println("running unaryPending(), ");
// if (check(OBJMEM) == true) System.out.println("OBJMEM pending");
 		return ( idStartPending() || check(INTEGER) || check(REAL) || check(STRING) ||
 			check(NOT) || check(OPEN_PAREN) || check(MINUS) || check(NEW) ||
			check(LAMBDA) || check(OBJMEM) );
 	}

 	public boolean idStartPending()
 	{
 		return check(ID);
 	}
 }
