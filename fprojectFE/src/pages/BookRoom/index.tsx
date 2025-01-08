import React, { useState } from "react";
import { Button, Checkbox, Col, DatePicker, Input, Radio, Row, Select, Space } from "antd";
import { RangePickerProps } from "antd/es/date-picker";
import { Outlet } from "react-router-dom";
import { bookRoom } from "../../services/bookRoomService";
import { RadioChangeEvent } from "antd";

const { RangePicker } = DatePicker;
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

const BookRoom: React.FC = () => {
  const [data, setData] = useState<BookingData>({
    time: "7h",
  });

  const optionsTime = [
    { value: "7h", label: "7h" },
    { value: "8h", label: "8h" },
    { value: "9h", label: "9h" },
  ];

  const handleSubmit = async () => {
    const response = await bookRoom(data);
    if (response) {
      console.log("Ok");
    } else {
      console.log("No");
    }
  };

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const object = {
      ...data,
      [e.target.name]: e.target.value,
    };
    setData(object);
  };

  const handleChangeSelect = (value: string) => {
    const object = {
      ...data,
      time: value,
    };
    setData(object);
  };

  const handleChangeDate: RangePickerProps["onChange"] = (dates, dateStrings) => {
    const object = {
      ...data,
      date: dateStrings as [string, string], // Gán kiểu mảng 2 chuỗi
    };
    setData(object);
  };

  const handleChangeCheckbox = (checkedValues: CheckboxValueType[]) => {
    const object = {
      ...data,
      services: checkedValues,
    };
    setData(object);
  };

  const handleChangeRadio = (e: RadioChangeEvent) => {
    const object = {
      ...data,
      gift: e.target.value,
    };
    setData(object);
  };

  return (
    <>
      <h2>Book Room</h2>
      <form>
        <Row gutter={[20, 20]}>
          <Col span={24}>
            <p>Họ và tên</p>
            <Input
              name="fullName"
              placeholder="Họ và tên"
              onBlur={handleChangeInput}
            />
          </Col>

          <Col span={12}>
            <p>Số điện thoại</p>
            <Input
              name="phone"
              placeholder="SĐT"
              onBlur={handleChangeInput}
            />
          </Col>

          <Col span={12}>
            <p>Email</p>
            <Input
              name="email"
              placeholder="Email"
              onBlur={handleChangeInput}
            />
          </Col>

          <Col span={12}>
            <p>Dịch vụ</p>
            <Checkbox.Group onChange={handleChangeCheckbox}>
              <Space direction="vertical">
                <Checkbox value="A">A</Checkbox>
                <Checkbox value="B">B</Checkbox>
                <Checkbox value="C">C</Checkbox>
              </Space>
            </Checkbox.Group>
          </Col>

          <Col span={12}>
            <p>Chọn ngày</p>
            <RangePicker
              placeholder={["Nhận phòng", "Trả phòng"]}
              format="DD/MM/YYYY"
              onChange={handleChangeDate}
            />
          </Col>

          <Col span={12}>
            <p>Chọn giờ</p>
            <Select
              onChange={handleChangeSelect}
              style={{ width: "100%" }}
              defaultValue={data.time}
              options={optionsTime}
            />
          </Col>

          <Col span={12}>
            <p>Ticket</p>
            <Radio.Group name="gift" onChange={handleChangeRadio}>
              <Space direction="vertical">
                <Radio value="A">A</Radio>
                <Radio value="B">B</Radio>
                <Radio value="C">C</Radio>
              </Space>
            </Radio.Group>
          </Col>

          <Col span={24}>
            <Button type="primary" onClick={handleSubmit}>
              Đặt phòng
            </Button>
          </Col>
        </Row>
      </form>
      <Outlet />
    </>
  );
};

export default BookRoom;
