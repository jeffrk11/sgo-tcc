package com.jeff.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.jeff.model.Funcionario;

public class UsuarioSistema extends User {
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private Funcionario funcionario;
	
	public UsuarioSistema(String nome, String username, String password, Collection<? extends GrantedAuthority> authorities, Funcionario func) {
		super(username, password, authorities);
		this.funcionario = func;
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

	public Funcionario getFunc() {
		return funcionario;
	}

}
