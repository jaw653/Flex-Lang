/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Evaluator class
 */

// Need to write evalMethodCall() and evalIDCall(), as well as add BUILTIN to types
// isFunctionCall() may be incorrect if there is an empty (ie null) exprlist to the cdr

// lookup should be returning the closure correct? so is there another env method I need?

// does it matter what evalexprdef() returns?

// evalImportDef doesn't add anything to the env; should it?
// still need to write evalmethodcall() ??

// need to run eval on main() or something to actually run the file

// currently getVal() will not be fatal so the program could keep running even if an arg is not resolved to its value

package eval;

import lex.*;
import parse.*;
import env.*;

import java.io.*;


public class Evaluator implements Types
{
	/**
	 * Default constructor
	 */
	public Evaluator()
	{
	}

/***** Helper Methods *****/
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

	/**
	 * Checks to make sure there is a correct number of command line args
	 * @args String array of command line args
	 */
	private static void checkCmdArgs(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Incorrect number of command line args");
			System.exit(-1);
		}
	}

	/**
	 * Safe method for opening file
	 * @filename The name of the file to be opened
	 * @return The pointer to the opened file object
	 */
	private static File openFile(String filename) throws IOException
	{
		File file = new File(filename);

		if ( !file.exists() )
		{
			System.out.println("File does not exist");
			System.exit(-1);
		}

		return file;
	}

	/**
	 * Converts an expression list to a val list for insertion to env table
	 * @head Root of the expression list to be converted
	 * @env Corresponding environment (only needed so I can call eval
	 * @return The newly created value list
	 */
	private Lexeme exprListToValList(Lexeme head, Environment env)
	{
		Lexeme tree = null, val = null, next = null;

		if (head != null)
		{
			val = eval(head.getCar(), env);	
			next = exprListToValList(head.getCdr(), env);
			tree = cons(V, val, next);
		}
		else tree = null;

		return tree;
	}


	/**
	 * Gets the evaluated arguments of a function
	 * @tree Root of the function for which to get args
	 * @env Corresponding environment
	 * @return A list of the evaluated args for insertion into an environment
	 */
	private Lexeme getArgs(Lexeme tree, Environment env)
	{
		return exprListToValList(tree.getCdr(), env);
	}

	/**
	 * Gets the parameters from the function definition
	 * @closure The closure from which to get the params
	 * @return The root of the paramList
	 */
	private Lexeme getParams(Lexeme closure)
	{
		return closure.getCdr().getCdr().getCar();
	}

	/**
	 * Gets the body from the function definition
	 * @closure The closure from which to get the body
	 * @return The root of the body/block
	 */
	private Lexeme getBody(Lexeme closure)
	{
/*
System.out.println("getbody:");
System.out.println("closure type is: " + closure.getType());
System.out.println("cdr is: " + closure.getCdr().getType());
System.out.println("cddr is: " + closure.getCdr().getCdr().getType());
System.out.println("cdddr is: " + closure.getCdr().getCdr().getCdr().getType());
*/
		return closure.getCdr().getCdr().getCdr();
	}

	/**
	 * Discerns whether or not the given lexeme is an operator type
	 * @l Lexeme to check
	 * @return True if l is an operator
	 */
	private static boolean isOperator(Lexeme l)
	{
		switch (l.getType())
		{
			case PLUS:
			case MINUS:
			case TIMES:
			case DIVIDE:
			case INCREMENT:
			case DECREMENT:
			case GREATER_THAN:
			case LESS_THAN:
			case EQUAL_TO:
			case GT_EQUAL:
			case LT_EQUAL:
			case MODULO:
			case PLUS_EQUAL:
			case MINUS_EQUAL:
				return true;
		}

		return false;
	}
	
	/**
	 * Discerns if the given Lexeme tree holds a function call
	 * @tree Root of tree to test
	 * @return If the given tree represents a function call
	 */
	private boolean isFunctionCall(Lexeme tree)
	{
		if (tree.getCdr().getType() == EXPRLIST)
			return true;
		return false;
	}

	/**
	 * Discerns if the given Lexeme tree holds a method call
	 * @tree Root of the tree to test
	 * @return If the given tree represents a method call
	 */
	private boolean isMethodCall(Lexeme tree)
	{
		if (tree.getCdr().getType() == GLUE)
		{
			if (tree.getCdr().getCar().getType() == ID)
				return true;
		}
		return false;
	}

	/**
	 * Determines if the function Lexeme is a built-in function
	 * @function The function in question
	 * @return True if the function is built-in
	 */
	private boolean isBuiltIn(Lexeme function)
	{
		if (function.getName() != "print" && function.getName() != "destroy")
			return false;
		return true;
	}


