package com.ecommercesystem.checkout.CheckOutRepositories;
import com.ecommercesystem.checkout.entity.orders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface OrdersRepo extends JpaRepository<orders,Integer> {

    @Transactional
    @Modifying
    @Query(value=" Insert into orders (reference,address,book_id,book_price,item_units,order_status,orderid,userid) Values(?1,?2,?3,?4,?5,'Placed',?6,?7)", nativeQuery=true)
    Integer purchaceItem(Integer reference,String address,Integer Book_id,Integer Product_price,Integer Units,Integer orderid,Integer userid);
    @Query(value = "Select orderid from orders ORDER BY orderid DESC Limit 1")
    Integer getNextOrderId();
    @Query(value = "Select reference from orders ORDER BY reference DESC Limit 1")
    Integer getNextReference();

    @Query(value="select * from orders a where a.userid=:userid", nativeQuery=true)
    List<orders> getorders(Integer userid);

    @Transactional
    @Modifying
    @Query(value="delete from orders where userid=?1 and orderid=?2", nativeQuery=true)
    Integer canselOrder(Integer userid,Integer orderid);

    @Transactional
    @Modifying
    @Query(value="delete from orders where userid=?1 and reference=?2", nativeQuery=true)
    Integer canselItem(Integer userid,Integer reference);

    // Get all Orders
    @Query(value="select * from orders", nativeQuery=true)
    List<orders> getAllOrders();

    @Transactional
    @Modifying
    @Query(value="update orders set order_status=?2 where orderid=?1", nativeQuery=true)
    Integer changeOrderStatus(int orderid, String newStatus);

    @Transactional
    @Modifying
    @Query(value="update orders set order_status=?2 where reference=?1", nativeQuery=true)
    Integer changeReferenceStatus(int reference, String newStatus);
}
