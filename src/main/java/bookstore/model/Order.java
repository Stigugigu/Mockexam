package bookstore.model;

public class Order {

	private Integer id;
	private String isbn;
	private String name;
	private String address;

	public Order() {
	}

	public Order(Integer id, String isbn, String name, String address) {
		this.id = id;
		this.isbn = isbn;
		this.name = name;
		this.address = address;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}
}
