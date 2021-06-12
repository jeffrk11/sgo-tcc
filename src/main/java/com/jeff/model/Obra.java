package com.jeff.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.jeff.enums.SituacaoObra;

@Entity
@Table(name = "tb_obra")
public class Obra implements Serializable{
	private static final  long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long cd;
	@NotEmpty
	@Column(name="nm_descricao")
	private String descricao;
	@Column(name="nm_controle", length= 20)
	private String controle;
	@Column(name="nm_estado", length= 20)
	private String estado;
	@Column(name="nm_cidade", length= 20)
	private String cidade;
	@Column(name="dt_inicio", length= 10)
	private String data_inicio;
	@Column(name="dt_fim", length= 10)
	private String datafim;
	@Column(name="ds_situacao", length= 20)
	private String situacao;
	@Column(name="nm_cliente", length = 40)
	private String cliente;
	@ManyToMany
	@JoinTable(name="tb_obras_funcionarios", 
		joinColumns=		{@JoinColumn(name="obra_cd")}, 
		inverseJoinColumns=	{@JoinColumn(name="funcionario_id")})
	private List<Funcionario> funcionario;
	@OneToMany(mappedBy="obra")
	private List<Movimentacao> movimentacao;
	
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public long getCd() {
		return cd;
	}
	public void setCd(long cd) {
		this.cd = cd;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getControle() {
		return controle;
	}
	public void setControle(String controle) {
		this.controle = controle;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getData_inicio() {
		return data_inicio;
	}
	public void setData_inicio(String data_inicio) {
		this.data_inicio = data_inicio;
	}
	public String getDatafim() {
		return datafim;
	}
	public void setDatafim(String data_fim) {
		this.datafim = data_fim;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public List<Funcionario> getFuncionarios() {
		return funcionario;
	}
	public void setFuncionarios(List<Funcionario> funcionario) {
		this.funcionario = funcionario;
	}
	public List<Movimentacao> getMovimentacao() {
		return movimentacao;
	}
	public void setMovimentacao(List<Movimentacao> movimentacao) {
		this.movimentacao = movimentacao;
	}
	
	
	//metodo auxliar pois vem do front uma string e preciso converter
		private Date stringToDate(String data,String format) throws ParseException {
			System.out.println(data.replace('-','/'));
			if(!data.equals("")) {
				return new SimpleDateFormat(format).parse(data.replace('-','/'));
			}else {
				return null;
			}
		}
	
	
}
