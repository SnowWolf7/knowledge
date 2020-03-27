// 抽象Subject
public abstract class Subject {

	private List<Observer> obsersers = new ArrayList<Observer>();

	// 增加观察者
	public void attach(Observer observer) {
		observers.add(observer);
	}

	// 删除观察者
	public void detech(Observer observer) {
		observers.remove(observer);
	}

	// 向观察者们发出通知
	public void notify(String newState) {
		for(Observer observer : observers) {
			observer.update(newState);
		}
	}
}

// 具体Subject类
public class ConcreteSubject extends Subject {
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String newState) {
		state = newState;
	}
}

// 抽象Observer
public interface Observer {
	public void update(String state);
}

// 具体Observer类
public class ConcreteObserver implements Observer {
	private String observerState;

	@override
	public void update(String state) {
		observerState = state
	}
}