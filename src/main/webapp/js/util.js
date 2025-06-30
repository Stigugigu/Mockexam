const TEMPLATE_ROOT = 'templates/';

export default {
	loadTemplate: function(template, tag) {
		const el = document.createElement(tag || 'div');
		return fetch(TEMPLATE_ROOT + template)
			.then(response => response.ok ? response.text() : Promise.reject(response))
			.then(tpl => { el.innerHTML = tpl; return el; })
			.catch(e => Promise.reject(`Loading template '${template}' failed!`));
	},
	interpolate(key, value, context = document) {
		if (value instanceof Object && !Array.isArray(value)) {
			for (let prop in value) {
				const newKey = key ? `${key}.${prop}` : prop;
				this.interpolate(newKey, value[prop], context);
			}
		} else {
			context.querySelectorAll(`[data-field="${key}"]`).forEach(el => el.innerHTML = value ?? '');
		}
	},
	bindForm(form, obj) {
		const names = [...new Set(Array.from(form.elements).map(el => el.name))];
		names.forEach(name => name && bindFormField(form[name], obj, name));
	}
}

function bindFormField(elem, obj, prop) {
	if (elem instanceof RadioNodeList) {
		elem.forEach(el => {
			if (el.type === 'radio') {
				el.checked = el.value == obj[prop];
				el.oninput = () => obj[prop] = el.value;
			} else if (el.type === 'checkbox') {
				el.checked = obj[prop].includes && obj[prop].includes(el.value);
				el.oninput = () => obj[prop] = Array.from(elem).filter(el => el.checked).map(el => el.value);
			} else {
				bindFormField(el, obj, prop);
			}
		})
	} else {
		elem.value = obj[prop] ?? '';
		elem.oninput = () => obj[prop] = elem.type === 'number' ? Number(elem.value) : elem.value;
	}
}
