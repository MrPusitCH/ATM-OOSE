public interface ATMAction {
    void checkBalance(Account account); // ตรวจสอบยอดเงิน
    boolean withdraw(Account account, double amount); // ถอนเงิน
    void deposit(Account account, double amount); // ฝากเงิน
    boolean transfer(Account fromAccount, Account toAccount, double amount); // โอนเงิน
    void convertThbToBtc(Account account, double thbAmount, double btcRate); // แปลง THB เป็น BTC
    void convertBtcToThb(Account account, double btcAmount, double btcRate); // แปลง BTC เป็น THB
}
