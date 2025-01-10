import React from "react";
import { Badge, Table, Tag, Tooltip } from "antd";
import EditRoom from "./EditRoom";
import { ColumnsType } from "antd/es/table";

interface Room {
  id: string; // Kiểu dữ liệu của `id`, có thể thay đổi nếu cần
  name: string;
  quantityBed: string;
  quantityPeople: number;
  typeRoom?: boolean|null; // `true` là VIP, `false` là Thường
  status?: boolean; // `true` là Còn phòng, `false` là Hết phòng
}

interface RoomTableProps {
  rooms: Room[]; // Danh sách các phòng
  onReload: () => void; // Hàm callback để reload dữ liệu
}

const RoomTable: React.FC<RoomTableProps> = ({ rooms, onReload }) => {
  const columns: ColumnsType<Room> = [
    {
      title: "Tên",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Số giường",
      dataIndex: "quantityBed",
      key: "quantityBed",
    },
    {
      title: "Số người",
      dataIndex: "quantityPeople",
      key: "quantityPeople",
    },
    {
      title: "Loại phòng",
      dataIndex: "typeRoom",
      key: "typeRoom",
      render: (_, record) => (
        <>
          {record.typeRoom ? (
            <Tooltip title="Chuẩn 5 sao">
              <Badge color="purple" text="VIP"></Badge>
            </Tooltip>
          ) : (
            <Tooltip title="Chuẩn thường">
              <Badge color="gray" text="Thường"></Badge>
            </Tooltip>
          )}
        </>
      ),
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      render: (_, record) => (
        <>
          {record.status ? (
            <>
              <Tag color="magenta">Sẵn sàng</Tag>
              <Badge color="green" text="Còn phòng"></Badge>
            </>
          ) : (
            <>
              <Tag color="magenta">Hết</Tag>
              <Badge color="red" text="Hết phòng"></Badge>
            </>
          )}
        </>
      ),
    },
    {
      title: "Hành động",
      key: "actions",
      render: (_, record) => (
        <>
          <EditRoom record={record} onReload={onReload}></EditRoom>
        </>
      ),
    },
  ];

  return <Table dataSource={rooms} columns={columns} rowKey="id"></Table>;
};

export default RoomTable;
