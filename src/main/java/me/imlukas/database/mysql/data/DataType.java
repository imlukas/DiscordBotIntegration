package me.imlukas.database.mysql.data;

public class DataType<T> {

    private final ColumnType type;
    private final String name;

    public DataType(ColumnType type, String name) {
        this.type = type;
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
