import axiosClient from '../../../config/axiosconfig';


type UserNameAndPictureResponse={
    email:string,
    name:string,
    picture:string,
}
type ChatRoomResponse={
    id:number,
    users:UserNameAndPictureResponse[],
}
type LocalDateTime=string;
type Message={
  name:string,
  content:string,
  emailSend:string,
  chatRoomId:number,
  timeSend:LocalDateTime,

}

const chatService = {
    getUserChatRooms: async (): Promise<ChatRoomResponse[]> => {
      try {
        const {data,status} = await axiosClient.get('/chatRoom/user');
        if(status===200)
          return data;
        else{
          throw new Error("Can not get user information");
        }
      } catch (error) {
        console.error('Error fetching Authorities:', error);
        throw error; 
      }
    },
    getMessageRoom: async (chatId:number): Promise<Message[]> => {
      try {
        const {data,status} = await axiosClient.get(`/api/messages/${chatId}`);
        if(status===200){
          console.log(data);
          return data;
        }
          
        else{
          throw new Error("Can not get user information");
        }
      } catch (error) {
        console.error('Error fetching Authorities:', error);
        throw error; 
      }
    },
};


export default chatService;