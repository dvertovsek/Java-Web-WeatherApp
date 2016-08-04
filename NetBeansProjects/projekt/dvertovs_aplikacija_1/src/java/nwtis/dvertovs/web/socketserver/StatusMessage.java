/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.socketserver;

/**
 *
 * @author dare
 */
public class StatusMessage {

    public static final String OK_10 = "OK 10;";

    public static final String AUTHENTICATION_FAILED_20 = "ERR 20;";
    public static final String AUTHORIZATION_FAILED_21 = "ERR 21;";

    public static final String ALREADY_PAUSED_30 = "ERR 30;";
    public static final String ALREADY_RUNNING_31 = "ERR 31;";
    public static final String ALREADY_STOPPED_32 = "ERR 32;";

    public static final String PAUSED_00 = "OK 00;";
    public static final String RUNNING_01 = "OK 01;";
    public static final String STOPPED_02 = "OK 02;";

    public static final String USER_ALREADY_EXISTS_33 = "ERR 33;";
    public static final String CANNOT_CHANGE_CATEGORY_34 = "ERR 34;";
    public static final String USER_DOESNT_EXIST_35 = "ERR 35;";
    
    public static final String NO_REQUESTS_LEFT_40 = "ERR 40;";
    public static final String ADRESS_ALREADY_EXISTS_41 = "ERR 41;";
    public static final String ADRESS_DOESNT_EXIST_42 = "ERR 42;";
    
    /**
     *
     * @param message tekst greške
     * @param ErrNo u parametar se prosljeđuje broj greške koji želimo vratiti
     * klijentu sa servera
     * @return metoda vraća konkatenirani string od broja greške
     */
    public static String customMessage(String message, String ErrNo) {
        String errString = "";
        return message + " " + errString + ErrNo + ";";
    }

}
