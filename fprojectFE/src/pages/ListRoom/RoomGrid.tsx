import { Badge, Card, Col, Row } from "antd";


interface Room {
    id: string; // Kiểu dữ liệu của `id`, có thể thay đổi nếu cần
    name: string;
    quantityBed: string;
    quantityPeople: number;
    typeRoom: boolean; // `true` là VIP, `false` là Thường
    status: boolean; // `true` là Còn phòng, `false` là Hết phòng
  }
  
  interface RoomGridProps {
    rooms: Room[]; // Danh sách các phòng
  }

const RoomGrid:React.FC<RoomGridProps>=({ rooms }) => {
    return(
        <>
            <Row gutter={[20,20]}>
                    {rooms.map((item)=>(
                        <Col span={12} key={item.id}>
                            <Badge.Ribbon text={item.typeRoom?"VIP":"Thuong"} color={item.typeRoom?"red":"gray"}>
                                <Card title={item.name}>
                                    <p>So giuong: <strong>{item.quantityBed}</strong></p>
                                    <p>So nguoi: <strong>{item.quantityPeople}</strong></p>
                                    <p>{item.status ? 
                                        (<Badge status="success" text="Con phong"></Badge>):
                                        (<Badge status="success" text="Con phong"></Badge>)
                                    }
                                    </p>
                                </Card>
                            </Badge.Ribbon>
                        </Col>
                    ))}
            </Row>
        </>
    );
}

export default RoomGrid;