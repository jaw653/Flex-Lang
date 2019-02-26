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

// currently a variable defined inside an if loop will belong to the overarching function
// currently do not allow for negative numbers
// add that command line arguments are implicitly included in Flex, no need to have them as params
package eval;

import lex.*;
import parse.*;
import env.*;

import java.io.*;
import java.util.Scanner;

public class Evaluator implements Types
{

	static int cmdLineNum;
	static String[] cmdArgs;

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
		if (args.length < 1)
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
	private Lexeme exprListToValList(Lexeme head, Environment env) throws IOException
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
	private Lexeme getArgs(Lexeme tree, Environment env) throws IOException
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

	/**
	 * Gets the constructor for a given class
	 * @id Name of the class, and therefore the constructor
	 * @env Corresponding environment
	 * @return Constructor function
	 */
	private Lexeme getConstructor(Lexeme id, Environment env) throws IOException
	{
		System.out.println("searching env: "); env.displayEnv(1);
		return env.getVal(id);
	}



/***** Private Methods *****/
	/**
	 * Gets the value of the ID at tree root from the environment
	 * @tree Lexeme for which to search environment
	 * @env Environment for which to search for Lexeme id
	 * @return The value of the id at tree
	 */
	private Lexeme lookup(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalProg(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalVarDef(Lexeme tree, Environment env) throws IOException
	{
		Lexeme id = null, val = null;
	
		id = eval(tree.getCar(), env);
		
		if (tree.getCdr() != null)
		{
			/* If not an object variable */
			//if (tree.getCdr().getType() != GLUE)
				val = eval(tree.getCdr(), env);
			//else
			//{
				
				// Call the instantiation method of the obj with id cdr()car()
				// do the above with optexprlist at cdr()cdr()
			//}
		}
		
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
	private Lexeme evalFuncDef(Lexeme tree, Environment env) throws IOException
	{
		Lexeme closure = null, paramList = null, block = null;

		closure = cons(CLOSURE, env.getEnv(), tree);
// System.out.println("funcdef being inserted: " + tree.getCar().getName());
		env.insertEnv(tree.getCar(), closure);
		
		if (tree.getCdr().getCar() != null) paramList = eval(tree.getCdr().getCar(), env);
//		block = eval(tree.getCdr().getCdr(), env);
		
		return closure;
	}

	/**
	 * Evaluates a lambda/anonymous function
	 * @tree Root of the lambda tree
	 * @env Corresponding env
	 * @return Closure of the lambda function
	 */
	private Lexeme evalLambda(Lexeme tree, Environment env) throws IOException
	{
		Lexeme closure = null, paramList = null;

		closure = cons(CLOSURE, env.getEnv(), tree);
	
		if (tree.getCdr().getCar() != null) paramList = eval(tree.getCdr().getCar(),env);

		return closure;
	}

	/**
	 * Evaluates the block of the run function; the equivalent of c main
	 * @tree Root of the run function parse tree
	 * @env Corresponding environment
	 * @return The value of the evaluated block
	 */
	private Lexeme evalRunDef(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalBlock(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalStatements(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalStatement(Lexeme tree, Environment env) throws IOException
	{
		return eval(tree.getCar(), env);
	}

	/**
	 * Eval method for while loop
	 * @tree Root of the while loop parse tree
	 * @env Corresponding environment
	 * @return The block of the while loop
	 */
	private Lexeme evalRetStmt(Lexeme tree, Environment env) throws IOException
	{
		return eval(tree.getCar(), env);
	}

	/**
	 * Adds class variables to its scope
	 * @tree Root of CLASSDEF tree to use for environmenet insertion
	 * @env The environment into which the newly created tree will be inserted
	 * @return The inserted Lexeme
	 */
	private Lexeme evalClassDef(Lexeme tree, Environment env) throws IOException
	{
		Environment classEnv = new Environment(env.extendEnv(null, null));
		Lexeme oclosure = cons(OCLOSURE, classEnv.getEnv(), tree);
		Lexeme classBody = eval(tree.getCdr(), classEnv);			//Adds all vars and funcdefs to env
		classEnv.insertEnv(tree.getCar(), oclosure);
env.displayEnv(1);
//		Environment classEnv = new Environment(env.extendEnv(classBody, null));
//		classEnv.insertEnv(tree.getCar(), oclosure);					//FIXME: should this be OCLOSURE?
		return oclosure;
	}

	/**
	 * Evaluator for class instantiation
	 * @tree Root of the class inst. parse tree
	 * @env Corresponding environment
	 * @return ...
	 */
	private Lexeme evalClassInst(Lexeme tree, Environment env) throws IOException
	{
		Lexeme oclosure = lookup(tree.getCdr().getCar(), env);
		Environment senv = new Environment(oclosure.getCar());
		Environment lenv = new Environment(senv.extendEnv(null, null));
		Lexeme body = oclosure.getCdr().getCdr();

		lenv.insertEnv(new Lexeme(ID, "this"), lenv.getEnv());
System.out.println("lenv is: "); lenv.displayEnv(1);
		return oclosure;
//		Lexeme constructor = getConstructor(tree.getCdr().getCar(), env);
//System.out.println("constructor is: "); constructor.display(); System.out.println();
//		return null;

/*
		Lexeme args = getArgs(tree, env);

		if (tree.getCar().getName().equals("print"))
			return evalPrint(args);
		
		Lexeme closure = lookup(tree.getCar(), env);
		Environment senv = new Environment(closure.getCar());
		Lexeme params = getParams(closure);
		Environment lenv = new Environment(senv.extendEnv(params, args));
		Lexeme body = getBody(closure);

		/* Insert variable that points to the local environment *
//		lenv.insertEnv(new Lexeme(ID, "this"), lenv.getEnv());
		
//		return eval(body, lenv);
*/
	}

	private Lexeme evalObjMem(Lexeme tree, Environment env) throws IOException
	{
		Lexeme obj = eval(tree.getCar(), env);
		return eval(tree.getCdr(), new Environment(obj));
/*
System.out.println("tree car is: " + tree.getCar().getName());
		Lexeme oclosure = lookup(tree.getCar(), env);
System.out.println("oclosure is: " + oclosure.getName());
System.out.println("env for evalobjmem is: ");
env.displayEnv(1);
return null;
*/
	}

	/**
	 * Evaluates an EXPRDEF
	 * @tree Root of the EXPRDEF tree
	 * @env Corresponding environment
	 * @return The value of the unary at the bottom of the expression
	 */
	private Lexeme evalExprDef(Lexeme tree, Environment env) throws IOException
	{
		Lexeme unary = null, operator = null, expr = null, opTree = null;

		unary = eval(tree.getCar(), env);

		if (tree.getCdr() != null)
		{
			/* Make a new operation tree to eval with evalOp */
			opTree = new Lexeme(tree.getCdr().getCar().getType());
			opTree.setCar(unary);
			opTree.setCdr(eval(tree.getCdr().getCdr(), env));
			return eval(opTree, env);
		}

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
	private Lexeme evalUnary(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalOp(Lexeme tree, Environment env) throws IOException
	{
		Lexeme arg1 = null, arg2 = null;
		Lexeme resolvedArg1 = null, resolvedArg2 = null;

		arg1 = eval(tree.getCar(), env);
		arg2 = eval(tree.getCdr(), env);

		/* Don't want to resolve the value of a var if we're assigning */
		if (arg1.getType() != INTEGER && arg1.getType() != REAL && tree.getType() != ASSIGN)
			resolvedArg1 = env.getVal(arg1);

		if (arg2.getType() != INTEGER && arg2.getType() != REAL && tree.getType() != ASSIGN)
			resolvedArg2 = env.getVal(arg2);
/*
System.out.println("arg1 is: ");
arg1.display(); System.out.println();
System.out.println("arg2 is: ");
arg2.display(); System.out.println();

System.out.println("resolvedArg1 is: ");
resolvedArg1.display(); System.out.println();
System.out.println("resolvedArg2 is: ");
resolvedArg2.display(); System.out.println();
*/
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


//		if (resolvedArg1 != null) operandType = resolvedArg1.getType();
		
		
		if (resolvedArg1 != null)
			arg1 = resolvedArg1;
		if (resolvedArg2 != null)
			arg2 = resolvedArg2;

		String operatorType = tree.getType();
		String operandType = arg1.getType();
// System.out.println("operand type is: " + operandType);
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
				return new Lexeme(operandType, a0+1);
			else if (operatorType == DECREMENT)
				return new Lexeme(operandType, a0-1);
			else if (operatorType == GREATER_THAN)
				return new Lexeme(operandType, a0>a1);		// Returns 0 or 1 based on bool
			else if (operatorType == LESS_THAN)
				return new Lexeme(operandType, a0<a1);
			else if (operatorType == EQUAL_TO)
				return new Lexeme(operandType, a0 == a1);
			else if (operatorType == NOT_EQUAL)
				return new Lexeme(operandType, a0 != a1);
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
				return new Lexeme(operandType, a0+1);
			else if (operatorType == DECREMENT)
				return new Lexeme(operandType, a0-1);
			else if (operatorType == GREATER_THAN)
				return new Lexeme(operandType, a0>a1);		// Returns 0 or 1 based on bool
			else if (operatorType == LESS_THAN)
				return new Lexeme(operandType, a0<a1);
			else if (operatorType == EQUAL_TO)
				return new Lexeme(operandType, a0 == a1);
			else if (operatorType == NOT_EQUAL)
				return new Lexeme(operandType, a0 != a1);
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
/*
		else if (operandType == ID)
		{
			Lexeme id = eval(tree.getCar(), env);
// System.out.print("id is: "); id.display(); System.out.println();
			env.updateVal(id, eval(tree.getCdr(), env));
		}
*/

		return null;				// This was placed here to circumvent error. If time replace all those returns with assignments and finish with one return

	}

	private Lexeme evalAssign(Lexeme tree, Environment env) throws IOException
	{
		Lexeme id = eval(tree.getCar(), env);
		Lexeme result = eval(tree.getCdr(), env);
System.out.println("tree car type is: " + tree.getCar().getType());	
		if (tree.getCar().getType() != OBJMEM)
			env.updateVal(id, eval(tree.getCdr(), env));
		else
		{
			Lexeme obj = eval(tree.getCar().getCar(), env);
			Environment objEnv = new Environment(obj);
			objEnv.updateVal(tree.getCdr().getCar(), result);

System.out.println("objenv is: ");
objEnv.displayEnv(1);
		}
/*
		Lexeme result = eval(tree.getCdr(), env);
		
		if (tree.getCar().getType() == VARIABLE)
			env.updateE
*/
		return result;
	}

	/**
	 * Evaluates the constructor of a function/method call			FIXME: I think...?
	 * @closure Parent closure of the constructor
	 * @env Corresponding environment
	 * @return The extended environment
	 */
	private Lexeme evalConstructor(Lexeme closure, Environment env) throws IOException
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
	private Lexeme evalFunctionCall(Lexeme tree, Environment env) throws IOException
	{
		Lexeme args = getArgs(tree, env);

		if (tree.getCar().getName().equals("print"))
			return evalPrint(args, env);
		else if (tree.getCar().getName().equals("newArray"))
			return evalNewArray(args);
		else if (tree.getCar().getName().equals("getArray"))
			return evalGetArray(args, env);
		else if (tree.getCar().getName().equals("setArray"))
			return evalSetArray(args, env);
		else if (tree.getCar().getName().equals("getArgCount"))
			return evalGetArgCount();
		else if (tree.getCar().getName().equals("getArg"))
			return evalGetArg(args, env);
		else if (tree.getCar().getName().equals("openReadFile"))
			return evalOpenReadFile(args, env);
		else if (tree.getCar().getName().equals("readInt"))
			return evalReadInt(args, env);
		
		Lexeme closure = lookup(tree.getCar(), env);
		Environment senv = new Environment(closure.getCar());
		Lexeme params = getParams(closure);
		Environment lenv = new Environment(senv.extendEnv(params, args));
		Lexeme body = getBody(closure);

		/* Insert variable that points to the local environment */
		lenv.insertEnv(new Lexeme(ID, "this"), lenv.getEnv());
		
		return eval(body, lenv);
	}

/***** Builtin functions *****/
	private Lexeme evalPrint(Lexeme args, Environment env) throws IOException
	{
		Lexeme toPrint = eval(args.getCar(), env);
		
		if (toPrint.getType() == ID)
			toPrint = lookup(toPrint, env);

		switch(toPrint.getType())
		{
			case STRING:
				System.out.println(toPrint.getName());
				break;
			case INTEGER:
				System.out.println(toPrint.getInt());
				break;
			case REAL:
				System.out.println(toPrint.getReal());
				break;
			default:
				System.out.println("Cannot print type " + toPrint.getType());
		}
		
		return null;
	}

	/**
	 * Allocates array
	 * @args The size of the array
	 * @return Lexeme with allocated array inside
	 */
	private Lexeme evalNewArray(Lexeme args)
	{
		int size = args.getCar().getInt();
		Lexeme arr = new Lexeme(ARRAY);
		arr.allocateArr(size);
		return arr;
	}

	/**
	 * Gets desired element from array
	 * @args The array from which to take the element along with the index
	 * @return New Lexeme of Integer type with the value of the array at desired index
	 */	 
	private Lexeme evalGetArray(Lexeme args, Environment env) throws IOException
	{
		Lexeme arr = lookup(args.getCar(), env);
		Lexeme index = args.getCdr().getCar();
		int i = index.getInt();
		
		return new Lexeme(INTEGER, arr.getArr(i));
	}

	/**
	 * Sets val at desired index
	 * @args The array, value and index
	 * @return The value substituted
	 */
	private Lexeme evalSetArray(Lexeme args, Environment env) throws IOException
	{
		Lexeme arr = lookup(args.getCar(), env);
		Lexeme index = args.getCdr().getCar();
		Lexeme val = args.getCdr().getCdr().getCar();
		int i = index.getInt();
		arr.setArr(i, val);
		return val;
	}

	/**
	 * Gets the number of command line arguments
	 * @return Integer Lexeme with number of cmd line args
	 */
	private Lexeme evalGetArgCount()
	{
		return new Lexeme(INTEGER, cmdLineNum);
	}

	/**
	 * Gets the command line arg at the given index
	 * @args Root of argument tree for function
	 * @env Corresponding environment
	 * @return String Lexeme of arg at given index
	 */
	private Lexeme evalGetArg(Lexeme args, Environment env) throws IOException
	{
		int index = eval(args.getCar(), env).getInt();
		return new Lexeme(STRING, cmdArgs[index]);
	}

	/**
	 * Opens a new file for reading
	 * @args Args with filename
	 * @env Corresponding environment
	 * @return Lexeme with filepointer value and type
	 */
	private Lexeme evalOpenReadFile(Lexeme args, Environment env) throws IOException
	{
		Lexeme fileIDLexeme = eval(args.getCar(), env);
		Lexeme fileNameLexeme = lookup(fileIDLexeme, env);
		String filename = fileNameLexeme.getName();
		
		File fp = new File(filename);

		Scanner sc = new Scanner(fp);
		return new Lexeme(FILE_POINTER, fp);
	}

	/**
	 * Reads an integer from the given file
	 * @args Filepointer from which to read the file
	 * @env Corresponding environment
	 * @return The read-in int as an Integer Lexeme
	 */
	private Lexeme evalReadInt(Lexeme args, Environment env) throws IOException
	{
		int i = 0;

		Lexeme fileLexeme = null;
		Lexeme fileID = eval(args.getCar(), env);

		if (fileID.getType() == ID)
			fileLexeme = lookup(fileID, env);

		File file = fileLexeme.getFp();
		
		Scanner sc = new Scanner(file);

		if (sc.hasNextInt())
			i = sc.nextInt();
		else
		{
			System.out.println("Flex error: no more integers!");
			System.exit(-1);
		}

		return new Lexeme(INTEGER, i);
	}

	/*
	private Lexeme evalMethodCall(Lexeme tree, Environment env)
	{
		Lexeme closure = eval(tree.getCar(), env);
	}
*/
	private Lexeme evalIDstart(Lexeme tree, Environment env) throws IOException
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
			else if (tree.getCar().getType() == ID && tree.getCdr().getType() == ID)
			{
				/* If a dot access for objects */
				Environment obj = new Environment(eval(tree.getCar(), env));
				return eval(tree.getCdr(), obj);
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
	private Lexeme evalImport(Lexeme tree, Environment env) throws IOException
	{
		return eval(tree.getCar(), env);
	}

	/**
	 * Eval method for a parameter list
	 * @tree Root of the paramList
	 * @env Corresponding environment
	 * @return id held in the paramList
	 */
	private Lexeme evalParamList(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalExprList(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalIf(Lexeme tree, Environment env) throws IOException
	{
		Lexeme expr = null, block = null, elif = null;

		expr = eval(tree.getCar(), env);
		
		/* If expr is true */
		if (expr.getInt() == 1)
			block = eval(tree.getCdr().getCar(), env);
		else
		{
			if (tree.getCdr().getCdr() != null)
				elif = eval(tree.getCdr().getCdr(), env);	//Eval else if
		}
		
		return block;
	}

	/**
	 * Eval method for else if statement
	 * @tree Root of the elif
	 * @env Corresponding environment
	 * @return The correct block
	 */
	private Lexeme evalElseIf(Lexeme tree, Environment env) throws IOException
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
	private Lexeme evalWhile(Lexeme tree, Environment env) throws IOException
	{
		Lexeme expr = null, block = null;

		expr = eval(tree.getCar(), env);

		while (expr.getInt() == 1)
		{
			block = eval(tree.getCdr(), env);
			expr = eval(tree.getCar(), env);
		}
		
		return expr;
	}

	/**
	 * Eval method for for loop
	 * @tree Root of the for loop parse tree
	 * @env Corresponding environment
	 * @return The block of the for loop
	 */
	private Lexeme evalFor(Lexeme tree, Environment env) throws IOException
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
	private Lexeme eval(Lexeme tree, Environment env) throws IOException
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
			case FILE_POINTER:
				return tree;
			case VARDEF:
				return evalVarDef(tree, env);
			case FUNCDEF:
				return evalFuncDef(tree, env);
			case LAMBDA:
				return evalLambda(tree, env);
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
			case CLASS_INSTANTIATION:
				return evalClassInst(tree, env);
			case OBJMEM:
				return evalObjMem(tree, env);
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
			case LESS_THAN:
			case GREATER_THAN:
			case LT_EQUAL:
			case GT_EQUAL:
			case EQUAL_TO:
				return evalOp(tree, env);
			case ASSIGN:
				return evalAssign(tree, env);
				
		}

		return null;							//FIXME: placeholder null return
	}


/***** Public Methods *****/
	public static void main(String[] args) throws IOException
	{
		checkCmdArgs(args);

		/* Set global command line variables */
		cmdLineNum = args.length;
		cmdArgs = args.clone();
// System.out.println("num cmd line args is: " + cmdLineNum);
// System.out.println("cmdargs is: " + cmdArgs[0]);
//		System.arraycopy(args, 0, cmdArgs, 0, args.length);

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


