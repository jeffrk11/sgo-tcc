package com.jeff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jeff.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
		Usuario findByLogin(String login);
		
		@Query(value = "SELECT * FROM usuario WHERE login = :login", nativeQuery = true)
		Usuario getByLogin(@Param("login")String login);
}
