package com.jeff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.jeff.model.Movimentacao;


public interface MovimentacaoRepository extends JpaRepository<Movimentacao, String>{
	@Query(value = "SELECT * FROM tb_movimentacao WHERE id_movimentacao = :id",nativeQuery = true)
	Movimentacao getMovimentacaoByID(@Param("id") long id);
	@Query(value = "SELECT * FROM tb_movimentacao WHERE obra_cd = :id", nativeQuery = true)
	List<Movimentacao> getMovimentacoesByObra(@Param("id") long id);
	//pega as movimentacoes q o funcionario esta vinculado
	@Query(value = "select * from tb_movimentacao JOIN tb_movimentacoes_funcionarios ON tb_movimentacao.id_movimentacao = tb_movimentacoes_funcionarios.movimentacao_cd WHERE ajv.tb_movimentacoes_funcionarios.funcionario_id = :id", nativeQuery = true)
	List<Movimentacao> getMovimentacoesByFuncionario(@Param("id") long id);
	
	@Query(value = "SELECT * FROM tb_movimentacao ORDER BY id_movimentacao", nativeQuery = true)
	List<Movimentacao> getAllMovis();
	//retorna movimentacao pela pesquisa
	@Query(value = "SELECT *  FROM tb_movimentacao WHERE"
			+ " nm_descricao like %:descricao% "
			+ "AND st_categoria like %:categoria "
			+ "AND st_tipo_movimentacao like %:tipo "
			+ "AND vl_pagamento like :valor%", nativeQuery = true)
	List<Movimentacao> getByPesquisa(@Param("descricao") String descricao,
									@Param("categoria") String categoria,
									@Param("tipo") String tipo,
									@Param("valor") String valor);
	
	@Query(value="SELECT st_situacao, count(*) FROM ajv.tb_movimentacao group by st_situacao",nativeQuery = true)
	List<Object[]> getQtdMovimentacaoSituacao();
}	
