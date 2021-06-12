package com.jeff.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.net.URLConnection;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jeff.enums.CategoriaMovimentacao;
import com.jeff.enums.SituacaoFuncionario;
import com.jeff.model.Funcionario;
import com.jeff.model.Movimentacao;
import com.jeff.model.Obra;
import com.jeff.repository.FuncionarioRepository;
import com.jeff.repository.MovimentacaoRepository;
import com.jeff.repository.ObraRepository;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioRepository fr;
	@Autowired
	private ObraRepository or;
	@Autowired
	private MovimentacaoRepository mr;
	
	@GetMapping("/lista")
	public ModelAndView listarfuncs() {
		// caminho da onde o arquivo html esta
		ModelAndView mv = new ModelAndView("funcionarios/funcionarios");
		// getAll feito por query utilizado no lugar do findAll
		List<Funcionario> funcs = fr.getAllFuncs();


		//           nome do obj,   o obj
		mv.addObject("funcionarios",funcs);
		return mv;
	}
	
	//atualiza apenas o fragmento da pagina sem precisar da reload
	@RequestMapping(value = "/pesquisar", method = RequestMethod.GET)
	public ModelAndView listarfuncsFragment(@RequestParam(value = "nome", required = false) String nome,
											@RequestParam(value = "cargo", required = false) String cargo,
											@RequestParam(value = "cd", required = false) String cd) {
		System.out.println(cd);
		if (nome.equals("TodosFunc")) {	
			return new ModelAndView("funcionarios/funcionarios :: resultsList").addObject("funcionarios",fr.getAllFuncs());
		} 
		else {
			//List<Funcionario> funcs = fr.getByNome(nome);
			List<Funcionario> funcs = fr.getByNomeAndCargoAndCD(nome,cargo,cd);

			if (funcs.size() == 0) {
				System.out.println("Nenhum funcionário encontrado");
				return new ModelAndView("funcionarios/funcionarios :: resultsList").addObject("funcionarios", new ArrayList<Funcionario>());
			} else {
				return new ModelAndView("funcionarios/funcionarios :: resultsList").addObject("funcionarios", funcs);
			}
		}
	}
	
	@RequestMapping(value="/cadastrarFunc", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ModelAndView cadastrarFunc() {		
		
		ModelAndView mv = new ModelAndView("funcionarios/cadastrarFunc");
		mv.addObject("situacao",SituacaoFuncionario.values());
		//sugere ao usuario o codigo do funcionario
		Long max = fr.getMaxCD();
		mv.addObject("cdMax",max == null ? 1: max+1);
		//todas as obras para selecionar qual adicionar
		List<Obra> obras =  or.getAllObras();
		mv.addObject("obras",obras);
		
		List<String> cds_funcs = fr.getCdsFunc();
		mv.addObject("cds_funcs",cds_funcs);
		
		//mandando para o front um funcionario vazio para que ele saiba quais os atributos pegar para o json automatico
		mv.addObject("func",new Funcionario());
		return mv; 
	}
	
	//cadastrar no banco de dados quando clicar no cadastrar da pagina 
	@RequestMapping(value="/cadastrarFunc", method= RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String cadastrarFuncPost(@RequestParam(value="myArray[]") String[] a,@RequestParam(required=false,value="perfil[]") byte[] p, @Valid Funcionario func) {
		if(p != null) {
			func.setImg(p);
		}		
		System.out.println("CADASTRO FUNCIONARIO");
		//salvar funcionario primeiro para que o banco veja q ele existe
		fr.save(func);
		// depois colocar o funcionario na obra
		List<Obra> obras = null;
		if(!a[0].equals("vazio")) {
			// todas as obras nas quais foram escolhidas no front
			obras = or.getAllByCds(a);
			for (Obra o : obras) {
				o.getFuncionarios().add(func);
			}
			// setar obras pelo obra repository pois a entidade obra é a dominante
			or.saveAll(obras);
		}
		return "redirect:/funcionarios/lista";
	}
	
	@RequestMapping(value = "/detalhe/{id}", method = RequestMethod.GET)
	public ModelAndView detalhesFunc(@PathVariable("id") long id) {
		Funcionario func = fr.getByID(id); //metodo gerado pelo spring
		ModelAndView mv = new ModelAndView("funcionarios/detalhesFunc");
		mv.addObject("situacao",SituacaoFuncionario.values());
		//System.out.println(new SimpleDateFormat("HH:mm").format(new Date()));
		
		
		mv.addObject("img_perfil", byteToInt(func.getImg()));
		mv.addObject("func", func);//aqui o mv ja tem acesso a instancia 
		
		//todas as obras para selecionar qual adicionar
		List<Obra> obras =  or.getAllObras();
		mv.addObject("obras",obras);
		
		List<String> cds_funcs = fr.getCdsFunc();
		mv.addObject("cds_funcs",cds_funcs);
		
		for( String s : or.getObrasByID( func.getId())) {
			System.out.println("obra trabalhada "+s);
		}
		
		List<Obra> obras_trabalhadas = or.getAllByCds( or.getObrasByID( func.getId()).toArray(new String[or.getObrasByID( func.getId()).size()]));
		mv.addObject("obras_trabalhadas",obras_trabalhadas);
		List<String> cds = or.getObrasByID( func.getId());
		mv.addObject("cds",cds);
		
		return mv;
	}
	
	private short[]  byteToInt(byte[] array) {
		short[] aux = new short[array.length];
		for(int i = 0; i < array.length; i++) {
			aux[i] = (short) (array[i]+128);
		}
		return aux;
	}
	  
	@RequestMapping(value="/saveFunc", method=RequestMethod.PUT)
	public String saveDetalhesFuncionario(@RequestParam(value="myArray[]") String[] a,@RequestParam(required=false,value="perfil[]") byte[] img, @Valid Funcionario func) {
		System.out.println("entrada "+func.getEntrada());
		if(img != null) {
			func.setImg(img);
		}

		if (!a[0].equals("vazio")) {
			
			// todas as obras nas quais foram escolhidas no front
			List<Obra> obras = or.getAllByCds(a);
			//pecorre todas as obras nas quaios foram escolhidas no front
			for (int i = 0; i < obras.size(); i++) {
				//defindo variavel q vai auxiliar na hora de verificar se o funcionario esta na obra
				boolean exist = false;
				//pecorrer a lista de funcionarios da obra pecorrida
				for (int j = 0; j < obras.get(i).getFuncionarios().size(); j++) {
					//se o funcionario tiver nessa obra, exist true e segue para o proximo
					if (func.getId() == obras.get(i).getFuncionarios().get(j).getId()) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					//se nao existir add func a obra
					obras.get(i).getFuncionarios().add(func);
				}
			}
			// setar obras pela entidade obra pois ela é a dominante
			or.saveAll(obras);
		}else {			
			//se lista de codigos de obras vazio remover funcionario de todas as obras
			or.saveAll(removerFuncFromTodasObras(func.getCd()));
		}
		//salva o funcionarios com seus dados alterados
		fr.save(func);
		return "redirect:/";
	}
	
	private List<Obra> removerFuncFromTodasObras(long id){
		//remover ja que não foi escolhido em nenhuma obra
		//pegar todas as obras nas quais esse func aparece no banco
		List<String> todasObrasFunc = or.getObrasByID(id);
		List<Obra> obras =  new ArrayList<>();
		//pecorrer todas as obras
		for(Obra o : or.getAllByCds(todasObrasFunc.toArray(new String[todasObrasFunc.size()]))) {
			//pecorrer cada funcionario na equipe da obra
			for(int i = 0; i < o.getFuncionarios().size(); i++) {
				//se funcionario em questão, tiver na equipe, é removido dessa obra
				if(id == o.getFuncionarios().get(i).getId()) {
					o.getFuncionarios().remove(i);
					System.out.println("removido da obra "+o.getCd());
				}
			}
			//adiciona a obras para posteriormente salvar os dados corretos
			obras.add(o);
		}
		
		return obras;
	}
	
	private List<Movimentacao> removerFuncFromTodasMovimentacoes(long id){
		List<Movimentacao> movis = mr.getMovimentacoesByFuncionario(id);
		for(Movimentacao m : movis) {
			for(Funcionario f : m.getFuncionario()) {
				if(f.getId() == id) {
					System.out.println("vai ser excluido da movimentacao");
					System.out.println(f.getNome());
				 	m.getFuncionario().remove(m.getFuncionario().indexOf(f));
				  break;
				}
			}
		}
		return movis;
	}
	
	@RequestMapping("/deletarFunc/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String deletarFuncionario(@PathVariable("id") long id) {
		//metodo retornando obras nas quais ele aparece ja removendo ele da equipe
		List<Obra> obras = removerFuncFromTodasObras(id);
		//salvar obras sem o funcionario
		or.saveAll(obras);
		//metodo retornando movimentacoes nas quais ele aparece ja removendo ele
		List<Movimentacao> movis = removerFuncFromTodasMovimentacoes(id);
		//salva
		mr.saveAll(movis);
		//deletar funcionario do banco
		fr.delete(fr.getByID(id));
		return "redirect:/funcionarios/lista";
	}
	
}
