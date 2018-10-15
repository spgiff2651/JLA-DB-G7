import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main
{
    /* Global declarations */
    /* Variables */

    private static String charClass, nextToken, input;
    private static int lexLen, token;
    private static char nextChar;
    private static char[] lexeme = new char[100];
    static FileInputStream in_fp;
    static File file;

    /* Character classes */
    /* Katie's help, setting classes and tokens to FINAL */

    private static final String LETTER = "LETTER";
    private static final String DIGIT = "DIGIT";
    private static final String UNKNOWN = "UNKNOWN";

    /* Token codes */

    private static final String INT_LIT = "INT_LIT";
    private static final String IDENT = "IDENT";
    private static final String ASSIGN_OP = "ASSIGN_OP";
    private static final String ADD_OP = "ADD_OP";
    private static final String SUB_OP = "SUB_OP";
    private static final String MULT_OP = "MULT_OP";
    private static final String DIV_OP = "DIV_OP";
    private static final String LEFT_PAREN = "LEFT_PAREN";
    private static final String RIGHT_PAREN = "RIGHT_PAREN";
    private static final String EOF = "END_OF_FILE";

    /****************************************************************************************/
    /* Method for outputting to file */
    static PrintWriter writer;
    static
    {
        try
        {
            writer = new PrintWriter("src\\lexprint.txt");
        }

        catch (FileNotFoundException e) {e.printStackTrace();}
    }

    /****************************************************************************************/
    /* Main Driver */
    public static void main(String[] args) throws Exception
    {
       try
        {
            lexLen = 0;
            for (int i = 0; i < 100; i++)
            {
                lexeme[i] = '0';
            }

            file = new File("src\\lextest.txt");
            in_fp = new FileInputStream(file);

            writer.println("Stephen P. Gifford, CSCI4200-DB, FALL 2018, Lexical Analyzer");
            writer.println("********************************************************");
            System.out.println("Stephen P. Gifford, CSCI4200-DB, FALL 2018, Lexical Analyzer");
            System.out.println("********************************************************");

            Scanner scan = new Scanner(new File("src\\lextest.txt"));
            while (scan.hasNextLine())
            {
                System.out.println(scan.nextLine());

                if (!(file.exists()))
                {
                    System.out.println("ERROR! No data in file.");
                }
                else
                {
                    getChar();
                    while(nextToken != EOF)
                        lex();
                    nextToken = "";
                }


                writer.println("********************************************************");
                System.out.println("********************************************************");
            }

            System.out.println();
            System.out.println("Lexical analysis of the program is complete!");
            //scan.close();
            writer.close();
        }

        catch(NoSuchElementException e)
        {
            writer.println("Error: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
        catch(FileNotFoundException e)
        {
            writer.println("Caught error: " + e.getMessage());
            System.out.println("Caught error: " + e.getMessage());
        }
        catch(IndexOutOfBoundsException e)
        {
            writer.println("Index Out of Bounds: " + e.getMessage());
            System.out.println("Index Out of Bounds: " + e.getMessage());
        }
    }

    /****************************************************************************************/
    /* Lookup - A function to lookup operators and parentheses and return the token */

    public static String lookupChar(char c)
    {
        switch (c)
        {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;

            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;

            case '+':
                addChar();
                nextToken = ADD_OP;
                break;

            case '-':
                addChar();
                nextToken = SUB_OP;
                break;

            case '*':
                addChar();
                nextToken = MULT_OP;
                break;

            case '/':
                addChar();
                nextToken = DIV_OP;
                break;

            case '=':
                addChar();
                nextToken = ASSIGN_OP;
                break;

            default:
                addChar();
                nextToken = EOF;
                break;
        }
        return nextToken;
    }

    /* Function declarations */

    /****************************************************************************************/
    /* addChar - A function to add nextChar to lexeme */
    public static void addChar()
    {
        if (lexLen <= 98)
        {
            lexeme[lexLen++] = nextChar;
            lexeme[lexLen] = 0;
        }

        else
        {
            writer.println("Error - lexeme is too long \n");
            System.out.println("Error - lexeme is too long \n");
        }
    }

    /****************************************************************************************/
    /* getChar - A function to get the next character of input and determine its character class */

    public static void getChar() throws IOException
    {
        if (in_fp.available() > 0)
        {
            nextChar = (char)in_fp.read();
            if (Character.isLetter(nextChar))
            {
                charClass = LETTER;
            }
            else if (Character.isDigit(nextChar))
            {
                charClass = DIGIT;
            }
            else
            {
                charClass = UNKNOWN;
            }
        }
        else
        {
            charClass = EOF;
        }
    }

    /****************************************************************************************/
    /* getNonBlack - a function to call getChar until it returns a non-whitespace character */

    public static void getNonBlank() throws IOException
    {
        while(Character.isSpaceChar(nextChar))
            getChar();
    }

    /****************************************************************************************/
    /* lex - a simple lexical analyzer for arithmetic expressions */

    public static String lex() throws IOException
    {
        lexLen = 0;
        getNonBlank();

        switch (charClass)
        {
            /* Parse Identifiers */
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextToken = IDENT;
                break;

            /* Parse Integer Literals */
            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;

            /* Parentheses and Operators */
            case UNKNOWN:
                lookupChar(nextChar);
                getChar();
                break;

            /* EOF */
            /* Katie's help add sys.out line */
            case EOF:
                writer.println("********************************************************");
                System.out.println("********************************************************");
                nextToken = EOF;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                lexeme[3] = 0;
                break;
        } /* End of Switch */

        /* Katie's Loop for printing tokens */
        writer.printf("%-10s%-15s%-10s","Next token is: ",nextToken,"Next lexeme is ");
        System.out.printf("%-10s%-15s%-10s","Next token is: ",nextToken,"Next lexeme is ");

        for (int i = 0; i < lexLen; i++)
        {
            for (int j = 0; j < lexLen; j++)
            {
                writer.print(lexeme[j]);
                System.out.print(lexeme[j]);
            }
            writer.println();
            System.out.println();
            return nextToken;
        }

        /* EOF */
        for (int i = 0; i < 3; i++)
        {
            writer.print(lexeme[i]);
            System.out.print(lexeme[i]);
        }

        writer.println();
        writer.println("********************************************************");
        System.out.println();
        System.out.println("********************************************************");

        return EOF;
    } /* End of Funtion Lex */
}
