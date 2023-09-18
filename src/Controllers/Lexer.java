/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Env;
import Models.Num;
import Models.Tag;
import Models.Token;
import Models.Word;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ian Silva
 */
public class Lexer {

    private static FileReader file;
    private static BufferedReader buffer;

    //words serve pra agrupar lexema e tag.
    private static Hashtable words;
    public static Env top;
    

    private static char ch;
    private static char estado;
    private static int line;

    //Rever esse estadoIni = ' '
    private static final char estadoIni = ' ';
    private static final int EOF = (char) -1;

    public static FileReader getFile() {
        return file;
    }

    public static void setFile(FileReader file) {
        Lexer.file = file;
        //Isso não devia estar aqui, but fds
        buffer = new BufferedReader(file);
    }

    public static void analiseLexica() {
        line = 1;
        
        // a words serve pra transformar o lexema em token
        words = new Hashtable();
        top = new Env();
        
        /*
        System.out.println("Teste");
        try {
            char test;
            do{
                test = (char)buffer.read();
                System.out.print(test);
            } while(test != (char)-1);
            
        } catch (IOException ex) {
            Logger.getLogger(Lexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("-------------------------------");
        */        

        inserePalavrasReservadas();
        try {
            Token readed;
            do {
                readed = scan();
                if(readed.tag != EOF){
                    top.put(readed, readed.tag);
                }
                //words.put(readed, readed.getTag());
            } while (readed != null && readed.tag != EOF);
        } catch (IOException ex) {
            //Logger.getLogger(Lexer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro na linha " + line);
            System.out.println("\t[" + ex.getMessage() + "]");
        }
    }

    public static void reserve(Word w) {
        words.put(w.lexema, w);
    }

    private static void readch() {
        try {
            ch = (char) buffer.read();
        } catch (IOException ex) {
            Logger.getLogger(Lexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static boolean readch(char c) {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    private static void inserePalavrasReservadas() {
        reserve(new Word("if", Tag.IF));
        reserve(new Word("class", Tag.ELSE));
        reserve(new Word("program", Tag.PRG));
        reserve(new Word("begin", Tag.BEG));
        reserve(new Word("end", Tag.END));
        reserve(new Word("type", Tag.TYPE));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("else", Tag.ELSE));
        
    }

    private static Token scan() throws IOException {
        //Desconsidera delimitadores na entrada
        //for (;; readch()) {
        while(true){
            readch();
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
                continue;
            } else if (ch == '\n') {
                line++; //conta linhas
            } else {
                break;
            }
        }
        switch (ch) {
            //Operadores
            case '&':
                if (readch('&')) {
                    return Word.and;
                } else {
                    return new Token('&');
                }
            case '|':
                if (readch('|')) {
                    return Word.or;
                } else {
                    return new Token('|');
                }
            case '=':
                if (readch('=')) {
                    return Word.eq;
                } else {
                    return new Token('=');
                }
            case '<':
                if (readch('=')) {
                    return Word.le;
                } else {
                    return new Token('<');
                }
            case '>':
                if (readch('=')) {
                    return Word.ge;
                } else {
                    return new Token('>');
                }
            case '!':
                if (readch('=')) {
                    return Word.ne;
                } else {
                    return new Token('!');
                }
            case '+':
                return Word.mais;
            case '-':
                return Word.menos;
            case '/':
                if (readch('/')) {
                    return Word.comment;
                } else if (readch('*')) {
                    return Word.commentStart;
                }else {
                    return Word.div;
                }
            case '*':
                if (readch('/')){
                    return Word.commentEnd;
                } else {
                    return Word.mult;
                }
            case '(':
                return Word.parAbre;
            case ')':
                return Word.parFecha;
            case '{':
                return Word.chaveAbre;
            case '}':
                return Word.chaveFecha;
            case ';':
                return Word.pontoVirgula;
        }
        //Números
        if (Character.isDigit(ch)) {
            boolean dotUsed = false;
            int casas = 1;
            int value = 0;
            if(ch == 0){
                return new Num(value);
            } else {
                do {
                    if(!dotUsed){
                        value = 10 * value + Character.digit(ch, 10);
                    } else if(ch == '.'){
                        dotUsed = true;
                    } else{
                        value = (int) ((int)value + (Math.pow(10, (-1)*casas) * Character.digit(ch,10)));
                    }
                    readch();
                } while (Character.isDigit(ch) || (ch =='.' && !dotUsed));
                
            }
            return new Num(value);
        }
        //Identificadores
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch));
            String s = sb.toString();
            Word w = (Word) words.get(s);
            if (w != null) {
                return w; //palavra já existe na HashTable
            }
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        //Caracteres não especificados
        Token t = new Token(ch);
        ch = ' ';
        return t;
    }
}
