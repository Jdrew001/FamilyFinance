package Models;

import java.util.Date;

public class JournalEntries {

    private int idJournal;
    private Date date;
    private double amount;
    private String categoryName;
    private String transactionName;

    public JournalEntries()
    {
        setIdJournal(0);
        setDate(new Date());
        setAmount(0.0);
        setCategoryName("");
        setTransactionName("");
    }

    public JournalEntries(int idJournal, Date date, double amount, String categoryName, String transactionName)
    {
        setIdJournal(idJournal);
        setDate(date);
        setAmount(amount);
        setCategoryName(categoryName);
        setTransactionName(transactionName);
    }

    public int getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(int idJournal) {
        this.idJournal = idJournal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }


}
