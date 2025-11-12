package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
            cliente.setTelefone(rs.getString("telefone"));
            cliente.setRua_Cliente(rs.getString("rua"));
            cliente.setNumeroCasa_Cliente(rs.getString("numero"));
            cliente.setCidade_Cliente(rs.getString("cidade"));
            cliente.setBairro_Cliente(rs.getString("bairro"));
            cliente.setCep_Cliente(rs.getString("cep"));

            // 5. CORRIGIDO: Retorna o objeto 'cliente'
            return cliente;
        }
    }

    public List<Cliente> findAll() {
        // SQL explícito para a tabela 'Cliente'
        String sql = "SELECT * FROM Cliente";

        return jdbcTemplate.query(sql, new ClienteRowMapper());
    }

}