package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository {

    //injeção
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //rowmapper
    private class ClienteRowMapper implements RowMapper<Cliente> {
        @Override
        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(rs.getLong("id_cliente"));
            cliente.setNome(rs.getString("nome"));
            cliente.setCpf(rs.getString("cpf"));
            cliente.setEmail(rs.getString("email"));

            Date dataNasc = rs.getDate("data_nascimento");
            if (dataNasc != null) {
                cliente.setDataNascimento(dataNasc.toLocalDate());
            }

            cliente.setTelefone(rs.getString("telefone"));
            cliente.setCidade(rs.getString("cidade"));
            cliente.setEstado(rs.getString("estado"));
            cliente.setAtivo(rs.getBoolean("ativo"));

            return cliente;
        }
    }

    //buaca todos
    public List<Cliente> listarTodos() {
        String sql = "SELECT * FROM Cliente WHERE ativo = true";
        return jdbcTemplate.query(sql, new ClienteRowMapper());
    }

    //Busca por id
    public Optional<Cliente> encontrePorId(Long id) {
        String sql = "SELECT * FROM Cliente WHERE id_cliente = ? AND ativo = true";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new Object[]{id}, new ClienteRowMapper());
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    //busca por cpf
    public Optional<Cliente> encontrePorCpf(String cpf) {
        String sql = "SELECT * FROM Cliente WHERE cpf = ? AND ativo = true";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new Object[]{cpf}, new ClienteRowMapper());
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    //Busca por emaail
    public Optional<Cliente> encontrePorEmail(String email) {
        String sql = "SELECT * FROM Cliente WHERE email = ? AND ativo = true";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new Object[]{email}, new ClienteRowMapper());
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    //Pesquisa clientes ATIVOS por nome
    public List<Cliente> encontrePorNomeContendo(String nome) {
        String sql = "SELECT * FROM Cliente WHERE nome ILIKE ? AND ativo = true";
        return jdbcTemplate.query(sql, new Object[]{"%" + nome + "%"}, new ClienteRowMapper());
    }


    public int update(Cliente cliente) {
        String sql = "UPDATE Cliente SET nome = ?, cpf = ?, email = ?, data_nascimento = ?, telefone = ?, " +
                "cidade = ?, estado = ?, ativo = ? " +
                "WHERE id_cliente = ?";


        return jdbcTemplate.update(sql,
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getDataNascimento(),
                cliente.getTelefone(),
                cliente.getCidade(),
                cliente.getEstado(),
                cliente.isAtivo(),
                cliente.getIdCliente());
    }

    //incluir
    public Cliente incluir(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nome, cpf, email, data_nascimento, telefone, cidade, estado, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_cliente"});
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getEmail());
            ps.setObject(4, cliente.getDataNascimento());
            ps.setString(5, cliente.getTelefone());
            ps.setString(6, cliente.getCidade());
            ps.setString(7, cliente.getEstado());
            ps.setBoolean(8, cliente.isAtivo());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            cliente.setIdCliente(keyHolder.getKey().longValue());
        }
        return cliente;
    }


    //DELETE) Faz o "Soft Delete" e marca como inativo
    public int softDeletePorId(Long id) {
        String sql = "UPDATE Cliente SET ativo = false WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Busca inteligente: procura o termo no Nome, Email ou CPF
    public List<Cliente> buscarPorTermo(String termo) {
        String sql = "SELECT * FROM Cliente WHERE ativo = true AND (" +
                "nome ILIKE ? OR " +
                "email ILIKE ? OR " +
                "cpf ILIKE ? " +
                ") ORDER BY nome";

        String pattern = "%" + termo + "%";

        return jdbcTemplate.query(sql, new Object[]{pattern, pattern, pattern}, new ClienteRowMapper());
    }


}