/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * Lexical Analysis Module
 */

 // Is there a way to compile with more warning settings and do I need to do it?
 // Need to account for something like below that ends with '\n'. Do I need to escape newline char?
            // printf("hello world\n");


import java.io.*;
import java.util.ArrayList;

class Scanner
{
    /**
     * Safe method for getting the next Character in a String
     * @str String from which to get next Character
     * @index Index of String from which to get Character
     * returns Character at index
     */
    private static void skipComment(PushbackInputStream stream, char identifier) throws IOException
    {
        try
        {
            Character curr = Character.valueOf( (char)stream.read() );

            if (identifier == '*')
            {
                while (stream.available() > 0)
                {
                    if (curr == '*')
                    {
                        Character next = Character.valueOf( (char)stream.read() );

                        if (next == '/')
                            return;
                    }
                    curr = Character.valueOf( (char)stream.read() );
                }
            }
            else
            {
                while (stream.available() > 0)
                {
                    if (curr == '\n')
                        return;

                    curr = Character.valueOf( (char)stream.read() );
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to skip over any whitespace in a File
     * @file The file to be read
     * returns ArrayList of Character objects from the file excluding whitespace
     */
    private static ArrayList<Character> skipWhiteSpace(File file) throws IOException
    {
        ArrayList<Character> input = new ArrayList<Character>();

        try
        {
            PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));
            // FileInputStream stream = new FileInputStream(file);
            Character current;

            while (stream.available() > 0)
            {
                current = Character.valueOf( (char)stream.read() );
                // System.out.println(current);

                if ( current != ' ' && current != '\t')
                {
                    if ( current != '/' )
                        input.add(current);

                    else
                    {
                        Character next = Character.valueOf( (char)stream.read() );      // FIXME: might run into an issue if there is no next (idk if while loop exits immediately)

                        if (next == '*' || next == '/')
                        {
                            // stream.unread(next);
                            skipComment(stream, next);
                        }
                        else
                            input.add(current);

                        // if next character is '*', skip until you find '*/'

                        // if next character is '\n', skip until you find '\n'
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return input;
    }

    /**
     * Lexer to return lexemes
     */
    public static int lex(ArrayList<Character> input) throws IOException
    {
/*
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
*/
        return 1;
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
            System.exit(-1);
        }

        File file = new File(args[0]);

        if ( !file.exists() )
        {
            System.out.println("File does not exist");
            System.exit(-1);
        }

        ArrayList<Character> input = new ArrayList<Character>();
        input = skipWhiteSpace(file);

        System.out.println(input);

/*
        if ( lex(input) != 1 )
        {
            System.out.println("error");
        }
*/
        // System.out.println(args[0]);
    }
}
