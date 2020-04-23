package API.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private Long id;
    private String login, pass;
    private List<String> roleList;

    public User(Long id, String login, String pass, String role) {
        this.id = id;
        this.login = login;
        this.pass = "{noop}" + pass;
        this.roleList = new ArrayList<>();
        if (role.contains(","))
            parseRoles(role);
        else
            this.roleList.add(role);

    }

    private void parseRoles(String role) {
        String[] roles = role.split(",");
        Collections.addAll(this.roleList, roles);
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public List<String> getRoleList() {
        return roleList;
    }
}
