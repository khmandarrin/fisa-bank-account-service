package dev.bank_account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@AllArgsConstructor
public class DeductResponse {
    private boolean settled;
    private String message;
    private List<BalanceInfo> balances;

    public static DeductResponse success(List<BalanceInfo> balances) {
        return new DeductResponse(true, "정산 처리 완료", balances);
    }

    public static DeductResponse fail(String reason) {
        return new DeductResponse(false, reason, null);
    }

    @Getter
    @AllArgsConstructor
    public static class BalanceInfo {
        private String bankCode;
        private BigDecimal balance;
    }
}