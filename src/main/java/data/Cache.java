package data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Edin Fazlic
 * Date: 4/19/14
 */
public abstract class Cache<E> {
	protected Map<String, E> container = new HashMap<>();

	protected boolean containsKey(String key) {
		return container.containsKey(key);
	}

	protected E getObject(String key) {
		return container.get(key);
	}

	protected void put(String key, E object) {
		container.put(key, object);
	}
}