/***** Private Methods *****/
	/**
	 * Gets the value of the ID at tree root from the environment
	 * @tree Lexeme for which to search environment
	 * @env Environment for which to search for Lexeme id
	 * @return The value of the id at tree
	 */
	private Lexeme lookup(Lexeme tree, Environment env)
	{
// System.out.println("looking up: "); tree.display();
		if (env.getVal(tree) != null)
		{
// System.out.println("id found! id is: ");
			env.getVal(tree).getName();
		}
		return env.getVal(tree);
	}

	/**
	 * Eval method for overarching program
	 * @tree Root of the PROG tree
	 * @env Corresponding environment
	 * @return the eval of the def
	 */
	private Lexeme evalProg(Lexeme tree, Environment env)
	{
		Lexeme def = null, prog = null;
		
		def = eval(tree.getCar(), env);
		if (tree.getCdr() != null)
			prog = eval(tree.getCdr(), env);
		
		return def;
	}
	/**
	 * Adds variable value to its scope
	 * @tree Root of the VARDEF tree
	 * @env Corresponding environment
	 * @return The value of the expression
	 */			//FIXME: is this guy returning the right thing?
	private Lexeme evalVarDef(Lexeme tree, Environment env)
	{
		Lexeme id = null, val = null;
	
		id = eval(tree.getCar(), env);
		
		if (tree.getCdr() != null)
			val = eval(tree.getCdr().getCar(), env);
	
		env.insertEnv(id, val);
// env.displayEnv(1);
		return val;
	}

	/**
	 * Adds function variables to its scope
	 * @tree Root of FUNCDEF tree to use for environment insertion
	 * @env The environment into which the newly created tree will be inserted
	 * @return The inserted Lexeme
	 */
	private Lexeme evalFuncDef(Lexeme tree, Environment env)
	{
		Lexeme closure = null, paramList = null, block = null;

		closure = cons(CLOSURE, env.getEnv(), tree);
		env.insertEnv(tree.getCar(), closure);
		
		if (tree.getCdr().getCar() != null) paramList = eval(tree.getCdr().getCar(), env);
//		block = eval(tree.getCdr().getCdr(), env);
		
		return closure;
	}

	/**
	 * Evaluates the block of the run function; the equivalent of c main
	 * @tree Root of the run function parse tree
	 * @env Corresponding environment
	 * @return The value of the evaluated block
	 */
	private Lexeme evalRunDef(Lexeme tree, Environment env)
	{
		Lexeme closure = null, paramList = null, block = null;

		closure = cons(CLOSURE, env.getEnv(), tree);
		env.insertEnv(tree.getCar(), closure);

		if (tree.getCdr().getCar() != null)
			paramList = eval(tree.getCdr().getCar(), env);

		block = eval(tree.getCdr().getCdr(), env);

		return block;
	}

	/**
	 * Evaluates a block parse tree
	 * @tree Root of the BLOCK tree to use for evaluation
	 * @env Corresponding environment
	 * @return The evaluated result of the block in Lexeme form
	 */
	private Lexeme evalBlock(Lexeme tree, Environment env)
	{
		Lexeme statements = null, retStmt = null;
		if (tree.getCar() != null) statements = eval(tree.getCar(), env);
		if (tree.getCdr() != null) retStmt = eval(tree.getCdr(), env);
		return retStmt;
	}

	/**
	 * Evaluates statements parse tree
	 * @tree Root of statements tree
	 * @env Corresponding environment
	 * @return statement or car(); this is somewhat the base case
	 */
	private Lexeme evalStatements(Lexeme tree, Environment env)
	{
		Lexeme statement = null, statements = null;
		statement = eval(tree.getCar(), env);
		if (tree.getCdr() != null) statements = eval(tree.getCdr(), env);
		return statement;
	}

	/**
	 * Evaluates the statement parse tree
	 * @tree Root of the statement tree
	 * @env Corresponding environment
	 * @return The eval() of an expr, loop, or definition
	 */
	private Lexeme evalStatement(Lexeme tree, Environment env)
	{
		return eval(tree.getCar(), env);
	}

	/**
	 * Eval method for while loop
	 * @tree Root of the while loop parse tree
	 * @env Corresponding environment
	 * @return The block of the while loop
	 */
	private Lexeme evalRetStmt(Lexeme tree, Environment env)
	{
		return eval(tree.getCar(), env);
	}

	/**
	 * Adds class variables to its scope
	 * @tree Root of CLASSDEF tree to use for environmenet insertion
	 * @env The environment into which the newly created tree will be inserted
	 * @return The inserted Lexeme
	 */
	private Lexeme evalClassDef(Lexeme tree, Environment env)
	{
		Lexeme closure = cons(CLOSURE, env.getEnv(), tree);
		env.insertEnv(tree.getCar(), closure);					//FIXME: should this be OCLOSURE?
		return closure;
	}

	/**
	 * Evaluates an EXPRDEF
	 * @tree Root of the EXPRDEF tree
	 * @env Corresponding environment
	 * @return The value of the unary at the bottom of the expression
	 */
	private Lexeme evalExprDef(Lexeme tree, Environment env)
	{
		Lexeme unary = null, operator = null, expr = null;

		unary = eval(tree.getCar(), env);

/*	
		if (tree.getCdr() != null)
		{
			operator = eval(tree.getCdr().getCar(), env);
			expr = eval(tree.getCdr().getCdr(), env);
		}
*/
//		return evalOp(tree.getCar(), env);
		return unary;
	}

	/**
	 * Evaluates a unary
	 * @tree Root of the unary to eval
	 * @env Corresponding environment
	 * @return eval() of children
	 */
	private Lexeme evalUnary(Lexeme tree, Environment env)
	{
		Lexeme ret = null;

		if (tree.getCdr() == null)
			ret = eval(tree.getCar(), env);
		else
			ret = null;//FIXME: this is a class instantiation, I think it might need its own type and corresponding eval method(s)
		return ret;
	}

	/**
	 * Evaluates an operation between unaries and/or expressions
	 * @tree Root of the EXPRDEF
	 * @env The corresponding environment
	 * @return A new Lexeme with the value of the result of the op
	 */
	private Lexeme evalOp(Lexeme tree, Environment env)
	{
		Lexeme arg1 = null, arg2 = null;
		Lexeme resolvedArg1 = null, resolvedArg2 = null;

		arg1 = eval(tree.getCar(), env);
		arg2 = eval(tree.getCdr(), env);

		if (arg1.getType() != INTEGER && arg1.getType() != REAL)
			resolvedArg1 = env.getVal(arg1);

		if (arg2.getType() != INTEGER && arg2.getType() != REAL)
			resolvedArg2 = env.getVal(arg2);

		/* Resolved arg will either == the value held by its ID,
		 * or it will be null if arg1 was already in its final resolved
		 * form, or it will be unknown if it's a var whose val couldn't be
		 * found
		 */
		if (resolvedArg1 != null)
		{
			if (resolvedArg1.getType() == UNKNOWN)	// If its a var expr, return the tree
				return tree;
		}

		if (resolvedArg2 != null)
		{
			if (resolvedArg2.getType() == UNKNOWN)
				return tree;
		}
// System.out.println("tree is: ");
// tree.display(); System.out.println();
// tree.getCar().display(); System.out.println();
// tree.getCdr().display(); System.out.println();
// System.out.println("env prior to getVal() "); env.displayEnv(1);
// env.getVal(new Lexeme(ID, "a"));
//System.out.print("arg1 is : "); arg1.display(); System.out.println();
//System.out.print(" and arg2 is: "); arg2.display(); System.out.println();
//System.out.print("env given to evalOp() "); env.displayEnv(1);
// System.out.println("value of a is: " + env.getVal(new Lexeme(ID, "a")).getInt());		

		String operandType = null;
		if (resolvedArg1 != null) operandType = resolvedArg1.getType();
		String operatorType = tree.getType();
		
		if (resolvedArg1 != null)
			arg1 = resolvedArg1;
		if (resolvedArg2 != null)
			arg2 = resolvedArg2;

		if (operandType == INTEGER)
		{
			int a0 = arg1.getInt();
			int a1 = arg2.getInt();

			if (operatorType == PLUS)
				return new Lexeme(operandType, a0+a1);
			else if (operatorType == MINUS)
				return new Lexeme(operandType, a0-a1);
			else if (operatorType == TIMES)
				return new Lexeme(operandType, a0*a1);
			else if (operatorType == DIVIDE)
				return new Lexeme(operandType, a0/a1);
			else if (operatorType == INCREMENT)
				return new Lexeme (operandType, a0+1);
			else if (operatorType == DECREMENT)
				return new Lexeme(operandType, a0-1);
			else if (operatorType == GREATER_THAN)
				return new Lexeme(operandType, a0>a1);		// Returns 0 or 1 based on bool
			else if (operatorType == LESS_THAN)
				return new Lexeme(operandType, a0<a1);
			else if (operatorType == EQUAL_TO)
				return new Lexeme(operandType, a0 == a1);
			else if (operatorType == GT_EQUAL)
				return new Lexeme(operandType, a0>=a1);
			else if (operatorType == LT_EQUAL)
				return new Lexeme(operandType, a0<=a1);
			else if (operatorType == MODULO)
				return new Lexeme(operandType, a0%a1);
			else if (operatorType == PLUS_EQUAL)
				return new Lexeme(operandType, a0+=a1);
			else if (operatorType == MINUS_EQUAL)
				return new Lexeme(operandType, a0-=a1);
			else
				return new Lexeme(UNKNOWN);
		}
		else if (operandType == REAL)
		{
			double a0 = arg1.getReal();
			double a1 = arg2.getReal();

			if (operatorType == PLUS)
				return new Lexeme(operandType, a0+a1);
			else if (operatorType == MINUS)
				return new Lexeme(operandType, a0-a1);
			else if (operatorType == TIMES)
				return new Lexeme(operandType, a0*a1);
			else if (operatorType == DIVIDE)
				return new Lexeme(operandType, a0/a1);
			else if (operatorType == INCREMENT)
				return new Lexeme (operandType, a0+1);
			else if (operatorType == DECREMENT)
				return new Lexeme(operandType, a0-1);
			else if (operatorType == GREATER_THAN)
				return new Lexeme(operandType, a0>a1);		// Returns 0 or 1 based on bool
			else if (operatorType == LESS_THAN)
				return new Lexeme(operandType, a0<a1);
			else if (operatorType == EQUAL_TO)
				return new Lexeme(operandType, a0 == a1);
			else if (operatorType == GT_EQUAL)
				return new Lexeme(operandType, a0>=a1);
			else if (operatorType == LT_EQUAL)
				return new Lexeme(operandType, a0<=a1);
			else if (operatorType == MODULO)
				return new Lexeme(operandType, a0%a1);
			else if (operatorType == PLUS_EQUAL)
				return new Lexeme(operandType, a0+=a1);
			else if (operatorType == MINUS_EQUAL)
				return new Lexeme(operandType, a0-=a1);
			else
				return new Lexeme(UNKNOWN);
		}

		return null;				// This was placed here to circumvent error. If time replace all those returns with assignments and finish with one return

	}

	/**
	 * Evaluates the constructor of a function/method call			FIXME: I think...?
	 * @closure Parent closure of the constructor
	 * @env Corresponding environment
	 * @return The extended environment
	 */
	private Lexeme evalConstructor(Lexeme closure, Environment env)
	{
		Environment senv = new Environment(closure.getCar());
		Environment xenv = new Environment(senv.extendEnv(null, null));
		Lexeme body = closure.getCdr().getCdr();
		eval(body, xenv);
		return xenv.getEnv();
	}
