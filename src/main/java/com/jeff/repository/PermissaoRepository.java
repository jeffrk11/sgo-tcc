package com.jeff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeff.model.Permissao;
import com.jeff.model.Usuario;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{
	
	List<Permissao> findByUsuarios(Usuario usuario);

	
}
