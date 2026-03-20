package dev.bank_account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeductRequest {

    @NotBlank(message = "은행 코드는 필수값입니다.")
    @Schema(description = "은행 코드", example = "001")
    private String bankCode;

    @NotNull(message = "금액은 필수값입니다.")
    @Schema(description = "정산 금액 (양수: 입금, 음수: 차감)", example = "-300000")
    private BigDecimal netAmount;

}