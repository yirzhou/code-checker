/*
*   PythonChecker Object
*   Written by: Yiren Zhou
*   
*   The purpose of the program is to scan a Python file and return:
*   1. File length (comments included)
*   2. Number of comments
*   3. Number of single-line comments
*   4. Number of comment blocks
*   5. Number of comment lines from comment blocks
*   6. Number of TODOs
*
*/

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.*;

public class PythonChecker {

    // Declare all member variables needed
    private int countLines;
    private int countTotalComments;
    private int countSingleComments;
    private int countMultiComments;
    private int countBlockComments;
    private int countTodos;
    private int consecutiveCounter;
    // Signal to detect if we are currently reading a part of a string block quoted by triple quotes
    private int tripleQuotesCounter;

    // Constructor
    public PythonChecker() {
        this.countLines = 0;
        this.countTotalComments = 0;
        this.countSingleComments = 0;
        this.countMultiComments = 0;
        this.countBlockComments = 0;
        this.countTodos = 0;
        this.consecutiveCounter = 0;
        this.tripleQuotesCounter = 0;
    }

    // The core function that does the file checking
    public void CheckFile(String filename) {
        // The following code counts the # lines and store them into a list
        try {
            // "allLines" stores all strings line by line in a list
            List<String> allLines = Files.readAllLines(Paths.get(filename));
            
			for (String line : allLines) {
                // Eliminate all whitespaces before and after the string
                line = StringUtils.trimToEmpty(line);
                /*
                    Counting comments:
                    A single-line comment is started with "#",
                */
                // If something like # ''' doesn't happen, it is not a comment; it is a string block
                if (line.contains("'''") 
                    && StringUtils.countMatches(line, "'''") % 2 == 1 
                    && !StringUtils.substringBefore(line, "'''").contains("#")) {
                        this.tripleQuotesCounter++;
                }                 

                { 
                    // This excludes with '#' and "#"
                    // Meaning that this line contains a comment
                    if (this.isCommentDoubleQuote(line) 
                        && this.isCommentSingleQuote(line) 
                        && this.tripleQuotesCounter % 2 == 0) {

                        if (this.consecutiveCounter >= 0 && StringUtils.substringBefore(line, "#").equals(""))
                            this.consecutiveCounter++;
                        else if (!StringUtils.substringBefore(line, "#").equals("")) { 
                            if(this.consecutiveCounter == 1)
                                this.countSingleComments++;
                            else if(this.consecutiveCounter > 1)
                                this.countBlockComments++;
                            

                            this.countSingleComments++;
                            this.consecutiveCounter = 0;
                        }

                        this.countTotalComments++;
                        
        
                        // If this is the end of the file and it marks a block of comment, increment the counter for comment blocks
                        if(this.consecutiveCounter > 1 && line.equals(allLines.get(allLines.size() - 1))) {
                            this.countBlockComments++;
                            this.consecutiveCounter = 0;
                        } else if (this.consecutiveCounter == 1 && line.equals(allLines.get(allLines.size() - 1))) {
                            this.consecutiveCounter = 0;
                            this.countSingleComments++;
                        }
                    
                        // This deals with # as a part of a string block, where I should do nothing because it's not a comment
                    } else if (line.contains("#") && this.tripleQuotesCounter % 2 == 1) {
                    
                    // If it is not a comment anymore, and we have only seen one comment so far, it is a single comment
                    } else {
                            if(this.consecutiveCounter == 1) {
                                this.countSingleComments++;
                            } else if (this.consecutiveCounter > 1) {
                                this.countBlockComments++;
                            }
                            this.consecutiveCounter = 0;
                        }
                }
                
                /*
                    Counting TODOs by just checking if it has a "TODO" in the line
                */
                {
                    if (this.isTODO(line))
                        this.countTodos++;
                }
                
            }

            // Printing results
            this.countLines = allLines.size();
            this.countMultiComments = this.countTotalComments - this.countSingleComments;

            System.out.println("Total # of lines: " + this.countLines);
            System.out.println("Total # of comment lines: " + this.countTotalComments);
            System.out.println("Total # of single line comments: " + this.countSingleComments);
            System.out.println("Total # of comment lines within block comments: " + this.countMultiComments);
            System.out.println("Total # of block line comments: " + this.countBlockComments);
            System.out.println("Total # of TODO's: " + this.countTodos);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    //  Function to return the number of occurrences of an element in an array of strings
    public int numberOfOccurrences(String[] arr, String element) {
        int count = 0;
        for(int i = 0; i < arr.length; ++i) {
            if (arr[i].contains(element)) count++;
        }
        return count;
    }

    /*
        The following three functions detects if the line is a comment
        The # char cannot be in any ' ', " ", or ''' '''
        But the line can start with a #, which is still a comment
    */
    public boolean isCommentDoubleQuote(String line) {
        if(!line.contains("#"))
            return false;

        if(line.contains("#") && StringUtils.substringsBetween(line, "\"", "\"") != null) {
            if(this.numberOfOccurrences(StringUtils.substringsBetween(line, "\"", "\""), "#") < StringUtils.countMatches(line, "#")) {
                return true;
            } else 
                return false;
        } else if (line.contains("#") && StringUtils.substringsBetween(line, "\"", "\"") == null){
            return true; 
        } 
        return false;
    }

    public boolean isCommentSingleQuote(String line) {
        if(!line.contains("#"))
            return false;

        if(line.contains("#") && StringUtils.substringsBetween(line, "'", "'") != null) {
            if(this.numberOfOccurrences(StringUtils.substringsBetween(line, "'", "'"), "#") < StringUtils.countMatches(line, "#")) {
                return true;
            } else 
                return false;
        } else if (line.contains("#") && StringUtils.substringsBetween(line, "'", "'") == null){
            return true; 
        } 
        return false;
    }

    public boolean isCommentTripleQuote(String line) {
        if(!line.contains("#"))
            return false;

        if(line.contains("#") && StringUtils.substringsBetween(line, "'''", "'''") != null) {
            if(this.numberOfOccurrences(StringUtils.substringsBetween(line, "'''", "'''"), "#") < StringUtils.countMatches(line, "#")) {
                return true;
            } else 
                return false;
        } else if (line.contains("#") && StringUtils.substringsBetween(line, "'''", "'''") == null){
            return true; 
        } 
        return false;
    }

    public boolean isTODO(String line) {
        if (this.isCommentSingleQuote(line) && this.isCommentDoubleQuote(line) && this.isCommentTripleQuote(line) && line.contains("TODO")) {
            // If the comment doesn't contain any double quote, it is a TODO
            // Else, count the occurrences of TODOs
            if (StringUtils.substringsBetween(line, "\"", "\"") == null)
                return true;
            else if (this.numberOfOccurrences(StringUtils.substringsBetween(line, "\"", "\""), "TODO") < StringUtils.countMatches(line, "TODO")) {
                return true;
            } 
        } 
        return false;
    }
}