public class CardInfo {
    private String name;
    private int id;
    private String type;

    public CardInfo(String name, int id, String type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public boolean equals(Object o){
        return equals((CardInfo) o);
    }

    private boolean equals(CardInfo cardInfo){
        if (cardInfo.name.equals(this.name) ||
            cardInfo.id == this.id)
        {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
