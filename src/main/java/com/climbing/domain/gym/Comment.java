package com.climbing.domain.gym;

//import com.climbing.domain.member.Member;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Entity
//@Getter
//@AllArgsConstructor
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private Gym gym;
//    private Member member;
//    private String value;
//}


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record Comment(String user,
                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd", timezone = "Asia/Seoul")
                      LocalDateTime createdAt,
                      String text) {

}
