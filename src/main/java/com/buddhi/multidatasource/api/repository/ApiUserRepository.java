package com.buddhi.multidatasource.api.repository;

import com.buddhi.multidatasource.api.model.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {
}
