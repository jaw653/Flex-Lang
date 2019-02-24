/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Types Module
 */

package lex;

public interface Types
{
    public static final String VAR = "VAR";
    public static final String REAL = "REAL";
    public static final String INTEGER = "INTEGER";
    public static final String CHARACTER = "CHARACTER";
    public static final String STRING = "STRING";
    public static final String DEFINE = "DEFINE";
    public static final String FUNCTION = "FUNCTION";
    public static final String ID = "ID";
    public static final String BUNDLE = "BUNDLE";
    public static final String RETURN = "RETURN";
    public static final String IF = "IF";
    public static final String ELSE = "ELSE";
    public static final String WHILE = "WHILE";
    public static final String FOR = "FOR";
    public static final String OPEN_PAREN = "OPEN_PAREN";
    public static final String CLOSE_PAREN = "CLOSE_PAREN";
    public static final String OPEN_BRACE = "OPEN_BRACE";
    public static final String CLOSE_BRACE = "CLOSE_BRACE";
    public static final String COMMA = "COMMA";
    public static final String PLUS = "PLUS";
    public static final String MINUS = "MINUS";
    public static final String TIMES = "TIMES";
    public static final String DIVIDE = "DIVIDE";
    public static final String LESS_THAN = "LESS_THAN";
    public static final String GREATER_THAN = "GREATER_THAN";
    public static final String LT_EQUAL = "LT_EQUAL";
    public static final String GT_EQUAL = "GT_EQUAL";
    public static final String EQUAL_TO = "EQUAL_TO";
	public static final String NOT_EQUAL = "NOT_EQUAL";
	public static final String NEGATE = "NEGATE";
    public static final String ASSIGN = "ASSIGN";
    public static final String SEMICOLON = "SEMICOLON";
    public static final String MODULO = "MODULO";
    public static final String UNKNOWN = "UNKNOWN";
    public static final String ENDOFINPUT = "ENDOFINPUT";
    public static final String BADNUM = "BADNUM";
    public static final String INCREMENT = "INCREMENT";
    public static final String PLUS_EQUAL = "PLUS_EQUAL";
    public static final String DECREMENT = "DECREMENT";
    public static final String MINUS_EQUAL = "MINUS_EQUAL";
    public static final String UNDERSCORE = "UNDERSCORE";
    public static final String SINGLE_QUOTE = "SINGLE_QUOTE";
    public static final String PERIOD = "PERIOD";
    public static final String CLASS = "CLASS";
    public static final String NEW = "NEW";
    public static final String NOT = "NOT";
	public static final String CLASS_INSTANTIATION = "CLASS_INSTANTIATION";
	public static final String OBJMEM = "OBJMEM";

    /***** Parse tree macros *****/
	public static final String RUNDEF = "RUNDEF";
	public static final String CLOSURE = "CLOSURE";
	public static final String OCLOSURE = "OCLOSURE";
    public static final String PROG = "PROG";
    public static final String DEF = "DEF";
    public static final String VARDEF = "VARDEF";
    public static final String ENV = "ENV";
    public static final String TABLE = "TABLE";
    public static final String I = "I";
    public static final String V = "V";
    public static final String FUNCDEF = "FUNCDEF";
	public static final String FUNCCALL = "FUNCCALL";
    public static final String CLASSDEF = "CLASSDEF";
    public static final String IDLIST = "IDLIST";
    public static final String STATEMENTS = "STATEMENTS";
    public static final String GLUE = "GLUE";
    public static final String EXPRDEF = "EXPRDEF";
    public static final String PARAMLIST = "PARAMLIST";
    public static final String BLOCK = "BLOCK";
    public static final String STATEMENT = "STATEMENT";
    public static final String IFSTMNT = "IFSTMNT";
    public static final String ELSEIF = "ELSEIF";
    public static final String WHILELOOP = "WHILELOOP";
    public static final String FORLOOP = "FORLOOP";
    public static final String RETSTMNT = "RETSTMNT";
    public static final String EXPRLIST = "EXPRLIST";
    public static final String IDSTART = "IDSTART";
    public static final String IMPORTDEF = "IMPORTDEF";
    public static final String UNARY = "UNARY";
}
