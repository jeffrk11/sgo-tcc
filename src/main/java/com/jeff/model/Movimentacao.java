package com.jeff.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeff.enums.CategoriaMovimentacao;
import com.jeff.enums.SituacaoMovimentacao;
import com.jeff.enums.TipoMovimentacao;

@Entity
@Table(name="tb_movimentacao")
public class Movimentacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //n posso esquecer, se n ele apenas faz update quando vo salvar
	@NotNull
	@Column(name="id_movimentacao")
	private long id;
	
	@NotEmpty //utilizado para strings, notnull para numeros
	@Column(name="nm_descricao",length = 100)
	private String descricao;
	
	@Column(name= "dt_vencimento")
	@Temporal(TemporalType.DATE)
	private Date vencimento;
	
	@Column(name= "dt_pagamento")
	@Temporal(TemporalType.DATE)
	private Date data_pagamento;
	
	@Column(name= "vl_pagamento")
	private Float pagamento;
	
	@Column(name="st_tipo_movimentacao",length = 10)
	@Enumerated(EnumType.STRING)
	private TipoMovimentacao tipo;
	
	@Column(name="st_situacao",length = 10)
	@Enumerated(EnumType.STRING)
	private SituacaoMovimentacao situacao;

	@Column(name="st_categoria",length = 20)
	@Enumerated(EnumType.STRING)
	private CategoriaMovimentacao categoria;
	
	@Column(name="ds_observacao",length = 255)
	private String obs;
	
	@Column(name="nr_documento",length = 100)
	private String documento;
	
	@Column(name="dt_referencia")
	@Temporal(TemporalType.DATE)
	private Date referencia;
	
	
	@ManyToMany
	@JoinTable(name="tb_movimentacoes_funcionarios", 
		joinColumns=		{@JoinColumn(name="movimentacao_cd")}, 
		inverseJoinColumns=	{@JoinColumn(name="funcionario_id")})
	private List<Funcionario> funcionario;
	
	@JsonIgnore
	@ManyToOne
	private Obra obra;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(String vencimento) throws ParseException {
		this.vencimento = stringToDate(vencimento,"yyyy/MM/dd");	
	}

	public Date getData_pagamento() {
		return data_pagamento;
	}

	public void setData_pagamento(String data_pagamento) throws ParseException {
		this.data_pagamento = stringToDate(data_pagamento,"yyyy/MM/dd");	
	}

	public Float getPagamento() {
		return pagamento;
	}

	public void setPagamento(Float pagamento) {
		this.pagamento = pagamento;
	}

	public TipoMovimentacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimentacao tipo) {
		this.tipo = tipo;
	}
	public SituacaoMovimentacao getSituacao() {
		return situacao;
	}
	public void setSituacao(SituacaoMovimentacao situacao) {
		this.situacao = situacao;
	}
	public CategoriaMovimentacao getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaMovimentacao categoria) {
		this.categoria = categoria;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public Date getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) throws ParseException {
		this.referencia = stringToDate(referencia,"yyyy/MM");	
	}	
	public List<Funcionario> getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(List<Funcionario> funcionario) {
		this.funcionario = funcionario;
	}
	public Obra getObra() {
		return obra;
	}
	public void setObra(Obra obra) {
		this.obra = obra;
	}
	@Override
	public String toString() {
		return "Movimentacao [id=" + id + ", descricao=" + descricao + ", vencimento=" + vencimento
				+ ", data_pagamento=" + data_pagamento + ", pagamento=" + pagamento + ", tipo=" + tipo + ", situacao="
				+ situacao + ", categoria=" + categoria + ", obs=" + obs + ", documento=" + documento + ", referencia="
				+ referencia + ", funcionario=" + funcionario + ", obra=" + obra + "]";
	}

	//metodo auxliar pois vem do front uma string e preciso converter
	private Date stringToDate(String data, String format) throws ParseException {
		System.out.println(data.replace('-', '/'));
		if (!data.equals("")) {
			return new SimpleDateFormat(format).parse(data.replace('-', '/'));
		} else {
			return null;
		}
	}
	
}
