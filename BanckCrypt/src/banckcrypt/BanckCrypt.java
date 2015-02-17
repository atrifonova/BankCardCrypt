/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banckcrypt;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BanckCrypt {

    public static void main(String[] args) {

        XStream xstream = new XStream();

        User firstUser = new User("tony", "12345", true, true);
        User secondUser = new User("test", "111", true, false);
        User thirdUser = new User("gosho", "qazse", false, true);

        xstream.alias("users", ListOfUsers.class);
        xstream.alias("user", User.class);

        ListOfUsers users = new ListOfUsers();

        users.getList().add(firstUser);
        users.getList().add(secondUser);
        users.getList().add(thirdUser);

        try {
            xstream.toXML(users, new FileOutputStream(new File("users.xml")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BanckCrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            FileInputStream fileInputStream = new FileInputStream("users.xml");
           
            ListOfUsers loadedUsers = (ListOfUsers)xstream.fromXML(fileInputStream);
            System.out.println(loadedUsers);
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }

}
