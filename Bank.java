package banking;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Bank {
    ArrayList <CreditCard> cardList = new ArrayList<>();
    Database database;
    Scanner scanner = new Scanner(System.in);
    boolean exit = true;
    boolean loggedIn = false;

    public Bank(Database database) throws SQLException {
        this.database = database;
    }

    public void menu() throws SQLException {
        while(exit) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            String choice = scanner.nextLine();
            int realChoice = Integer.parseInt(choice);
            System.out.println();
            switch (realChoice) {
                case 1: {
                    createAccount();
                    System.out.println();
                    break;
                }
                case 2: {
                    logIntoAccount();
                    System.out.println();
                    break;
                }
                case 0: {
                    System.out.println("Bye!");
                    exit = false;
                    break;
                }
            }
        }
    }

    public void createAccount() throws SQLException {
        CreditCard creditCard = new CreditCard();
 //       cardList.add(creditCard);
        database.insertInformation(creditCard.getId(),creditCard.getCreditNumber(),Integer.toString(creditCard.getPin()));
        System.out.printf("Your card has been created\n" +
                "Your card number:\n" +
                "%s\n" +
                "Your card PIN:\n" +
                "%d\n",creditCard.getCreditNumber(),creditCard.getPin());
    }
    public void logIntoAccount() throws SQLException {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter  your PIN:");
        String strPin = scanner.nextLine();
        int pin = Integer.parseInt(strPin);
        if(database.isExist(cardNumber,strPin)){
            System.out.println("You have successfully logged in!\n");
            loggedIn = true;
            customerInterface(cardNumber,strPin);
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }
    public void customerInterface(String number, String pin) throws SQLException {
        while (loggedIn) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            String choice = scanner.nextLine();
            int choiceInt = Integer.parseInt(choice);
            switch (choiceInt) {
                case 1: {
                    database.getBalance(number,pin);
                    break;
                }
                case 2: {
                    System.out.println("Enter income:");
                    String income = scanner.nextLine();
                    int incomeInt = Integer.parseInt(income);
                    database.updateBalance(incomeInt,number,pin);
                    break;
                }
                case 3: {
                    System.out.println("Transfer");
                    System.out.println("Enter card number:");
                    String account = scanner.nextLine();
                    if(database.isAccountExist(account) && this.luhnAlgorithmCheck(account)  ){
                        System.out.println("Enter how much money you want to transfer:");
                        String amount = scanner.nextLine();
                        int amountOfMoney = Integer.parseInt(amount);
                        if(database.checkBalance(number,pin) >= amountOfMoney) {
                            database.depositMoney(account,amountOfMoney);
                            database.withdrawMoney(number,amountOfMoney);
                            System.out.println("Success!\n");
                        } else {
                            System.out.println("Not enough money!\n");
                        }
                    } else if (!this.luhnAlgorithmCheck(account)){
                        System.out.println("Probably you made mistake in the card number. Please try again!\n");
                    } else {
                        System.out.println("Such a card does not exist\n");
                    }
                    break;
                }
                case 4 : {
                        database.deleteAccount(number);
                    System.out.println("The account has been closed\n");
                    break;
                }
                case 5 : {
                    System.out.println("You have successfully logged out!\n ");
                    loggedIn = false;
                    break;
                }
                case 0: {
                    loggedIn = false;
                    exit = false;
                    System.out.println("Bye!");
                    break;
                }
            }
        }
    }

    public boolean luhnAlgorithmCheck (String number) {
        int[] creditNumberArr = new int[16];
        int numberSum = 0;
        for(int i = 0; i < 16 ; i++) {
            creditNumberArr[i] = Integer
                    .parseInt(String.valueOf(number.charAt(i)));
        }
        for (int i = 0; i < creditNumberArr.length-1; i++) {
            if(i % 2 == 0) {
                creditNumberArr[i] *= 2;
            }
            if(creditNumberArr[i] > 9) {
                creditNumberArr[i] -= 9;
            }
            numberSum += creditNumberArr[i];
        }
        numberSum += creditNumberArr[15];
        return numberSum % 10 == 0;
    }







}
