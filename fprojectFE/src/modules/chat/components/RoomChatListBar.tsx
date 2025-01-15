import { Col, Image, Row } from "antd";
import React, { useEffect, useState } from "react";
import chatService from "../services/chatService";
import './roomchatlistbar.css';
import { useAuth } from "../../auth/hooks/useAuth";


import SockJS from "sockjs-client";
import Stomp from 'stompjs';
import ChatPart from "../pages";


type UserNameAndPictureResponse={
    email:string,
    name:string,
    picture:string,
}
type ChatRoomResponse={
    id:number,
    users:UserNameAndPictureResponse[],
}
const RoomChatListBar:React.FC=()=>{
    const {user}=useAuth();
    const [roomchats,setRoomChats]=useState<ChatRoomResponse[]>([]);
    const [stompClientState, setStompClientState] = useState<Stomp.Client | null>(null);
    const [isConnect,setIsConnect]=useState<boolean>(false);
    const [activeRoom,setActiveRoom]=useState<number>(0);
    const currEmail=user?user.email:"Vo danh tieu tot"
    // Lay danh sach cac phong chat cua user
    useEffect(()=>{
        (async()=>{
            
            const getRoomChats=await chatService.getUserChatRooms();
            console.log("EFfect 1")

            setRoomChats(getRoomChats);
            if(!stompClientState) connect();
            console.log(getRoomChats)
        })()
    },[])

    const connect = () => {
        const socket = new SockJS("http://localhost:8080/sockjs");
        const stompClient = Stomp.over(socket);
        setStompClientState(stompClient);

        // Kết nối WebSocket
        stompClient.connect({}, () => {
            console.log('WebSocket connected');
            setIsConnect(true);
        }, errorCallback);
    };

    const errorCallback = (error: string | Stomp.Frame) => {
        if (typeof error === 'string') {
            console.error("Error socket: " + error);
        } else {
            console.error("Error frame: ", error);
        }
    };

    const handleChangRoom=(id:number)=>{
        setActiveRoom(id);
    }


    return(
        <>
            <Row>
                <Col span={6}>
                    <div className="scroll-bar">
                        {roomchats.map((roomchat,index)=>(
                            <Row key={index} 
                              onClick={()=>handleChangRoom(roomchat.id)}
                              className={`box-chat${roomchat.id === activeRoom ? " active" : ""}`}>
                                <Col span={6}>
                                    <Image 
                                        preview={false}
                                        className="avt-user"
                                        src={roomchat.users[0].email!=currEmail?
                                            roomchat.users[0].picture:
                                            roomchat.users[1].picture}
                                        alt="Placeholder Image" 
                                    />
                                </Col>
                                <Col span={18} className="room-name">
                                                {roomchat.users[0].email!=currEmail?
                                                roomchat.users[0].name:
                                                roomchat.users[1].name}
                                </Col>
                            </Row>
                        ))}
                    </div>
                </Col>

                {/*Cua so chat ############################################################################################################*/}
                {isConnect?<Col span={18}>
                    {roomchats.map((roomchat,index)=>{
                        console.log(roomchat.id+"    "+activeRoom);
                        return <div key={index}>
                            <ChatPart active={roomchat.id===activeRoom?true:false} chatRoomId={roomchat.id} stompClientState={stompClientState}></ChatPart>
                        </div>
                    })}
                </Col>:
                <></>}
            </Row>
        
        </>
    )






}

export default RoomChatListBar;