package com.greenfox.repository;

import com.greenfox.model.Status;
import org.springframework.data.repository.CrudRepository;

public interface HeartbeatRepository extends CrudRepository<Status, Boolean> {
}