/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Ian Silva
 */
public class SintaticError extends Exception{
    private final String error;

    public SintaticError(String error){
        super();
        this.error = error;
    }

    @Override
    public String getMessage() {
        return "Syntatic Error: " + this.error;
    }
}
