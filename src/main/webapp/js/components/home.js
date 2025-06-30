import util from '../util.js';
import service from '../service.js';
import store from '../store.js';
import router from '../router.js';

export default {
	title: 'Home',
	render: async function() {
		const view = await util.loadTemplate('home.html');
		if (!store.getBooks()) {
			service.getBooks().then(books => {
				store.setBooks(books);
				books.forEach(book => renderBook(view, book));
			});
		} else {
			store.getBooks().forEach(book => renderBook(view, book));
		}
		return view;
	}
};

function renderBook(view, book) {
	const template =
		`<li>
			<img src="${book.image}" alt="${book.title}"/>
			<h2>${book.title}</h2>
			<h4>${book.author}</h4>	
			<span class="price">Fr. ${book.price.toFixed(2)}</span>
			<button class="buy" data-isbn="${book.isbn}">Buy now</button>
		</li>`;

	const li = document.createElement('li');
	li.innerHTML = template;
	li.querySelector('.buy').addEventListener('click', e => {
		router.navigate('/order', book.isbn);
	});
	li.querySelector('img') //reroute klick on image
	   .addEventListener('click', () =>
		        router.navigate('/book', book.isbn));
	li.querySelector('h2') //reroute klick on h2 (title of book)
	   .addEventListener('click', () =>
		        router.navigate('/book', book.isbn));
	view.querySelector('ul').append(li);
}
