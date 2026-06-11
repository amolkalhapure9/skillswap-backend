package com.skillswap.userservice;

import com.skillswap.userdto.MessageDto;
import com.skillswap.userentity.Message;
import com.skillswap.userrepository.MessageRepository3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository3 messageRepository;

    public String  saveMessage(MessageDto dto){
        Message message=new Message();
        message.setSenderId(dto.getSenderId());
        message.setReceiverId(dto.getReceiverId());
        message.setContent(dto.getContent());
        message.setSendAt(LocalDateTime.now());

        Message chat=messageRepository.save(message);

          if(chat!=null){
              return "Message is sent successfully";
          }
          else{
              return "Failed";
          }


    }

    public List<Message> getAllMessages(String userid, String curuserid){

        return messageRepository.getAllMessages(userid, curuserid);
    }


}
