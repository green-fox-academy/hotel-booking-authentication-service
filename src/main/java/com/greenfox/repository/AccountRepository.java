package com.greenfox.repository;

import com.greenfox.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
  Account findAccountByEmail(String email);
  Page<Account> findAllByAdmin(boolean admin, Pageable pageable);
  Page<Account> findOneById(Long id, Pageable pageable);
}
