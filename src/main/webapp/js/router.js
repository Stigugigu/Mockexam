const routes = Object.create(null);

export default {
	register: function(path, component) {
		routes[path] = component;
	},
	navigate: function(path, param) {
		path += param ? '/' + param : '';
		if (location.hash !== '#' + path) {
			location.hash = path;
		} else {
			updateView();
		}
	}
};

window.addEventListener('hashchange', updateView);

async function updateView() {
	try {
		const [path, param] = decodeURI(location.hash).split('/').splice(1);
		const component = routes['/' + (path || '')];
		if (!component) {
			throw '<h2>404 Not Found</h2><p>Sorry, page not found!</p>';
		}
		const view = await component.render(param);
		document.querySelector('main').replaceChildren(view);
		document.title = 'Bookstore' + (component.title ? ' - ' + component.title : '');
	} catch (e) {
		console.error(e);
		const view = document.createElement('div');
		view.innerHTML = e;
		document.querySelector('main').replaceChildren(view);
	}
}
