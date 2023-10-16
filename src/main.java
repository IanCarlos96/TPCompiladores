
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
        
        //Informar o nome do arquivo aqui
        String filename = "./src/files/test1_corrigido.txt";
        //System.out.println(System.getProperty("user.dir"));
        
        
        File file = new File(filename);
        try(FileReader fr = new FileReader(file)){
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