/*
	/**
	 * Evaluates the arguments as a helper to evalFunctionCall
	 * @args Root of args to be evaluated
	 * @env The corresponding environment
	 * @return Root of the list of expressions 
	 *
	private Lexeme evalArgs(Lexeme args, Environment env)
	{
		if (args == null)
			return null;
		else
			return eval(args, env);		//returns the expression list of the function call
//			return cons(GLUE, eval(args.getCar(), env), evalArgs(args.getCdr(), env)); //FIXME: this guy was in the else statement
	}
*/

	/**
	 * Evaluates the actual use of a functioncall
	 * @tree Root of function tree to use for evaluation
	 * @env Environment corresponding to the function call in tree
	 * @return Root Lexeme
	 */
	private Lexeme evalFunctionCall(Lexeme tree, Environment env)
	{
// System.out.print("at beginning of function call, ");
// env.displayEnv(1);
		Lexeme closure = lookup(tree.getCar(), env);
		Lexeme args = getArgs(tree, env);
// System.out.println("+++args are of type: " + args.getType());
//System.out.println("===args are: "); args.getCar().getCar().getCar().display(); System.out.println();
// System.out.println("while eval(args, env) is: ");
// eval(args, env).display();
//		if (isBuiltIn(closure)) return evalBuiltIn(closure, args);	//FIXME: uncomment, need to write ebuiltin
		Environment senv = new Environment(closure.getCar());
		Lexeme params = getParams(closure);
// System.out.println("params are: ");
// params.getCar().display();
// System.out.println("args are of type: ");
// args.display();
		Environment lenv = new Environment(senv.extendEnv(params, args));
		Lexeme body = getBody(closure);
// System.out.print("lenv is: "); lenv.displayEnv(1);	
//  lenv.getVal(new Lexeme(ID, "a")).display();
		return eval(body, lenv);
	}
