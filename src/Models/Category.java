package Models;

public class Category {

    private int id;
    private String name;

    public Category()
    {
        setId(0);
        setName("");
    }

    public Category(int id, String name)
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
