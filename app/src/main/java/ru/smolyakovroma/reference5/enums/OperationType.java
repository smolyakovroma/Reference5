package ru.smolyakovroma.reference5.enums;

import java.util.HashMap;
import java.util.Map;

public enum OperationType {

	ALL(0), INCOME(1), OUTCOME(2), TRANSFER(3), CONVERT(4) ; // нумерация id - как в таблице

	private static Map<Integer, OperationType> map = new HashMap<Integer, OperationType>();

	static {
		for (OperationType oper : OperationType.values()) {
			map.put(oper.getId(), oper);
		}
	}

	private Integer id;

	private OperationType(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public static OperationType getType(int id) {
        return map.get(id);
    }

}
