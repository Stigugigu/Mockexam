let data = {};

export default {
	setBooks: function(books) {
		data.books = books;
	},

	getBooks: function() {
		return data.books;
	},

	getBook: function(isbn) {
		return data.books.find(b => b.isbn === isbn);
	},

	clear: function() {
		data = {};
	}
};
