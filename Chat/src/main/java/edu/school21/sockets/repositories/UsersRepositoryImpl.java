package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UsersRepositoryImpl implements UsersRepository<User>{
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    public UsersRepositoryImpl(DataSource namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(namedParameterJdbcTemplate);
    }

    @Override
    public User findById(Long id) throws SQLException {
        String sql = "select * from day09.users where id = :id";
        List<User> list = namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("id", id),new BeanPropertyRowMapper<>(User.class));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from day09.users";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User save(User entity){
        String email = entity.getEmail();
        String password = entity.getPassword();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email);
        map.put("password", password);
        String strQuery =
                "insert into day09.users(email, password) values (:email, :password) returning id;";
//        Long id = namedParameterJdbcTemplate.query(strQuery, map, (PreparedStatementCallback) ps -> ps.executeUpdate());
        List<User> list = namedParameterJdbcTemplate.query(strQuery, new MapSqlParameterSource().addValues(map),new BeanPropertyRowMapper<>(User.class));
        return list.get(0);
    }
    @Override
    public void update(User entity) throws NoSuchFieldException, IllegalAccessException {
        String sqlQuery = "update day09.users set email = :email where id = :id;";
        Long id = entity.getId();
        String email = entity.getEmail();
        namedParameterJdbcTemplate.update(sqlQuery, new MapSqlParameterSource().addValue("email", email).addValue("id", id));
    }

    @Override
    public void delete(Long id) {
        String strQuery =
                "delete from day09.users where id = :id";
        if (namedParameterJdbcTemplate.update(strQuery, new MapSqlParameterSource("id", id)) == 0)
        {
            System.err.println("Not found user");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "select * from day09.users where email = :email";
        List<User> userList = namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource().addValue("email", email),new BeanPropertyRowMapper<>(User.class));
        return userList.stream().findAny();
    }

    @Override
    public void resetSequence() {
        String sql = "alter sequence day09.users_id_seq restart with 1;";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
