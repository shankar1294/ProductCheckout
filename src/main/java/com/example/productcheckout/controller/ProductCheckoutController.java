package com.example.productcheckout.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.productcheckout.model.Cart;
import com.example.productcheckout.model.Products;
import com.example.productcheckout.repository.ProductCheckoutRepository;

@RestController
public class ProductCheckoutController {
	
	@Autowired
	ProductCheckoutRepository productCheckoutRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@PostMapping(value = "/product/checkout" , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> createCart(@RequestBody Map<String, String> req) throws Exception{

		System.out.println("inside cart");
		Map<String, String> response = new HashMap<String, String>();
		Cart cart = new Cart();
		if(req.get("productid") == null || req.get("quantity") == null || req.get("userid") == null || req.get("email") == null){
			throw new Exception("input value cannot be null");
		} else if(req.get("productid") == "" || req.get("quantity") == "" || req.get("userid") == "" || req.get("email") == "") {
			throw new Exception("input value cannot be empty");
		}
		int productId = Integer.parseInt(req.get("productid"));
		System.out.println("before template prdid"+ productId);
		Products product = restTemplate.getForObject("http://product-entry/product/" + productId, Products.class);
		System.out.println("after template");
		int orderQuantity = Integer.parseInt(req.get("quantity"));
		if(product != null) {
			if(orderQuantity <= product.getStock()) {
				System.out.println("inside produt not null");
				double offerAmount = (product.getOffer() * product.getPrice() / 100 ) ;
				double amount = product.getPrice() - offerAmount;
				double totalAmount = orderQuantity * amount;
				response.put("price", String.valueOf(totalAmount));
				response.put("message", "product added to cart");

				// add to cart
				cart.setEmail(req.get("email"));
				cart.setPrice(totalAmount);
				cart.setProductid(productId);
				cart.setQuantity(orderQuantity);
				cart.setUserid(Integer.parseInt(req.get("userid")));
				productCheckoutRepository.save(cart);
				System.out.println("saved in cart");
			} else {
				response.put("message", "out of stock");
			}
		} else {
			response.put("message", "out of stock");
		}
		return response;
	}
	
	@GetMapping("/product/checkout/{id}")
	public Cart getById(@PathVariable String id) {
		Optional<Cart> cart = productCheckoutRepository.findById(Integer.parseInt(id));
		if(cart.isPresent()) {
			return cart.get();
		}
		return null;
	}
	
//	@GetMapping("/removeCart/{id}")
//	public String removeCart(@PathVariable int id) throws Exception {		
//		 productCheckoutRepository.deleteByUserId(id);	
//		 return "success";
//	}

}
