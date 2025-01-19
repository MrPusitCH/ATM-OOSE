import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATMSystem atmSystem = new ATMSystem();

        // ตั้งค่าผู้ดูแลระบบครั้งแรก
        System.out.println("=== ATM System Initialization ===");
        atmSystem.setManager(scanner);

        // เริ่มต้นระบบ ATM
        atmSystem.startATM(scanner);

        // ปิด Scanner
        scanner.close();
    }
}
