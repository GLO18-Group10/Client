/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.Logic;

/**
 *
 * @author Peterzxcvbnm
 */
public class MessageParser {

    LogicFacade logic;

    public MessageParser(LogicFacade logic) {
        this.logic = logic;
    }

    public String toProtocol00(String ID, String password) {
        String message = "00" + ";" + ID + ";" + password;
        logic.sendMessage(message);
        return fromProtocol00(logic.receiveMessage());

    }

    private String fromProtocol00(String message) {
        String data = message;
        return data;
    }
/**
 * Protocol 01: get customer info
 * @return 01
 */
    public String toProtocol01() {
        return "01";
    }
/**
 * Message to split the response from the server into an array
 * @param message Message from the server
 * @return An array of the different parameters from the server
 */
    public String[] fromProtocol(String message) {
        //Split the received data into the different parts
        String[] data = message.split(";");
        return data;
    }
}
