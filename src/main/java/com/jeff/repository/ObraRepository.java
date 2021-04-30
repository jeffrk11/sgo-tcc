package com.jeff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jeff.model.Funcionario;
import com.jeff.model.Obra;

public interface ObraRepository extends JpaRepository<Obra, String> {
	Obra findByCd(long cd);
	
	//@Query(value="INSERT INTO tb_obra (nm_descricao) values (:text)",nativeQuery = true)
	//void insertObra(@Param("text")String descricao);
		 
	@Query(value = "SELECT * FROM tb_obra ORDER BY cd ASC", nativeQuery = true)
	List<Obra> getAllObras();
	
	//retorna obras com os codigos passados
	@Query(value = "SELECT * FROM tb_obra WHERE cd in :cds", nativeQuery = true)
	List<Obra> getAllByCds(@Param("cds") String[] codigos);
	
	@Query(value="SELECT funcionario_id FROM tb_obras_funcionarios where  obra_cd = :cd",nativeQuery = true)
	List<String> getEquipeByCD(@Param("cd") long cd_obra);
	
	@Query(value = "SELECT obra_cd FROM tb_obras_funcionarios where  funcionario_cd = :cd", nativeQuery = true)
	List<String> getObrasByCD(@Param("cd") long cd);
	
	@Query(value = "SELECT obra_cd FROM tb_obras_funcionarios where  funcionario_id = :id", nativeQuery = true)
	List<String> getObrasByID(@Param("id") long id);
	//retorna apenas os controles das obras
	@Query(value= "SELECT nm_controle FROM tb_obra",nativeQuery = true)
	List<String> getControlesObras();
	//dashboard info --------------------------------------------------
	@Query(value="SELECT count(*) FROM tb_obra where ds_situacao = :situacao",nativeQuery = true)
	int getQtdObraSituacao(@Param("situacao") String situacao);
	//retorna agrupado a contagem de situacao
	@Query(value = "SELECT ds_situacao, count(*) FROM tb_obra group by ds_situacao", nativeQuery = true)
	List<Object[]> getQtdObraGroupBySituacao ();
	
}
