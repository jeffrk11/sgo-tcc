package com.jeff.controllers;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jeff.model.Funcionario;
import com.jeff.model.Movimentacao;
import com.jeff.model.Obra;
import com.jeff.repository.FuncionarioRepository;
import com.jeff.repository.ObraRepository;

@Validated
@Controller
@RequestMapping("/funcionarios")
public class ResumoFuncController {
	@Autowired
	FuncionarioRepository fr;
	@Autowired
	ObraRepository or;
	
	@RequestMapping(value="/resumofunc/{id}", method= RequestMethod.GET)
	public ModelAndView resumofunc(@PathVariable("id") long id) {
		Funcionario func = fr.getByID(id); //metodo gerado pelo spring
		ModelAndView mv = new ModelAndView("resumoFunc/resumoFunc");
		
		mv.addObject("movisfunc",func.getMovimentacao());
		System.out.println("TAMANHO MOVIMENTACOES "+func.getMovimentacao().size());
		
		mv.addObject("img_perfil", byteToInt(func.getImg()));
		mv.addObject("func", func);//aqui o mv ja tem acesso a instancia 
		List<Obra> obras_trabalhadas = or.getAllByCds( or.getObrasByID( func.getId()).toArray(new String[or.getObrasByID( func.getId()).size()]));
		mv.addObject("obras_trabalhadas",obras_trabalhadas);
		
		return mv;
	}
	
	@RequestMapping(value="/resumofuncPost", method= RequestMethod.POST)
	public String resumoFuncPost(@RequestParam(value="img[]") int[] a) {
		System.out.println("entrou");
		System.out.println(a.length);
		for(int i : a) {
			System.out.print(i+", ");
		}
		
		return "redirect:/obras";
	}
	
	private short[]  byteToInt(byte[] array) {
		short[] aux = new short[array.length];
		for(int i = 0; i < array.length; i++) {
			aux[i] = (short) (array[i]+128);
		}
		return aux;
	}
}
