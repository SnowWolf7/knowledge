/**
 * Implemetion
 */
public interface Prototype {
	public abstract Object clone();
}

public class ConcreatePrototype implements Prototype {
	public Object cloen() {
		return super.clone();
	}
}

public class Client {
	public static void main(String[] args) {
		ConcreatePrototype obj1 = new ConcreatePrototype();
		ConcreatePrototype obj2 = (ConcreatePrototype)obj1.clone();
	}
}

/**
 * Example
 *
 * Violation of Likov's Substitution Principle
 */
class Rectangle {
	protected int m_width;
	protected int m_height;

	public int getWigth() {
		return this.m_width
	}
	public void setWigth(int width) {
		this.m_width = width;
	}
	public int getHeight() {
		return this.m_height;
	}
	public void setHeight(int height) {
		this.m_height = heigth;
	}
	public int getArea() {
		return this.m_width * this.m_height;
	}
}

class Square extends Rectangle {
	public void setWigth(int width) {
		this.m_width = width;
		this.m_height = width;
	}
	public void setHeight(int height) {
		this.m_height = height;
		this.m_width = height;
	}
}

class LaTest {
	private static Rectangle getRectangle() {
		//it can be an object returned by some factory...
		return new Square();
	}

	public static void main(String[] args) {
		Rectangle r = LaTest.getRectangle();
		r.setWigth(5);
		r.setHeight(10);
		System.out.print(r.getArea());
	}
}
