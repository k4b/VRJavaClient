/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

/**
 *
 * @author karol
 */
public class LogWriter {
    
    public static void log(int id, String text) {
        String s = "[Client " + id + "] ";
        s += text + Constants.NEWLINE;
        s += "------------------------------------" + Constants.NEWLINE;
        System.out.println(s);
    }
}
