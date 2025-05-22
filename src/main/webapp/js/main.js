import router from './router.js';

import home from './components/home.js';
import order from './components/order.js';

router.register('/', home);
router.register('/order', order);

router.navigate('/');
