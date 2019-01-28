import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Arrays;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;





public class task1 {



    // List of Java keywords, obtained from:
    //   https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html
    public static String [] keywords     = { 
        "abstract", "continue", "for", "new", "switch", 
        "assert", "default", "goto", "package", "synchronized", 
        "boolean", "do", "if", "private", "this", 
        "break", "double", "implements", "protected", "throw", 
        "byte", "else", "import", "public", "throws", 
        "case", "enum", "instanceof", "return", "transient", 
        "catch", "extends", "int", "short", "try", 
        "char", "final", "interface", "static", "void", 
        "class", "finally", "long", "strictfp", "volatile", 
        "const", "float", "native", "super", "while" 
    };

    public static String [] indentifiers = {};
    public static String [] mathOps      = { "=", "+", "-", "*", "/", "++", "--" };
    public static String [] logicalOps   = { ">", "<", "==", "!", "!=", ">=", "<=" };
    public static String [] numericals   = {};
    public static String [] others       = { "(", ")", "{", "}", "[", "]", ",", ";", "&&", "||" };

    public static ArrayList<String> recordedKeys    = new ArrayList<String>();
    public static ArrayList<String> recordedIds     = new ArrayList<String>();
    public static ArrayList<String> recordedMathOps = new ArrayList<String>();
    public static ArrayList<String> recordedLogOps  = new ArrayList<String>();
    public static ArrayList<String> recordedNums    = new ArrayList<String>();
    public static ArrayList<String> recordedOthers  = new ArrayList<String>();

    public static ArrayList<String> unclassifieds   = new ArrayList<String>();



    // Since 'trim()' function trims only leading and trailing whitespaces,
    // a custom method is necessary.
    public static String trimInternalSpace(String line) {
        // Needed to write this but turned out it is not necessary anymore
        return line;
    }



    // Insert whitespaces before and after symbols like '+', '', ';', and
    // braces etc. so that entire lines can be split using a single whitespace
    // to separate tokens.
    public static String padWithWhitespaces(String line) {

        String paddedLine = "";
        char [] ch = line.toCharArray();
        
        for (int c=0; c<ch.length; ++c) {

            // Insert whitespace before ',' and ';'.
            if ( ch[c]==',' || ch[c]==';' ) {
                paddedLine += " " + ch[c];
            }

            // Insert whitespace both before and after in the following cases.
            // Some cases might have been missed.
            else if ( 
                        ch[c]=='(' 
                        || ch[c]=='{' 
                        || ch[c]=='[' 
                        || ch[c]==')' 
                        || ch[c]=='}' 
                        || ch[c]==']' 
                        || ( ch[c]=='=' && ( ch[c-1]!='!' || ch[c+1]!='=' ) ) 
                        || ( ch[c]=='=' && ch[c+1]!='=' ) 
                        || ( ch[c]=='+' && ch[c+1]!='+' ) 
                        || ( ch[c]=='-' && ch[c+1]!='-' ) 
                        || ( ch[c]=='*' && ch[c+1]!='*' ) 
                        || ch[c]=='/' 
                        || ch[c]=='%' 
                        || ( ch[c]=='>' && ch[c+1]!='=' ) 
                        || ( ch[c]=='<' && ch[c+1]!='=' ) 
                      ) {
                paddedLine += " " + ch[c] + " ";
            }

            // If no scope of inserting whitespace found, just concatenate
            // to String.
            else {
                paddedLine += ch[c];
            }

        }

        return paddedLine;

    }



    // Returns "true" if token matches any item from the list of stored
    // keywords. Otherwise, returns "false".
    public static boolean isKeyword(String token) {
        return ( Arrays.asList(keywords).contains(token) ) ? true : false;
    }



    // Returns "true" if token matches the pattern for Java identifiers.
    // Otherwise, returns "false".
    // Source for identifier pattern validation:
    //   https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.8
    //
    // BTW, the following lines of code are apparently valid and it is weird:
    //
    //     String _ = "someString";
    //     String __ = "someString";
    //
    public static boolean isIdentifier(String token) {

        Pattern idPattern = Pattern.compile("([a-zA-Z_]+)+\\w*");
        Matcher match = idPattern.matcher(token);

        // Return "true" only when the entire token - not a substring - matches
        // with the pattern.
        return ( match.find() && match.group().length()==token.length() ) ? true : false;

    }



    // Returns "true" if token matches any item from the list of stored
    // mathematical operators. Otherwise, returns "false".
    // The classification of operators have been done by following the question
    // provided, so they may not match the actual Java classification. (For
    // example. the equal sign ('=') has been tagged as a mathematical operator,
    // when it is actually an assignment operator.)
    public static boolean isMathOp(String token) {
        return ( Arrays.asList(mathOps).contains(token) ) ? true : false;
    }



    // Returns "true" if token matches any item from the list of stored
    // logical operators. Otherwise, returns "false".
    // The classification of operators have been done by following the question
    // provided, so they may not match the actual Java classification. (For
    // example. the equal sign ('=') has been tagged as a mathematical operator,
    // when it is actually an assignment operator.)
    public static boolean isLogicalOp(String token) {
        return ( Arrays.asList(logicalOps).contains(token) ) ? true : false;
    }



    // Returns "true" if token matches the pattern for either an integer or a
    // decimal number. Otherwise, returns "false".
    public static boolean isNumerical(String token) {

        Pattern intPattern = Pattern.compile("\\d+");
        Matcher intMatch = intPattern.matcher(token);
        
        Pattern decPattern = Pattern.compile("\\d+(.\\d+)?");
        Matcher decMatch = decPattern.matcher(token);

        return (decMatch.find()) ? true : (intMatch.find()) ? true : false;

    }



