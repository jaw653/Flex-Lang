/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * Lexical Analysis Module
 */

 // Is there a way to compile with more warning settings and do I need to do it?


import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

class Lexical
{
    /**
     * Method to skip over any whitespace in a File
     * @file The file to be read
     * returns ArrayList of Character objects from the file excluding whitespace
     */
    private static ArrayList<Character> skipWhiteSpace(File file) throws IOException
    {
        Character ch;
        ArrayList<Character> input = new ArrayList<Character>();

        Scanner sc = new Scanner(file);

        String currString = sc.next();

        while (sc.hasNext())
        {
            int size = currString.length();

            for (int i = 0; i < size; i++)
            {
                ch = Character.valueOf(currString.charAt(i));
                input.add(ch);
/*
*** Should not need cause scanner does not take whitespace
                if ( !(ch.isWhitespace()) )
                {
                    input.add(ch);
                }
*/
            }

            currString = sc.next();
        }

        return input;
    }

    /**
     * Lexer to return lexemes
     */
    public static int lex(File file) throws IOException
    {
        char ch;

        ArrayList<Character> charList = skipWhiteSpace(file);

        for (int i = 0; i < charList.size(); i++)
        {
            System.out.println(charList.get(i));
        }
        // ch = sc.next.charAt(0);

        // if input failed return new lexeme(ENDofINPUT)

        // switch on single character tokens
        // ie case '(': return new lexeme(OPAREN)

        // default case:
        // if character.isDig



        return 1;       //FIXME: placeholder

    }

    /**
     * Main methods to enact other methods
     * @args The command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        if (args.length < 1)
        {
            System.out.println("Incorrect number of command line args");
            System.exit(0);
        }

        File file = new File(args[0]);

        if ( lex(file) != 1 )
        {
            System.out.println("error");
        }

        // System.out.println(args[0]);
    }
}
