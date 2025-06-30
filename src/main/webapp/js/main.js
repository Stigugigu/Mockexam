import router from './router.js';

import home from './components/home.js';
import order from './components/order.js';
import book from './components/book.js'
//when registering a new page , import new component , register route
router.register('/', home);
router.register('/order', order);
router.register('/book', book); //<-
//
router.navigate('/');
