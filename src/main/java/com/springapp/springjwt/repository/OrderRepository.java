package com.springapp.springjwt.repository;

import com.springapp.springjwt.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order WHERE orderUuid = ?1")
    Order findOrderById(String orderId);

    @Query("delete FROM Order WHERE orderUuid = ?1")
    Order deleteOrderById(String orderId);

    @Query("FROM Order WHERE userName = ?1 AND isPayed = ?2")
    Order findOrderByNameAndStatus(String username, Boolean isPaid);

    @Modifying
    @Query("update Order u set u.isPayed = :status where u.orderUuid = :orderId")
    void updateOrderStatus(@Param(value = "orderId") String orderId, @Param(value = "status") Boolean status);
}
