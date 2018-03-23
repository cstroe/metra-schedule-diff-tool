public class Station {
    public final int id;
    public final String name;

    private Station(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station of(String id, String name) {
        return new Station(Integer.parseInt(id), name);
    }

    @Override
    public String toString() {
        return name;
    }
}
