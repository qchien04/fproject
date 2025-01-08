import { Button, Popconfirm } from "antd";
import {DeleteOutlined} from "@ant-design/icons"
import { deleteRoom } from "../../services/RoomService";

interface Room {
    id: string; // Kiểu dữ liệu của `id`, có thể thay đổi nếu cần
    name: string;
    quantityBed: string;
    quantityPeople: number;
    typeRoom: boolean; // `true` là VIP, `false` là Thường
    status: boolean; // `true` là Còn phòng, `false` là Hết phòng
  }
  
interface DeleteRoomProps {
    record: Room; // Danh sách các phòng
    onReload: () => void; // Hàm callback để reload dữ liệu
}
const DeleteRoom:React.FC<DeleteRoomProps> = ({ record, onReload }) => {
    const handleDelete=async()=>{
        const response=await deleteRoom(record.id);
        if(response){
            onReload();
            alert("Xoa thanh cong");
        }else{
            alert("ngu")
        }
    }
    return(
        <>
        <Popconfirm title="Chac chan khong?" 
            onConfirm={handleDelete}>
                <Button danger size="small" icon={<DeleteOutlined/>}/>
        </Popconfirm>
            
        </>
    );
}

export default DeleteRoom;