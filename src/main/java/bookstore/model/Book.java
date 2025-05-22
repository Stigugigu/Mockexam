package bookstore.model;

public class Book {

	private final String isbn;
	private final String title;
	private final String author;
	private final double price;
	private final int pages;
	private final String date;
	private final String image;
	private final String subtitle;
	private final String description;

	public Book(String isbn, String title, String author, double price, int pages, String date, String image, String subtitle, String description) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.price = price;
		this.pages = pages;
		this.date = date;
		this.image = image;
		this.subtitle = subtitle;
		this.description = description;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public double getPrice() {
		return price;
	}

	public int getPages() {
		return pages;
	}

	public String getDate() {
		return date;
	}

	public String getImage() {
		return image;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getDescription() {
		return description;
	}
}
