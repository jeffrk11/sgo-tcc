package com.jeff.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jeff.model.Grupo;
import com.jeff.model.Permissao;
import com.jeff.model.Usuario;
import com.jeff.repository.PermissaoRepository;
import com.jeff.repository.UsuarioRepository;

@Component
public class UsuarioUserDetailsService implements UserDetailsService{

	 @Autowired
	  private UsuarioRepository ur;
	  @Autowired
	  private PermissaoRepository pr;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario u = ur.getByLogin(username);
		if(u == null) {
			System.out.println("usuario nao encontrado");
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		//System.out.println("LoadUserByUsername "+u.getFuncionario().getNome());                   // pode ser nulo tratar
		return new UsuarioSistema(u.getNome(), u.getLogin(), u.getSenha(), getUsuarioAuthorities(u), u.getFuncionario());
	}
	
	public  Collection<? extends GrantedAuthority> getUsuarioAuthorities(Usuario usuario) {
		Collection<GrantedAuthority> auths = new ArrayList<>();
		System.out.println("O usuario "+usuario.getNome()+" tem as permissoes: ");
		for(Permissao p: pr.findByUsuarios(usuario)) {
			System.out.println(p.getNome());
			auths.add(new SimpleGrantedAuthority("ROLE_" + p.getNome()));
		}
		return auths;
	}
	/* algaworks
	public Collection<? extends GrantedAuthority> authorities(Usuario usuario) {
		System.out.println("authoritis1");
	    return authorities(gr.findByUsuarios(usuario));
	  }
	
	public Collection<? extends GrantedAuthority> authorities(List<Grupo> grupos) {
	    Collection<GrantedAuthority> auths = new ArrayList<>();
	    System.out.println("authoritis2");
	    for (Grupo grupo: grupos) {
	      List<Permissao> lista = pr.findByGrupos(grupo);
	      System.out.println(grupo.getNome());
	      for (Permissao permissao: lista) {
	    	  System.out.println(permissao.getNome());
	        auths.add(new SimpleGrantedAuthority("ROLE_" + permissao.getNome()));
	      }
	    }
	    return auths;
	  }
*/
}
