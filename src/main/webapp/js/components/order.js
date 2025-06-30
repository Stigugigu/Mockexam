import router from '../router.js';
import util from '../util.js';
import service from "../service.js";

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
		view.querySelector('form').addEventListener('submit', async e => {
			e.preventDefault();		//no reload

			try {
				await service.addOrder(order);		//POST /api/orders
				showMsg('Order successful', false);
			} catch (err){
				const status = err.status || 0;
				showMsg('Error', true)
			}
		})
		return view;
	}
};

function showMsg(text, isError){
	document.getElementById('orderForm').classList.add('hidden'); //form ausblenden

	const box = document.getElementById('orderMsg');
	box.textContent = text;
	box.classList.remove('hidden');
	box.classList.toggle('error', isError); //red Msg
	box.classList.toggle('success', !isError); //green Msg
}