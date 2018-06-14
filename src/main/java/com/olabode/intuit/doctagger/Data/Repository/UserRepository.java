package com.olabode.intuit.doctagger.Data.Repository;

import com.olabode.intuit.doctagger.Data.Objects.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
