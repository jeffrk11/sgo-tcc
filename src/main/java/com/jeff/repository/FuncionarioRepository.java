package com.jeff.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jeff.model.Funcionario;
import com.jeff.model.Obra;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String>{
	Funcionario findByCd(long cd);
	Iterable<Funcionario> findByObra(Obra obra);//spring faz o metodo pra vc
	
	@Query(value = "SELECT * FROM tb_funcionario ORDER BY cd_func", nativeQuery = true)
	List<Funcionario> getAllFuncs();
	
	@Query(value = "SELECT * FROM tb_funcionario WHERE id_func = :id", nativeQuery = true)
	Funcionario getByID(@Param("id")long id);
	
	@Query(value = "SELECT * FROM tb_funcionario WHERE cd_func = :cd", nativeQuery = true)
	Funcionario getByCD(@Param("cd")long cd);

	
	//retorna funcionarios pelo cargo pesquisado
	@Query(value = "SELECT *  FROM tb_funcionario WHERE nm_cargo = :Cargo", nativeQuery = true)
	List<Funcionario> getByCargo(@Param("Cargo") String cargo);
	
	//retorna funcionarios com o nome pesquisado
	@Query(value = "SELECT *  FROM tb_funcionario WHERE nm_func like %:Nome%", nativeQuery = true)
	List<Funcionario> getByNome(@Param("Nome") String nome);
	
	//retorna funcionarios com o nome pesquisado
	@Query(value = "SELECT *  FROM tb_funcionario WHERE nm_func like %:Nome% AND nm_cargo like %:Cargo% AND cd_func like %:Cd", nativeQuery = true)
	List<Funcionario> getByNomeAndCargoAndCD(@Param("Nome") String nome,@Param("Cargo") String cargo,@Param("Cd") String cd);
	
	//retorna funcionarios com os codigos passados
	@Query(value = "SELECT * FROM tb_funcionario WHERE cd_func in :cds", nativeQuery = true)
	List<Funcionario> getAllByCds(@Param("cds") String[] codigos);
	//retorna funcionarios com os ids passados
	@Query(value = "SELECT * FROM tb_funcionario WHERE id_func in :ids", nativeQuery = true)
	List<Funcionario> getAllByIds(@Param("ids") String[] ids);
	
	//retorn quantidade de funcionarios na tabela
	@Query(value= "SELECT count(id_func) FROM tb_funcionario",nativeQuery = true)
	Long getQtdFunc();
	//retorna valor maximo do codigo do funcionario
	@Query(value= "SELECT MAX(cd_func) FROM tb_funcionario",nativeQuery = true)
	Long getMaxCD();
	//retorna apenas os codigos dos funcionarios
	@Query(value= "SELECT cd_func FROM tb_funcionario",nativeQuery = true)
	List<String> getCdsFunc();
	
	//retorna funcionarios da obra pelo cd da obra
	//@Query(value = "SELECT obra_cd  FROM tb_obras_funcionarios WHERE funcionario_cd = :cd")
	//List<String> getObrasByCD(@Param("cd") String situacao);
	
	//dashboard info ------------------------------------------------------------------------------------
	//retorna contagem de situacao
	@Query(value="SELECT count(*) FROM tb_funcionario where ds_situacao = :situacao",nativeQuery = true)
	int getQtdFuncSituacao(@Param("situacao") String situacao);
	//retorna agrupado a contagem de situacao
	@Query(value = "SELECT ds_situacao, count(*) FROM tb_funcionario group by ds_situacao", nativeQuery = true)
	List<Object[]> getQtdFuncGroupBySituacao ();
	//retorna soma salarios de funcionarios
	@Query(value="SELECT SUM(vl_salario) from tb_funcionario where ds_situacao = :situacao",nativeQuery = true)
	float getSalarioSituacao(@Param("situacao") String situacao);
	
	//@Query(value = "SELECT funcionarios_cd_func FROM tb_obra_funcionarios where obra_cd = :cd", nativeQuery =true)
	//List<String> getByObra(@Param("cd")Long cd_obra);
	//@Query(value = "SELECT funcionario_cd_func FROM tb_funcionario_obra where obra_cd = :cd", nativeQuery =true)
	//List<String> getByObraFunc(@Param("cd")Long cd_obra);
	
	//retorna cds obras trabalahdas pelo func
	//@Query(value="SELECT obra_cd FROM ajv.tb_obra_funcionarios where funcionarios_cd_func = :cd",nativeQuery = true)
	//List<String> getObrasByFuncCD(@Param("cd") long cd_func);
	//@Query(value="SELECT obra_cd FROM ajv.tb_funcionario_obra where funcionario_cd_func = :cd",nativeQuery = true)
	//List<String> getObrasByFuncCDObra(@Param("cd") long cd_func);
	
	//apagar funcionario da tb_obra_funcionario para evitar erro
	
	//@Query(value="DELETE FROM tb_obra_funcionarios WHERE obra_cd in :cd_obra AND funcionarios_cd_func = :cd_func",nativeQuery = true)
	//public void deleteFuncFromObra(@Param("cd_obra") String[] cd_obra, @Param("cd_func") long cd_func);
	
	
	
	
}
