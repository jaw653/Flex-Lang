/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Pretty Printer class
 */

// going to need to fix for loop tree structure so that you know when to print semicolons
// might need to fix idStart tree as well so that I can properly print class method call

package prettyPrint;

import lex.*;
import parse.*;
import recognize.*;

import java.io.*;

public class PrettyPrinter implements Types
{
	/**
	 * Default constructor
	 */
	public PrettyPrinter()
	{
	}

/***** Private Methods *****/
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
	 * Pretty print method for overarching program parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printProg(Lexeme tree)
	{
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for def parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printDef(Lexeme tree)
	{
		prettyPrint(tree.getCar());
	}

	/**
	 * Pretty print method for vardef parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printVarDef(Lexeme tree)
	{
		System.out.print("var " + tree.getCar().getName() + " = ");
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for import def parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printImport(Lexeme tree)
	{
		System.out.println("bundle \"" + tree.getCar().getName() + "\"");
	}

	/**
	 * Pretty print method for expression parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printExpr(Lexeme tree)
	{
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for paramList parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printParamList(Lexeme tree)
	{
		System.out.print("var " + tree.getCar().getName());

		if (tree.getCdr() != null)
			System.out.print(", ");

		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for block parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printBlock(Lexeme tree)
	{
		System.out.println("{");
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
		System.out.println("}")
	}

	/**
	 * Pretty print method for statements parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printStatements(Lexeme tree)
	{
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for statement parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printStmnt(Lexeme tree)
	{
		prettyPrint(tree.getCar());
	}

	/**
	 * Pretty print method for return statement parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printRetStatement(Lexeme tree)
	{
		System.out.println("return " + prettyPrint(tree.getCar()));
	}

	/**
	 * Pretty print method for unary parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printUnary(Lexeme tree)
	{
		if (tree.getCar().getType() == EXPRDEF)
		{
			System.out.print("(" + prettyPrint(tree.getCar()) + ")");
		}
		else
		{
			prettyPrint(tree.getCar());
			prettyPrint(tree.getCdr());
		}
	}

	/**
	 * Pretty print method for operator parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printOp(Lexeme tree)
	{
		prettyPrint(tree.getCar());

		switch (tree.getType())
		{
			case "PLUS":
				System.out.print(" + ");
				break;
			case "MINUS":
				System.out.print(" - ");
				break;
			case "TIMES":
				System.out.print(" * ");
				break;
			case "DIVIDE":
				System.out.print(" / ");
				break;
			case "MODULO":
				System.out.print(" % ");
				break;
			case "GREATER_THAN":
				System.out.print(" > ");
				break;
			case "GT_EQUAL":
				System.out.print(" >= ");
				break;
			case "LESS_THAN":
				System.out.print(" < ");
				break;
			case "LT_EQUAL":
				System.out.print(" <= ");
				break;
			case "EQUAL_TO":
				System.out.print(" == ");
				break;
			case "PLUS_EQUAL":
				System.out.print(" -= ");
				break;
			case "MINUS_EQUAL":
				System.out.print(" -= ");
				break;
			case "ASSIGN":
				System.out.print(" = ");
				break;
		}

		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for if statement parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printIf(Lexeme tree)
	{
		System.out.print("if (");
		prettyPrint(tree.getCar());
		System.out.print(")");
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for else if parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printElseIf(Lexeme tree)
	{
		System.out.print("else ");
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for while loop parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printWhile(Lexeme tree)
	{
		System.out.print("while (");
		prettyPrint(tree.getCar());
		System.out.print(")");
		prettyPrint(tree.getCdr());
	}

	/**													//FIXME: going to need to fix the structure of this guy so that I can correctly print syntactic sugar
	 * Pretty print method for for loop parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printFor(Lexeme tree)
	{
		System.out.print("for (");
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCar());
	}

	/**
	 * Pretty print method for expressionList parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printExprList(Lexeme tree)
	{
		prettyPrint(tree.getCar());
		if (exprList != null)
		{
			System.out.print(", ");
			prettyPrint(tree.getCdr());
		}
	}

	/**
	 * Pretty print method for idStart parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printIDstart(Lexeme tree)
	{
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for function definition
	 * @tree The Lexeme tree to print
	 */
	private void printFunction(Lexeme tree)
	{
		System.out.print("define function ");
		prettyPrint(tree.getCar());
		System.out.print("(");
		prettyPrint(tree.getCdr().getCar());
		System.out.print(")");
		prettyPrint(tree.getCdr().getCdr());
	}

	/**
	 * Pretty print method for class definition
	 * @tree The Lexeme tree to print
	 */
	private void printClass(Lexeme tree)
	{
		System.out.print("define class ");
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
	}

/*
	/**
	 * Pretty print method for statements
	 * @tree The Lexeme tree to print
	 *
	private void printStatements(Lexeme tree)
	{
		while (tree != null)
		{
			prettyPrint(tree.getCar());
			System.out.println();
			tree = tree.getCdr();
		}
	}
*/
	/**
	 * Pretty print method for IDlist
	 * @tree The Lexeme tree to print
	 */
	private void printIDlist(Lexeme tree)
	{
		while (tree != null)
		{
			prettyPrint(tree.getCar());
			if (tree.getCdr() != null) System.out.print(",");
			tree = tree.getCdr();
		}
	}

/***** Public Methods *****/
	public void prettyPrint(Lexeme tree)
	{
		System.out.println("tree is of type: " + tree.getType());
		if (tree.getType() == PROG) printProg(tree);
		else if (tree.getType() == DEF) printDef(tree);
		else if (tree.getType() == VARDEF) printVarDef(tree);
		else if (tree.getType() == FUNCDEF) printFunction(tree);
		else if (tree.getType() == CLASSDEF) printClass(tree);
		else if (tree.getType() == IMPORTDEF) printImport(tree);
		else if (tree.getType() == EXPRDEF) printExpr(tree);
		else if (tree.getType() == PARAMLIST) printParamList(tree);
		else if (tree.getType() == BLOCK) printBlock(tree);
		else if (tree.getType() == STATEMENTS) printStatements(tree);
		else if (tree.getType() == STATEMENT) printStmnt(tree);
		else if (tree.getType() == RETSTMNT) printRetStatement(tree);
		else if (tree.getType() == UNARY) printUnary(tree);

		else if (tree.getType() == PLUS || tree.getType() == MINUS ||
			tree.getType() == TIMES || tree.getType() == DIVIDE ||
			tree.getType() == MODULO || tree.getType() == GREATER_THAN ||
			tree.getType() == LESS_THAN || tree.getType() == GT_EQUAL ||
			tree.getType() == LT_EQUAL || tree.getType() == ASSIGN ||
			tree.getType() == EQUAL_TO || tree.getType() == PLUS_EQUAL ||
			tree.getType() == MINUS_EQUAL)
		{
			printOp(tree);
		}
		else if (tree.getType() == IFSTMNT) printif(tree);
		else if (tree.getType() == ELSEIF) printElseIf(tree);
		else if (tree.getType() == WHILELOOP) printWhile(tree);
		else if (tree.getType() == FORLOOP) printFor(tree);
		else if (tree.getType() == EXPRLIST) printExprList(tree);
		else if (tree.getType() == IDSTART) printIDstart(tree);

		else if (tree.getType() == STRING) System.out.println("\"" + tree.getName() + "\"");
		else if (tree.getType() == INTEGER) System.out.println(tree.getInt());
		else if (tree.getType() == REAL) System.out.println(tree.getReal());
//		else if (tree.getType() == IDLIST) printIDlist(tree);
		else if (tree.getType() == ID) System.out.print(tree.getName());

		else if (tree.getType() == GLUE)
		{
			prettyPrint(tree.getCar());
			prettyPrint(tree.getCdr());
		}

/*		FIXME: does this need to be here?
		else if (tree.getType() == VAR)
		{
			System.out.print("var " + tree.getCar().getName() + " = " + tree.getCdr().getInt());
			System.out.print()
		}
*/
/*		FIXME: don't think I need this...
		else if (tree.getType() == OPEN_PAREN)
		{
			System.out.print("(");
			prettyPrint(tree.getCdr());
			System.out.print(")");
		}
*/
/*		FIXME: The below may not be implemented in my language (ie can just create a negative via math rather than assignment)
		else if (tree.getType() == UMINUS)
		{
			System.out.print("-");
//			System.out.print(tree.getCdr().);		//FIXME: not sure if I need to print int, real, string after this, or just int/real
		}
*/
/*
		else if (tree.getType() == PLUS)
		{
			prettyPrint(tree.getCar());
			System.out.print(" + ");
			prettyPrint(tree.getCdr());
		}
		else if (tree.getType() == MINUS)
		{
			prettyPrint(tree.getCar());
			System.out.print(" - ");
			prettyPrint(tree.getCdr());
		}
		else if (tree.getType() == TIMES)
		{
			prettyPrint(tree.getCar());
			System.out.print(" * ");
			prettyPrint(tree.getCdr());
		}
		else if (tree.getType() == DIVIDE)
		{
			prettyPrint(tree.getCar());
			System.out.print(" / ");
			prettyPrint(tree.getCdr());
		}
		else if (tree.getType() == MODULO)
		{
			prettyPrint(tree.getCar());
			System.out.print(" / ");
			prettyPrint(tree.getCdr());
		}
*/
		// FIXME: should I have a main() here that goes through each lexeme?
		// FIXME: where should I be building the trees? In this file?
	}

	public static void main(String[] args) throws IOException
	{
		checkCmdArgs(args);

		File file = openFile(args[0]);
		PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));


		Lexer i = new Lexer(stream);
		Lexeme curr = i.lex();

		Parser p = new Parser(curr, stream);

		PrettyPrinter pp = new PrettyPrinter();

		pp.prettyPrint(p.program());

		stream.close();
	}
}
