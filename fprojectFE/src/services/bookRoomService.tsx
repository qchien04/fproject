import { post } from "../utils/request"

type CheckboxValueType = string | number;
interface BookingData {
  fullName?: string;
  phone?: string;
  email?: string;
  services?: CheckboxValueType[];
  date?: [string, string];
  time: string;
  gift?: string;
}

export const bookRoom=async(options:BookingData)=>{
    const result=await post("book-room",options);
    return result;
}

