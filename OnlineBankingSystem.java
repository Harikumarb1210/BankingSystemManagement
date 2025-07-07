import java.util.*;

class Account {
    static Random rand = new Random();
    static int nextAccountNumber = 81189101;

    String name;
    String accountNumber;
    String pin;
    double balance = 0.0;
    List<String> transactions = new ArrayList<>();
    boolean active = true;

    public Account(String name, String pin) {
        this.name = name;
        this.pin = pin;
        this.accountNumber = generateAccountNumber();
    }

    private String generateAccountNumber() {
        return String.valueOf(nextAccountNumber++);
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: " + amount);
    }

    public boolean withdraw(double amount, String pin) {
        if (!this.pin.equals(pin)) return false;
        if (balance >= amount) {
            balance -= amount;
            transactions.add("Withdraw: " + amount);
            return true;
        }
        return false;
    }

    public boolean changePin(String oldPin, String newPin) {
        if (this.pin.equals(oldPin)) {
            this.pin = newPin;
            return true;
        }
        return false;
    }

    public List<String> getMiniStatement() {
        int size = transactions.size();
        return transactions.subList(Math.max(0, size - 5), size);
    }

    public void addTransaction(String msg) {
        transactions.add(msg);
    }
}

class Admin {
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";

    public boolean login(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }
}

public class OnlineBankingSystem {
    static Scanner sc = new Scanner(System.in);
    static Map<String, Account> accounts = new HashMap<>();
    static Admin admin = new Admin();

    public static void main(String[] args) {
        while (true) {
            System.out.println("====== Welcome to Veltrix Bank ======");
            System.out.print("Enter your Role (admin/customer)?  ");
            String role = sc.nextLine().trim().toLowerCase();

            switch (role) {
                case "admin":
                    handleAdmin();
                    break;
                case "customer":
                    handleCustomer();
                    break;
                default:
                    System.out.println("Invalid role.");
            }
        }
    }

    private static void handleAdmin() {
        System.out.print("Enter admin username: ");
        String user = sc.nextLine();
        System.out.print("Enter admin password: ");
        String pass = sc.nextLine();

        if (admin.login(user, pass)) {
            System.out.println("Admin logged in.");
            System.out.println("List of all accounts:");
            for (Account acc : accounts.values()) {
                if (acc.active)
                    System.out.println("Account: " + acc.accountNumber + ", Name: " + acc.name + ", Balance: " + acc.balance);
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void handleCustomer() {
        while (true) {
            System.out.println("1. Create Account\n2. Login\n3. Back");
            System.out.print("Enter your choice... ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Set your pin: ");
                    String pin = sc.nextLine();
                    Account newAcc = new Account(name, pin);
                    accounts.put(newAcc.accountNumber, newAcc);
                    System.out.println("Account created. Account Number: " + newAcc.accountNumber);
                    break;
                case "2":
                    System.out.print("Enter account number: ");
                    String accNo = sc.nextLine();
                    System.out.print("Enter pin: ");
                    String loginPin = sc.nextLine();
                    Account acc = accounts.get(accNo);
                    if (acc != null && acc.pin.equals(loginPin) && acc.active) {
                        customerMenu(acc);
                    } else {
                        System.out.println("Invalid login.");
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void customerMenu(Account acc) {
        while (true) {
            System.out.println("1. Deposit\n2. Withdraw\n3. Change Pin\n4. Transaction History\n5. Mini Statement\n6. Check Balance\n7. Deactivate Account\n8. Logout");
            System.out.print("Stick your choice here ==> ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter amount to deposit: ");
                    double depAmt = Double.parseDouble(sc.nextLine());
                    acc.deposit(depAmt);
                    System.out.println("Deposited successfully.");
                    break;
                case "2":
                    System.out.print("Enter pin: ");
                    String pin = sc.nextLine();
                    System.out.print("Enter amount to withdraw: ");
                    double withAmt = Double.parseDouble(sc.nextLine());
                    if (acc.withdraw(withAmt, pin)) {
                        System.out.println("Withdrawn successfully.");
                    } else {
                        System.out.println("Failed to withdraw.");
                    }
                    break;
                case "3":
                    System.out.print("Enter old pin: ");
                    String oldPin = sc.nextLine();
                    System.out.print("Enter new pin: ");
                    String newPin = sc.nextLine();
                    if (acc.changePin(oldPin, newPin)) {
                        System.out.println("Pin changed successfully.");
                    } else {
                        System.out.println("Invalid old pin.");
                    }
                    break;
                case "4":
                    System.out.println("Transaction History:");
                    for (String t : acc.transactions) {
                        System.out.println(t);
                    }
                    break;
                case "5":
                    System.out.println("Mini Statement:");
                    for (String t : acc.getMiniStatement()) {
                        System.out.println(t);
                    }
                    break;
                case "6":
                    System.out.println("Current Balance: " + acc.balance);
                    break;
                case "7":
                    acc.active = false;
                    System.out.println("Account deactivated.");
                    return;
                case "8":
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}