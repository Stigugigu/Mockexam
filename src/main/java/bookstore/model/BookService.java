package bookstore.model;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class BookService {

	private static final List<Book> books = BookData.getBooks();

	public static List<Book> getBooks() {
		return books;
	}

	public static List<Book> getBooks(String query) {
		return books.stream().filter(b -> b.getTitle().toLowerCase().contains(query.toLowerCase())).collect(toList());
	}

	public static Book getBook(String isbn) throws BookNotFoundException {
		return books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElseThrow(BookNotFoundException::new);
	}
}
