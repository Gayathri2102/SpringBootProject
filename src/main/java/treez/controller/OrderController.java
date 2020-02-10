package treez.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.DuplicateKeyException;
import treez.model.Inventory;
import treez.model.Item;
import treez.model.Order;
import treez.repository.InventoryRepository;
import treez.repository.OrderRepository;;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@GetMapping(value = "")
	public Map<String, Object> getAllOrders() {
		
		List<Order> order =  orderRepository.findAll();
		Map<String, Object> responseMap = new HashMap<>();
		
		responseMap.put("order", order);
		responseMap.put("status", 0);
		responseMap.put("message", "Success");
		
		return responseMap;
	}
	
	@GetMapping(value = "/{orderId}")
	public Map<String, Object> getOrderbyId(@PathVariable Integer orderId) {
		
		Map<String, Object> responseMap = new HashMap<>();
		Order order =  orderRepository.findByOrderId(orderId);
		
		responseMap.put("order", order);
		responseMap.put("status", 0);
		responseMap.put("message", "Success");
		return responseMap;
	}
	
	@PostMapping(value = "")
	public Map<String, Object> saveOrder(@RequestBody Order order) {
		
		Map<String, Object> responseMap = new HashMap<>();
		try {
			order = fulfillOrder(order);
			Order savedOrder = orderRepository.save(order);		
			responseMap.put("order", savedOrder);
			responseMap.put("status", 0);
			responseMap.put("message", "Success");
		} catch (DuplicateKeyException e) {
			responseMap.put("order", "");
			responseMap.put("status", 1);
			responseMap.put("message", "Order Already, Please Use a different order id or call update");
		} catch (Exception e) {
			responseMap.put("order", "");
			responseMap.put("status", -1);
			responseMap.put("message", e.getMessage());
		}		
	    return responseMap;
	}
	
	@PutMapping(value = "/{orderId}")
	public Map<String, Object> updateOrder(@PathVariable Integer orderId, @RequestBody Order order) {
		Order existingOrder =  orderRepository.findByOrderId(orderId);
		Order updatedOrder = null;
		if (existingOrder != null)
		{
			order.setId(existingOrder.getId());
			existingOrder = cancelOrder(existingOrder);
			order = fulfillOrder(order);			
			updatedOrder = orderRepository.save(order);
		}
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("order", updatedOrder);
		responseMap.put("status", 0);
		responseMap.put("message", "Success");
	    return responseMap;
	}
	
	@DeleteMapping(value = "/{orderId}")
	public Map<String, Object> deleteOrder(@PathVariable Integer orderId) {
		Map<String, Object> responseMap = new HashMap<>();		
		try {
			Order existingOrder =  orderRepository.findByOrderId(orderId);
			if (existingOrder !=null && existingOrder.getOrderStatus().equals("Completed"))
			{
				cancelOrder(existingOrder);
			}
			orderRepository.deleteByOrderId(orderId);			
			responseMap.put("order", "");
			responseMap.put("status", 0);
			responseMap.put("message", "Success");
			
		} catch (Exception e) {
			responseMap.put("order", "");
			responseMap.put("status", -1);
			responseMap.put("message", e.getMessage());
		}			
	    return responseMap;
	}
	
	private Order fulfillOrder(Order order)
	{
		String orderStatus = "Completed";
		List<Item> items = order.getItems();
		List<Inventory> inventories = inventoryRepository.findAll();
		List<Inventory> updateInventories = new ArrayList<Inventory>();
		HashMap<Integer,Inventory> inventoryMap = new HashMap<Integer,Inventory>();
		for (Inventory inventory: inventories)
		{
			inventoryMap.put(inventory.getProdId(),inventory);
		}
		Integer itemQuantity;
		for (Item item: items) {
			Inventory inventory = inventoryMap.get(item.getProdId());
			itemQuantity = item.getQuantity();
			if (inventory == null || inventory.getQuantity()-itemQuantity<0)
			{
				orderStatus = "Cancelled";
				updateInventories.clear();
				break;
			}
			inventory.setQuantity(inventory.getQuantity()-itemQuantity);
			updateInventories.add(inventory);
		}
		for (Inventory inventory: updateInventories)
		{
			inventoryRepository.save(inventory);
		}
		order.setOrderStatus(orderStatus);
		return order;
	}
	
	private Order cancelOrder(Order order)
	{
		if (order.getOrderStatus().equals("Cancelled"))
		{
			return order;
		}
		List<Item> items = order.getItems();
		List<Inventory> inventories = inventoryRepository.findAll();
		HashMap<Integer,Inventory> inventoryMap = new HashMap<Integer,Inventory>();
		for (Inventory inventory: inventories)
		{
			inventoryMap.put(inventory.getProdId(),inventory);
		}
		for (Item item: items) {
			Inventory inventory = inventoryMap.get(item.getProdId());
			inventory.setQuantity(inventory.getQuantity()+item.getQuantity());
			inventoryRepository.save(inventory);
		}
		order.setOrderStatus("Cancelled");
		return order;
	}
}

