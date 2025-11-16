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
            cliente.setIdcliente(rs.getLong("id_cliente"));
            cliente.setNomeCliente(rs.getString("nome"));
            cliente.setCpfCliente(rs.getString("cpf"));
            cliente.setEmailCliente(rs.getString("email"));

            // Converte java.sql.Date para java.time.LocalDate
            Date dataNasc = rs.getDate("data_nascimento");
            if (dataNasc != null) {
                cliente.setDataNascimentoCliente(dataNasc.toLocalDate());
            }

            cliente.setTelefone(rs.getString("telefone"));
            cliente.setRuaCliente(rs.getString("rua"));
            cliente.setNumeroCasaCliente(rs.getString("numero"));
            cliente.setBairroCliente(rs.getString("bairro"));
            cliente.setCidadeCliente(rs.getString("cidade"));
            cliente.setCepCliente(rs.getString("cep"));
            cliente.setEstadoCliente(rs.getString("estado"));
            cliente.setAtivoCliente(rs.getBoolean("ativo"));

            return cliente;
        }
    }

    //buaca todos
    public List<Cliente> listarTodos() {
        // SQL corrigido para incluir a lógica do soft-delete
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
         String sql = "SELECT * FROM Cliente WHERE nome ILIKE ? AND ativo = true"; // ILIKE para case não sensitive
         return jdbcTemplate.query(sql, new Object[]{"%" + nome + "%"}, new ClienteRowMapper());
     }


    public int update(Cliente cliente) {
        String sql = "UPDATE Cliente SET nome = ?, cpf = ?, email = ?, data_nascimento = ?, telefone = ?, " +
                "rua = ?, numero = ?, bairro = ?, cidade = ?, cep = ?, estado = ?, ativo = ? " +
                "WHERE id_cliente = ?";

        return jdbcTemplate.update(sql,
                cliente.getNomeCliente(),
                cliente.getCpfCliente(),
                cliente.getEmailCliente(),
                cliente.getDataNascimentoCliente(),
                cliente.getTelefoneCliente(),
                cliente.getRuaCliente(),
                cliente.getNumeroCasaCliente(),
                cliente.getBairroCliente(),
                cliente.getCidadeCliente(),
                cliente.getCepCliente(),
                cliente.getEstadoCliente(),
                cliente.isAtivoCliente(),
                cliente.getIdCliente()); // ID é o último
    }

    //incluir
    public Cliente incluir(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nome, cpf, email, data_nascimento, telefone, rua, numero, bairro, cidade, cep, estado, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_cliente"});
            ps.setString(1, cliente.getNomeCliente());
            ps.setString(2, cliente.getCpfCliente());
            ps.setString(3, cliente.getEmailCliente());
            ps.setObject(4, cliente.getDataNascimentoCliente());
            ps.setString(5, cliente.getTelefoneCliente());
            ps.setString(6, cliente.getRuaCliente());
            ps.setString(7, cliente.getNumeroCasaCliente());
            ps.setString(8, cliente.getBairroCliente());
            ps.setString(9, cliente.getCidadeCliente());
            ps.setString(10, cliente.getCepCliente());
            ps.setString(11, cliente.getEstadoCliente());
            ps.setBoolean(12, cliente.isAtivoCliente());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            cliente.setIdcliente(keyHolder.getKey().longValue());
        }
        return cliente;
    }


    //DELETE) Faz o "Soft Delete" e marca como inativo
    public int softDeletePorId(Long id) {
        String sql = "UPDATE Cliente SET ativo = false WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, id);
    }
}