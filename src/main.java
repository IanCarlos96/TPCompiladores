
import Controllers.Lexer;
import Utils.Utils;
import java.io.File;
import java.io.FileReader;

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
        String filename = "./src/files/test1.txt";
        //System.out.println(System.getProperty("user.dir"));
        
        
        File file = new File(filename);
        try(FileReader fr = new FileReader(file)){
            Lexer.setFile(fr);
            Lexer.analiseLexica();
            Utils.printTabelaSimbolos(Lexer.top);
        }catch(Exception ex){
            System.out.println("["+ex.getMessage()+"]");
        }
    }
    
}
