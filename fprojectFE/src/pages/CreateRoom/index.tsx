import React from "react";
import { Button, Form, Input, InputNumber, message, Select, Switch } from "antd";
import { Outlet } from "react-router-dom";
import { createRoom } from "../../services/RoomService";

const { Option } = Select;

// Định nghĩa kiểu dữ liệu cho form
interface CreateRoomFormValues {
  name: string;
  quantityBed: number;
  quantityPeople: number;
  description?: string;
  utils?: string[];
  status?: boolean;
  typeRoom?: boolean;
}

const CreateRoom: React.FC = () => {
  const [form] = Form.useForm<CreateRoomFormValues>(); // Sử dụng form với kiểu TypeScript
  const [messageApi, contextHolder] = message.useMessage();

  const handleSubmit = async (values: CreateRoomFormValues) => {
    const response = await createRoom(values);
    if (response) {
      messageApi.open({
        type: "success",
        content: "Tạo phòng thành công",
        duration: 5,
      });

      form.resetFields();
    } else {
      messageApi.open({
        type: "error",
        content: "Tạo phòng không thành công",
        duration: 5,
      });
      form.resetFields();
    }
  };

  const rules1 = [
    {
      required: true,
      message: "Bắt buộc!",
    },
  ];

  return (
    <>
      {contextHolder}
      <h2>Tạo phòng</h2>
      <Form
        form={form}
        name="create-room"
        layout="vertical"
        onFinish={handleSubmit}
      >
        <Form.Item label="Tên phòng" name="name" rules={rules1}>
          <Input />
        </Form.Item>

        <Form.Item label="Số lượng giường" name="quantityBed" rules={rules1}>
          <InputNumber min={1} />
        </Form.Item>

        <Form.Item label="Số người tối đa" name="quantityPeople" rules={rules1}>
          <InputNumber min={1} max={10} />
        </Form.Item>

        <Form.Item label="Mô tả" name="description">
          <Input.TextArea showCount maxLength={100} />
        </Form.Item>

        <Form.Item label="Dịch vụ" name="utils">
          <Select
            style={{ width: "100%" }}
            mode="multiple"
            allowClear
          >
            <Option value="Wifi">Wifi</Option>
            <Option value="hehe">hehe</Option>
            <Option value="io">io</Option>
            <Option value="wtf">wtf</Option>
          </Select>
        </Form.Item>

        <Form.Item
          valuePropName="checked"
          label="Trạng thái"
          name="status"
        >
          <Switch
            checkedChildren="Còn phòng"
            unCheckedChildren="Hết phòng"
          />
        </Form.Item>

        <Form.Item
          valuePropName="checked"
          label="Loại phòng"
          name="typeRoom"
        >
          <Switch
            checkedChildren="VIP"
            unCheckedChildren="Thường"
          />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit">
            Tạo phòng
          </Button>
        </Form.Item>
      </Form>

      <Outlet />
    </>
  );
};

export default CreateRoom;
