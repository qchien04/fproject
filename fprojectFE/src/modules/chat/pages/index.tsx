import Stomp from 'stompjs';
import React, { useState, useRef, useEffect, memo } from 'react';
import { useAuth } from '../../auth/hooks/useAuth';
import { Button, Col, Form, Input, Row, Tooltip } from 'antd';
import { SmileOutlined, ArrowRightOutlined, PictureOutlined } from '@ant-design/icons';
import "./chat.css";
import chatService from '../services/chatService';

type LocalDateTime=string;
type MessageType = {
  name: string;
  content: string;
  emailSend: string;
  chatRoomId:number;
  timeSend:LocalDateTime,
};
type FormSend={
  content:string,
}
type MessageProps = {
  name:string;
  message: MessageType;
  currEmail: string | undefined;
  key: React.Key;
};
type Typing={
  name:string,
  emailSend:string,
  type:string,
  chatRoomId:number,
}
const Message = memo(({ message, currEmail }: MessageProps) => {
  return (
    <div
      className={message.emailSend === currEmail ? "inner-outgoing" : "inner-incoming"}
    >
      {message.emailSend !== currEmail && (
        <div className="inner-name">{message.name}</div>
      )}
      {message.content && (
        <div className="inner-content">{message.content}</div>
      )}
    </div>
  );
});
type prop={
  chatRoomId:number,
  active:boolean,
  stompClientState:Stomp.Client | null;
}

