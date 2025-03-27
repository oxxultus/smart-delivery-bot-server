package bwajo.bwajoserver.repository;

import bwajo.bwajoserver.entity.PaymentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentItemRepository extends JpaRepository<PaymentItem, Long> {
}
