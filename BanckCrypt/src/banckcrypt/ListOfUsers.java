/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banckcrypt;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Toni
 */
public class ListOfUsers {

    private List<User> list = new ArrayList<User>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (User user : list) {
            builder.append(user.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public List<User> getList() {
        return list;
    }
}
