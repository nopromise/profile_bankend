package com.example.profile_bankend.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserProfileMapper {
    @Insert("""
            INSERT INTO users (id, username, gender, email)
            VALUES (#{id}, #{username}, #{gender}, #{email})
            """)
    int insert(UserProfile userProfile);

    @Update("""
            UPDATE users
            SET username = #{username},
                gender = #{gender},
                email = #{email}
            WHERE id = #{id}
            """)
    int updateById(UserProfile userProfile);

    @Select("""
            SELECT id, username, gender, email
            FROM users
            WHERE id = #{id}
            """)
    UserProfile findById(Long id);
}
