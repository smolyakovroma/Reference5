package ru.smolyakovroma.reference5.model;

import java.io.Serializable;

public class BaseSprElement implements Serializable{


    private static final long serialVersionUID = -637194393474848761L;

    private String name;
    private String code;
    private Integer id;
    private boolean folder;
    private boolean remove;
    private Integer parent_id;
    private boolean topFolder;


}
