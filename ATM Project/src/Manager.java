public class Manager extends Person {
    private String login;
    private String password;

    public Manager(String id, String name, String gender, String login, String password) {
        super(id, name, gender);
        this.login = login;
        this.password = password;
    }

    public boolean validateLogin(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }
}
