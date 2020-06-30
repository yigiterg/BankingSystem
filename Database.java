package banking;

import java.io.File;
import java.sql.*;

public class Database {

    private final Connection con;
    private final DatabaseMetaData meta;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final String  url;
    public Database(String url) throws SQLException {

        this.url = url;
        this.con = DriverManager.getConnection(url);
        meta = con.getMetaData();
        resultSet = meta.getTables(null,null,"card",null);
        if(!resultSet.next()) {
            this.statement = con.createStatement();
            String tableContent =  "CREATE TABLE card " +
                    " (id INTEGER not NULL ," +
                    " number TEXT," +
                    " pin TEXT,"    +
                    " balance INTEGER DEFAULT 0)";
            statement.executeUpdate(tableContent);
        }
        resultSet.close();
    }

   /* public void createTable() throws SQLException {
       this.statement = con.createStatement();
       String tableContent =  "CREATE TABLE card " +
                               " (id INTEGER not NULL PRIMARY KEY," +
                               " number VARCHAR(16)," +
                                " pin VARCHAR(4),"    +
                                " balance INTEGER DEFAULT 0)";
       statement.executeUpdate(tableContent);

    }*/
    public void insertInformation(int idNumber, String accountNumber, String accountPin) throws SQLException {
        this.preparedStatement = con.prepareStatement("INSERT INTO card(id, number, pin) " +
                "VALUES(?, ?, ?)");
        this.preparedStatement.setInt(1,idNumber);
        this.preparedStatement.setString(2,accountNumber);
        this.preparedStatement.setString(3,accountPin);
        this.preparedStatement.executeUpdate();
     //   preparedStatement.close();
    }

    public boolean isAccountExist(String account) throws SQLException {
        this.preparedStatement = con.prepareStatement("select *  from card WHERE number = ?");
        preparedStatement.setString(1,account);
        resultSet = preparedStatement.executeQuery();
        return resultSet.next();

    }


    public boolean isExist(String account, String password) throws SQLException {
        this.preparedStatement = con.prepareStatement("select *  from card WHERE pin =? AND number = ?");
        preparedStatement.setString(1,password);
        preparedStatement.setString(2,account);
        resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public void retrieveInformation(String account, String password) throws SQLException {
        this.preparedStatement = con.prepareStatement("select id, number, pin, balance from card WHERE pin = ? AND number = ?" );
        preparedStatement.setString(1,password);
        preparedStatement.setString(2,account);
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            int id  = resultSet.getInt("id");
            String accountNumber = resultSet.getString("number");
            String pin = resultSet.getString("pin");
            int balance = resultSet.getInt("balance");
            System.out.println("ID: " + id);
            System.out.println("Account number: " + accountNumber);
            System.out.println("Pin: " + pin);
            System.out.println("Balance: " + balance);
            resultSet.close();
        }
    }

    public void getBalance(String account, String password) throws SQLException {
        this.preparedStatement = con.prepareStatement("select balance from card WHERE number = ? AND pin = ?");
        preparedStatement.setString(1,account);
        preparedStatement.setString(2,password);
        resultSet = preparedStatement.executeQuery();
        int balance = resultSet.getInt("balance");
        System.out.printf("Balance: %d\n",balance);
        preparedStatement.close();
    }

    public int checkBalance(String account, String password) throws SQLException {
        this.preparedStatement = con.prepareStatement("select balance from card WHERE number = ? AND pin = ?");
        preparedStatement.setString(1,account);
        preparedStatement.setString(2,password);
        resultSet = preparedStatement.executeQuery();
        return resultSet.getInt("balance");
    }

    public void updateBalance (int balance, String account, String pin) throws SQLException {
        this.preparedStatement = con.prepareStatement("UPDATE card  SET balance = ? WHERE  pin = ? AND number = ? " );
        preparedStatement.setInt(1,balance);
        preparedStatement.setString(2,pin);
        preparedStatement.setString(3,account);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deleteAccount (String account) throws SQLException {
        this.preparedStatement = con.prepareStatement("DELETE FROM card WHERE number = ?" );
        preparedStatement.setString(1,account);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void removeTable () throws SQLException {
        this.preparedStatement = con.prepareStatement("DROP TABLE card ");
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void connectionClose() throws SQLException {
        con.close();
    }
    public void depositMoney(String account, int amount) throws SQLException {
        this.preparedStatement = con.prepareStatement("UPDATE card  SET balance = balance+? WHERE  number = ? " );
        preparedStatement.setInt(1,amount);
        preparedStatement.setString(2,account);
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }
    public void withdrawMoney(String account, int amount) throws SQLException {
        this.preparedStatement = con.prepareStatement("UPDATE card  SET balance = balance-? WHERE  number = ? " );
        preparedStatement.setInt(1,amount);
        preparedStatement.setString(2,account);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }




}
