package bwajo.bwajoserver.repository;

import bwajo.bwajoserver.entity.PaymentList;
import bwajo.bwajoserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentListRepository extends JpaRepository<PaymentList, Long> {
    List<PaymentList> findByUser(User user);

    boolean existsByUniqueNumber(String uniqueNumber);

    // uniqueNumber로 PaymentList를 찾는 메서드
    Optional<PaymentList> findByUniqueNumber(String uniqueNumber);
}
