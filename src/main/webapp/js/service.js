const BASE_URL = '/api';

export default {
	getBooks: function(query) {
		return ajax('/books' + (query ? '?query=' + query : ''), {
			method: 'GET',
			headers: {'Accept': 'application/json'}
		});
	}
};

function ajax(path, options) {
	return fetch(BASE_URL + path, options)
		.then(response => {
			if (!response.ok) throw response;
			return response.headers.get('Content-Type') === 'application/json' ? response.json() : response;
		})
}
