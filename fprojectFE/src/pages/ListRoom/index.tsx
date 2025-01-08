import { useEffect, useState } from "react";
import { getListRoom } from "../../services/RoomService";
import { Button } from "antd";
import { UnorderedListOutlined, AppstoreOutlined } from "@ant-design/icons";
import RoomGrid from "./RoomGrid";
import RoomTable from "./RoomTable";

// Định nghĩa kiểu dữ liệu cho Room
interface Room {
    id: string;       
    name: string;             
    quantityBed: string;      
    quantityPeople: number; 
    description?: string;     
    utils?: string[];        
    status: boolean;     
    typeRoom: boolean;         
}
const ListRoom: React.FC = () => {
  const [rooms, setRooms] = useState<Room[]>([]); // Sử dụng kiểu cho state rooms
  const [isGrid, setIsGrid] = useState<boolean>(true); // Kiểu cho state isGrid

  // Hàm fetchApi để lấy danh sách phòng
  const fetchApi = async () => {
    const response: Room[] = await getListRoom(); // Đảm bảo kiểu trả về là mảng Room
    console.log(response);
    setRooms(response.reverse());
  };

  useEffect(() => {
    fetchApi();
  }, []);

  // Hàm reload để tải lại danh sách phòng
  const handleReload = () => {
    fetchApi();
  };

  return (
    <>
      <Button onClick={() => setIsGrid(false)}>
        <UnorderedListOutlined />
      </Button>
      <Button onClick={() => setIsGrid(true)}>
        <AppstoreOutlined />
      </Button>
      {isGrid ? (
        <RoomGrid rooms={rooms} />
      ) : (
        <RoomTable rooms={rooms} onReload={handleReload} />
      )}
    </>
  );
};

export default ListRoom;
