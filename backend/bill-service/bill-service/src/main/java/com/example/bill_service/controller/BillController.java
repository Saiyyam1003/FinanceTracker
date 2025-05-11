package com.example.bill_service.controller;

import com.example.bill_service.dto.BillDTO;
import com.example.bill_service.model.Bill;
import com.example.bill_service.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    @Autowired
    private BillService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bill createBill(@RequestBody BillDTO dto) {
        return service.createBill(dto);
    }

    @PatchMapping("/{id}/paid")
    public Bill markBillPaid(@PathVariable Long id) {
        return service.markBillPaid(id);
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return service.getAllBills();
    }
}