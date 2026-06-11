public class Item {
    public int id;
    public int size;
    public int value;

    public Item(int id, int size, int value) {
        this.id = id;
        this.size = size;
        this.value = value;
    }

    @Override
    public String toString() {
        return "[" + id + ", " + size + ", " + value + "]";
    }
}