    // Returns "true" if token matches any item from the list of stored
    // characters that fall under neither of the categories mentioned before.
    // Otherwise, returns "false".
    public static boolean isOther(String token) {
        return ( Arrays.asList(others).contains(token) ) ? true : false;
    }



    // Keep track of the tokens that have been identified so far (so that
    // duplication does not happen).
    // Each token is sent to this method accompanied by a specific index that
    // indicates which category the token falls under.
    public static boolean recordToken(String token, int index) {
        switch (index){
            case 0:
                if ( !(recordedKeys.contains(token)) ) {
                    recordedKeys.add(token);
                }
                break;
            case 1:
                if ( !(recordedIds.contains(token)) ) {
                    recordedIds.add(token);
                }
                break;
            case 2:
                if ( !(recordedMathOps.contains(token)) ) {
                    recordedMathOps.add(token);
                }
                break;
            case 3:
                if ( !(recordedLogOps.contains(token)) ) {
                    recordedLogOps.add(token);
                }
                break;
            case 4:
                if ( !(recordedNums.contains(token)) ) {
                    recordedNums.add(token);
                }
                break;
            case 5:
                if ( !(recordedOthers.contains(token)) ) {
                    recordedOthers.add(token);
                }
                break;
        }
        return true;
    }



    // To detect any typing mistakes or such errors. Mainly used for debugging
    // purposes.
    public static boolean recordUnclassified(String token) {
        if ( !(unclassifieds.contains(token)) ) {
            unclassifieds.add(token);
        }
        return false;
    }



    // Recorded tokens are printed in the output format given in the question.
    // A comma is used for all tokens except the group that comma itself is
    // a member of. In that case, a whitespace has simply been used to
    // separate the tokens.
    public static void printTokensFromArrayList(ArrayList list, char ch) {
        if (list.size()>0) {
            System.out.print( list.get(0) );
            for (int c=1; c<list.size(); ++c) {
                // Print a comma followed by a whitespace as the separator
                // generally; but for one group of tokens, print only the
                // whitespace.
                System.out.print( ((ch!='\\') ? ch : "") + " " + list.get(c) );
            }
        }
        System.out.println();
    }



    // Use comma (,) as the separator between two tokens for as group of tokens
    // but one - where comma itself is a token. Use whitespace in that case
    // instead.
    public static void printRecordedTokens() {

        // Token group names as given in the question.
        String [] keys = { "Keywords", "Identifiers", "Math operators", "Logical operators", "Numerical values", "Others" };

        System.out.print( keys[0] + ": " );
        printTokensFromArrayList( recordedKeys, ',');

        System.out.print( keys[1] + ": " );
        printTokensFromArrayList( recordedIds, ',');

        System.out.print( keys[2] + ": " );
        printTokensFromArrayList( recordedMathOps, ',');

        System.out.print( keys[3] + ": " );
        printTokensFromArrayList( recordedLogOps, ',');

        System.out.print( keys[4] + ": " );
        printTokensFromArrayList( recordedNums, ',');

        // The punctuation comma is not used to separate this group of tokens,
        // since the character comma itself is one of them.
        System.out.print( keys[5] + ": " );
        printTokensFromArrayList( recordedOthers, '\\');

    }



    // Print anything not included in the token groups listed.
    // This method was written mainly for debugging purposes.
    public static void printUnclassifiedElements() {

        System.out.print("Unclassified: ");

        // The ArrayList for unclassified tokens always gets an item which
        // seems to be an "empty" character (not whitespace).
        if (unclassifieds.size()>0) {
            System.out.print(unclassifieds.get(0));
            for (int c=1; c<unclassifieds.size(); ++c) {
                System.out.print(unclassifieds.get(c));
            }
        }

        else {
            System.out.print("none");
        }

        System.out.println();

    }





    public static void main(String [] nothing) {

        // Use try-catch block in case input file is not found.
        try {
            
            BufferedReader br = new BufferedReader( new FileReader("input.txt") );
            String line = "";

            // If next line exists (i.e., is not null):
            while ( ( line = br.readLine() )!=null ) {

                // Trim leading and trailing whitespaces.
                line = line.trim();

                // Need to write this
                line = trimInternalSpace(line);

                // Insert whitespaces before and/or after characters such as
                // ',', '+', '=', braces, etc.
                line = padWithWhitespaces(line);

                boolean added = false;
                String [] tokens = line.split(" ");

                for (int c=0; c<tokens.length; ++c) {

                    // "True" is returned if any token is identified as one
                    // included in the lists. Otherwise "false" is returned,
                    // which may later be used to detect that the input code
                    // may contain invalid tokens.
                    added = ( isKeyword(tokens[c]) )             ? recordToken( tokens[c], 0 )
                        : ( isIdentifier(tokens[c]) )            ? recordToken( tokens[c], 1 )
                            : ( isMathOp(tokens[c]) )            ? recordToken( tokens[c], 2 )
                                : ( isLogicalOp(tokens[c]) )     ? recordToken( tokens[c], 3 )
                                    : ( isNumerical(tokens[c]) ) ? recordToken( tokens[c], 4 )
                                        : ( isOther(tokens[c]) ) ? recordToken( tokens[c], 5 )
                                            : recordUnclassified(tokens[c]);

                }

            }

            printRecordedTokens();

            // // Uncomment this if necessary.
            // printUnclassifiedElements();

        }

        catch (Exception e) {
            System.out.println("Error: File not found!");
        }

    }



}
