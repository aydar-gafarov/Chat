package edu.school21.sockets.services;

import edu.school21.sockets.chatrooms.Chatroom;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChatRoomServiceImpl implements ChatRoomService<Chatroom> {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ChatRoomServiceImpl(DataSource namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(namedParameterJdbcTemplate);
    }

    @Override
    public Chatroom findById(Long id) {
        String sql = "select * from day09.chatrooms where id = :id";
        List<Chatroom> list = namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("id", id),new BeanPropertyRowMapper<>(Chatroom.class));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Chatroom> findAll() {
        String sql = "select * from day09.chatrooms";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Chatroom.class));
    }

    @Override
    public Chatroom save(Chatroom entity){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", entity.getName());
        String strQuery =
                "insert into day09.chatrooms(name) values (:name) returning id;";
//        Long id = namedParameterJdbcTemplate.query(strQuery, map, (PreparedStatementCallback) ps -> ps.executeUpdate());
        List<Chatroom> list = namedParameterJdbcTemplate.query(strQuery, new MapSqlParameterSource().addValues(map),new BeanPropertyRowMapper<>(Chatroom.class));
        return list.get(0);
    }
}
