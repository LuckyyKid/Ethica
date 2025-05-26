package com.ethica.ethica_backend.Repo;

import com.ethica.ethica_backend.Entity.financialAdviser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisorRepository extends CrudRepository<financialAdviser, String> {

}
