package com.starwars.batch.repository;

import com.starwars.batch.domain.People;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by joaquinanton on 21/7/17.
 */
public interface PeopleRepository extends JpaRepository<People,String> {
}