/*
	private Lexeme evalMethodCall(Lexeme tree, Environment env)
	{
		Lexeme closure = eval(tree.getCar(), env);
	}
*/
	private Lexeme evalIDstart(Lexeme tree, Environment env)
	{
		Lexeme id = null;

		id = eval(tree.getCar(), env);

		if (tree.getCdr() != null)
		{
			if (tree.getCdr().getType() == INCREMENT || tree.getCdr().getType() == DECREMENT)
				return evalOp(tree, env);
			else if (tree.getCdr().getType() == GLUE)
			{
				/* Means that there is an id op unary */
				if (isOperator(tree.getCdr().getCar()))
					return eval(tree.getCdr().getCar(), env);
			}
			else
				return eval(tree.getCdr(), env);
		}

		return id;
	}

	/**
	 * Eval method for an import definition
	 * @tree Root of the importdef
	 * @env Corresponding environment
	 * @return eval of the ID lexeme to the left
	 */
	private Lexeme evalImport(Lexeme tree, Environment env)
	{
		return eval(tree.getCar(), env);
	}

	/**
	 * Eval method for a parameter list
	 * @tree Root of the paramList
	 * @env Corresponding environment
	 * @return id held in the paramList
	 */
	private Lexeme evalParamList(Lexeme tree, Environment env)
	{
		Lexeme id = null, plist = null;

		id = eval(tree.getCar(), env);

		if (tree.getCdr() != null)
			return evalParamList(tree.getCdr(), env);

		return id;
	}

	/**
	 * Eval method for expression list
	 * @tree Root of the expression list
	 * @env Corresponding environment
	 * @return first evaluated expression
	 */
	private Lexeme evalExprList(Lexeme tree, Environment env)
	{
		Lexeme expr = null, elist = null;

		expr = eval(tree.getCar(), env);
		if (tree.getCdr() != null)
			elist = eval(tree.getCdr(), env);
	
		return expr;
	}
	
	/**
	 * Eval method for if statement
	 * @tree Root of the if statement
	 * @env Corresponding environment
	 * @return The correct block
	 */
	private Lexeme evalIf(Lexeme tree, Environment env)
	{
		Lexeme expr = null, block = null, elif = null;

		expr = eval(tree.getCar(), env);
		
		/* If expr is true */
		if (expr.getInt() == 1)
			block = eval(tree.getCdr().getCar(), env);
		else
		{
			if (tree.getCdr().getCdr() != null)
				block = eval(tree.getCdr().getCdr(), env);
		}
		
		return block;
	}

	/**
	 * Eval method for else if statement
	 * @tree Root of the elif
	 * @env Corresponding environment
	 * @return The correct block
	 */
	private Lexeme evalElseIf(Lexeme tree, Environment env)
	{
		Lexeme ifstmt = null, block = null;

		return eval(tree.getCar(), env);
	}

	/**
	 * Eval method for while loop
	 * @tree Root of the while loop parse tree
	 * @env Corresponding environment
	 * @return The block of the while loop
	 */
	private Lexeme evalWhile(Lexeme tree, Environment env)
	{
		Lexeme expr = null, block = null;

		expr = eval(tree.getCar(), env);

		if (expr.getInt() == 1)
			block = eval(tree.getCdr(), env);
		else
			return null;

		return eval(tree, env);
	}

	/**
	 * Eval method for for loop
	 * @tree Root of the for loop parse tree
	 * @env Corresponding environment
	 * @return The block of the for loop
	 */
	private Lexeme evalFor(Lexeme tree, Environment env)
	{
		Lexeme expr = null, expr1 = null, expr2 = null, block = null;

		expr = eval(tree.getCar().getCar(), env);
		expr1 = eval(tree.getCar().getCdr(), env);
		expr2 = eval(tree.getCdr().getCar(), env);

		/* If expression is true */
		if (expr1.getInt() == 1)
			block = eval(tree.getCdr().getCdr(), env);
		else
			return null;

		return eval(tree, env);
	}

	/**
	 * Main evaluator method
	 * @tree Root of the tree to be evaluated
	 * @env Corresponding environment to tree
	 * @return root of another Lexeme tree
	 */
	private Lexeme eval(Lexeme tree, Environment env)
	{
 tree.display();System.out.println();
		switch (tree.getType())
		{
			case PROG:
				return evalProg(tree, env);
			case DEF:
				return eval(tree.getCar(), env);
			case INTEGER:
			case REAL:
			case STRING:
			case ID:
				return tree;
			case VARDEF:
				return evalVarDef(tree, env);
			case FUNCDEF:
				return evalFuncDef(tree, env);
			case BLOCK:
				return evalBlock(tree, env);
			case STATEMENTS:
				return evalStatements(tree, env);
			case STATEMENT:
				return evalStatement(tree, env);
			case RETSTMNT:
				return evalRetStmt(tree, env);
			case CLASSDEF:
				return evalClassDef(tree, env);
			case EXPRDEF:
				return evalExprDef(tree, env);
			case UNARY:
				return evalUnary(tree, env);
			case FUNCCALL:
				return evalFunctionCall(tree, env);
			case RUNDEF:
				return evalRunDef(tree, env);
			case IDSTART:
				return evalIDstart(tree, env);
			case IMPORTDEF:
				return evalImport(tree, env);
			case PARAMLIST:
				return evalParamList(tree, env);
			case EXPRLIST:
				return evalExprList(tree, env);
			case IFSTMNT:
				return evalIf(tree, env);
			case ELSEIF:
				return evalElseIf(tree, env);
			case WHILELOOP:
				return evalWhile(tree, env);
			case FORLOOP:
				return evalFor(tree, env);
			case PLUS:
			case MINUS:
			case TIMES:
			case DIVIDE:
			case MODULO:
				return evalOp(tree, env);
				
		}

		return null;							//FIXME: placeholder null return
	}


