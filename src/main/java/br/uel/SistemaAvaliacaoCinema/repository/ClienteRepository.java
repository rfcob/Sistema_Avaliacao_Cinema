package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository {

    // Injeta o JdbcTemplate
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class ClienteRowMapper implements RowMapper<Cliente> {
        @Override
        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cliente cliente = new Cliente();

            cliente.setId_cliente(rs.getLong("id_cliente"));
            cliente.setNome(rs.getString("nome"));
            cliente.setCpf_Cliente(rs.getString("cpf"));
            cliente.setEmail_Cliente(rs.getString("email"));
            cliente.setTelefone(rs.getString("telefone"));
            cliente.setRua_Cliente(rs.getString("rua"));
            cliente.setNumeroCasa_Cliente(rs.getString("numero"));
            cliente.setCidade_Cliente(rs.getString("cidade"));
            cliente.setBairro_Cliente(rs.getString("bairro"));
            cliente.setCep_Cliente(rs.getString("cep"));

            //Retorna o objeto 'cliente'
            return cliente;
        }
    }

    public List<Cliente> findAll() {
        // SQL explícito para a tabela 'Cliente'
        String sql = "SELECT * FROM Cliente";

        return jdbcTemplate.query(sql, new ClienteRowMapper());
    }

    //Busca um cliente específico pelo seu ID.
    public Optional<Cliente> findById(Long id) {
        String sql = "SELECT * FROM Cliente WHERE id_cliente = ?";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new Object[]{id}, new ClienteRepository.ClienteRowMapper());
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    //buscar por nome
    public Optional<Cliente> findByNome(String nome) {
        String sql = "SELECT * FROM Cliente WHERE nome = ?";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new Object[]{nome}, new ClienteRepository.ClienteRowMapper());
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    //Novo cliente
    public Cliente save(Cliente cliente) {
        // SQL explícito para inserção
        String sql = "INSERT INTO Cliente(nome, cpf, email, telefone, rua, numero, cidade, bairro, cep, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder(); // Objeto para guardar a chave retornada

        jdbcTemplate.update(connection -> {
            // Precisamos especificar qual coluna contém a chave gerada (SERIAL)
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_cliente"});
            ps.setString(1, cliente.getNome_Cliente());
            ps.setString(2, cliente.getCpf_Cliente());
            ps.setString(3, cliente.getEmail_Cliente());
            ps.setString(4, cliente.getTelefone_Cliente());
            ps.setString(5, cliente.getRua_Cliente());
            ps.setString(6, cliente.getNumeroCasa_Cliente());
            ps.setString(7, cliente.getCidade_Cliente());
            ps.setString(8, cliente.getBairro_Cliente());
            ps.setString(9, cliente.getCep_Cliente());
            ps.setString(10, cliente.getEstado_Cliente());
            return ps;
        }, keyHolder);

        // Atualiza cliente pelo ID que o banco de dados gerou
        if (keyHolder.getKey() != null) {
            cliente.setId_cliente(keyHolder.getKey().longValue());
        }
        return cliente;
    }

    // Atualiza um cliente existente no banco de dados.
    public int update(Cliente cliente) {
        String sql = "UPDATE Cliente SET nome = ?, cpf = ?, " +
                "email = ?" +
                "WHERE id_cliente = ?";

        // jdbcTemplate.update
        return jdbcTemplate.update(sql,
                cliente.getNome_Cliente(),
                cliente.getCpf_Cliente(),
                cliente.getEmail_Cliente(),
                cliente.getId_cliente());
    }

    //Deleta um cliente
    public int deleteById(Long id) {
         String sql = "DELETE FROM Cliente WHERE id_cliente = ?";
         return jdbcTemplate.update(sql, id);
    }

}