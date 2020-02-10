package treez.controller;

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
import treez.repository.InventoryRepository;;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@GetMapping(value = "")
	public Map<String, Object> getAllInventory() {
		
		List<Inventory> inventory =  inventoryRepository.findAll();
		Map<String, Object> responseMap = new HashMap<>();
		
		responseMap.put("inventory", inventory);
		responseMap.put("status", 0);
		responseMap.put("message", "Success");
		
		return responseMap;
	}
	
	@GetMapping(value = "/{prodId}")
	public Map<String, Object> getInventoryById(@PathVariable Integer prodId) {
		
		Map<String, Object> responseMap = new HashMap<>();
		Inventory inventory =  inventoryRepository.findByProdId(prodId);
		
		responseMap.put("inventory", inventory);
		responseMap.put("status", 0);
		responseMap.put("message", "Success");
		return responseMap;
	}
	
	@PostMapping(value = "")
	public Map<String, Object> saveInventory(@RequestBody Inventory inventory) {
		
		Map<String, Object> responseMap = new HashMap<>();
		try {
			Inventory savedInventory = inventoryRepository.save(inventory);		
			responseMap.put("inventory", savedInventory);
			responseMap.put("status", 0);
			responseMap.put("message", "Success");
			
		} catch (DuplicateKeyException e) {
			responseMap.put("inventory", "");
			responseMap.put("status", 1);
			responseMap.put("message", "Product Already Exists in inventory");
		} catch (Exception e) {
			responseMap.put("inventory", "");
			responseMap.put("status", -1);
			responseMap.put("message", e.getMessage());
		}		
	    return responseMap;
	}
	
	@PutMapping(value = "/{prodId}")
	public Map<String, Object> updateInventory(@PathVariable Integer prodId, @RequestBody Inventory inventory) {
		Inventory existingInventory =  inventoryRepository.findByProdId(prodId);
		Inventory updatedInventory = null;
		if (existingInventory != null)
		{
			inventory.setId(existingInventory.getId());
			updatedInventory = inventoryRepository.save(inventory);
		}
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("inventory", updatedInventory);
		responseMap.put("status", 0);
		responseMap.put("message", "Success");
	    return responseMap;
	}
	
	@DeleteMapping(value = "/{prodId}")
	public Map<String, Object> deleteInventory(@PathVariable Integer prodId) {
		
		Map<String, Object> responseMap = new HashMap<>();		
		try {
			inventoryRepository.deleteByProdId(prodId);			
			responseMap.put("inventory", true);
			responseMap.put("status", 200);
			responseMap.put("message", "Success");
			
		} catch (Exception e) {
			responseMap.put("inventory", "");
			responseMap.put("status", -1);
			responseMap.put("message", e.getMessage());
		}			
	    return responseMap;
	}
}
