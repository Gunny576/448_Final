/**
    A simple bank with a single accounts.
    Garrett and Austin will be implementing the actual Bank
*/
public class Bank
{
    private int anAccount;
    private int aPin;
    private double aBalance;
    
    public Bank() {  
        anAccount = 12345;
        aPin = 1234;
        aBalance = 100.0;
    }
    
    public Bank(int account, int pin, double amount) {  
        anAccount = account;
        aPin = pin;
        aBalance = amount;
    }
    
    public boolean findAccount(int accountNumber) {  
        return (accountNumber == anAccount);
    }
    
    public boolean tryPin(int accountNumber, int accountPin) {
        return (accountNumber == anAccount && accountPin == aPin);
    }
    
    public double getBalance(int accountNumber, int accountPin) {
        if (accountNumber == anAccount && accountPin == aPin)
            return aBalance;
        else
            return -1.0;
    }
    
    public double withdraw(int accountNumber, int accountPin, double amount) {
        if (accountNumber == anAccount && accountPin == aPin) {
            if (aBalance >= amount) {
                aBalance -= amount;
                return aBalance;
            }
            else
                return -1.0;
        }
        else
            return -1.0;
    }
    
    public double deposit(int accountNumber, int accountPin, double amount) {
        if (accountNumber == anAccount && accountPin == aPin) {
            aBalance += amount;
            return aBalance;
        }
        else
            return -1.0;
    }
}
