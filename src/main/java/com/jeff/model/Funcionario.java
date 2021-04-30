package com.jeff.model;

import java.sql.Blob;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="tb_funcionario")
public class Funcionario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //n posso esquecer, se n ele apenas faz update quando vo salvar
	@NotNull
	@Column(name="id_func")
	private long id;
	@Column(name="cd_func") // sempre definir nomes antes e não durante o desenvolvimento, se não o banco cria outro atributo
	private long cd;
	
	@NotEmpty //utilizado para strings, notnull para numeros
	@Column(name="nm_func",length = 80)
	private String nome;
	@Column(name="nm_cargo",length = 80)
	private String cargo;
	@Column(name= "ds_situacao",length = 20)
	private String situacao;
	@Column(name="vl_salario", columnDefinition="float default 0")
	private float salario = 0;
	@Column(name= "dt_admissao")
	@Temporal(TemporalType.DATE)
	private Date admissao;
	@Column(name= "dt_demissao")
	@Temporal(value = TemporalType.DATE)
	private Date demissao;

	@Column(name= "dt_entrada")
	@Temporal(TemporalType.TIME)
	private Date entrada;
	@Column(name= "dt_saida")
	@Temporal(value = TemporalType.TIME)
	private Date saida;
	@Column(name= "vl_nota", length=1)
	private byte nota;
	
	@Column(name= "ds_obervacao")
	private String obs;
	@Column(name="vl_transporte", columnDefinition="float default 0")
	private float transporte = 0;
	@Column(name="nm_banco", length = 50)
	private String banco;
	@Column(name="st_tipo_conta", length = 10)
	private String tipo_conta;
	@Column(name="nr_agencia", length = 10)
	private String agencia;
	@Column(name="nr_conta", length = 20)
	private String conta;
	@Column(name="ds_telefone", length = 16)
	private String celular;
	@Column(name="nm_cidade", length = 50)
	private String cidade;
	@Column(name="nm_estado", length = 20)
	private String estado;
	
	@Column(name="ft_perfil", columnDefinition="BLOB")
	@Lob
	private byte[] img;
	
	//por algum motivo many to many estava dando erro e isso solucionou o problema de serialization 
	@JsonIgnore 
	@ManyToMany(mappedBy = "funcionario")
	private List<Obra> obra;
	@JsonIgnore
	@ManyToMany(mappedBy = "funcionario")
	private List<Movimentacao> movimentacao;

	public List<Obra> getObra() {
		return obra;
	}	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}


	public void setObra(List<Obra> obra) {
		this.obra = obra;
	}
	public long getCd() {
		return cd;
	}
	public void setCd(long cd) {
		this.cd = cd;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public float getSalario() {
		return salario;
	}
	public void setSalario(float salario) {
		this.salario = salario;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public Date getAdmissao() {
		return admissao;
	}
	public void setAdmissao(String admissao) throws ParseException {
		this.admissao = stringToDate(admissao,"yyyy/MM/dd");		
	}
	public Date getDemissao() {
		return demissao;
	}
	public void setDemissao(String demissao) throws ParseException {
		this.demissao = stringToDate(demissao,"yyyy/MM/dd");	
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public byte[] getImg() {
		return img;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	public float getTransporte() {
		return transporte;
	}
	public void setTransporte(float transporte) {
		this.transporte = transporte;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getTipo_conta() {
		return tipo_conta;
	}
	public void setTipo_conta(String tipo_conta) {
		this.tipo_conta = tipo_conta;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	
	public Date getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) throws ParseException {
		this.entrada = stringToDate(entrada, "HH:mm");
	}
	public Date getSaida() {
		return saida;
	}
	public void setSaida(String saida) throws ParseException {
		this.saida = stringToDate(saida, "HH:mm");
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	public byte getNota() {
		return nota;
	}
	public void setNota(byte nota) {
		this.nota = nota;
	}
	
	
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
