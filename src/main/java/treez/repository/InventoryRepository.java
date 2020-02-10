package treez.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import treez.model.*;

public interface InventoryRepository extends MongoRepository<Inventory, Long>{

	public Inventory findByProdId(Integer prodId);
	public Long deleteByProdId(Integer prodId);
	public Long deleteById(String id);
}
