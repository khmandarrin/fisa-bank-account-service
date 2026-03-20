package dev.bank_account;

import dev.bank_account.dto.DeductRequest;
import dev.bank_account.dto.DeductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bok")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    /**
     * 정산 요청 처리
     * settlement-service에서 은행별 정산 금액 리스트를 받아
     * 각 계좌에 입금 또는 차감을 수행
     */
    @PostMapping("/settle")
    public ResponseEntity<DeductResponse> settle(@RequestBody List<DeductRequest> requests) {
        DeductResponse response = bankAccountService.settle(requests);
        return ResponseEntity.ok(response);
    }

}
