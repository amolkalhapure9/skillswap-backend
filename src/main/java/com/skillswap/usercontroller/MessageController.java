package com.skillswap.usercontroller;

import com.skillswap.userdto.MessageDto;
import com.skillswap.userentity.Message;
import com.skillswap.userservice.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class MessageController {



    @Autowired
    MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;



    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto dto){

        String result=messageService.saveMessage(dto);
        return ResponseEntity.ok(new ApiResponse(true, result, dto));



    }
    @CrossOrigin("*")
    @GetMapping("/getMessage")
    public ResponseEntity<?> getMessage(@RequestParam String userId, Principal principal){
        String curuser=principal.getName();


        List<Message> list=messageService.getAllMessages(userId, curuser);
        return ResponseEntity.ok(new ApiResponse(true, "Extracted", list));


    }


    @MessageMapping("/chat")
    public void sendChatMessage(MessageDto msg, Principal principal) {


    messageService.saveMessage(msg);

    System.out.println("Controller hit");

        System.out.println(
                "Controller Principal = " +
                        principal
        );

        System.out.println(
                "Receiver = " +
                        msg.getReceiverId()
        );
        messagingTemplate.convertAndSendToUser(
                 msg.getReceiverId(),
                "/queue/messages",
                msg
        );
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/messages",
                msg
        );
        System.out.println("Message sent");

    }


}
