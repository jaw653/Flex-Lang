/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexical Analysis Module
 */

 // Is there a way to compile with more warning settings and do I need to do it?
 // Need to account for something like below that ends with '\n'. Do I need to escape newline char?
            // printf("hello world\n");
// should not have newline characters in the array. Maybe make it easy on myself and just get rid of '//' comments?


import java.io.*;
import java.util.ArrayList;


class Scanner
{

    /**
     * Main method to be run for Scanner
     * @args The command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        if (args.length < 1)
        {
            System.out.println("Incorrect number of command line args");
            System.exit(-1);
        }

        File file = new File(args[0]);

        if ( !file.exists() )
        {
            System.out.println("File does not exist");
            System.exit(-1);
        }

/*
        if ( lex(input) != 1 )
        {
            System.out.println("error");
        }
*/
        // System.out.println(args[0]);
    }
}
