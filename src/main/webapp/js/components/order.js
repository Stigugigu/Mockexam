import router from '../router.js';
import util from '../util.js';

export default {
	title: 'Order',
	render: async function(isbn) {
		const view = await util.loadTemplate('order.html');

		const order = {isbn};
		util.bindForm(view.querySelector('form'), order);

		view.querySelector('[data-action=cancel]').addEventListener('click', e => {
			e.preventDefault();
			router.navigate('/');
		});
		return view;
	}
};
