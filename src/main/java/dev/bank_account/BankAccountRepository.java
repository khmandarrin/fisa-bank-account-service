package dev.bank_account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByBankCode(String bankCode);
}
