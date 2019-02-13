/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Pretty Printer class
 */

package prettyPrint;

import lex.*;
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

	/**
	 * Pretty print method for function definition
	 * @tree The Lexeme tree to print
	 */
	private void printFunction(Lexeme tree)
	{
		System.out.print("function ");
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
		System.out.print("class ");
		prettyPrint(tree.getCar());
		prettyPrint(tree.getCdr());
	}

	/**
	 * Pretty print method for statements
	 * @tree The Lexeme tree to print
	 */
	private void printStatements(Lexeme tree)
	{
		while (tree != null)
		{
			prettyPrint(tree.getCar());
			System.out.println();
			tree = tree.getCdr();
		}
	}

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

	public void prettyPrint(Lexeme tree)
	{
		if (tree.getType() == STRING) System.out.println("\"" + tree.getName() + "\"");
		else if (tree.getType() == INTEGER) System.out.println(tree.getInt());
		else if (tree.getType() == REAL) System.out.println(tree.getReal());
		else if (tree.getType() == FUNCDEF) printFunction(tree);
		else if (tree.getType() == CLASSDEF) printClass(tree);
		else if (tree.getType() == STATEMENTS) printStatements(tree);
		else if (tree.getType() == IDLIST) printIDlist(tree);

/*		FIXME: does this need to be here?
		else if (tree.getType() == VAR)
		{
			System.out.print("var " + tree.getCar().getName() + " = " + tree.getCdr().getInt());
			System.out.print()
		}
*/
		else if (tree.getType() == OPEN_PAREN)
		{
			System.out.print("(");
			prettyPrint(tree.getCdr());
			System.out.print(")");
		}
/*		FIXME: The below may not be implemented in my language (ie can just create a negative via math rather than assignment)
		else if (tree.getType() == UMINUS)
		{
			System.out.print("-");
//			System.out.print(tree.getCdr().);		//FIXME: not sure if I need to print int, real, string after this, or just int/real
		}
*/
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

		// FIXME: should I have a main() here that goes through each lexeme?
		// FIXME: where should I be building the trees? In this file?
	}
}
