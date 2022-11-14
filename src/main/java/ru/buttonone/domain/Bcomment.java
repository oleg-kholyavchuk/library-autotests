package ru.buttonone.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bcomment {
    private String id;
    @JsonProperty("book_id")
    private String bookId;
    private String nickname;
    private String message;
}
