package com.jeff.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jeff.enums.CategoriaMovimentacao;
import com.jeff.enums.SituacaoMovimentacao;
import com.jeff.enums.TipoMovimentacao;
import com.jeff.model.Funcionario;
import com.jeff.model.Movimentacao;
import com.jeff.model.Obra;
import com.jeff.repository.FuncionarioRepository;
import com.jeff.repository.MovimentacaoRepository;
import com.jeff.repository.ObraRepository;

@Controller
@RequestMapping("/financeiro")
public class MovimentacaoController {
	@Autowired
	ObraRepository or;
	@Autowired
	FuncionarioRepository fr;
	@Autowired
	MovimentacaoRepository mr;
	
	@RequestMapping(value="/consultar", method = RequestMethod.GET)
	public ModelAndView consultarMovimentacoes() {
		ModelAndView mv = new ModelAndView("financeiro/consultar");
		mv.addObject("categorias",CategoriaMovimentacao.values());
		mv.addObject("tipos",TipoMovimentacao.values());
		
		// getAll feito por query utilizado no lugar do findAll
		List<Movimentacao> movis = mr.getAllMovis();
		//           nome do obj,   o obj
		mv.addObject("movimentacoes", movis);
		return mv;
	}
	//pesquisa para o fragmento dinamico
	@RequestMapping(value="/pesquisar", method = RequestMethod.GET)
	public ModelAndView procurarMovimentacao(@RequestParam(value = "descricao", required = false)String descricao,
											@RequestParam(value = "categoria", required = false)String categoria,
											@RequestParam(value = "tipo", required = false)String tipo,
											@RequestParam(value = "valor", required = false)String valor) {
		ModelAndView mv = new ModelAndView("financeiro/consultar :: resultsList");
		
		
		List<Movimentacao> movis = mr.getByPesquisa(descricao,categoria,tipo, valor);
		
		mv.addObject("movimentacoes",movis);
		
		return mv;
	}
	@RequestMapping(value = "/movimentacao/lancar", method = RequestMethod.GET)
	public ModelAndView lancarGet() {
		ModelAndView mv = new ModelAndView("financeiro/lancamento");
		
		mv.addObject("categorias",CategoriaMovimentacao.values());
		mv.addObject("tipos",TipoMovimentacao.values());
		
		List<Obra> obras =  or.getAllObras();
		mv.addObject("obras",obras);
		List<Funcionario> funcionarios = fr.getAllFuncs();
		mv.addObject("funcionarios",funcionarios);
		
		//mandando para o front um funcionario vazio para que ele saiba quais os atributos pegar para o json automatico
		mv.addObject("movimentacao",new Movimentacao());
		
		mv.addObject("linkPost","/financeiro/movimentacao/lancar");
		mv.addObject("linkTipo","POST");
		return mv;
	}
	
	@RequestMapping(value="/movimentacao/lancar", method=RequestMethod.POST)
	public String lancarPost(@RequestParam(value="vinculados[]", required = false) String[] vinculados,@RequestParam(value="tipoVinculado", required = false) char tipo, Movimentacao movimentacao) {
		System.out.println("ENTROU NO CONTROLLER");
		System.out.println(movimentacao.toString());
		
		switch(tipo) {
			case 'f':
				movimentacao.setFuncionario(fr.getAllByCds(vinculados));	
				break;
			case 'o':
				movimentacao.setObra(or.getAllByCds(vinculados).get(0));
				break;
		}
		mr.save(movimentacao);
		
		return "redirect:/financeiro/movimentacao/lancar";
	}
	
	@RequestMapping(value="/movimentacao/editar/{id}", method=RequestMethod.GET)
	public ModelAndView detalheMovimentacao(@PathVariable("id") long id) {
		ModelAndView mv = new ModelAndView("financeiro/lancamento");
		mv.addObject("movimentacao", mr.getMovimentacaoByID(id));

		mv.addObject("categorias",CategoriaMovimentacao.values());
		mv.addObject("tipos",TipoMovimentacao.values());
		
		List<Obra> obras =  or.getAllObras();
		mv.addObject("obras",obras);
		List<Funcionario> funcionarios = fr.getAllFuncs();
		mv.addObject("funcionarios",funcionarios);
		
		mv.addObject("linkPost","/financeiro/movimentacao/editar");
		mv.addObject("linkTipo","PUT");
		
		return mv;
	}
	
	@RequestMapping(value="/movimentacao/editar", method=RequestMethod.PUT)
	public String detalheMovimentacaoPost(@RequestParam(value="vinculados[]", required = false) String[] vinculados,@RequestParam(value="tipoVinculado", required = false) char tipo, Movimentacao movimentacao) {
		System.out.println("PUUUUUUUUUUUUUT");
		System.out.println(movimentacao.toString());
		switch(tipo) {
			case 'f':
				movimentacao.setFuncionario(fr.getAllByCds(vinculados));	
				break;
			case 'o':
				movimentacao.setObra(or.getAllByCds(vinculados).get(0));
				break;
		}
		mr.save(movimentacao);
		
		return "redirect:/";
	}
	
	//GET pois eh um link feito no html, e o html apenas fornece GET
	@RequestMapping(value="/deletarMovimentacao/{id}",method = RequestMethod.GET)
	public String deletarMovimentacao(@PathVariable("id") long id) {
		System.out.println("DELETE");
		
		mr.delete(mr.getMovimentacaoByID(id));
		
		return "redirect:/";
	}
}
