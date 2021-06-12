package com.jeff.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeff.enums.SituacaoFuncionario;
import com.jeff.enums.SituacaoObra;
import com.jeff.model.Funcionario;
import com.jeff.model.Movimentacao;
import com.jeff.model.Obra;
import com.jeff.repository.FuncionarioRepository;
import com.jeff.repository.MovimentacaoRepository;
import com.jeff.repository.ObraRepository;

@Controller
@RequestMapping("/obras")
public class ObraController {
	@Autowired
	private ObraRepository obraRepo;
	@Autowired
	private FuncionarioRepository funcRepo;
	@Autowired
	private MovimentacaoRepository mr;
	@RequestMapping(value="/cadastrarObra", method = RequestMethod.GET)
	public ModelAndView form(){
		ModelAndView mv = new ModelAndView("obra/cadastrarObra");
		
		List<Funcionario> funcionarios = funcRepo.getAllFuncs();
		mv.addObject("situacao",SituacaoObra.values());
		mv.addObject("funcionarios",funcionarios);
		List<String> controles = obraRepo.getControlesObras();
		mv.addObject("controles",controles);
		
		mv.addObject("obra",new Obra());
		return mv;
	}
		
	@RequestMapping(value="/cadastrarObraPost", method = RequestMethod.POST)
	public String cadastrarObraPost(@RequestParam(value="myArray[]") String[] a, @Valid Obra obra, BindingResult result,RedirectAttributes atributes) { //valid spring faz automaticamente pra setar os valores
		System.out.println(result.hasErrors());
		if(result.hasErrors()) {
			//se tiver error no banco n compila o resto
			return "redirect:/obras/cadastrarObra";
		}
		
		//System.out.println(obra.getInfo());
		//se a equipe de funcionarios estiver diferente de vazia
		if(!a[0].equals("vazio")) {
			obra.setFuncionarios(funcRepo.getAllByIds(a));
		}
		
		obraRepo.save(obra);
		return "redirect:/obras/lista";
		//n retorna pois o ajax que faz o post, ele que redireciona
	}
	
	@RequestMapping("/lista")
	public ModelAndView ListaObras() {
		ModelAndView mv = new ModelAndView("obra/obras");
		Iterable<Obra> obras = obraRepo.getAllObras();
		mv.addObject("obra",obras);
		return mv;
	}
	
	@RequestMapping(value="/detalhesObra/{cd}",method=RequestMethod.GET)
	public ModelAndView detalhesObra(@PathVariable("cd") long cd) {	
		Obra obra = obraRepo.findByCd(cd); //com isso ja tenho a instancia	
		ModelAndView mv = new ModelAndView("obra/detalhesObra");
		mv.addObject("obra", obra);//aqui o front ja tem acesso ao objeto 
		mv.addObject("situacao",SituacaoObra.values());
		//pegar lista do banco de todos os funcionarios para auxiliar na tabela do front
		List<Funcionario> funcionarios = funcRepo.getAllFuncs();
		mv.addObject("funcionarios",funcionarios);
		//pega equipe q esta definida nessa obra e joga para o front manipular
		List<Funcionario> equipe =  funcRepo.getAllByIds(obraRepo.getEquipeByCD(cd).toArray( new String[obraRepo.getEquipeByCD(cd).size()]));
		mv.addObject("equipe",equipe);
		
		List<String> controles = obraRepo.getControlesObras();
		mv.addObject("controles",controles);
		
		return mv;
	}
	
	@RequestMapping(value="/saveDetalhesObra/{cd}", method=RequestMethod.PUT)
	public String saveDetalhesObra(@RequestParam(value="myArray[]") String[] a, @Valid Obra obra) {
		//definindo funcionarios pelo parametro q foi enviado do front
		List<Funcionario> funcs = funcRepo.getAllByIds(a);
		//setando funcionarios para essa obra
		obra.setFuncionarios(funcs);
		//salvando funcionarios para essa obra
		obraRepo.save(obra);
		
		return "redirect:/obras/lista";
	}

	@RequestMapping("/deletarObra/{cd}")
	public String deletarObra(@PathVariable("cd") long cd) {
		//remover obra das movimentacoes
		List<Movimentacao> movis = mr.getMovimentacoesByObra(cd);
		for( Movimentacao m : movis) {
			m.setObra(null);
		}
		Obra obra = obraRepo.findByCd(cd);
		obraRepo.delete(obra);
		
		return "redirect:/obras/lista";
	}
	
	@RequestMapping("/deletarFunc")
	public String deletarFunc(long cd) {
		Funcionario func = funcRepo.findByCd(cd);
		funcRepo.delete(func);
		return null;
	}
}
