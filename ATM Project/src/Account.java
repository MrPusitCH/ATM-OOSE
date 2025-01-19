public class Account extends Person {
    private String login;
    private String password;
    private double balance;
    private double btcBalance; // ยอดเงินใน BTC

    public Account(String id, String name, String gender, String login, String password, double balance) {
        super(id, name, gender);
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.btcBalance = 0.0; // ตั้งค่าเริ่มต้นสำหรับ BTC เป็น 0
    }

    public String getLogin() {
        return login;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(double btcBalance) {
        this.btcBalance = btcBalance;
    }
}
