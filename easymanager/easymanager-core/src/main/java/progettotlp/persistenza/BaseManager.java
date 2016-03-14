package progettotlp.persistenza;

import java.io.Serializable;

public interface BaseManager {

	public <T> T get(Class<T> clazz, Serializable id);
}
