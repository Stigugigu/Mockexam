package bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

	private static final List<Order> orders = new ArrayList<>();

	public static List<Order> getOrders() {
		return orders;
	}

	public static void addOrder(Order order) {
		int id = orders.stream().mapToInt(Order::getId).max().orElse(0) + 1;
		order.setId(id);
		orders.add(order);
	}

	public static void removeOrder(int id) throws OrderNotFoundException {
		if (!orders.removeIf(o -> o.getId() == id)) {
			throw new OrderNotFoundException();
		}
	}
}
