package bwajo.bwajoserver.repository;

import bwajo.bwajoserver.entity.PaymentList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentListRepository extends JpaRepository<PaymentList, Long> {
}
