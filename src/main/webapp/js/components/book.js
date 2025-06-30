import util  from '../util.js';
import store from '../store.js';
import service from '../service.js';

export default {
    title: 'Book',
    async render(isbn) {
        const view = await util.loadTemplate('book.html');

        // get book from storage
        const book = store.getBook(isbn);
        // Buch nicht gefunden? → komplette Liste laden, Store füllen, erneut rendern
            if (!book) {
              const books = await service.getBooks();   // GET /api/books
              store.setBooks(books);
              return this.render(isbn);                 // zweiter Durchlauf, jetzt mit Buch
            }

        // Fields to overwrite
        for (let [key, val] of Object.entries({
            author : book.author,
            title : book.title,
            subtitle : book.subtitle,
            description : book.description
        })) {
            util.interpolate(key, val, view);
        }
        const img = view.querySelector('[data-field=image]');
        img.src = book.image;
        img.alt = book.title;

        return view;
    }
};