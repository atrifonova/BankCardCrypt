/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banckcrypt;

/**
 *
 * @author Toni
 */
public class User {
    
    private String userName;
    private String password;
    private boolean canEncrypt;
    private boolean canDecrypt;

    public User() {
        this("", "", false, false);
    }
    

    public User(String userName, String password, boolean canEncrypt, boolean canDecrypt) {
        setUserName(userName);
        setPassword(password);
        setCanEncrypt(canEncrypt);
        setCanDecrypt(canDecrypt);
    }

    public void setUserName(String userName) {
        if (userName != null) {
            this.userName = userName;
        } else {
            this.userName = "";
        }
    }

    public String getUserName() {
        return userName;
    }
    

    public void setPassword(String password) {
        if (password != null) {
            this.password = password;
        } else {
            this.userName = "";
        }
    }

    public String getPassword() {
        return password;
    }
    

    public void setCanEncrypt(boolean canEncrypt) {
        this.canEncrypt = canEncrypt;
    }

    public boolean isCanEncrypt() {
        return canEncrypt;
    }
    

    public void setCanDecrypt(boolean canDecrypt) {
        this.canDecrypt = canDecrypt;
    }

    public boolean isCanDecrypt() {
        return canDecrypt;
    }
    
    
        @Override
    public String toString() {
        return String.format("username: %s\npassword: %s\ncanEncrupt: %b\ncanDecrypt: %b\n", 
                userName, password, canEncrypt, canDecrypt );
    }
    
}
