package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.PaymentListNumber;
import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.CartList;
import bwajo.bwajoserver.entity.PaymentList;
import bwajo.bwajoserver.entity.PaymentStatus;
import bwajo.bwajoserver.entity.User;
import bwajo.bwajoserver.repository.PaymentItemRepository;
import bwajo.bwajoserver.repository.PaymentListRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

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

        // 결제 리스트가 없을 때
        Mockito.when(paymentListRepository.existsByUniqueNumber(Mockito.anyString())).thenReturn(false);
        Mockito.when(paymentListRepository.save(Mockito.any(PaymentList.class))).thenReturn(new PaymentList());

        PaymentListNumber result = paymentService.addPayment(user, cartList);

        assertEquals(201, result.getCode());
        assertEquals("결제 리스트가 생성되었습니다.", result.getMessage());
    }

    @Test
    public void testAddPayment_Failure_AlreadyExists() {
        User user = new User();
        CartList cartList = new CartList();
        cartList.setUser(user);

        // 결제 리스트가 이미 존재하는 경우
        Mockito.when(paymentListRepository.existsByUniqueNumber(Mockito.anyString())).thenReturn(true);

        PaymentListNumber result = paymentService.addPayment(user, cartList);

        assertEquals(400, result.getCode());
        assertEquals("결제 리스트가 이미 존재합니다.", result.getMessage());
    }

    @Test
    public void testUndoPayment_Success() {
        User user = new User();
        String paymentListUniqueNumber = "uniquePayment123";

        // 결제 리스트 존재
        PaymentList paymentList = new PaymentList();
        paymentList.setUniqueNumber(paymentListUniqueNumber);

        Mockito.when(paymentListRepository.findByUniqueNumber(paymentListUniqueNumber)).thenReturn(Optional.of(paymentList));
        Mockito.when(paymentListRepository.save(Mockito.any(PaymentList.class))).thenReturn(paymentList);

        ResultMessage result = paymentService.undoPayment(user, paymentListUniqueNumber);

        assertEquals(200, result.getCode());
        assertEquals("결제가 취소되었습니다.", result.getMessage());
    }

    @Test
    public void testUndoPayment_Failure_NotFound() {
        User user = new User();
        String paymentListUniqueNumber = "uniquePayment123";

        // 결제 리스트 존재하지 않음
        Mockito.when(paymentListRepository.findByUniqueNumber(paymentListUniqueNumber)).thenReturn(null);

        ResultMessage result = paymentService.undoPayment(user, paymentListUniqueNumber);

        assertEquals(404, result.getCode());
        assertEquals("결제 리스트를 찾을 수 없습니다.", result.getMessage());
    }
    /*
    @Test
    public void testUpdateStatus_Success() {
        String paymentListUniqueNumber = "uniquePayment123";
        PaymentStatus newStatus = PaymentStatus.DELIVERY_FAILED;

        // 결제 리스트 존재
        PaymentList paymentList = new PaymentList();
        paymentList.setUniqueNumber(paymentListUniqueNumber);
        paymentList.setPaymentStatus(PaymentStatus.DELIVERY_PROCESSING);

        Mockito.when(paymentListRepository.findByUniqueNumber(paymentListUniqueNumber)).thenReturn(Optional.of(paymentList));
        Mockito.when(paymentListRepository.save(Mockito.any(PaymentList.class))).thenReturn(paymentList);

        PaymentListNumber result = paymentService.updateStatus(paymentListUniqueNumber, newStatus);

        assertEquals(200, result.getCode());
        assertEquals("결제 상태가 업데이트되었습니다.", result.getMessage());
        assertEquals(newStatus, paymentList.getPaymentStatus());
    }
*/
    @Test
    public void testFindPaymentsForUser() {
        User user = new User();

        // 사용자에 대한 결제 내역이 존재
        PaymentList payment1 = new PaymentList();
        PaymentList payment2 = new PaymentList();
        Mockito.when(paymentListRepository.findByUser(user)).thenReturn(Arrays.asList(payment1, payment2));

        var payments = paymentService.findPaymentsForUser(user);

        assertNotNull(payments);
        assertEquals(2, payments.size());
    }

    @Test
    public void testFindAllPaymentsList() {
        // 모든 결제 내역 조회
        PaymentList payment1 = new PaymentList();
        PaymentList payment2 = new PaymentList();
        Mockito.when(paymentListRepository.findAll()).thenReturn(Arrays.asList(payment1, payment2));

        var payments = paymentService.findAllPaymentsList();

        assertNotNull(payments);
        assertEquals(2, payments.size());
    }
}