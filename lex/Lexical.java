/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * Lexical Analysis Module
 */

import java.io.*;
import java.util.Scanner;

class Lexical
{
    /**
     * Lexer to return lexemes
     */
    public static int lex(File file)
    {
        char ch;
        Scanner sc = new Scanner(file);

        // skipWhiteSpace();

        ch = sc.next.charAt(0);

        // if input failed return new lexeme(ENDofINPUT)

        



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
        Scanner sc = new Scanner(file);

        char ch = sc.next().charAt(0);

        while (sc.hasNext())
        {
            System.out.println(ch + " ");
            ch = sc.next().charAt(0);
        }

        // System.out.println(args[0]);
    }
}
