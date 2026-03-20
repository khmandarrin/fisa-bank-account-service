package dev.bank_account;

import dev.bank_account.dto.DeductRequest;
import dev.bank_account.dto.DeductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BankAccountService {

    private final BankAccountRepository accountRepository;

    /**
     * 은행별 정산 처리
     * netAmount 양수 → 입금, 음수 → 차감
     */
    public DeductResponse settle(List<DeductRequest> requests) {
        try {
            List<DeductResponse.BalanceInfo> balances = new ArrayList<>();

            for (DeductRequest request : requests) {
                Account account = accountRepository.findByBankCode(request.getBankCode())
                        .orElseThrow(() -> new RuntimeException("은행 코드를 찾을 수 없습니다: " + request.getBankCode()));

                if (request.getNetAmount().compareTo(BigDecimal.ZERO) > 0) {
                    account.deposit(request.getNetAmount());
                } else {
                    account.deduct(request.getNetAmount().abs());
                }
                balances.add(new DeductResponse.BalanceInfo(request.getBankCode(), account.getBalance()));
            }
            return DeductResponse.success(balances);
        } catch (Exception e) {
            return DeductResponse.fail(e.getMessage());
        }
    }
}