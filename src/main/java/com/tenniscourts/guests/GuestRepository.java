package com.tenniscourts.guests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query(value ="SELECT * FROM GUEST WHERE UPPER(NAME) LIKE %?1%", nativeQuery = true)
    List<Guest> findByName(String name);

}
