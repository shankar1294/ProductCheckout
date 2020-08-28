package com.example.productentry.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.productentry.model.Products;
import com.example.productentry.repository.ProductRepository;

@RestController
public class ProductEntryController {
	
	@Autowired
	ProductRepository productRepository;
	
	@PostMapping(value = "/product" , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Products createProduct(@RequestBody Map<String, String> req ) throws Exception{
		
		Products product = new Products();
		if(req.get("productname") == null || req.get("stock") == null || req.get("offer") == null || req.get("price") == null){
			throw new Exception("input value cannot be null");
		} else if(req.get("productname") == "" || req.get("stock") == "" || req.get("offer") == "" || req.get("price") == "") {
			throw new Exception("input value cannot be empty");
		}
		
		product.setProductname(req.get("productname"));
		product.setStock(Integer.parseInt(req.get("stock")));
		product.setPrice(Double.parseDouble(req.get("price")));
		product.setOffer(req.get("offer"));
	
		return productRepository.save(product);
	}
	
	
	@PostMapping("/product/{id}")
    public Products update(@PathVariable String id, @RequestBody Map<String, String> body){
        int productId = Integer.parseInt(id);
        
        Products product = null;
        Optional<Products> products = productRepository.findById(productId);
        if(products.isPresent()) {
        	product  = products.get();
        	product.setProductname(body.get("productname"));
    		product.setStock(Integer.parseInt(body.get("stock")));
    		product.setPrice(Double.parseDouble(body.get("price")));
    		product.setOffer(body.get("offer"));        
        }
        return productRepository.save(product);
    }
	
	@GetMapping("/all-products")
	public List<Products> getAllProduct() throws Exception{
		
		return productRepository.findAll();
		
	}

}
