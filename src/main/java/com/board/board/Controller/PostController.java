package com.board.board.Controller;


import com.board.board.Dto.RequestDto;
import com.board.board.Dto.ResponseDto;
import com.board.board.Entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

//클라이언트가 > contol > service > re > DB > re > ser

@RestController
@RequestMapping("/api")
@Slf4j
public class PostController {

    private final JdbcTemplate jdbcTemplate;
    public PostController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/posts")
    public ResponseDto createBoard(@RequestBody RequestDto requestDto) {
        // RequestDto -> Entity
        Post post = new Post(requestDto);

        log.info("post: {}", post);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO postsmanager (title, name, PW, contents) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, post.getTitle());
                    preparedStatement.setString(2, post.getName());
                    preparedStatement.setString(3, post.getPW());
                    preparedStatement.setString(4, post.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        if (Objects.nonNull(keyHolder.getKey())) {
            Long id = keyHolder.getKey().longValue();
            post.setId(id);
        }

        // Entity -> ResponseDto
        return new ResponseDto(post);
    }


    //- 제목, 작성자명, 작성 내용, 작성 날짜를 조회하기
    //- 작성 날짜 기준 내림차순으로 정렬하기
    @GetMapping("/posts")
    public List<ResponseDto> getPosts() {
        // DB 조회
        String sql = "SELECT * FROM postsmanager";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
            Long id = rs.getLong("id");
            String title = rs.getString("title");
            String name = rs.getString("name");
            String contents = rs.getString("contents");
            return new ResponseDto(id, title, name, contents);
        });
    }

    @PutMapping("/posts/{id}")
    public Long updatePost(@PathVariable(value = "id") Long id, @RequestBody RequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findById(id);

//        if(post != null) {
            // post 내용 수정
            String sql = "UPDATE postsmanager SET title = ?, contents = ? WHERE id = ?";

            // 반영된 row 갯수
            int result = jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getContents(), id);
            if (result == 0) {
                throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
            }
            return id;
//            return id;
//        } else {
//            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
//        }
    }

    @DeleteMapping("/posts/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findById(id);
        if(post != null) {
            String sql = "DELETE FROM postsmanager WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Post findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM postsmanager WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Post post = new Post();
                post.setName(resultSet.getString("username"));
                post.setContents(resultSet.getString("contents"));
                return post;
            } else {
                return null;
            }
        }, id);
    }
}
