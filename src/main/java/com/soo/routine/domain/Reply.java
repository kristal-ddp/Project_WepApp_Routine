package com.soo.routine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reply {

    private int replyNum;
    private int memberNum;
    private int boardNum;
    private String replyContent;
    private String replyCreated;

}
