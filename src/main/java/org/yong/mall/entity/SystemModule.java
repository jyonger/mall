package org.yong.mall.entity;

public class SystemModule {

    private long id;
    private String name;
    private String link;
    private int number;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "SystemModule [id=" + id + ", name=" + name + ", link=" + link + ", number=" + number + "]";
    }

}
