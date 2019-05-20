package com.yqh.marketing.domain.entity;

import lombok.Data;

@Data
public class Ticket {
    String USER_ID;
    String USER_TICKET;
    String EXPIRE_TIME;
    int USER_TYPE;
    String AUTH_FROM;
    String OPEN_ID;
}
