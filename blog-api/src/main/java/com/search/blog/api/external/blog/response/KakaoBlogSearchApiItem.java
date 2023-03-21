package com.search.blog.api.external.blog.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Getter
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PUBLIC)
public class KakaoBlogSearchApiItem {

    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime datetime;

}