const ChatPart:React.FC<prop> = ({chatRoomId,stompClientState,active}) => {
  console.log("render")
  const { user } = useAuth();
  const chatBoxRef = useRef<HTMLDivElement | null>(null);
  const [form] = Form.useForm();
  const [messages, setMessages] = useState<MessageType[]>([
    { name: "a ba xì dầu", content: 'Xin chào!', emailSend: user?user.email:"Vo danh tieu tot",chatRoomId:1,timeSend:"2025-01-15T10:30:45"},
    { name: "a bốn tương cá", content: 'Chào bạn!', emailSend: 'User2',chatRoomId:1,timeSend:"2025-01-15T10:30:45"},
    { name: "a ba xì dầu", content: 'Xin chàChào bạn!Chào bạn!Chào bạn!Chào bạn!Chào bạn!o!',timeSend:"2025-01-15T10:30:45", emailSend: user?user.email:"Vo danh tieu tot",chatRoomId:1},
    { name: "a bốn tương cá", content: 'Chào bạn!', emailSend: 'User2',chatRoomId:1,timeSend:"2025-01-15T10:30:45"},
    { name: "a ba xì dầu", content: 'Xin chào!', emailSend: user?user.email:"Vo danh tieu tot",chatRoomId:1,timeSend:"2025-01-15T10:30:45"},
  ]
);

  useEffect(()=>{
    (async()=>{  
      const getRoomChats=await chatService.getMessageRoom(chatRoomId);
      setMessages(prechat=>[...prechat,...getRoomChats]);
      })()
  },[])
  useEffect(() => {
    if (stompClientState && stompClientState.connected) {
        const subscription = stompClientState.subscribe(`/room/${chatRoomId}`, onMessageReceive);
        console.log("Subscribed to /room/1");

        return () => {
            subscription.unsubscribe();
        };
    }
}, [stompClientState]);
  const onMessageReceive = (serverRespone: Stomp.Message) => {
    console.log("Received message:", JSON.parse(serverRespone.body));
    const newMessage=JSON.parse(serverRespone.body)

    if(!newMessage.type){
      setMessages(prevMessages => [...prevMessages, newMessage]);
    }
    else{
      if(user&&newMessage.emailSend!=user.email)
        handleTyping(newMessage);
      }
  };

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight+100; // Cuộn xuống cuối
    }
  }, [messages]); 




  const handleTyping=(data:Typing)=>{
    console.log("ok");
    const elementListTyping=chatBoxRef.current?.querySelector(".inner-list-typing");
    if(elementListTyping){
      if(data.type == "show"){
        const existTyping = elementListTyping.querySelector(`[user-id="${data.emailSend}"]`);
        if(!existTyping){
          const boxTyping = document.createElement("div");
          boxTyping.classList.add("box-typing");
          boxTyping.setAttribute("user-id", data.emailSend);
          boxTyping.innerHTML = `
            <div class="inner-name">${data.name}</div>
            <div class="inner-dots"><span></span><span></span><span></span></div>
          `;
          elementListTyping?.appendChild(boxTyping);
          if(chatBoxRef.current) chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight+100; 
        }
      }else {
        if(elementListTyping){
          const boxTypingDelete = elementListTyping.querySelector(`[user-id="${data.emailSend}"]`);
          if(boxTypingDelete) {
            elementListTyping.removeChild(boxTypingDelete);
          }
        }
      
      }
    }
  }

  const sendMessageToRoom = (values:FormSend) => {
    form.resetFields();
    const newMessage:MessageType={
      chatRoomId:chatRoomId,
      emailSend:user?user.email:"underfind",
      content:values.content,
      name:user?user.name:"Vo danh tieu tot",
      timeSend:"2025-01-15T10:30:45",
    }
    if (stompClientState && stompClientState.connected) {
      stompClientState.send("/app/sendMessage", {}, JSON.stringify(newMessage));
    } else {
      console.error("WebSocket is not connected");
    }
  };

  const sendPersonalMessage = () => {
    if (stompClientState && stompClientState.connected) {
      stompClientState.send("/app/personnalmessage", {}, JSON.stringify('cua a ne'));
    } else {
      console.error("WebSocket is not connected");
    }
  };
  
  const showTyping=()=>{
    let timeOut;
    if (stompClientState && stompClientState.connected) {
      const typing={
        name:user?user.name:"anh ba xì dầu",
        emailSend:user?user.email:"Vo danh tieu tot",
        type:"show",
        chatRoomId:chatRoomId,
      }
      stompClientState.send("/app/typing", {}, JSON.stringify(typing));
      clearTimeout(timeOut);
  
      timeOut = setTimeout(() => {
        const hide={
          name:user?user.name:"anh ba xì dầu",
          emailSend:user?user.email:"Vo danh tieu tot",
          type:"hidden",
          chatRoomId:chatRoomId,
        }
        stompClientState.send("/app/typing", {}, JSON.stringify(hide));
    }, 3000);
    } else {
      console.error("WebSocket is not connected");
    }
  }

  return (
    <>
    {active?<div>
      <button onClick={sendPersonalMessage}>Send User</button>

      <div className="container my-3">
        {/* Header */}
        <div className="row">
          <div className="col-12">
            <h3>Chat</h3>
          </div>
        </div>

        {/* Chat messages */}
        <Row>
          <Col span={24}>
            <div className="chat" my-id={user?.email}>
              <div className="inner-body" ref={chatBoxRef}>
                  {messages.map((message, index) => (
                    <Message key={index} currEmail={user?.email} message={message} />
                  ))}
                <div className="inner-list-typing"/>
              </div>

              {/* Image upload section */}
              <div className="inner-preview-images">
                <div className="custom-file-container" data-upload-id="upload-images" />
              </div>

              {/* Footer with message input */}
              <div className="inner-foot">
                <Form form={form} className="inner-form" onFinish={sendMessageToRoom}>
                  <div className="d-flex">
                    <Form.Item label="content" name="content">
                      <Input
                        type="text"
                        placeholder="Nhập nội dung..."
                        className="flex-grow-1"
                        onChange={showTyping}
                      />
                    </Form.Item>
                    <Tooltip title="Emojis">
                      <Button
                        icon={<SmileOutlined />}
                        className="mx-1"
                        type="text"
                      />
                    </Tooltip>
                    <Tooltip title="Upload Image">
                      <label
                        htmlFor="file-upload-with-preview-upload-images"
                        className="btn btn-sm btn-light mr-1 mb-0"
                      >
                        <PictureOutlined />
                      </label>
                    </Tooltip>
                    <Button
                      htmlType="submit"
                      icon={<ArrowRightOutlined />}
                      type="primary"
                    />
                  </div>
                </Form>
              </div>
            </div>
          </Col>
        </Row>

        
        {/* Emoji picker */}
        <div className="tooltip" role="tooltip">
          <div className="emoji-picker light" />
        </div>
      </div>
    </div>:
    <></>}
    </>
  );
};

export default ChatPart;
