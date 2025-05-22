package bookstore.controller;

import bookstore.model.Book;
import bookstore.model.BookNotFoundException;
import bookstore.model.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/books/*")
public class BookServlet extends HttpServlet {

	private final ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
			String query = request.getParameter("query");
			List<Book> books = query != null ? BookService.getBooks(query) : BookService.getBooks();
			response.setContentType("application/json");
			objectMapper.writeValue(response.getOutputStream(), books);
			return;
		}

		if (pathInfo.equals("/")) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}

		try {
			String isbn = pathInfo.substring(1);
			Book book = BookService.getBook(isbn);
			response.setContentType("application/json");
			objectMapper.writeValue(response.getOutputStream(), book);
		} catch (NumberFormatException ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (BookNotFoundException ex) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