/***** Public Methods *****/
	public static void main(String[] args) throws IOException
	{
		checkCmdArgs(args);

		File file = openFile(args[0]);
		PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));

		Evaluator e = new Evaluator();

		Lexer i = new Lexer(stream);
		Lexeme curr = i.lex();

		Parser p = new Parser(curr, stream);


		Environment env = new Environment();
		Lexeme tree = p.program();
		e.eval(tree, env);

		System.out.println();
		env.displayEnv(1);									//FIXME: this line for testing purposes only

/*
		tree.display();
		tree.getCar().display();
		tree.getCar().getCar().display();
		tree.getCar().getCar().getCar().display();
		tree.getCar().getCar().getCdr().display();
		tree.getCar().getCar().getCdr().getCdr().display();	//block
		tree.getCar().getCar().getCdr().getCdr().getCar().display();	//statements
//		tree.getCar().getCar().getCdr().getCdr().getCar().getCar().display();
		Lexeme statement = tree.getCar().getCar().getCdr().getCdr().getCar().getCar();
		statement.display();
		statement.getCar().display();
		statement.getCar().getCar().display();
		statement.getCar().getCdr().display();
		Lexeme un = statement.getCar().getCdr().getCar();
		un.display();
		un.getCar().display();
//		tree.getCar().getCar().getCdr().getCar().getCar().getCar().getCar().display();
//		tree.getCar().getCar().getCdr().getCar().getCar().getCar().getCar().getCar().display();
//		env.displayEnv(1);
*/

		stream.close();
	}
}


