package com.example.bill_service.service;

   import com.example.bill_service.dto.BillDTO;
   import com.example.bill_service.model.Bill;
   import com.example.bill_service.model.Bill.BillType;
   import com.example.bill_service.repository.BillRepository;
   import org.junit.jupiter.api.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.context.SpringBootTest;
   import org.springframework.boot.test.mock.mockito.MockBean;

   import java.time.LocalDate;
   import java.util.Optional;

   import static org.junit.jupiter.api.Assertions.assertEquals;
   import static org.junit.jupiter.api.Assertions.assertThrows;
   import static org.mockito.ArgumentMatchers.any;
   import static org.mockito.Mockito.when;

   @SpringBootTest
   public class BillServiceTest {
       @Autowired
       private BillService service;

       @MockBean
       private BillRepository repository;

       @Test
       public void testCreateRecurringBill() {
           BillDTO dto = new BillDTO();
           dto.setAmount(200.0);
           dto.setPayee("Electric Co");
           dto.setDueDate(LocalDate.of(2025, 5, 10));
           dto.setBillType("RECURRING");
           dto.setRecurrencePeriodDays(30);
           dto.setNotificationEnabled(true);
           dto.setPaid(true); // Ignored, should be false

           Bill bill = new Bill();
           bill.setAmount(dto.getAmount());
           bill.setPayee(dto.getPayee());
           bill.setDueDate(dto.getDueDate());
           bill.setBillType(BillType.RECURRING);
           bill.setRecurrencePeriodDays(dto.getRecurrencePeriodDays());
           bill.setNotificationEnabled(true);
           bill.setPaid(false); // Default
           bill.setId(3L);

           when(repository.save(any(Bill.class))).thenReturn(bill);

           Bill result = service.createBill(dto);
           assertEquals(3L, result.getId());
           assertEquals("Electric Co", result.getPayee());
           assertEquals(30, result.getRecurrencePeriodDays());
           assertEquals(false, result.isPaid());
       }

       @Test
       public void testMarkRecurringBillPaid() {
           Bill bill = new Bill();
           bill.setId(3L);
           bill.setAmount(200.0);
           bill.setPayee("Electric Co");
           bill.setDueDate(LocalDate.of(2025, 5, 10));
           bill.setBillType(BillType.RECURRING);
           bill.setRecurrencePeriodDays(30);
           bill.setNotificationEnabled(true);
           bill.setPaid(false);

           when(repository.findById(3L)).thenReturn(Optional.of(bill));
           when(repository.save(any(Bill.class))).thenReturn(bill);

           Bill result = service.markBillPaid(3L);
           assertEquals(false, result.isPaid());
           assertEquals(LocalDate.of(2025, 6, 9), result.getDueDate());
       }

       @Test
       public void testMarkOneTimeBillPaid() {
           Bill bill = new Bill();
           bill.setId(2L);
           bill.setAmount(50.0);
           bill.setPayee("Repair Shop");
           bill.setDueDate(LocalDate.of(2025, 5, 12));
           bill.setBillType(BillType.ONE_TIME);
           bill.setRecurrencePeriodDays(null);
           bill.setNotificationEnabled(true);
           bill.setPaid(false);

           when(repository.findById(2L)).thenReturn(Optional.of(bill));
           when(repository.save(any(Bill.class))).thenReturn(bill);

           Bill result = service.markBillPaid(2L);
           assertEquals(true, result.isPaid());
           assertEquals(LocalDate.of(2025, 5, 12), result.getDueDate());
       }

       @Test
       public void testInvalidBillId() {
           when(repository.findById(999L)).thenReturn(Optional.empty());
           assertThrows(IllegalArgumentException.class, () -> service.markBillPaid(999L));
       }
   }