package bookstore.controller;

import bookstore.model.Order;
import bookstore.model.OrderNotFoundException;
import bookstore.model.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {
	//check the basic auth header
	// returns --> true User Credentials OK
	// returns --> false response filled with 401/403
	private boolean isAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//get header
		String header = request.getHeader("Authorization");

		//header is missing --> 401
		if (header == null || !header.startsWith("Basic ")){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
			return false;
		}
		//Base64 part decode
		String decoded = new String(
				Base64.getDecoder().decode(header.substring(6)),
				StandardCharsets.UTF_8); // "user:pw"
		//compare to admin
		if(!"admin:12345".equals(decoded)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
			return false;
		}
		return true;
	}
	private final ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//authentication
		if (!isAdmin(request,response)){
			return;
		}
		//1. step get path
		String path = request.getPathInfo(); //evoke path
		//2. step get orders (loop trough them)
		if (path == null || path.equals("/")){ //if the path is empty or "/" means clients wants full  list of all the orders
			List<Order> oders = OrderService.getOrders();
			response.setContentType("application/json");
			response.getWriter().write(objectMapper.writeValueAsString(oders));
			return;
		}
		//3. if a specific item gets requested "/api/orders/{id}"
		try {
			int id = Integer.parseInt(path.substring(1)); //get rid of the slash
			Order order = OrderService.getOrders().stream().filter(x -> x.getId() == id).findFirst().orElse(null); //search for the order
			if (order == null){
				//item not found
				response.setStatus(HttpServletResponse.SC_NOT_FOUND); //404
				return;
			} else {
				//item found
				response.setContentType("application/json"); //200
				response.getWriter().write(objectMapper.writeValueAsString(order)); //return order
			}

		} catch (NumberFormatException e) {
			//bad request from url
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //400
		}


	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//1. step get path
		String path = request.getPathInfo();
		if (path != null && !path.equals("/")){ //(path == null || path.equals("/") blocks the paths that should be enabled )
			//posts are only allowed on the api/orders!!!
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); //405
			return;
		}
		//2. step parse and validate the request body
		try {
			Order order = objectMapper.readValue(request.getReader(), Order.class);
			if (order.getName() == null || (order.getIsbn() == null || order.getIsbn().isBlank()) || order.getAddress() == null){
				//check if the mandatory fields are filled out
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			OrderService.addOrder(order); //add order AFTER CHECKING
			response.setStatus(HttpServletResponse.SC_CREATED); //return 201
			response.setContentType("application/json");
			response.getWriter().write(objectMapper.writeValueAsString(order)); //order now has ID
			} catch (NumberFormatException e) {
			//bad request error handling
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if (!isAdmin(request,response)){
			return;
		}
		//1. step get path
		String path = request.getPathInfo();
		if (path == null || path.equals("/")){
			//delete are only allowed on a specific item !!!!
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
		//parse and get the item ID in the form /api/orders/{ID}
		int id; //forward declaration for later use in second try block
		try {
			id = Integer.parseInt(path.substring(1)); //get rid of the slash$

		} catch (NumberFormatException e) {
			//id not a number
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //400
			return;
		}
		try {
			OrderService.removeOrder(id);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT); //204
		} catch (OrderNotFoundException e) { //order was not found !!!!
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (Exception e) { //500 error for all other cases
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
