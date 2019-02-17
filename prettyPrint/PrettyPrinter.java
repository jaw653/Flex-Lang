/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Pretty Printer class
 */


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
	 * Display method for parse tree
	 * @tree The root Lexeme parse tree to display
	 */
	private void displayTree(Lexeme tree)
	{
		if (tree.getCar() != null)
		{System.out.print("{Left"); displayTree(tree.getCar()); System.out.print("Left}");}
		System.out.print("<" + tree.getType() + ">");
		if (tree.getCdr() != null)
		{System.out.print("{Right"); displayTree(tree.getCdr()); System.out.print("Right}");}
	}

	/**
	 * Pretty print method for overarching program parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printProg(Lexeme tree)
	{
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for def parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printDef(Lexeme tree)
	{
		if (tree.getCar() != null) prettyPrint(tree.getCar());
	}

	/**
	 * Pretty print method for vardef parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printVarDef(Lexeme tree)
	{
		System.out.print("var " + tree.getCar().getName());
		if (tree.getCdr() != null)
		{
			System.out.print(" = ");

			if (tree.getCdr().getType() != GLUE)
				prettyPrint(tree.getCdr());			// Pretty print the expression
			else
			{
				/* Pretty print the object instantiation */
				System.out.print("new _");
				if (tree.getCdr().getCar() != null) prettyPrint(tree.getCdr().getCar());
				System.out.print("(");
				if (tree.getCdr().getCdr() != null) prettyPrint(tree.getCdr().getCdr());
				System.out.print(")_");
			}
		}

		System.out.print(";\n");
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
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		if (tree.getCdr() != null)
		{
			prettyPrint(tree.getCdr().getCar());
			prettyPrint(tree.getCdr().getCdr());
		}
	}

	/**
	 * Pretty print method for paramList parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printParamList(Lexeme tree)
	{
		System.out.print("var " + tree.getCar().getName());

		if (tree.getCdr() != null)
		{
			System.out.print(", ");
			if (tree.getCdr() != null) prettyPrint(tree.getCdr());
		}
	}

	/**
	 * Pretty print method for block parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printBlock(Lexeme tree)
	{
		System.out.println("\n{");
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
		System.out.println("}\n");
	}

	/**
	 * Pretty print method for statements parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printStatements(Lexeme tree)
	{
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for statement parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printStmnt(Lexeme tree)
	{
		if (tree.getCar() != null)
		{
			if (tree.getCar().getType() == EXPRDEF)
			{
				printExpr(tree.getCar());
				System.out.print(";\n");
			}
			else
				prettyPrint(tree.getCar());
		}
	}

	/**
	 * Pretty print method for return statement parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printRetStatement(Lexeme tree)
	{
		System.out.print("return ");
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		System.out.print(";\n");
	}

	/**
	 * Pretty print method for unary parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printUnary(Lexeme tree)
	{
		if (tree.getCar() != null)
		{
			if (tree.getCar().getType() == EXPRDEF)
			{
				System.out.print("(");
				if (tree.getCar() != null) prettyPrint(tree.getCar());
				System.out.print(")");
			}
			else
			{
				if (tree.getCar() != null) prettyPrint(tree.getCar());
				if (tree.getCdr() != null) prettyPrint(tree.getCdr());
			}
		}
	}

	/**
	 * Pretty print method for operator parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printOp(Lexeme tree)
	{
		if (tree.getCar() != null) prettyPrint(tree.getCar());

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
			case "INCREMENT":
				System.out.print("++");
				break;
			case "DECREMENT":
				System.out.print("--");
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

		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for if statement parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printIf(Lexeme tree)
	{
		System.out.print("if (");
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		System.out.print(")");
		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for else if parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printElseIf(Lexeme tree)
	{
		System.out.print("else ");
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for while loop parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printWhile(Lexeme tree)
	{
		System.out.print("while (");
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		System.out.print(")");
		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
	}

	/**													//FIXME: going to need to fix the structure of this guy so that I can correctly print syntactic sugar
	 * Pretty print method for for loop parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printFor(Lexeme tree)
	{
		System.out.print("for (");

		prettyPrint(tree.getCar().getCar());
		System.out.print("; ");

		prettyPrint(tree.getCar().getCdr());
		System.out.print("; ");

		prettyPrint(tree.getCdr().getCar().getCar().getCar());

		System.out.print(")");

		prettyPrint(tree.getCdr().getCdr());
	}

	/**
	 * Pretty print method for expressionList parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printExprList(Lexeme tree)
	{
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		if (tree.getCdr() != null)
		{
			System.out.print(", ");
			if (tree.getCdr() != null) prettyPrint(tree.getCdr());
		}
	}

	/**
	 * Pretty print method for idStart parse tree
	 * @tree The Lexeme tree to print
	 */
	private void printIDstart(Lexeme tree)
	{
		if (tree.getCdr() != null)
		{
			/* Printing a method call */
			if (tree.getCdr().getCar() != null &&
				tree.getCdr().getCar().getType() == ID)
			{
				if (tree.getCar() != null) prettyPrint(tree.getCar());
				System.out.print(".");
				prettyPrint(tree.getCdr().getCar());
				System.out.print("(");
				if (tree.getCdr().getCdr() != null) prettyPrint(tree.getCdr().getCdr());
				System.out.print(")");
			}
			else if (tree.getCdr() != null && tree.getCdr().getType() == GLUE)
			{
				if (tree.getCar() != null) prettyPrint(tree.getCar());
				if (tree.getCdr() != null && tree.getCdr().getCar() != null)
				{
					if (tree.getCdr().getCar().getType() == INCREMENT ||
						tree.getCdr().getCar().getType() == DECREMENT)
					{
						if (tree.getCdr().getCar().getType() == INCREMENT)
							System.out.print("++");
						else
							System.out.print("--");
					}

					prettyPrint(tree.getCdr().getCar());
				}
				if (tree.getCdr() != null && tree.getCdr().getCdr() != null) prettyPrint(tree.getCdr().getCdr());
			}
		}
		else
		{
			if (tree.getCar() != null) prettyPrint(tree.getCar());
			if (tree.getCdr() != null) prettyPrint(tree.getCdr());
		}

//		System.out.print(";\n");
	}

	/**
	 * Pretty print method for function definition
	 * @tree The Lexeme tree to print
	 */
	private void printFunction(Lexeme tree)
	{
		System.out.print("define function ");
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		System.out.print("(");
		if (tree.getCdr() != null && tree.getCdr().getCar() != null) prettyPrint(tree.getCdr().getCar());
		System.out.print(")");
		if (tree.getCdr() != null && tree.getCdr().getCdr() != null) prettyPrint(tree.getCdr().getCdr());
	}

	/**
	 * Pretty print method for class definition
	 * @tree The Lexeme tree to print
	 */
	private void printClass(Lexeme tree)
	{
		System.out.print("define class ");
		if (tree.getCar() != null) prettyPrint(tree.getCar());
		if (tree.getCdr() != null) prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for IDlist
	 * @tree The Lexeme tree to print
	 */
	private void printIDlist(Lexeme tree)
	{
		while (tree != null)
		{
			if (tree.getCar() != null) prettyPrint(tree.getCar());
			if (tree.getCdr() != null) System.out.print(",");
			tree = tree.getCdr();
		}
	}

/***** Public Methods *****/
	public void prettyPrint(Lexeme tree)
	{
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
		else if (tree.getType() == IFSTMNT) printIf(tree);
		else if (tree.getType() == ELSEIF) printElseIf(tree);
		else if (tree.getType() == WHILELOOP) printWhile(tree);
		else if (tree.getType() == FORLOOP) printFor(tree);
		else if (tree.getType() == EXPRLIST) printExprList(tree);
		else if (tree.getType() == IDSTART) printIDstart(tree);

		else if (tree.getType() == STRING) System.out.print("\"" + tree.getName() + "\"");
		else if (tree.getType() == INTEGER) System.out.print(tree.getInt());
		else if (tree.getType() == REAL) System.out.print(tree.getReal());
		else if (tree.getType() == ID) System.out.print(tree.getName());

		else if (tree.getType() == GLUE)
		{
			if (tree.getCar() != null) prettyPrint(tree.getCar());
			if (tree.getCdr() != null) prettyPrint(tree.getCdr());
		}
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
