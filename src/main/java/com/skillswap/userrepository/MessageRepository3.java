package com.skillswap.userrepository;

import com.skillswap.userentity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository3 extends JpaRepository<Message, Long> {

    @Query("""
select m from Message m
where (m.senderId= :curuserid AND m.receiverId= :userid) OR
(m.senderId= :userid AND m.receiverId= :curuserid)
""")
    public List<Message> getAllMessages(@Param("userid") String userid, @Param("curuserid") String curuserid);



}
