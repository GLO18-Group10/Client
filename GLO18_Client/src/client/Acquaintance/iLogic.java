/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.Acquaintance;

/**
 *
 * @author Jeppe Enevold
 */
public interface iLogic {
    public void injectLink(iLink LinkLayer);
    void startConnection();
    void sendMessage(String message);
    String receiveMessage();
    public void toProtocol07(String ID, String name, String	birthday, String phonenumber, String address, String email, String password);
}
