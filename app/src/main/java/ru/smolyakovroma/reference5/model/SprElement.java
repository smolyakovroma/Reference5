package ru.smolyakovroma.reference5.model;

import java.io.Serializable;

public class SprElement implements Serializable{

    private static final long serialVersionUID = 7898184753931983339L;

    private String name;
    private String code;
    private Integer id;
    private boolean folder;
    private boolean remove;
    private Integer parent_id;
    private boolean topFolder;

    public SprElement(String name, String code, boolean folder, boolean remove, Integer parent_id) {
        this.name = name;
        this.code = code;
        this.folder = folder;
        this.remove = remove;
        this.parent_id = parent_id;
    }

    public SprElement() {

    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public boolean isTopFolder() {
        return topFolder;
    }

    public void setTopFolder(boolean topFolder) {
        this.topFolder = topFolder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SprElement that = (SprElement) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
