package com.example.fproject.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRespone {

    private Integer id;
    private ArrayList<UserNameAndPicture> users=new ArrayList<>();;
}

