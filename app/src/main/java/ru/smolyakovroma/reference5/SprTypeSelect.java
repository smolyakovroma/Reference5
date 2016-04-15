package ru.smolyakovroma.reference5;

public enum SprTypeSelect {

    ONLY_GROUPS(1),
    ONLY_ELEMENTS(0),
    ALL(-1);

    private int type_index;

    private SprTypeSelect(int type_index) {
        this.type_index = type_index;
    }

    public int getType_index() {
        return type_index;
    }
}
