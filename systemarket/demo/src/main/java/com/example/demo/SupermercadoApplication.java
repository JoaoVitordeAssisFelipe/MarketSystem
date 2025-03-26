package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@SpringBootApplication
public class SupermercadoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SupermercadoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(ProdutoRepository repository) {
		return args -> {
			repository.save(new Produto("Bolacha ", 2.50, 35));
			repository.save(new Produto("Macarrão", 4.0, 23));
		};
	}
}

@Entity
class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private double preco;
	private int quantidade;

	public Produto() {}

	public Produto(String nome, double preco, int quantidade) {
		this.nome = nome;
		this.preco = preco;
		this.quantidade = quantidade;
	}

	// Getters e Setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public double getPreco() { return preco; }
	public void setPreco(double preco) { this.preco = preco; }
	public int getQuantidade() { return quantidade; }
	public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}

@Repository
interface ProdutoRepository extends JpaRepository<Produto, Long> {}

@RestController
@RequestMapping("/produtos")
class ProdutoController {
	private final ProdutoRepository repository;

	public ProdutoController(ProdutoRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Produto> listarTodos() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Produto buscarPorId(@PathVariable Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
	}

	@PostMapping
	public Produto criarProduto(@RequestBody Produto produto) {
		return repository.save(produto);
	}

	@PutMapping("/{id}")
	public Produto atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
		Produto produto = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
		produto.setNome(produtoAtualizado.getNome());
		produto.setPreco(produtoAtualizado.getPreco());
		produto.setQuantidade(produtoAtualizado.getQuantidade());
		return repository.save(produto);
	}

	@DeleteMapping("/{id}")
	public void deletarProduto(@PathVariable Long id) {
		repository.deleteById(id);
	}
}

