package com.ecommerce.produtos.repository;

import com.ecommerce.produtos.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveSalvarEBuscarProduto() {
        Produto produto = new Produto();
        produto.setNome("Notebook Dell");
        produto.setDescricao("Notebook Dell Inspiron i7");
        produto.setPreco(new BigDecimal("4500.00"));
        produto.setEstoque(10);
        produto.setCategoria("Eletrônicos");
        produto.setImagemUrl("https://example.com/notebook.jpg");

        Produto salvo = produtoRepository.save(produto);

        assertNotNull(salvo.getId());
        assertEquals("Notebook Dell", salvo.getNome());
        assertEquals(new BigDecimal("4500.00"), salvo.getPreco());
    }

    @Test
    void deveBuscarPorCategoria() {
        Produto produto = new Produto();
        produto.setNome("Notebook Dell");
        produto.setPreco(new BigDecimal("4500.00"));
        produto.setEstoque(10);
        produto.setCategoria("Eletrônicos");
        produtoRepository.save(produto);

        List<Produto> produtos = produtoRepository.findByCategoria("Eletrônicos");

        assertFalse(produtos.isEmpty());
        assertEquals("Eletrônicos", produtos.get(0).getCategoria());
    }

    @Test
    void deveBuscarPorNomeIgnorandoCase() {
        Produto produto = new Produto();
        produto.setNome("Notebook Dell");
        produto.setPreco(new BigDecimal("4500.00"));
        produto.setEstoque(10);
        produto.setCategoria("Eletrônicos");
        produtoRepository.save(produto);

        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase("notebook");

        assertFalse(produtos.isEmpty());
        assertEquals("Notebook Dell", produtos.get(0).getNome());
    }

    @Test
    void deveDeletarProduto() {
        Produto produto = new Produto();
        produto.setNome("Produto para deletar");
        produto.setPreco(new BigDecimal("100.00"));
        produto.setEstoque(5);
        Produto salvo = produtoRepository.save(produto);

        produtoRepository.deleteById(salvo.getId());

        assertTrue(produtoRepository.findById(salvo.getId()).isEmpty());
    }
}