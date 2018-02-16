package Models;

public class TransactionType {

    private int id;
    private String name;

    public TransactionType()
    {
        setId(id);
        setName(name);
    }

    public TransactionType(int id, String name)
    {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}