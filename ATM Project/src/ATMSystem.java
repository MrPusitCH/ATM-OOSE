import java.util.ArrayList;
import java.util.Scanner;

public class ATMSystem implements ATMAction {
    private ArrayList<Account> accounts;
    private Manager manager;
    private double btcRate; // อัตราแลกเปลี่ยน BTC เป็น THB

    public ATMSystem() {
        accounts = new ArrayList<>();
    }

    public void setManager(Scanner scanner) {
        if (manager != null) {
            System.out.println("Manager already set!");
            return;
        }

        System.out.println("\n=== Set Manager Information ===");
        String id;
        while (true) {
            System.out.print("Enter Manager ID (13-digit citizen ID): ");
            id = scanner.nextLine();
            if (id.matches("\\d{13}")) break;
            System.out.println("Invalid ID! Please enter a valid 13-digit citizen ID.");
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Login: ");
        String login = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        manager = new Manager(id, name, gender, login, password);
        System.out.println("Manager created successfully!");
    }

    public void setBTCRate(Scanner scanner) {
        if (manager == null) {
            System.out.println("Manager is not set. Please set a manager first.");
            return;
        }

        System.out.print("Enter BTC rate (e.g., 16532 for 1 BTC = 16,532 THB): ");
        btcRate = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer
        System.out.println("BTC rate set successfully: 1 BTC = " + btcRate + " THB");
    }

    public boolean login(Scanner scanner) {
        System.out.print("Enter Login: ");
        String login = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (manager != null && manager.validateLogin(login, password)) {
            System.out.println("Welcome, Manager!");
            managerMenu(scanner);
            return true;
        }

        Account account = findAccountByLogin(login, password);
        if (account != null) {
            System.out.println("Welcome, " + account.getName() + "!");
            userMenu(scanner, account);
            return true;
        }

        return false;
    }

    @Override
    public void checkBalance(Account account) {
        System.out.println("Your Balance in THB: " + account.getBalance());
        System.out.println("Your Balance in BTC: " + account.getBtcBalance());
    }

    @Override
    public boolean withdraw(Account account, double amount) {
        if (amount > 0 && amount <= account.getBalance()) {
            account.setBalance(account.getBalance() - amount);
            System.out.println("Withdrawal successful! Remaining balance: " + account.getBalance());
            return true;
        }
        System.out.println("Insufficient balance or invalid amount.");
        return false;
    }

    public boolean withdrawBtc(Account account, double btcAmount) {
        if (btcAmount > 0 && btcAmount <= account.getBtcBalance()) {
            account.setBtcBalance(account.getBtcBalance() - btcAmount);
            System.out.println("Withdrawal successful! Remaining BTC balance: " + account.getBtcBalance());
            return true;
        }
        System.out.println("Insufficient BTC balance or invalid amount.");
        return false;
    }

    @Override
    public void deposit(Account account, double amount) {
        if (amount > 0) {
            account.setBalance(account.getBalance() + amount);
            System.out.println("Deposit successful! Current balance: " + account.getBalance());
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    @Override
    public boolean transfer(Account fromAccount, Account toAccount, double amount) {
        if (withdraw(fromAccount, amount)) {
            deposit(toAccount, amount);
            System.out.println("Transfer successful! Transferred " + amount + " to " + toAccount.getName());
            return true;
        }
        System.out.println("Transfer failed!");
        return false;
    }

    public boolean transferBtc(Account fromAccount, Account toAccount, double btcAmount) {
        if (withdrawBtc(fromAccount, btcAmount)) {
            toAccount.setBtcBalance(toAccount.getBtcBalance() + btcAmount);
            System.out.println("Transfer successful! Transferred " + btcAmount + " BTC to " + toAccount.getName());
            return true;
        }
        System.out.println("Transfer failed!");
        return false;
    }

    @Override
    public void convertThbToBtc(Account account, double thbAmount, double btcRate) {
        if (thbAmount > 0 && thbAmount <= account.getBalance()) {
            double btcEquivalent = thbAmount / btcRate;
            account.setBalance(account.getBalance() - thbAmount);
            account.setBtcBalance(account.getBtcBalance() + btcEquivalent);
            System.out.println("Converted " + thbAmount + " THB to " + btcEquivalent + " BTC");
        } else {
            System.out.println("Insufficient THB balance or invalid amount.");
        }
    }

    @Override
    public void convertBtcToThb(Account account, double btcAmount, double btcRate) {
        if (btcAmount > 0 && btcAmount <= account.getBtcBalance()) {
            double thbEquivalent = btcAmount * btcRate;
            account.setBtcBalance(account.getBtcBalance() - btcAmount);
            account.setBalance(account.getBalance() + thbEquivalent);
            System.out.println("Converted " + btcAmount + " BTC to " + thbEquivalent + " THB");
        } else {
            System.out.println("Insufficient BTC balance or invalid amount.");
        }
    }

    public void managerMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. Add New Account");
            System.out.println("2. View All Accounts");
            System.out.println("3. Set BTC Rate");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageAccounts(scanner);
                    break;
                case 2:
                    viewAccounts();
                    break;
                case 3:
                    setBTCRate(scanner);
                    break;
                case 4:
                    System.out.println("Logged out as Manager.");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    public void userMenu(Scanner scanner, Account account) {
        while (true) {
            System.out.println("\n=== ATM Menu ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Convert THB to BTC");
            System.out.println("6. Convert BTC to THB");
            System.out.println("7. Logout");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    checkBalance(account);
                    break;

                case 2:
                    System.out.println("Withdraw Options:");
                    System.out.println("1. Withdraw in THB");
                    System.out.println("2. Withdraw in BTC");
                    System.out.print("Choose: ");
                    int withdrawOption = scanner.nextInt();
                    scanner.nextLine();

                    if (withdrawOption == 1) {
                        System.out.print("Enter amount to withdraw in THB: ");
                        double withdrawThb = scanner.nextDouble();
                        scanner.nextLine();
                        withdraw(account, withdrawThb);
                    } else if (withdrawOption == 2) {
                        System.out.print("Enter amount to withdraw in BTC: ");
                        double withdrawBtc = scanner.nextDouble();
                        scanner.nextLine();
                        withdrawBtc(account, withdrawBtc);
                    } else {
                        System.out.println("Invalid option! Returning to menu.");
                    }
                    break;

                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();
                    deposit(account, depositAmount);
                    break;

                case 4:
                    System.out.println("Transfer Options:");
                    System.out.println("1. Transfer in THB");
                    System.out.println("2. Transfer in BTC");
                    System.out.print("Choose: ");
                    int transferOption = scanner.nextInt();
                    scanner.nextLine();

                    if (transferOption == 1) {
                        System.out.print("Enter recipient's Login ID: ");
                        String recipientLogin = scanner.nextLine();
                        System.out.print("Enter amount to transfer in THB: ");
                        double transferThb = scanner.nextDouble();
                        scanner.nextLine();

                        Account recipient = findAccountByLoginOnly(recipientLogin);
                        if (recipient == null) {
                            System.out.println("Recipient account not found!");
                        } else {
                            transfer(account, recipient, transferThb);
                        }
                    } else if (transferOption == 2) {
                        System.out.print("Enter recipient's Login ID: ");
                        String recipientLogin = scanner.nextLine();
                        System.out.print("Enter amount to transfer in BTC: ");
                        double transferBtc = scanner.nextDouble();
                        scanner.nextLine();

                        Account recipient = findAccountByLoginOnly(recipientLogin);
                        if (recipient == null) {
                            System.out.println("Recipient account not found!");
                        } else {
                            transferBtc(account, recipient, transferBtc);
                        }
                    } else {
                        System.out.println("Invalid option! Returning to menu.");
                    }
                    break;

                case 5:
                    System.out.print("Enter amount in THB to convert to BTC: ");
                    double thbToBtc = scanner.nextDouble();
                    scanner.nextLine();
                    convertThbToBtc(account, thbToBtc, btcRate);
                    break;

                case 6:
                    System.out.print("Enter amount in BTC to convert to THB: ");
                    double btcToThb = scanner.nextDouble();
                    scanner.nextLine();
                    convertBtcToThb(account, btcToThb, btcRate);
                    break;

                case 7:
                    System.out.println("Logged out.");
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    public void manageAccounts(Scanner scanner) {
        int availableSlots = 5 - accounts.size();
        if (availableSlots <= 0) {
            System.out.println("The system already has the maximum number of accounts (5).");
            return;
        }

        System.out.print("Enter the number of accounts to add (1 to " + availableSlots + "): ");
        int numAccounts = scanner.nextInt();
        scanner.nextLine();

        if (numAccounts > availableSlots) {
            System.out.println("You can only add up to " + availableSlots + " accounts.");
            return;
        }

        for (int i = 1; i <= numAccounts; i++) {
            System.out.println("\nAdding Account " + i + " of " + numAccounts);
            String accountID, accountName, gender, login, password;
            double balance;

            do {
                System.out.print("Enter Account ID (13 digits): ");
                accountID = scanner.nextLine();
            } while (!accountID.matches("\\d{13}"));

            do {
                System.out.print("Enter Full Name (max 50 characters): ");
                accountName = scanner.nextLine();
            } while (accountName.length() > 50);

            System.out.print("Enter Gender: ");
            gender = scanner.nextLine();

            System.out.print("Enter Login Name: ");
            login = scanner.nextLine();

            do {
                System.out.print("Enter Password (4 digits): ");
                password = scanner.nextLine();
            } while (!password.matches("\\d{4}"));

            do {
                System.out.print("Enter Initial Balance (0 to 1,000,000): ");
                balance = scanner.nextDouble();
                scanner.nextLine();
            } while (balance < 0 || balance > 1_000_000);

            accounts.add(new Account(accountID, accountName, gender, login, password, balance));
            System.out.println("Account " + i + " added successfully!");
        }
    }

    public void viewAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the system.");
            return;
        }

        System.out.println("\n=== List of Accounts ===");
        for (Account account : accounts) {
            System.out.println("Account ID: " + account.getId());
            System.out.println("Name: " + account.getName());
            System.out.println("Gender: " + account.getGender());
            System.out.println("Balance: " + account.getBalance());
            System.out.println("BTC Balance: " + account.getBtcBalance());
            System.out.println("----------------------------");
        }
    }

    private Account findAccountByLogin(String login, String password) {
        for (Account account : accounts) {
            if (account.getLogin().equals(login) && account.validatePassword(password)) {
                return account;
            }
        }
        return null;
    }

    private Account findAccountByLoginOnly(String login) {
        for (Account account : accounts) {
            if (account.getLogin().equals(login)) {
                return account;
            }
        }
        return null;
    }

    public void startATM(Scanner scanner) {
        System.out.println("Welcome to the ATM System!");
        setBTCRate(scanner);

        while (true) {
            System.out.println("\n=== ATM System ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (!login(scanner)) {
                        System.out.println("Invalid login or password!");
                    }
                    break;

                case 2:
                    System.out.println("Thank you for using the system. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
