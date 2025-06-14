/*
 * TURKON Programming Language Implementation
 * A custom programming language with TURKON syntax
 */

package turkon;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TURKONMain {

    public static void main(String[] args) {
        System.out.println("=== TURKON Programming Language Interpreter ===");
        
        try {
            // Example 1: Basic arithmetic operations
            String input1 = new String(Files.readAllBytes(Paths.get("test_files/example1.tkn")));
            System.out.println("\n--- Processing example1.tkn ---");
            
            TURKONLexer lexer1 = new TURKONLexer(input1);
            List<TURKONLexer.Token> tokens1 = lexer1.tokenize();
            TURKONParser parser1 = new TURKONParser(tokens1);
            parser1.parse();
            System.out.println("[OK] Example 1 parsing completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error processing example1.tkn: " + e.getMessage());
        }
        
        try {
            // Example 2: While loop
            String input2 = new String(Files.readAllBytes(Paths.get("test_files/example2.tkn")));
            System.out.println("\n--- Processing example2.tkn ---");
            
            TURKONLexer lexer2 = new TURKONLexer(input2);
            List<TURKONLexer.Token> tokens2 = lexer2.tokenize();
            TURKONParser parser2 = new TURKONParser(tokens2);
            parser2.parse();
            System.out.println("[OK] Example 2 parsing completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error processing example2.tkn: " + e.getMessage());
        }
        
        try {
            // Example 3: Countdown loop
            String input3 = new String(Files.readAllBytes(Paths.get("test_files/example3.tkn")));
            System.out.println("\n--- Processing example3.tkn ---");
            
            TURKONLexer lexer3 = new TURKONLexer(input3);
            List<TURKONLexer.Token> tokens3 = lexer3.tokenize();
            TURKONParser parser3 = new TURKONParser(tokens3);
            parser3.parse();
            System.out.println("[OK] Example 3 parsing completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error processing example3.tkn: " + e.getMessage());
        }
        
        try {
            // Example 4: Conditional statements
            String input4 = new String(Files.readAllBytes(Paths.get("test_files/example4.tkn")));
            System.out.println("\n--- Processing example4.tkn ---");
            
            TURKONLexer lexer4 = new TURKONLexer(input4);
            List<TURKONLexer.Token> tokens4 = lexer4.tokenize();
            TURKONParser parser4 = new TURKONParser(tokens4);
            parser4.parse();
            System.out.println("[OK] Example 4 parsing completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error processing example4.tkn: " + e.getMessage());
        }
        
        System.out.println("\n=== TURKON Language Processing Complete ===");
    }
}