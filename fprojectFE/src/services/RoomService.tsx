import { post, get ,del, patch} from "../utils/request"

interface CreateRoomFormValues {
    name: string;
    quantityBed: number;
    quantityPeople: number;
    description?: string;
    utils?: string[];
    status?: boolean;
    typeRoom?: boolean;
  }


export const createRoom=async(options:CreateRoomFormValues)=>{
    const result=await post("create-room",options);
    return result;
}

export const getListRoom=async()=>{
    const result=await get("rooms");
    return result;
}


export const deleteRoom=async(id:string)=>{
    const result=await del('rooms',id);
    return result;
}

export const updateRoom=async(id:string,options:CreateRoomFormValues)=>{
    const result=await patch(`rooms/${id}`,options);
    return result;
}

