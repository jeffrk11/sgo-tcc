package com.jeff.controllers;

import java.math.BigInteger;
import java.sql.Blob;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jeff.model.Funcionario;
import com.jeff.repository.FuncionarioRepository;
import com.jeff.repository.MovimentacaoRepository;
import com.jeff.repository.ObraRepository;
import com.jeff.repository.UsuarioRepository;
import com.jeff.security.UsuarioSistema;

@Controller
@ControllerAdvice
public class IndexController {
	@Autowired
	FuncionarioRepository fr;
	@Autowired
	ObraRepository or;
	@Autowired
	MovimentacaoRepository mr;
	
	@GetMapping("/login")
	public String login() {
		return "fixos/login"; //retorna a pagina de login
	}
	//model global para todas as pagians
	@ModelAttribute
	public void addAtributos(Model model) {
		try {
			UsuarioSistema u = (UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//pode recerber null
			Funcionario aux = u.getFunc();

			
			if(aux == null) {
				aux = new Funcionario();
				aux.setNome(u.getNome());
				aux.setCargo(u.getAuthorities().toString().replaceAll("ROLE_","").replace("[", "").replace("]",""));
			}
			//System.out.println("roles: "+u.getAuthorities().toString().replaceAll("ROLE_","").replace("[", "").replace("]",""));
			model.addAttribute("usuario_funcionario", aux);

			if(aux.getImg() != null) {
				model.addAttribute("img_user_perfil", byteToInt(aux.getImg()));
			}
			
			//model.addAttribute("ft_user_func",fr.getByID(aux.getId()));
		}catch (Exception e) {
			System.out.println(e);
			System.out.println("usuario nullo "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}	
	}
	
	@RequestMapping("/")
	@PreAuthorize("hasAnyRole('VISITANTE','ADMIN','USUARIO')")
	public ModelAndView index() {
		//mascaras
		NumberFormat mask = NumberFormat.getCurrencyInstance();
		
		
		//pegar informações para o dash board
		ModelAndView mv = new ModelAndView("dashboard/dashboard");
		//Map de inforçoes de obras e funcionarios
		Map<String, Object> dashinfo = new HashMap<String, Object>();
		
		//get situacoes funcionarios agrupadas
		int qtdFunc =0;
		for(Object[] ob : fr.getQtdFuncGroupBySituacao()) {
			//funcionarios situacao situacao / qtd
			dashinfo.put((String) ob[0], ob[1]);
			qtdFunc += Integer.valueOf( String.valueOf(ob[1]));
		}
		dashinfo.put("QtdFunc", qtdFunc);
		//Obras --------------------------
		int qtdObra = 0;
		for(Object[] ob : or.getQtdObraGroupBySituacao()) {
			//obras situacao situacao / qtd
			dashinfo.put("QtdObra"+ ob[0], ob[1]);
			qtdObra += Integer.valueOf(ob[1].toString());
		}	
		dashinfo.put("QtdObra",qtdObra);
		
		//quantidade de todas as obras
		if(dashinfo.get("ATIVO") != null) {
			//Salarios ativos
			dashinfo.put("SalarioAtivos", mask.format(fr.getSalarioSituacao("ATIVO")));
		}
		
		//financeiro ---------------------
		 for(Object[] ob : mr.getQtdMovimentacaoSituacao()) {
			dashinfo.put("QtdMovimentacao"+ob[0], ob[1]);
		 }
		
		for (Map.Entry<String, Object> entry : dashinfo.entrySet()) {
		    System.out.println(entry.getKey() + " / " + entry.getValue());
		}
		
		mv.addObject("dashinfo", dashinfo);
		
		//return "index"; vai para a pagina index
		//redirecionar html n precisa de redirect, redirect é apenas quando ja tenho um metodo com request
		return mv;//"redirect:/obras"; // redireciona para lista de obras
	}
	
	private short[]  byteToInt(byte[] array) {
		short[] aux = new short[array.length];
		for(int i = 0; i < array.length; i++) {
			aux[i] = (short) (array[i]+128);
		}
		return aux;
	}
}
