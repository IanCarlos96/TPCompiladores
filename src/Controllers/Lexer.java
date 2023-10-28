/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Env;
import Models.Num;
import Models.Float;
import Models.Tag;
import Models.Token;
import Models.Word;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ian Silva
 */
public class Lexer {

    private static FileReader file;

    //words serve pra agrupar lexema e tag.
    private static Hashtable<String, Word> words;
    private static ArrayList<Token> tokenList;
    //public static Env top;

    private static char ch;
    //private static char estado;
    private static int line;

    //Rever esse estadoIni = ' '
    //private static final char estadoIni = ' ';
    private static final int EOF = (char) -1;

    public static FileReader getFile() {
        return file;
    }

    public static void setFile(String filename) {
        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Hashtable<String, Word> getWords() {
        return words;
    }

    public static ArrayList<Token> getTokenList() {
        return tokenList;
    }

    public static void analiseLexica() throws IOException, Exception {
        line = 1;

        // a words serve pra transformar o lexema em token
        words = new Hashtable();
        tokenList = new ArrayList();

        
        inserePalavrasReservadas();
        
        /*Jeito que funcionava na primeira entrega 2
        try {
            Token readed;
            do {
                readed = scan();
                if (readed.tag != EOF) {
                    //top.put(readed, readed.tag);
                    tokenList.add(readed);
                    readch();
                }
                //words.put(readed, readed.getTag());

            } while (readed != null && readed.tag != EOF);
        } catch (IOException ex) {
            //Logger.getLogger(Lexer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro na linha " + line);
            System.out.println("\t[" + ex.getMessage() + "]");
        }
        */
        
        for (Token token = scan(); token.tag != 65535 && token.tag != EOF; token = scan()){
            if(token.tag != Tag.COMMENTBLOCK && token.tag != Tag.COMMENT){
                tokenList.add(token);
            }
            
        }
    }

    public static void reserve(Word w) {
        words.put(w.lexema, w);
    }

    private static void readch() throws IOException {
        ch = (char) file.read();
    }

    private static boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    private static void inserePalavrasReservadas() {

        reserve(new Word("class", Tag.CLASS));
        reserve(new Word("program", Tag.PRG));
        reserve(new Word("begin", Tag.BEG));
        reserve(new Word("end", Tag.END));
        reserve(new Word("type", Tag.TYPE));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("read", Tag.READ));
        reserve(new Word("write", Tag.WRITE));

    }

    private static Token scan() throws IOException, Exception {
        //Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
            } else if (ch == '\n') {
                line++; //conta linhas
            } else {
                break;
            }
        }

        StringBuffer sb = new StringBuffer();

        switch (ch) {
            //Operadores
            case '&':
                if (readch('&')) {
                    return new Word("&&", Tag.AND, line);
                } else {
                    return new Token('&');
                }
            case '|':
                if (readch('|')) {
                    return new Word("||", Tag.OR, line);
                } else {
                    return new Token('|');
                }
            case '=':
                if (readch('=')) {
                    return new Word("==", Tag.EQ, line);
                } else {
                    return new Word ("=", Tag.ASSIGN, line);
                }
            case '<':
                if (readch('=')) {
                    return new Word("<=", Tag.LE, line);
                } else {
                    return new Word("<", Tag.LT,line);//Token('<');
                }
            case '>':
                if (readch('=')) {
                    return new Word(">=", Tag.GE, line);
                } else {
                    return new Word(">", Tag.GT, line);//Token('>');
                }
            case '!':
                if (readch('=')) {
                    return new Word("!=", Tag.NE, line);
                } else {
                    return new Token('!');
                }
            case '+':
                readch();
                return new Word("+", Tag.MAIS, line);
            case '-':
                readch();
                return new Word("-", Tag.MENOS, line);
            case '/':
                readch();
                if (ch == '*') {
                    while (true) {
                        readch();
                        if (ch == '*') {
                            readch();
                            while (ch == '*') {
                                sb.append(ch);
                                readch();
                            }
                            if (ch == '/') {
                                readch(); // Teste?
                                break;
                            } else {
                                sb.append('*');
                            }
                        } if((int)ch==65535)
                            throw new Exception("Um comentário não foi fechado");

                        sb.append(ch);
                    }
                    String s = sb.toString();
                    Word w = words.get(s);
                    if (w != null) {
                        return w;
                    }
                    w = new Word(s, Tag.COMMENTBLOCK, line);
                    words.put(s, w);
                    return w;
                } else if (ch == '/') {
                    while (ch != '\n') {
                        readch();
                    }
                    return new Word("//", Tag.COMMENT, line);
                } else {
                    return new Word("/", Tag.DIV, line);
                }
                
            case '*':
                if (readch('/')) {
                    return new Word("*/", Tag.COMMENTEND, line);
                } else {
                    return new Word("*", Tag.MULT, line);
                }
            case '(':
                readch();
                return new Word("(", Tag.PARABRE, line);
            case ')':
                readch();
                return new Word(")", Tag.PARFECHA, line);
            case '{':
                readch();
                return new Word("{", Tag.CHAVEABRE, line);
            case '}':
                readch();
                return new Word("}", Tag.CHAVEFECHA, line);
            case ';':
                readch();
                //String s = sb.toString();
                return new Word(";", Tag.PONTOVIRGULA, line);
            case '"':
                readch();
                //sb = new StringBuffer();
                while (ch != '"') {
                    if (ch != '"') {
                        sb.append(ch);
                    }
                    readch();
                    if(ch == '\n')
                        throw new Exception("Um literal não foi fechado");
                }
                Word w = words.get(sb.toString());
                if (w != null) {
                    return w;
                }
                w = new Word(sb.toString(), Tag.LITERAL, line);
                words.put(sb.toString(), w);
                readch();
                return w;
        }
        //Números
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            if (ch != '.') {
                return new Num(value, line);
            }

            float aux = 10;
            float float_value = 0;
            while (true) {
                readch();
                if (!Character.isDigit(ch)) {
                    break;
                }
                float_value += (Character.digit(ch, 10) / 10.0);
                aux = aux * 10;
            }
            return new Float(float_value, line);
        }

        //Identificadores
        if (Character.isLetter(ch)) {
            sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch) || ch == '_');
            String s = sb.toString();
            Word w = (Word) words.get(s);
            if (w != null) {
                return new Word(w.lexema, w.tag, line);
                //return w; //palavra já existe na HashTable
            }
            w = new Word(s, Tag.ID, line);
            words.put(s, w);
            return w;
        }
        //Caracteres não especificados
        Token t = new Token(ch, line);
        ch = ' ';
        return t;
    }
}
