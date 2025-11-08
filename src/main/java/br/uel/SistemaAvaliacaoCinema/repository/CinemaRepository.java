package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Cinema;
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
public class CinemaRepository {

    // Injeta o JdbcTemplate
    @Autowired
    private JdbcTemplate jdbcTemplate;


    // Classe interna privada que implementa a interface RowMapper.

    private class CinemaRowMapper implements RowMapper<Cinema> {
        @Override
        public Cinema mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cinema cinema = new Cinema();
            cinema.setIdCinema(rs.getLong("id_cinema"));
            cinema.setNome(rs.getString("nome"));
            cinema.setLocalizacao(rs.getString("localizacao"));
            cinema.setTipoEstabelecimento(rs.getString("tipo_estabelecimento"));
            cinema.setNumeroSalas(rs.getInt("numero_salas"));
            return cinema;
        }
    }

     //busca todos os cinemas na tabela.
     //retonrna Uma lista de objetos Cinema

    public List<Cinema> findAll() {
        String sql = "SELECT * FROM Cinema";
        // jdbcTemplate.query executa o SQL
        return jdbcTemplate.query(sql, new CinemaRowMapper());
    }

    //Busca um cinema específico pelo seu ID.

    public Optional<Cinema> findById(Long id) {
        String sql = "SELECT * FROM Cinema WHERE id_cinema = ?";
        try {
            Cinema cinema = jdbcTemplate.queryForObject(sql, new Object[]{id}, new CinemaRowMapper());
            return Optional.of(cinema);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    //Novo cinema no banco de dados.

    public Cinema save(Cinema cinema) {
        // SQL explícito para inserção
        String sql = "INSERT INTO Cinema (nome, localizacao, tipo_estabelecimento, numero_salas) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder(); // Objeto para guardar a chave retornada

        jdbcTemplate.update(connection -> {
            // Precisamos especificar qual coluna contém a chave gerada (SERIAL)
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_cinema"});
            ps.setString(1, cinema.getNome());
            ps.setString(2, cinema.getLocalizacao());
            ps.setString(3, cinema.getTipoEstabelecimento());
            ps.setInt(4, cinema.getNumeroSalas());
            return ps;
        }, keyHolder);

        // Atualiza cinema pelo ID que o banco de dados gerou
        if (keyHolder.getKey() != null) {
            cinema.setIdCinema(keyHolder.getKey().longValue());
        }
        return cinema;
    }

   // Atualiza um cinema existente no banco de dados.
   // Assume que o objeto 'cinema' já tem um ID válido

    public int update(Cinema cinema) {
        // SQL explícito para atualização
        String sql = "UPDATE Cinema SET nome = ?, localizacao = ?, " +
                "tipo_estabelecimento = ?, numero_salas = ? " +
                "WHERE id_cinema = ?";

        // jdbcTemplate.update executa o SQL e retorna o número de linhas afetadas
        return jdbcTemplate.update(sql,
                cinema.getNome(),
                cinema.getLocalizacao(),
                cinema.getTipoEstabelecimento(),
                cinema.getNumeroSalas(),
                cinema.getIdCinema());
    }

    //Deleta um cinema do banco de dados pelo seu ID.

    public int deleteById(Long id) {
        // SQL explícito para deleção
        String sql = "DELETE FROM Cinema WHERE id_cinema = ?";

        return jdbcTemplate.update(sql, id);
    }
}