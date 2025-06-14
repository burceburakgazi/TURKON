package turkon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TURKON Language Parser and Interpreter
 */
public class TURKONParser {
    private final List<TURKONLexer.Token> tokens;
    private int currentTokenIndex = 0;
    private final Map<String, Integer> symbolTable = new HashMap<>();

    public TURKONParser(List<TURKONLexer.Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            parseStatement();
        }
    }

    private void parseStatement() {
        TURKONLexer.Token token = getCurrentToken();
        switch (token.getType()) {
            case IF:
                parseIfStatement();
                break;
            case FOR:
                parseForStatement();
                break;
            case WHILE:
                parseWhileStatement();
                break;
            case PRINT:
                parsePrintStatement();
                break;
            case IDENTIFIER:
                parseAssignment();
                break;
            default:
                throw new IllegalArgumentException("Unexpected token: " + token);
        }
    }

    private void parseIfStatement() {
        consume(TURKONLexer.TokenType.IF);
        consume(TURKONLexer.TokenType.LPAREN);
        int condition = parseExpression();
        consume(TURKONLexer.TokenType.RPAREN);
        consume(TURKONLexer.TokenType.LBRACE);
        if (condition != 0) {
            while (!check(TURKONLexer.TokenType.RBRACE)) {
                parseStatement();
            }
        } else {
            while (!check(TURKONLexer.TokenType.RBRACE)) {
                advance();
            }
        }
        consume(TURKONLexer.TokenType.RBRACE);

        if (match(TURKONLexer.TokenType.ELSE)) {
            consume(TURKONLexer.TokenType.LBRACE);
            if (condition == 0) {
                while (!check(TURKONLexer.TokenType.RBRACE)) {
                    parseStatement();
                }
            } else {
                while (!check(TURKONLexer.TokenType.RBRACE)) {
                    advance();
                }
            }
            consume(TURKONLexer.TokenType.RBRACE);
        }
    }

    private void parseForStatement() {
        consume(TURKONLexer.TokenType.FOR);
        consume(TURKONLexer.TokenType.LPAREN);
        parseAssignment();
        consume(TURKONLexer.TokenType.SEMICOLON);
        int conditionIndex = currentTokenIndex;
        int condition = parseExpression();
        consume(TURKONLexer.TokenType.SEMICOLON);
        int incrementIndex = currentTokenIndex;
        parseAssignment();
        consume(TURKONLexer.TokenType.RPAREN);
        consume(TURKONLexer.TokenType.LBRACE);

        while (condition != 0) {
            int startIndex = currentTokenIndex;
            while (!check(TURKONLexer.TokenType.RBRACE)) {
                parseStatement();
            }
            currentTokenIndex = incrementIndex;
            parseAssignment();
            currentTokenIndex = conditionIndex;
            condition = parseExpression();
            currentTokenIndex = startIndex;
        }

        while (!check(TURKONLexer.TokenType.RBRACE)) {
            advance();
        }
        consume(TURKONLexer.TokenType.RBRACE);
    }

    private void parseWhileStatement() {
        consume(TURKONLexer.TokenType.WHILE);
        consume(TURKONLexer.TokenType.LPAREN);
        int conditionIndex = currentTokenIndex;
        int condition = parseExpression();
        consume(TURKONLexer.TokenType.RPAREN);
        consume(TURKONLexer.TokenType.LBRACE);

        while (condition != 0) {
            int startIndex = currentTokenIndex;
            while (!check(TURKONLexer.TokenType.RBRACE)) {
                parseStatement();
            }
            currentTokenIndex = conditionIndex;
            condition = parseExpression();
            currentTokenIndex = startIndex;
        }

        while (!check(TURKONLexer.TokenType.RBRACE)) {
            advance();
        }
        consume(TURKONLexer.TokenType.RBRACE);
    }

    private void parseAssignment() {
        String identifier = consume(TURKONLexer.TokenType.IDENTIFIER).getText();
        consume(TURKONLexer.TokenType.ASSIGN);
        int value = parseExpression();
        symbolTable.put(identifier, value);
        consume(TURKONLexer.TokenType.SEMICOLON);
    }

    private void parsePrintStatement() {
        consume(TURKONLexer.TokenType.PRINT);
        consume(TURKONLexer.TokenType.LPAREN);
        int value = parseExpression();
        consume(TURKONLexer.TokenType.RPAREN);
        consume(TURKONLexer.TokenType.SEMICOLON);
        System.out.println("TURKON Output: " + value);
    }

    private int parseExpression() {
        int value = parseTerm();
        while (match(TURKONLexer.TokenType.PLUS, TURKONLexer.TokenType.MINUS, TURKONLexer.TokenType.EQ, TURKONLexer.TokenType.NE, TURKONLexer.TokenType.LT, TURKONLexer.TokenType.GT, TURKONLexer.TokenType.LE, TURKONLexer.TokenType.GE)) {
            TURKONLexer.Token operator = getPreviousToken();
            int right = parseTerm();
            switch (operator.getType()) {
                case PLUS:
                    value += right;
                    break;
                case MINUS:
                    value -= right;
                    break;
                case EQ:
                    value = (value == right) ? 1 : 0;
                    break;
                case NE:
                    value = (value != right) ? 1 : 0;
                    break;
                case LT:
                    value = (value < right) ? 1 : 0;
                    break;
                case GT:
                    value = (value > right) ? 1 : 0;
                    break;
                case LE:
                    value = (value <= right) ? 1 : 0;
                    break;
                case GE:
                    value = (value >= right) ? 1 : 0;
                    break;
            }
        }
        return value;
    }

    private int parseTerm() {
        int value = parseFactor();
        while (match(TURKONLexer.TokenType.MULT, TURKONLexer.TokenType.DIV)) {
            TURKONLexer.Token operator = getPreviousToken();
            int right = parseFactor();
            switch (operator.getType()) {
                case MULT:
                    value *= right;
                    break;
                case DIV:
                    value /= right;
                    break;
            }
        }
        return value;
    }

    private int parseFactor() {
        if (match(TURKONLexer.TokenType.IDENTIFIER)) {
            String identifier = getPreviousToken().getText();
            if (symbolTable.containsKey(identifier)) {
                return symbolTable.get(identifier);
            } else {
                throw new IllegalArgumentException("Undefined variable: " + identifier);
            }
        } else if (match(TURKONLexer.TokenType.NUMBER)) {
            return Integer.parseInt(getPreviousToken().getText());
        } else if (match(TURKONLexer.TokenType.LPAREN)) {
            int value = parseExpression();
            consume(TURKONLexer.TokenType.RPAREN);
            return value;
        } else {
            throw new IllegalArgumentException("Unexpected token: " + getCurrentToken());
        }
    }

    private boolean match(TURKONLexer.TokenType... types) {
        for (TURKONLexer.TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private TURKONLexer.Token consume(TURKONLexer.TokenType type) {
        if (check(type)) {
            return advance();
        } else {
            throw new IllegalArgumentException("Expected token of type " + type + " but got " + getCurrentToken());
        }
    }

    private boolean check(TURKONLexer.TokenType type) {
        if (isAtEnd()) return false;
        return getCurrentToken().getType() == type;
    }

    private TURKONLexer.Token advance() {
        if (!isAtEnd()) currentTokenIndex++;
        return getPreviousToken();
    }

    private boolean isAtEnd() {
        return currentTokenIndex >= tokens.size();
    }

    private TURKONLexer.Token getCurrentToken() {
        if (isAtEnd()) return null;
        return tokens.get(currentTokenIndex);
    }

    private TURKONLexer.Token getPreviousToken() {
        if (currentTokenIndex == 0) return null;
        return tokens.get(currentTokenIndex - 1);
    }
}