import { Button, Modal, notification, Spin } from "antd";
import { EditOutlined } from "@ant-design/icons";
import { useState } from "react";
import { Form, Input, InputNumber, Select, Switch } from "antd";

const { Option } = Select;

interface Room {
  id: string; // Kiểu dữ liệu của `id`, có thể thay đổi nếu cần
  name: string;
  quantityBed: string;
  quantityPeople: number;
  typeRoom?: boolean|null; // `true` là VIP, `false` là Thường
  status?: boolean; // `true` là Còn phòng, `false` là Hết phòng
}
  
interface EditRoomProps {
    record: Room; // Danh sách các phòng
    onReload: () => void; // Hàm callback để reload dữ liệu
}

const EditRoom: React.FC<EditRoomProps> = ({ record, onReload }) => {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [form] = Form.useForm();
  const [notifyApi, contextHolder] = notification.useNotification();
  const [spinning, setSpinning] = useState<boolean>(false);

  const handleSubmit = async (values: Room) => {
    const response = values;
    setSpinning(true);
    setTimeout(() => {
      if (response) {
        notifyApi.success({
          message: `Cập nhật thành công`,
          description: `Bạn đã cập nhật thành công phòng ${record.name}`,
          duration: 10,
          placement: 'bottomRight',
        });
        setShowModal(false);
        onReload();
      } else {
        notifyApi.error({
          message: "Cập nhật thất bại",
          description: `Bạn đã cập nhật thất bại`,
          duration: 5,
        });
      }
      setSpinning(false);
    }, 3000);
  };

  const rules1 = [
    {
      required: true,
      message: 'Bắt buộc!',
    },
  ];

  const handleShowModal = () => {
    setShowModal(true);
  };

  const handleCancel = () => {
    setShowModal(false);
    form.resetFields();
  };

  return (
    <>
      <Button
        onClick={handleShowModal}
        type="primary"
        size="small"
        icon={<EditOutlined />}
      />
      <Modal open={showModal} footer={null} onCancel={handleCancel}>
        <Spin spinning={spinning} tip="Đang cập nhật">
          <h2>Chỉnh sửa</h2>
          {contextHolder}
          <Form
            form={form}
            name="create-room"
            initialValues={record}
            layout="vertical"
            onFinish={handleSubmit}
          >
            <Form.Item label="Tên phòng" name="name" rules={rules1}>
              <Input />
            </Form.Item>

            <Form.Item label="Số lượng giường" name="quantityBed" rules={rules1}>
              <Input />
            </Form.Item>

            <Form.Item label="Số người tối đa" name="quantityPeople" rules={rules1}>
              <InputNumber min={1} max={10} />
            </Form.Item>

            <Form.Item label="Mô tả" name="description">
              <Input.TextArea showCount maxLength={100} />
            </Form.Item>

            <Form.Item label="Dịch vụ" name="utils">
              <Select style={{ width: "100%" }} mode="multiple" allowClear>
                <Option value="Wifi">Wifi</Option>
                <Option value="hehe">hehe</Option>
                <Option value="io">io</Option>
                <Option value="wtf">wtf</Option>
              </Select>
            </Form.Item>

            <Form.Item valuePropName="checked" label="Trạng thái" name="status">
              <Switch checkedChildren="Còn phòng" unCheckedChildren="Hết phòng" />
            </Form.Item>

            <Form.Item valuePropName="checked" label="Loại phòng" name="typeRoom">
              <Switch checkedChildren="Vip" unCheckedChildren="Thường" />
            </Form.Item>

            <Form.Item>
              <Button type="primary" htmlType="submit">
                Cập nhật
              </Button>
            </Form.Item>
          </Form>
        </Spin>
      </Modal>
    </>
  );
};

export default EditRoom;
