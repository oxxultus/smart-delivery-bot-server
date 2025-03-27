package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.PaymentListNumber;
import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.*;
import bwajo.bwajoserver.repository.PaymentItemRepository;
import bwajo.bwajoserver.repository.PaymentListRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentServiceImplTest {

    @Mock
    private PaymentListRepository paymentListRepository;

    @Mock
    private PaymentItemRepository paymentItemRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    public void testAddPayment_Success() {
        User user = new User();
        CartList cartList = new CartList();
        cartList.setUser(user);

        Mockito.when(paymentListRepository.existsByUniqueNumber(Mockito.anyString())).thenReturn(false);
        Mockito.when(paymentListRepository.save(Mockito.any(PaymentList.class))).thenReturn(new PaymentList());

        PaymentListNumber result = paymentService.addPayment(user, cartList);

        assertEquals(201, result.getCode());
        assertEquals("결제 리스트가 생성되었습니다.", result.getMessage());
    }

    // 추가적인 테스트 메서드들 작성
}