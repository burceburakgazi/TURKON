package turkon;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * TURKON Language Lexical Analyzer
 * Tokenizes TURKON source code into meaningful tokens
 */
public class TURKONLexer {
    private static final Map<String, TokenType> keywords = new HashMap<>();
    private static final String TOKEN_REGEX = "\\s*(\\d+|[a-zA-ZğüşöçıİĞÜŞÖÇ_][a-zA-ZğüşöçıİĞÜŞÖÇ0-9_]*|[{}()=;<>!+\\-/*])\\s*";
    private static final Pattern TOKEN_PATTERN = Pattern.compile(TOKEN_REGEX);
    private Matcher matcher;
    private String input;

    static {
        // TURKON Language Keywords (Turkish-based)
        keywords.put("eğer", TokenType.IF);           // Conditional check
        keywords.put("aksi", TokenType.ELSE);         // Alternative branch (part 1)
        keywords.put("takdirde", TokenType.ELSE);     // Alternative branch (part 2)
        keywords.put("aksi takdirde", TokenType.ELSE); // Full else clause
        keywords.put("için", TokenType.FOR);          // For loop
        keywords.put("iken", TokenType.WHILE);        // While loop
        keywords.put("yazdır", TokenType.PRINT);      // Output command
        keywords.put("başla", TokenType.BEGIN);       // Block start
        keywords.put("bitir", TokenType.END);         // Block end
        keywords.put("değişken", TokenType.VAR);      // Variable declaration
        keywords.put("fonksiyon", TokenType.FUNC);    // Function declaration
        keywords.put("dön", TokenType.RETURN);        // Return statement
        keywords.put("doğru", TokenType.TRUE);        // Boolean true
        keywords.put("yanlış", TokenType.FALSE);      // Boolean false
        keywords.put("boş", TokenType.NULL);          // Null value
    }

    public TURKONLexer(String input) {
        this.input = input;
        matcher = TOKEN_PATTERN.matcher(input);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        
        while (matcher.find()) {
            String tokenText = matcher.group().trim();
            
            if (tokenText.isEmpty()) {
                continue;
            }
            
            // Handle multi-word Turkish keywords
            if (tokenText.equals("aksi")) {
                String nextToken = lookAhead();
                if (nextToken.equals("takdirde")) {
                    tokenText = "aksi takdirde";
                    matcher.find(); // Skip next token
                }
            }
            
            TokenType type = determineTokenType(tokenText);
            tokens.add(new Token(type, tokenText));
        }
        
        return tokens;
    }

    private String lookAhead() {
        int currentPos = matcher.end();
        Matcher tempMatcher = TOKEN_PATTERN.matcher(input);
        tempMatcher.region(currentPos, input.length());
        if (tempMatcher.find()) {
            return tempMatcher.group().trim();
        }
        return "";
    }

    private TokenType determineTokenType(String tokenText) {
        // Check keywords first
        if (keywords.containsKey(tokenText)) {
            return keywords.get(tokenText);
        }
        
        // Check for numbers
        if (tokenText.matches("\\d+")) {
            return TokenType.NUMBER;
        }
        
        // Check for identifiers (variable names, function names)
        if (tokenText.matches("[a-zA-ZğüşöçıİĞÜŞÖÇ_][a-zA-ZğüşöçıİĞÜŞÖÇ0-9_]*")) {
            return TokenType.IDENTIFIER;
        }
        
        // Check for operators and symbols
        switch (tokenText) {
            case "=": return TokenType.ASSIGN;
            case "+": return TokenType.PLUS;
            case "-": return TokenType.MINUS;
            case "*": return TokenType.MULT;
            case "/": return TokenType.DIV;
            case "%": return TokenType.MOD;
            case "(": return TokenType.LPAREN;
            case ")": return TokenType.RPAREN;
            case "{": return TokenType.LBRACE;
            case "}": return TokenType.RBRACE;
            case ";": return TokenType.SEMICOLON;
            case ",": return TokenType.COMMA;
            case "==": return TokenType.EQ;
            case "!=": return TokenType.NE;
            case "<": return TokenType.LT;
            case ">": return TokenType.GT;
            case "<=": return TokenType.LE;
            case ">=": return TokenType.GE;
            case "&&": return TokenType.AND;
            case "||": return TokenType.OR;
            case "!": return TokenType.NOT;
            default: 
                throw new IllegalArgumentException("Unknown token: " + tokenText);
        }
    }

    public static class Token {
        private final TokenType type;
        private final String text;

        public Token(TokenType type, String text) {
            this.type = type;
            this.text = text;
        }

        public TokenType getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return String.format("Token{type=%s, text='%s'}", type, text);
        }
    }

    public enum TokenType {
        // Keywords
        IF, ELSE, FOR, WHILE, PRINT, BEGIN, END, VAR, FUNC, RETURN,
        
        // Literals
        IDENTIFIER, NUMBER, TRUE, FALSE, NULL,
        
        // Operators
        ASSIGN, PLUS, MINUS, MULT, DIV, MOD,
        EQ, NE, LT, GT, LE, GE, AND, OR, NOT,
        
        // Delimiters
        LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, COMMA
    }
}