package core.model;

import jdbc.queryGenerator.Id;

public class Account {
    @Id
    private long no;
    private String type;
    private int rest;

    public Account(long no, String types, int rest) {
        this.no = no;
        this.type = types;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }
}
