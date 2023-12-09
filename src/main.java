
import Controllers.Lexer;
import Controllers.Sintaxer;
import Models.Token;
import Models.Word;
import Utils.Utils;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Ian Silva
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Informar o nome do arquivo aqui ou por argumento
        String filepath = "./src/files/";
        String filename = "";
        if(args.length == 0){
            filename = "test8.txt";
        } else {
            filename = args[0];
        }
        
        //System.out.println(System.getProperty("user.dir"));
        String fullFile = filepath+filename;
        
        File file = new File(fullFile);
        try(FileReader fr = new FileReader(file)){
            System.out.println("Iniciando análise léxica do arquivo "+fullFile);
            Lexer.setFile(file.getAbsolutePath());
            Lexer.analiseLexica();
            ArrayList<Token> token_list = Lexer.getTokenList();
            Hashtable<String, Word> words = Lexer.getWords();
            Sintaxer sintatic = new Sintaxer(token_list, words);
            sintatic.program();
            System.out.println("Fim da análise sintática");
            //Utils.printTabelaSimbolos(Lexer.top);
        }catch(Exception ex){
            System.out.println("["+ex.getMessage()+"]");
        }
    }
    
}
