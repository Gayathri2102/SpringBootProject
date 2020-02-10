package treez.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import treez.model.*;

public interface OrderRepository extends MongoRepository<Order, Long>{

	public Order findByOrderId(Integer orderId);
	public Long deleteByOrderId(Integer orderId);
	public Long deleteById(String id);
}
