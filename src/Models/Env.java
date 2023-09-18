/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Hashtable;

/**
 *
 * @author Ian Silva
 */
public class Env {

    private Hashtable table; //tabela de símbolos do ambiente
    protected Env prev; //ambiente imediatamente superior

    public Env(Env n) {
        table = new Hashtable(); //cria a TS para o ambiente
        prev = n; //associa o ambiente atual ao anterior
    }
    
    //Por quê? pq eu posso.
    public Env(){
        table = new Hashtable();
        prev = null;
    }

    public void put(Token w, int tag) {
        table.put(w, tag);
    }

    public Tag get(Token w) {
        for (Env e = this; e != null; e = e.prev) {
            Tag found = (Tag) e.table.get(w);
            if (found != null) //se Token existir em uma das TS
            {
                return found;
            }
        }
        return null; //caso Token não exista em uma das TS
    }

    public Hashtable getTable() {
        return table;
    }
    
    
}
