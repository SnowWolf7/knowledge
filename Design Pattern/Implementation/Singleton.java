// 懒汉模式
public final class Singleton {
	// 定义单例对象
	private static volatile Singleton INSTANCE = null;
	private Singleton() {}

	// 获得单例对象
	public static synchronized Singleton getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Singleton()
		}
	}
	return INSTANCE
}

// 饿汉模式
public class Singleton {
	private static Singleton INSTANCE = new Singleton()
	private Singleton() {}

	public static  Singleton getInstance() {
		return INSTANCE
	}
}